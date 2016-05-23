package parallelComputingLib;

import java.io.IOException;
import java.nio.*;
import java.nio.channels.*;

public class GeneServer extends Server{
  public static final byte COMPONENT_DENSITY = 1;
  public static final byte ROW_SUM           = 2;
  public static final byte DIVISION          = 3;
  private int rowsumDoublesPerRow            = 200;
  private int componentDensityDoublesPerRow  = 200;
  private int divisionDoublesPerRow          = 200;
  private boolean getRowSums                 = true;
  private boolean getComponentDensities      = false;

  public static void main(String[] args){
    GeneServer s = new GeneServer();
    s.setupStorageFile();
    s.setupServer();
    s.setupAdvertising();
  }
  protected ByteBuffer getWorkerData(){
    if(getRowSums)
      return getRowSumPacket();
    else if(getComponentDensities)
      return getComponentDensityPacket();
    return getDivisionPacket();
  }
  private ByteBuffer getRowSumPacket(){
    try {
      b.clear();
      b.put(GeneServer.ROW_SUM); //identify the operation to perform
      b.putLong(rowPointer);
      int rowsToSend = (b.capacity()-25)/(8*rowsumDoublesPerRow);
      rowPointer+=rowsToSend;
      b.putLong(rowsToSend);
      b.putLong(rowsumDoublesPerRow);

      for(int j=0;j<rowsToSend;j++){
        String line = dataReader.readLine();
        if(line!=null){
          String[] valueArray = line.split(" ");
          int size = valueArray.length;
          for(int i=0;i<size;i++)
            b.putDouble(Double.parseDouble(valueArray[i]));
        }
        else{
          System.out.println("Total Time for row Sums: " + (System.currentTimeMillis() - start));
          getRowSums = false; //stop getting row sums
          getComponentDensities = true; //start getting componentDensities
        }
      }
      b.flip();
      return b;
    }catch (IOException ex) {System.out.println("IOException in getRowSumPacket(): " + ex);}
    return null;
  }
  private ByteBuffer getComponentDensityPacket(){
    try{
      b.clear();
      b.put(GeneServer.COMPONENT_DENSITY); //identify operation to perform
      b.putLong(rowPointer);
      //For a component density packet 3 params are needed for each call
      //24 byte groups will be created for each call
      int rowsToSend = (b.capacity()-25)/(24*componentDensityDoublesPerRow); //calculate the rows to send
      b.putLong(rowsToSend);
      b.putLong(componentDensityDoublesPerRow);

      for(int i=0;i<rowsToSend;i++){
        String line = dataReader.readLine();
        if(line!=null){
          String[] valueArray = line.split(" ");
          int size = valueArray.length;
          for(int j=0;j<size;j++)
            b.putDouble(Double.parseDouble(valueArray[j]));
        }
        else{
          System.out.println("Total Time for componentDensities: " + (System.currentTimeMillis() - start));
          getComponentDensities = false; //stop getting componentDensities
          //start getting divisions
        }
      }
      b.flip();
      return b;
    }catch(IOException ex){System.out.println("IOException in getComponentDensityPacket(): " + ex);}
    return null;
  }
  private ByteBuffer getDivisionPacket(){
    try{
      b.clear();
      b.put(GeneServer.DIVISION); //identify operation to perform
      b.putLong(rowPointer);
      //For a division packet many numerators get divided by 1 denominator
      //Therefore 1 row = [denominator   ][...numerators...]
      //                  [double 8 bytes][ 8 byte sets    ]
      //Each row will be begin with the denominator and end with numerators
      int rowsToSend = (b.capacity()-25)/(8+(8*divisionDoublesPerRow)); //calculate the rows to send
      b.putLong(rowsToSend);
      b.putLong(divisionDoublesPerRow);

      for(int i=0;i<rowsToSend;i++){
        String line = dataReader.readLine();
        if(line!=null){
          String[] valueArray = line.split(" ");
          int size = valueArray.length;
          for(int j=0;j<size;j++)
            b.putDouble(Double.parseDouble(valueArray[j]));
        }
        else{
          System.out.println("Total Time for Divisions: " + (System.currentTimeMillis() - start));
          shutdown(); //we're done
        }
      }
      b.flip();
      return b;
    }catch(IOException ex){System.out.println("IOException in getComponentDensityPacket(): " + ex);}
    return null;
  }
  protected void handleData(ByteBuffer data){
    switch(data.get()){
      case GeneServer.COMPONENT_DENSITY:
        handleComponentDensityData(data);
        break;
      case GeneServer.ROW_SUM:
        handleRowSumData(data);
        break;
      case GeneServer.DIVISION:
        handleDivisionData(data);
        break;
    }
/*
    //process results from workers
    // [0 1 2 3 4 5 6 7 | 8 9 10 11 12 13 14 15 | Data ... ]
    // [startingRow     | numbersPerRow         | Data ... ]

    long startingRow = data.getLong();
    int size = (data.limit()-8)/8;
    long rowID = startingRow;
    for(int i=0;i<size;i++){
      cfs.store(data.getDouble(),rowID);
      rowID++;
    }
*/
  }
  private void handleRowSumData(ByteBuffer data){
    long startingRow = data.getLong();
    int size = (data.limit()-8)/8;
    long rowID = startingRow;
    for(int i=0;i<size;i++){
      cfs.store(data.getDouble(),rowID);
      rowID++;
    }
  }
  private void handleComponentDensityData(ByteBuffer data){
    long startingRow = data.getLong();
    int size = (data.limit()-8)/8;
    long rowID = startingRow;
    for(int i=0;i<size;i++){
      cfs.store(data.getDouble(),rowID);
      rowID++;
    }
  }
  private void handleDivisionData(ByteBuffer data){
    long startingRow = data.getLong();
    int size = (data.limit()-8)/8;
    long rowID = startingRow;
    for(int i=0;i<size;i++){
      cfs.store(data.getDouble(),rowID);
      rowID++;
    }
  }
  protected void identifyUDPData(ByteBuffer msg, String ip){
    int n = Character.getNumericValue((char)msg.get(0));

    switch(n){
      case 0:
        removeWorker(ip);
      case 1:
        //advertisement
        ComputerProperties cp = new ComputerProperties();
        cp.computerID = String.valueOf(msg.getLong(1));
        cp.computerIP = ip;
        checkForDuplicateWorker(cp);
        stopWorkerAdvertising(ip);
        break;
    }
  }
  protected void identifyTCPDataReadEvent(ByteBuffer msg, int read, SocketChannel callback){
    //data was read from a worker. Pull apart results and send new data
    handleData(msg);
    try {
      callback.write(getWorkerData());
    }catch (IOException ex) {System.out.println("IOException in identifyTCPDataReadEvent(): " + ex);}
  }
  protected void identifyTCPDataAcceptEvent(SocketChannel callback){
    //new worker was identified.  Send them starting data
    try {
      start = System.currentTimeMillis();
      callback.write(getWorkerData());
    }catch (IOException ex) {System.out.println("IOException in identifyTCPDataAcceptEvent(): " + ex);}
  }
}
