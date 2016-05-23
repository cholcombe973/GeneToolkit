package parallelComputingLib;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

/**
 *
 * <p>Title: GeneWorker</p>
 * <p>Description: This class provides a concrete implementation of the Worker class.  This subclass has full
 * access to all methods and variables of it's parent becuase I've made all methods that would normally be
 * private, protected instead.  Nothing is hidden from an extending class.
 *
 * </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author Chris Holcombe
 * @version 1.0
 */

public class GeneWorker extends Worker{
  public static final byte COMPONENT_DENSITY = 1;
  public static final byte ROW_SUM           = 2;
  public static final byte DIVISION          = 3;

  public static void main(String[] args){
    GeneWorker w = new GeneWorker();
    w.setupWorker();
  }
  /**
   * This method was created to further filter data to where it needs to go based on the first byte
   * in the message.  The first byte specifies which operation the worker should perform on the data given
   * and all what data structure it will be dealing with.
   * @param msg ByteBuffer The data to process from the server.
   * @param callback SocketChannel The channel with which to send the results back to the server once finished.
   */
  private void identifyOperation(ByteBuffer msg, SocketChannel callback){
    switch(msg.get()){
      case GeneWorker.COMPONENT_DENSITY:
        processComponentDensityPacket(msg,callback);
        break;
      case GeneWorker.ROW_SUM:
        processRowSumPacket(msg,callback);
        break;
      case GeneWorker.DIVISION:
        processDivisionPacket(msg,callback);
        break;
    }
  }
  /**
   * This method handles packets that need to have a row sum performed and returned.
   * @param msg ByteBuffer The data to process from the server.
   * @param callback SocketChannel The channel with which to send the results back to the server once finished.
   */
  private void processRowSumPacket(ByteBuffer msg, SocketChannel callback){
    long startingRow = msg.getLong(); //send this back to server to ident the row we worked on
    int numberOfRows = (int)msg.getLong();
    int numbersPerRow = (int)msg.getLong();

    int size = ((msg.limit()-25)/8);
    int pointer = 0;
    double returnVal = 0;

    ByteBuffer returnValBuffer = ByteBuffer.allocate((numberOfRows*8)+8);
    returnValBuffer.putLong(startingRow);

    for(int i=0;i<size;i++){
      if(pointer<numbersPerRow){
        returnVal+=msg.getDouble();
        pointer++;
      }else{
        pointer = 0;
        returnValBuffer.putDouble(returnVal);
        returnVal = 0;

        returnVal +=msg.getDouble();
        pointer++;
      }
    }
    returnValBuffer.putDouble(returnVal);

    returnValBuffer.flip();
    try {
      callback.write(returnValBuffer);
    }catch (IOException ex) {System.out.println("IOException in identifyTCPData(): " + ex);}
  }
  /**
   * This method handles packets that need have a division performed and returned.
   * @param msg ByteBuffer The data to process from the server.
   * @param callback SocketChannel The channel with which to send the results back to the server once finished.
   */
  private void processDivisionPacket(ByteBuffer msg, SocketChannel callback){
    long startingRow = msg.getLong(); //send this back to server to ident the row we worked on
    int numberOfRows = (int)msg.getLong();
    int numbersPerRow = (int)msg.getLong();

    int size = ((msg.limit()-25)/8);
    int pointer = 0;
    double returnVal = 0;


    ByteBuffer returnValBuffer = ByteBuffer.allocate((numberOfRows*8)+8);
    returnValBuffer.putLong(startingRow);

  }
  /**
   * This method handles packets that need to have a component density function performed and returned.
   * @param msg ByteBuffer The data to process from the server.
   * @param callback SocketChannel The channel with which to send the results back to the server once finished.
   */
  private void processComponentDensityPacket(ByteBuffer msg, SocketChannel callback){
    long startingRow = msg.getLong(); //send this back to server to ident the row we worked on
    int numberOfRows = (int)msg.getLong();
    int numbersPerRow = (int)msg.getLong();

    int size = ((msg.limit()-25)/8);
    int pointer = 0;
    double returnVal = 0;


    ByteBuffer returnValBuffer = ByteBuffer.allocate((numberOfRows*8)+8);
    returnValBuffer.putLong(startingRow);


    double result = normalDist(0,0,0); //send the data through the normal distribution function
  }
  protected void identifyTCPData(ByteBuffer msg, SocketChannel callback){
    identifyOperation(msg,callback);

/*    long startingRow = msg.getLong(); //send this back to server to ident the row we worked on
    int numberOfRows = (int)msg.getLong();
    int numbersPerRow = (int)msg.getLong();

    int size = ((msg.limit()-24)/8);
    int pointer = 0;
    double returnVal = 0;

    ByteBuffer returnValBuffer = ByteBuffer.allocate((numberOfRows*8)+8);
    returnValBuffer.putLong(startingRow);

    for(int i=0;i<size;i++){
      if(pointer<numbersPerRow){
        returnVal+=msg.getDouble();
        pointer++;
      }else{
        pointer = 0;
        returnValBuffer.putDouble(returnVal);
        returnVal = 0;

        returnVal +=msg.getDouble();
        pointer++;
      }
    }
    returnValBuffer.putDouble(returnVal);

    returnValBuffer.flip();
    try {
      callback.write(returnValBuffer);
    }catch (IOException ex) {System.out.println("IOException in identifyTCPData(): " + ex);}
*/
  }
  protected void identifyUDPData(ByteBuffer msg, String ip){
    int n = Character.getNumericValue((char)msg.get(0));

    switch(n){
      case 0:
        //kill message
        if(server.computerIP.equals(ip)){
          System.exit(0);
        }
        break;
      case 3:
        server.computerID = String.valueOf(msg.getLong(1));
        server.computerIP = ip;
        if(!connected)
          connectToServer(ip);
        break;

      case 4:
        //stop advertising
        if(advertise)
          stopAdvertising();
        break;
    }
  }
/**
 * Returns the normal distribution for the specified mean and standard
 * deviation.  This function has a very wide range of applications in statistics,
 * including hypothesis testing.
 * @param x double The value for which you want the distribution
 * @param m double The arithmetic mean of the distribution
 * @param o double The standard deviation of the distribution
 * @return double The normal distribution
 */
  private double normalDist(double x, double m, double o){
    return  1/(StrictMath.sqrt(2*Math.PI)*o)*StrictMath.exp(-(x-m)*(x-m)/(2*o*o));
  }

}
