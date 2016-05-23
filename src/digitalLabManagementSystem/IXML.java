package digitalLabManagementSystem;

/*
  Interface that XML plugins will need to conform to

*/
import java.io.File;
import java.util.Hashtable;

public interface IXML {
  public void setParseFile(File f);
  public Hashtable parseFile();
//  public static final IXML INSTANCE = (IXML)PluginFactory.getPlugin(IXML.class);
}
