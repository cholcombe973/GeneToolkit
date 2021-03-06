package parallelComputingLib;

import java.io.*;
import java.util.Hashtable;
/**
 * <p>Title: CFS(Chris File System)</p>
 *
 * <p>Description: The CFS file system is a very simple file system used to store results.<br>
 * To store numbers, supply an id and a number to store.  The id
 * is used to locate the memory block to set.  All blocks are 8 bytes long for
 * maxmium storage capacity.
 * <br>
 *   Ex: store(1,53084);
 * <br>
 *   CFS will go to block 1 and store 53084.
 * </p>
 *<p>
 * In order for CFS to store complex data formats meta data tagging is used.  Tags built very similar to the
 * ID3v2 tags of MP3's was used so that a variable amount of data can be prepended to a data file. Tags can contain
 * information for example about how software should interpret this particular file when parsing.
 * All ID3v2 frames consist of one frame header followed by one or more
 * fields containing the actual information.
 * <br><br>Overall Tag Structure:
 * <br>+-----------------------------+
 * <br>&nbsp|&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp Header (5 bytes)&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp|
 * <br>+-----------------------------+
 * <br>&nbsp|&nbsp&nbsp&nbsp Frames (variable length)&nbsp&nbsp&nbsp&nbsp|
 * <br>+-----------------------------+
 * <br>The frame header is always 15 bytes and laid out as follows:
 * <br><br>
 * <br>Frame ID [3 chars]
 * <br>Data ID [1 byte]
 * <br>Size&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp[8 byte long]
 * <br>Data &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp[...]
 * <br><br>
 * The frame ID is made out of the characters capital A-Z and 0-9 and is 3 characters or less long always.  If
 * a frame ID is longer than 3 characters it will be silently ignored and nothing will be written.
 * Bear in mind that someone else might have used the same identifier as you.
 * All other identifiers are either used or reserved for future use.
 * The data ID is used for parsers to signal how to interpret the data contained in the frame.  Data ID's are provided
 * by this class.
 * <br><br>
 * The frame ID is followed by a size descriptor containing the size of
 * the data in the final frame. The size is excluding the frame header ('total
 * frame size' - 14 bytes) and is stored as an 8 byte long. There's no need to worry about implementation details
 * they were provided for clarity.  Frame size, and location is determined by the class automatically.
 * <br><br><b>Important!</b>All calls for storing meta data must be made before storing any data in the file because
 * metadata is prepended to the file.  Then after all meta data calls are complete
 * you must call createMetaDataFooter() so that the file will parse correctly.
 * File corruption will occur if this procedure is not followed.
 * <br><br>When parsing a CFS file, a frameID is read in and if it's not of interest to the parser, read the data
 * size in and seek past the frame. Size will also tell you where the next frame begins in the data.
 * </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author Chris Holcombe
 * @version 1.0
 */
public class CFS {
  private File storageFile;
  private RandomAccessFile rf;
  private long internalPointer = 0;
  public static final byte BYTE    = 0;
  public static final byte BOOLEAN = 1;
  public static final byte CHAR    = 2;
  public static final byte DOUBLE  = 3;
  public static final byte FLOAT   = 4;
  public static final byte INT     = 5;
  public static final byte LONG    = 6;
  public static final byte SHORT   = 7;
  public static final byte STRING  = 8;
  private Hashtable metaData = new Hashtable();

  /**
   *
   * @param storageFile String to a file that you wish to use as a data storage file. Doesn't have to exist yet.
   */

  public CFS(String storageFile) {
    this.storageFile = new File(storageFile);
    setupStorageFile();
    createHeader();

  }
  /**
   * This method creates the header for the ID3v2 tag.
   */
  private void createHeader(){
    try {
      rf.seek(0); //header starts the file off
      rf.write((byte)'T');
      rf.write((byte)'A');
      rf.write((byte)'G');
      rf.write(1); //major version number
      rf.write(0); //minor version number
      internalPointer = 5;
    }catch (IOException ex) {System.out.println("IOException in createHeader(): " + ex);}
  }
  /**
   * This method is called initially to open the necessary files to begin storing data.
   *
   */
  private void setupStorageFile(){
    try {
      if(!storageFile.exists()){
        storageFile.createNewFile();
      }
      rf = new RandomAccessFile(storageFile,"rw");
    }catch (IOException ex) {System.out.println("FileNotFoundException in cfs setupStorageFile(): " + ex);}
  }
  /**
   * This method will return the Hashtable that was generated by calling parseCFS().  If parseCFS() was not called
   * before this method it will still return an empty hashtable.  If you are using meta data and are not getting
   * any results back check to see if the file was parsed before called this method.
   * @return Hashtable The Hashtable containing all meta data in key value pairs.
   */
  public Hashtable getMetaData(){
    return metaData;
  }
  /**
   * This method looks up and returns a double at whatever position is supplied to it.
   * <br>This method is synchronized and is safe for multi-threaded use.
   * @param position long - This method will use any number supplied assuming it's before the End of File marker.
   * @return double - Returns the double at the position supplied.  Returns zero if IOException is thrown
   */
  public synchronized double get(long position){
    try {
      rf.seek(internalPointer+(position*8));
      return rf.readDouble();
    }catch (IOException ex) {System.out.println("IOException in cfs read(): " + ex);}
    return 0;
  }
  /**
   * Method stores a supplied double at any position in the file.  The position supplied to this method must be a multiple of 8
   * or none of the numbers will be stored correctly.  No checking has been implemented yet for speed's sake.  Only doubles
   * are being stored right now.  This method is synchronized and is safe for multi-threaded use.
   * @param d double - Any double you would like to have saved in a file
   * @param position long - The position to store the double at.  Must be a multiple of 8.
   */
  public synchronized void store(double d, long position){
    try {
      rf.seek(internalPointer+(position*8));
      rf.writeDouble(d);
    }catch (IOException e) {System.out.println("IOException in cfs store: " + e);}
  }
  /**
   * Debug method used to view in the console all the values currently stored in the file system.
   * This method prints to System.out as the default.
   */
  public void fileSystemDump(){
    System.out.println("::Dumping File System::");
    try {
      System.out.println("File length: " + rf.length());
      //the ID3v2 tag needs to be dumped also..

      //the ID3v2 tag needs to be dumped also..
      int loops = (int) (rf.getFilePointer()-internalPointer) / 8;
      for(int i=0;i<loops;i++)
        System.out.println("Positon: [" + (internalPointer+(i*8)) + "] " + get(i));
    }catch (IOException ex) {System.out.println("IOException in cfs fileSystemDump(): " + ex);}
  }
  /**
   * This method stores data in a frame given a Frame ID 3 letters or less.
   * @param frameID String The Frame ID.
   * @param data boolean The data to store in the frame.
   */
  public void storeMetaData(String frameID,boolean data){
    try{
      if(frameID.length()<=3){
        rf.writeChars(frameID);
        rf.write(CFS.BOOLEAN);
        rf.writeLong(1);
        rf.write(data ? 1 : 0);
        internalPointer+=16;
      }
    }catch(IOException ex){System.out.println("IOException in storeMetaData(String frameID, boolean data): " + ex);}
  }
  /**
   * This method stores data in a frame given a Frame ID 3 letters or less.
   * @param frameID String The Frame ID.
   * @param data byte The data to store in the frame.
   */
  public void storeMetaData(String frameID,byte data){
    try{
      if(frameID.length()<=3){
        rf.writeChars(frameID);
        rf.write(CFS.BYTE);
        rf.writeLong(1);
        rf.write(data);
        internalPointer+=16;
      }
    }catch(IOException ex){System.out.println("IOException in storeMetaData(String frameID, byte Data): " + ex);}
  }
  /**
   * This method stores data in a frame given a Frame ID 3 letters or less.
   * @param frameID String The Frame ID.
   * @param data float The data to store in the frame.
   */
  public void storeMetaData(String frameID,float data){
    try{
      if(frameID.length()<=3){
        rf.writeChars(frameID);
        rf.write(CFS.FLOAT);
        rf.writeLong(4);
        rf.writeFloat(data);
        internalPointer+=19;
      }
    }catch(IOException ex){System.out.println("IOException in storeMetaData(String frameID, short Data): " + ex);}
  }
  /**
   * This method stores data in a frame given a Frame ID 3 letters or less.
   * @param frameID String The Frame ID.
   * @param data short The data to store in the frame.
   */
  public void storeMetaData(String frameID,short data){
    try{
      if(frameID.length()<=3){
        rf.writeChars(frameID);
        rf.write(CFS.SHORT);
        rf.writeLong(2);
        rf.write( (data >>> 8) & 0xFF);
        rf.write( (data >>> 0) & 0xFF);
        internalPointer+=17;
      }
    }catch(IOException ex){System.out.println("IOException in storeMetaData(String frameID, short Data): " + ex);}
  }

  /**
   * This method stores data in a frame given a Frame ID 3 letters or less.
   * @param frameID String The Frame ID.
   * @param data int The data to store in the frame.
   */
  public void storeMetaData(String frameID,int data){
    try{
      if (frameID.length() <= 3) {
        rf.writeChars(frameID);
        rf.write(CFS.INT);
        rf.writeLong(4);
        rf.write((data >>> 24) & 0xFF);
        rf.write((data >>> 16) & 0xFF);
        rf.write((data >>>  8) & 0xFF);
        rf.write((data >>>  0) & 0xFF);
        internalPointer+=19;
      }
    }catch (IOException ex) {System.out.println("IOException in storeMetaData(String frameID, int Data): " + ex);}
  }
  /**
   * This method stores data in a frame given a Frame ID 3 letters or less.
   * @param frameID String The Frame ID.
   * @param data char The data to store in the frame.
   */
  public void storeMetaData(String frameID,char data){
    try {
      if(frameID.length()<=3){
        rf.writeChars(frameID);
        rf.write(CFS.CHAR);
        rf.writeLong(2);
        rf.write((data >>> 8) & 0xFF);
        rf.write((data >>> 0) & 0xFF);
        internalPointer+=17;
      }
    }catch (IOException ex) {System.out.println("IOException in storeMetaData(String frameID, char Data): " + ex);}
  }
  /**
   * This method stores data in a frame given a Frame ID 3 letters or less.
   * @param frameID String The Frame ID.
   * @param data String Teh data to store in the frame.
   */
  public void storeMetaData(String frameID, String data){
    try {
      if(frameID.length()<=3){
        rf.writeChars(frameID);
        rf.write(CFS.STRING);
        int clen = data.length();
        int blen = 2 * clen;
        rf.writeLong(blen);
        byte[] b = new byte[blen];
        char[] c = new char[clen];
        data.getChars(0, clen, c, 0);
        for (int i = 0, j = 0; i < clen; i++) {
          b[j++] = (byte) (c[i] >>> 8);
          b[j++] = (byte) (c[i] >>> 0);
        }
        rf.write(b, 0, blen);
        internalPointer+=(15+blen);
      }
    }catch (IOException ex) {System.out.println("IOException in storeMetaData(String frameID, String Data): " + ex);}
  }
  /**
   * This method stores data in a frame given a Frame ID 3 letters or less.
   * @param frameID String Teh Frame ID.
   * @param data double The data to store in the frame.
   */
  public void storeMetaData(String frameID,double data){
    if(frameID.length()<=3){
      try {
        rf.writeChars(frameID);
        rf.write(CFS.DOUBLE);
        rf.writeLong(8);
        rf.writeDouble(data);
        internalPointer+=23;
      }catch (IOException ex) {System.out.println("IOException in storeMetaData(String frameID, double Data): " + ex);}
    }
  }
  /**
   * This method stores data in a frame given a Frame ID 3 letters or less.
   * @param frameID String The Frame ID.
   * @param data long The data to store in the frame.
   */
  public void storeMetaData(String frameID,long data){
    if(frameID.length()<=3){
      try {
        rf.writeChars(frameID);
        rf.write(CFS.LONG);
        rf.writeLong(8);
        rf.writeLong(data);
        internalPointer+=23;
      }catch (IOException ex) {System.out.println("IOException in storeMetaData(String frameID, long Data): " + ex);}
    }
  }
  /**
   * This method creates the trailing footer for the tag system.  This must be called right before inserting
   * any data into the file so that the parser knows where the tags stop and the data begins.
   */
  public void createMetaDataFooter(){
    try{
      rf.writeChars("END");
      rf.write(CFS.BYTE);
      rf.writeLong(1);
      rf.writeBoolean(false);
      internalPointer+=16;
    }catch(IOException e){System.out.println("IOException in createMetaDataFooter(): " + e);}
  }
  /**
   * After calling parseCFS all meta data can be accessed through the resulting Hashtable.  This data can be very helpful
   * when processing the data contained in the file.
   */
  public void parseCFS(){
    try{
      rf.seek(5); //starting point after the header
      char[] id = new char[3];
      while(true){
        // 1.parse the frameID
        id[0] = (char)((rf.read() << 8) + (rf.read() << 0));
        id[1] = (char)((rf.read() << 8) + (rf.read() << 0));
        id[2] = (char)((rf.read() << 8) + (rf.read() << 0));

        if(id[0]=='E'&&id[1]=='N'&&id[2]=='D'){
          break;
        }else{
          // 2.parse the data type
          switch(rf.read()){
            case CFS.BOOLEAN:
              rf.seek(rf.getFilePointer()+8);
              metaData.put(new String(id),new Boolean(rf.read()==0?false:true));
              break;
            case CFS.BYTE:
              rf.seek(rf.getFilePointer()+8);
              metaData.put(new String(id),new Byte((byte)rf.read()));
              break;
            case CFS.CHAR:
              rf.seek(rf.getFilePointer()+8);
              metaData.put(new String(id),new Character((char)((rf.read() << 8) + (rf.read() << 0))));
              break;
            case CFS.DOUBLE:
              rf.seek(rf.getFilePointer()+8);
              metaData.put(new String(id),new Double(rf.readDouble()));
              break;
            case CFS.FLOAT:
              rf.seek(rf.getFilePointer()+8);
              metaData.put(new String(id),new Float(rf.readFloat()));
              break;
            case CFS.INT:
              rf.seek(rf.getFilePointer()+8);
              metaData.put(new String(id),new Integer(((rf.read() << 24) + (rf.read() << 16) + (rf.read() << 8) + (rf.read() << 0))));
              break;
            case CFS.LONG:
              rf.seek(rf.getFilePointer()+8);
              metaData.put(new String(id),new Long(rf.readLong()));
              break;
            case CFS.SHORT:
              rf.seek(rf.getFilePointer()+8);
              metaData.put(new String(id),new Short((short)((rf.read() << 8) + (rf.read() << 0))));
              break;
            case CFS.STRING:
              long size = ((long)(rf.readInt()) << 32) + (rf.readInt() & 0xFFFFFFFFL);
              char[] buff = new char[(int)size];
              for(int i=0;i<size;i++){
                buff[i] = (char)((rf.read() << 8) + (rf.read() << 0));
              }
              metaData.put(new String(id),new String(buff));
              break;
          }
        }
      }
    }catch(IOException e){System.out.println("IOException in parseCFS: " + e);}
  }
  /**
   * Method called just before closing down the file system.  This will close all open connections to the underlying storage drive.
   */
  public void shutdown(){
    try {
      rf.close();
    }catch (IOException ex) {System.out.println("IOException in cfs shutdown(): " + ex);}
  }
}
