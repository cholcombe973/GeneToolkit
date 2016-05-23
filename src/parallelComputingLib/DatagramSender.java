package parallelComputingLib;

import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.io.*;
/**
 * <p>Title: DatagramSender</p>
 *
 * <p>Description: The DatagramSender class is used to direct UDP packets to specific computers.
 * </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 * @author Chris Holcombe
 * @version 1.0
 */

public class DatagramSender extends Thread {
  private DatagramChannel dc;
  private ByteBuffer src;
  private SocketAddress target;

  /**
   * Creates a new DatagramSender object with a channel to use, data to send and a address to send to.  The DatagramChannel
   * used by this class must be opened already before passing a reference to this class to use.  Otherwise an IOException will
   * be thrown.
   * @param dc DatagramChannel
   * @param src ByteBuffer
   * @param target SocketAddress
   */
  public DatagramSender(DatagramChannel dc, ByteBuffer src,SocketAddress target){
    this.dc  = dc;
    this.src = src;
    this.target = target;
  }
  /**
   * This method runs in a new thread and sends the data.
   */
  public void run(){
    try{
      if(dc.isOpen()){
        dc.send(src,target);
      }
    }catch(IOException e){System.out.println("IOException in DatagramSender run(): " + e);}
  }
}
