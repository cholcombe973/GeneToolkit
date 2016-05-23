package digitalLabManagementSystem;

/*
   Interface that loggin plugin's need to conform to
*/
public interface ILog {
  public void logSevereMsg(String msg);
  public void logConfigMsg(String msg);
  public void logWarningMsg(String msg);
//  public static final ILog INSTANCE = (ILog)PluginFactory.getPlugin(ILog.class);
}
