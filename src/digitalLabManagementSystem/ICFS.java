package digitalLabManagementSystem;

import java.util.Hashtable;

public interface ICFS {
  public Hashtable getMetaData();
  public double get(long position);
  public void store(double d, long position);
  public void fileSystemDump();
  public void storeMetaData(String frameID,boolean data);
  public void storeMetaData(String frameID,byte data);
  public void storeMetaData(String frameID,float data);
  public void storeMetaData(String frameID,short data);
  public void storeMetaData(String frameID,int data);
  public void storeMetaData(String frameID,char data);
  public void storeMetaData(String frameID, String data);
  public void storeMetaData(String frameID,double data);
  public void storeMetaData(String frameID,long data);
  public void createMetaDataFooter();
  public void parseCFS();
  public void shutdown();
}
