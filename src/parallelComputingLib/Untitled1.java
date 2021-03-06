package parallelComputingLib;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class Untitled1 {
//  private BufferedReader dataReader;
//  private ByteBuffer b = ByteBuffer.allocateDirect(8192+16);
//  private int doublesPerRow = 10;
//  private long rowPointer = 0;

/*
   public static void main(String[] args){
    System.out.println((byte)'T');
    System.out.println((byte)('T' >>> 8)); //high byte
    System.out.println((byte)('T' >>> 0)); //low byte




  }
*/
/*
//creates new data files for training data
public static void main(String[] args){
  //create a data file
  long start = System.currentTimeMillis();
  try {
    File f = new File("Data\\Mu.txt");
    DataOutputStream dout = new DataOutputStream(new FileOutputStream(f));
    Random r = new Random(System.currentTimeMillis());
    for (int i = 0; i < 5000; i++) {
      for (int j = 0; j < 200; j++) {
        dout.writeBytes(r.nextDouble()+" ");
      }
      dout.writeBytes("\r\n");
    }
    dout.close();
  }catch(IOException e){System.out.println(e);}
  long end = System.currentTimeMillis() - start;
  System.out.println("Total Time: " + end);
}
*/
//public static void main(String[] args){
//  //test functions
//  Untitled1 u = new Untitled1();
//  System.out.println("Result: " + u.normalDist(25,10,5));
//}
/**
 * Returns the normal distribution for the specified mean and standard
 * deviation.  This function has a very wide range of applications in statistics,
 * including hypothesis testing.
 * @param x double The value for which you want the distribution
 * @param m double The arithmetic mean of the distribution
 * @param o double The standard deviation of the distribution
 * @return double The normal distribution
 */
/*
private double normalDist(double x, double m, double o){
  return  1/(StrictMath.sqrt(2*Math.PI)*o)*StrictMath.exp(-(x-m)*(x-m)/(2*o*o));
}

public static void main(String[] args){
  //compute row sums
  CFS c = new CFS("crap.txt");
  long start = System.currentTimeMillis();
  try{
    File f = new File("DataHuge.txt");
    BufferedReader r = new BufferedReader(new FileReader(f));
    String values = "";
    int count = 1;
    double[] columnSums = new double[200];

    while((values = r.readLine())!=null){
      String[] valueArray = values.split(" ");
      double[] doubles = new double[valueArray.length];
      int size = valueArray.length;
      for(int i=0;i<size;i++){
        doubles[i] = Double.parseDouble(valueArray[i]);
      }
      double rowSums = 0;
      for(int i=0;i<doubles.length;i++){
        rowSums+=doubles[i];
        columnSums[i]+=doubles[i];
      }
      c.store(rowSums,count);
      //System.out.println("Row " +count+ " Sum: " + rowSums);
      count++;
    }
    for(int i=0;i<columnSums.length;i++){
      //System.out.println("Column Sum: " + columnSums[i]);
    }
    r.close();
  }catch(IOException e){System.out.println(e);}
  System.out.println("Total Time: " + (System.currentTimeMillis()- start));
}
*/
/*
public static void main(String[] args){
  Untitled1  u = new Untitled1();
  u.doShit();
  while(true){
    u.identifyTCPData(u.getWorkerData(null),null);
  }
}
public void doShit(){
  try {
    Properties p = new Properties();
    p.load(new FileInputStream("ServerProps.txt"));
    dataReader = new BufferedReader(new FileReader(new File(p.getProperty("DataFile"))));
  }catch (IOException ex) {System.out.println("OH SHIT: " + ex);}
}
  private synchronized ByteBuffer getWorkerData(SocketChannel callback){
//    ByteBuffer b = ByteBuffer.allocate(4096); //2039 left for data     254 doubles = 2032
  b.clear();
  b.putLong(rowPointer);
  b.putLong(doublesPerRow);
  rowPointer++;
  try {
    int bytesToSend = 100/(doublesPerRow);
    int k = 1;

    for(int j=0;j<bytesToSend;j++){
      String line = dataReader.readLine();
      if(line!=null){
        String[] valueArray = line.split(" ");
        int size = valueArray.length;
        for(int i=0;i<size;i++){
          System.out.println("Putting: " + Double.parseDouble(valueArray[i]));
          b.putDouble(Double.parseDouble(valueArray[i]));
          k++;
        }
      }
      else{
//        System.out.println("Total Time: " + (System.currentTimeMillis() - start));
        System.exit(0);
      }
    }
    b.flip();
    return b;
  }catch (IOException ex) {System.out.println("IOException in getWorkerData(): " + ex);}
  return null;
}

  private synchronized void identifyTCPData(ByteBuffer msg, SocketChannel callback){
    long startingRow = msg.getLong(); //send this back to server to ident the row we worked on
    int numbersPerRow = (int)msg.getLong();

    int size = ((msg.limit()-16)/8);
    System.out.println("Startring row: " + startingRow);
    System.out.println("Numbers per row: " + numbersPerRow);
    System.out.println("Size: " + size);
    int pointer = 0;
    int returnArrayPointer = 0;
    double returnVal = 0;
    double[] returnVals = new double[size/numbersPerRow];
    double donsPubicHair = 0;

    int temp = 1;

    for(int i=0;i<size;i++){
      if(pointer<numbersPerRow){
        donsPubicHair = msg.getDouble();
        System.out.println(temp + ". Recieved: " +donsPubicHair);
        temp++;
        returnVal += donsPubicHair;
        pointer++;
      }
      else{
        pointer = 0;
        returnVals[returnArrayPointer] = returnVal;
        System.out.println("returnVals["+returnArrayPointer+"]= " + returnVal);
        returnVal = 0;
        returnArrayPointer++;

        //start the first iteration again
        donsPubicHair = msg.getDouble();
        System.out.println(temp + ". Recieved: " +donsPubicHair);
        temp++;
        returnVal += donsPubicHair;
        pointer++;
      }
    }
    returnVals[returnArrayPointer] = returnVal;
    System.out.println("returnVals["+returnArrayPointer+"]= " + returnVal);

    ByteBuffer result = ByteBuffer.allocate((returnVals.length*8)+8);
    result.putLong(startingRow);
    size = returnVals.length;
    System.out.println("Size is now: " + size);
    for(int i=0;i<size;i++){
      System.out.println( "result: " + returnVals[i] );
      result.putDouble(returnVals[i]);
    }
    result.flip();
  }
*/
}
