package digitalLabManagementSystem;

public interface IEnvironmentProperties {
  public void registerSystemProperty(String envKey, String envValue);
  public void unregisterSystemProperty(String envKey);
  public String querySystemProperty(String envKey);
}
