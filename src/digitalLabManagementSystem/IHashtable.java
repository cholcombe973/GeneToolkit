package digitalLabManagementSystem;

import java.util.ArrayList;

public interface IHashtable {
  public void clear();
  public boolean containsKey(Object key);
  public boolean containsValue(Object value);
  public ArrayList elements();
  public Object[] get(Object key);
  public void put(Object key, Object[] values);
  public int size();
  public String toString();
}
