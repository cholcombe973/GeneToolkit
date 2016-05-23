package digitalLabManagementSystem;

/**
   Handles all details with References
   This Engine should be started 1st
 */

import java.util.Hashtable;

public class ReferenceEngine {
  private Hashtable ht = new Hashtable(50);

  public Object getReference(String className){
    if(ht.get(className)==null)
      throw new NullPointerException("Null Pointer Exception: Reference table returned null for:" + className);
    return ht.get(className);
  }
  public void putReference(String className,Object ref){
    ht.put(className,ref);
  }
}
