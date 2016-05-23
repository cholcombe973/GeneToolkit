package digitalLabManagementSystem;
/*
  Interface that database plugins will conform to
*/
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.*;

public interface IDatabase {
  public void setPropertiesFile(File f);
  public void prepareStatement(String statement);
  public void setConnectionString(String connection);
  public String getConnectionString();
  public void setDriver(String driver);
  public String getDriver();
  public ResultSet executePreparedQuery();
  public int executePreparedUpdate();

  //all stuff inherited from PreparedStatement
  public void setArray(int i, Array x);
  public void setAsciiStream(int parameterIndex, InputStream x, int length);
  public void setBigDecimal(int parameterIndex, BigDecimal x);
  public void setBinaryStream(int parameterIndex, InputStream x, int length);
  public void setBlob(int i, Blob x);
  public void setBoolean(int parameterIndex, boolean x) ;
  public void setByte(int parameterIndex, byte x) ;
  public void setBytes(int parameterIndex, byte[] x) ;
  public void setCharacterStream(int parameterIndex, Reader reader, int length) ;
  public void setClob(int i, Clob x) ;
  public void setDate(int parameterIndex, java.sql.Date x) ;
  public void setDate(int parameterIndex, java.sql.Date x, Calendar cal) ;
  public void setDouble(int parameterIndex, double x) ;
  public void setFloat(int parameterIndex, float x) ;
  public void setInt(int parameterIndex, int x) ;
  public void setLong(int parameterIndex, long x) ;
  public void setNull(int parameterIndex, int sqlType) ;
  public void setNull(int paramIndex, int sqlType, String typeName) ;
  public void setObject(int parameterIndex, Object x) ;
  public void setObject(int parameterIndex, Object x, int targetSqlType) ;
  public void setObject(int parameterIndex, Object x, int targetSqlType,int scale) ;
  public void setRef(int i, Ref x) ;
  public void setShort(int parameterIndex, short x) ;
  public void setString(int parameterIndex, String x) ;
  public void setTime(int parameterIndex, Time x) ;
  public void setTime(int parameterIndex, Time x, Calendar cal) ;
  public void setTimestamp(int parameterIndex, Timestamp x) ;
  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) ;
  public void setURL(int parameterIndex, URL x) ;

  public void connect();
  public void disconnect();
}
