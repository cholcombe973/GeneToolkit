package digitalLabManagementSystem;


import java.net.*;
import java.nio.*;

public interface INetwork {
  public void openConnection(InetSocketAddress remote);
  public void closeConnection();
  public void readData(ByteBuffer data);
  public void readData(ByteBuffer[] data);
  public void readData(byte[] Data);
  public void writeData(ByteBuffer data);
  public void writeData(ByteBuffer[] data);
  public void writeData(byte[] data);

}
