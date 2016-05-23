package parallelComputingLib;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

/*
  This class allows multiple objects to listen for data with a single channel. Callback event model used.

  To use this class implement UDPListener and override NetworkEventOccured(NetworkEvent e){} with custom code.
  Then startup UDPListen and register your class to listen for events.
*/
/**
 * <p>Title: UDPListen</p>
 *
 * <p>Description:   This class allows multiple objects to listen for data with a single channel. Callback event model used.
 * To use this class implement UDPListener and override NetworkEventOccured(NetworkEvent e){} with custom code.
 * Then startup UDPListen and register your class to listen for events.
 *
 * </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 * @author Chris Holcombe
 * @version 1.0
 */

public class UDPListen extends Thread implements NetworkListener{
  private ArrayList listeners = new ArrayList();
  private DatagramChannel channel;
  private String inet;
  private int port;

  /**
   * Creates a new UDPListen object with a specified port.
   * @param port int The port UDPListen should listen on.
   */
  public UDPListen(int port){
    this.port = port;
  }
  /**
   * Adds a new listener class that can receive network events fired off by this class.
   * @param listener NetworkListener Any class that implements NetworkListener
   */
  public void addListener(NetworkListener listener){
    listeners.add(listener);
  }
  /**
   * Removes a listener class from receiving network events.
   * @param listener NetworkListener Any class that implements NetworkListener
   */
  public void removeListener(NetworkListener listener){
    listeners.remove(listener);
  }
  /**
   * Utility method that returns true if packets incoming from a socket are loopback's.
   * @param socket String The listening socket.
   * @param inet String The string of the local ip address.
   * @return boolean Returns if the socket is receiving data through the loopback.
   */
  private boolean compareAddresses(String socket,String inet){
    String s = socket.substring(socket.indexOf("/")+1,socket.indexOf(":"));
    String t = inet.substring(inet.indexOf("/")+1,inet.length());
    if(s.equalsIgnoreCase(t)){
      return true;
    }
    return false;
  }
  /**
   * This is the main method of the class and all network IO is processed here.  This sends network events
   * to all registered listeners.
   */
  public void run(){
    //Allocate buffers
    ByteBuffer dst = ByteBuffer.allocateDirect(2048);
    try {
      Selector selector = Selector.open();
      channel.register(selector, SelectionKey.OP_READ);

      while (selector.isOpen()) {
        selector.select(0);
        //Get set of ready objects
        Iterator it = selector.selectedKeys().iterator();
        //Look at each key in the set
        while (selector.selectedKeys().size() > 0) {
          SelectionKey key = (SelectionKey) it.next();
          if (key.isReadable()) {
            //read what is ready
            //Get Channel
            DatagramChannel dChannel = (DatagramChannel) key.channel();
            dst.clear();
            SocketAddress a = dChannel.receive(dst);
            dst.flip(); //Make buffer readable
            //If info is from local computer ignore it and break this while
            if (compareAddresses(a.toString(), inet)) {
              break;
            }
            String socketString = a.toString();

            //callback to listeners with data
            NetworkEvent e = new NetworkEvent(dst, NetworkEvent.udpEvent,socketString.substring(socketString.indexOf("/") + 1,socketString.indexOf(":")));
            fireUDPEvent(e);

          } //End if readable
          it.remove(); //Remove key it's been handled
        } //End while iterator
      } //End while selector
    }catch (IOException e) {System.out.println("IOException in UDPListen run(): " + e);}
  }
  /**
   * Utility method used to obtain the channel this class is using for UDP.
   * @return DatagramChannel Returns the class channel.
   */
  public DatagramChannel getChannel(){
    return channel;
  }
  /**
   * This method takes care of setting up all channels and buffers for UDP operation.
   * This method should be called first after creating any new UDP listeners.
   */
  public void setup(){
    try {
      inet = InetAddress.getLocalHost().toString();
      channel = channel.open();
      channel.configureBlocking(false); //Configure for non-blocking I/0
      DatagramSocket socket = channel.socket();
      InetSocketAddress isA = new InetSocketAddress(InetAddress.getByName("0.0.0.0"), port);
      socket.bind(isA);
    }catch (IOException ex) {System.out.println("IOException in UDPListen setup(): " + ex);}
  }
  /**
   * This method takes care of sending network events to all registered listeners.
   * @param e NetworkEvent The NetworkEvent that will be delievered to listeners.
   */
  private void fireUDPEvent(NetworkEvent e){
    int length = listeners.size();
    for(int i=0;i<length;i++)
      ((NetworkListener)listeners.get(i)).networkEventOccurred(e);
  }
  /**
   * Implementation side effect.
   * @param e NetworkEvent
   */
  public void networkEventOccurred(NetworkEvent e){}
}
