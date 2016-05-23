package digitalLabManagementSystem;


import java.io.*;
import java.util.*;
import org.kxml2.io.*;
import org.xmlpull.v1.*;

/**
 *
 * <p>Title: XMLModule</p>
 * <p>Description: This is a key module to the functioning of the framework.  It handles the parsing
 * of all the xml files. The infinite possible heirarchy of xml is confined to key value pairs in this
 * implementation.  The reason being is that the underlying storage engine is a hashtable.
 * </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author Chris Holcombe
 * @version 1.0
 */
public class XMLModule extends Module implements IXML{
  private File storageFile;
  private ReferenceEngine re;
  private Hashtable tags = new Hashtable();
  //TODO: Create a new implementation of hashtable to map a key to multiple values
  private ModuleManager ref;
  private KXmlParser parse = new KXmlParser();

  public XMLModule(ReferenceEngine re) {
    this.re = re;
  }
  public void initModule(){
    setName("XML Module");
    setAuthor("Chris Holcombe");
    setDescription("This module handles XML parsing");
    setVersion("1.0");
    ref = (ModuleManager)re.getReference("ModuleManager");
    setModulePID(ref.getPID());
    ref.registerModule(".xml",this);
  }
  public void cleanupModule(){
    if(ref!=null)
      ref.unregisterModule(".xml");
  }
  public Hashtable parseFile(){
    try {
      parse.setInput(new FileReader(storageFile));
      parse.nextTag();
      parse.require(XmlPullParser.START_TAG, null, "Module");
      while (parse.nextTag() == XmlPullParser.START_TAG)
        tags.put(parse.getName(),parse.nextText());
      parse.require(XmlPullParser.END_TAG, null, "Module");
      return tags;
    }catch (IOException ex) {System.out.println("IOException in XMLMOdule parseFile(): " + ex);}
    catch (XmlPullParserException ex) {System.out.println("XMLPullParserException in XMLModule parseFile(): " + ex);}
    return null;
  }
  public void setParseFile(File storageFile){
    this.storageFile = storageFile;
  }

}
