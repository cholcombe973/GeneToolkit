package digitalLabManagementSystem;

import java.util.Date;

public interface IAlarm {
  public void setAlarm(Date time,final String message);
  public void setRepeatingAlarm(Date time, final String message,final int repeatTimes,long delay);
}
