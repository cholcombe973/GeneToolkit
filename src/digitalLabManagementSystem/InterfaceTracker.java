package digitalLabManagementSystem;

/**
 *
 * <p>Title: Interface</p>
 *
 * <p>Description: The purpose of this class is to keep track of dynamically loaded interfaces.
 * It's job is to hold all the information that is efficiently possible to load.  As powerful as this class
 * is, it will be transparent to users of the architecture.  All plugins or modules will still be
 * routed through the ModuleManager.
 *
 * *****NEW IDEA!***  Dynamic library integration so that static linking can occur and everything appears normal.
 * </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 *
 * @author Chris Holcombe
 * @version 1.0
 */
import java.lang.reflect.*;
import java.util.Hashtable;
import static digitalLabManagementSystem.Constants.*;

public class InterfaceTracker {
  //we need to map interfaces herr
  //so and interface will have a property file -> classname
  private Hashtable ht = new Hashtable();


  public InterfaceTracker() {

  }
  /**
   * Returns the class reference of the class the interface is mapped to.
   * @param interfaceName String
   * @return Class
   */
  public Class requestInterface(String interfaceName){
    Object[] values = (Object[])ht.get(interfaceName);
    return (Class)values[3];
  }
  /**
   *
   * @param interfaceName String
   * @return Method[]
   */
  public Method[] requestDeclaredMethods(String interfaceName){
    return (Method[])((Object[])ht.get(interfaceName))[1];
  }
  /**
   *
   * @param interfaceName String
   * @return Package
   */
  public Package requestInterfacePackage(String interfaceName){
    return (Package)((Object[])ht.get(interfaceName))[2];
  }
  /**
   *
   * @param interfaceName String
   * @return String
   */
  public String requestMappedClassName(String interfaceName){
    return (String)((Object[])ht.get(interfaceName))[0];
  }

  public void storeInterfaceData(String interfaceName,String classMappedToName,Method[] declaredMethods,Package classPackage,Class interfaceClass){
      //***Think about how you want to store this ***
    ht.put(interfaceName,new Object[]{classMappedToName,declaredMethods,classPackage,interfaceClass});
  }
  public Object dynamicInvoke(String interfaceName,String methodName){
    //take the method argument and invoke it on the underlying class, return object result
    Object[] metaData = (Object[])ht.get(interfaceName);
    Method[] declaredMethods = (Method[])metaData[1];
    Class interfaceClass = (Class)metaData[3];

    for(int i=0;i<declaredMethods.length;i++){
      Method m = declaredMethods[i];
      if(m.getName().equalsIgnoreCase(methodName)){
        try {
          m.invoke(interfaceClass, new Object[] {});
        }
        catch (InvocationTargetException ex) {System.out.println("");}
        catch (IllegalArgumentException ex) {System.out.println("");}
        catch (IllegalAccessException ex) {System.out.println("");}
        break;
      }
    }
    return null;
  }

}
