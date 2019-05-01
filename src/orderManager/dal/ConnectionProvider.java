package orderManager.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author leopo
 */
public class ConnectionProvider {

  private final SQLServerDataSource ds;

  public ConnectionProvider() throws IOException {
    Properties databaseProperties = new Properties();
    databaseProperties.load(new FileInputStream("src\\orderManager\\dal\\credentials.txt"));
    ds = new SQLServerDataSource();
    ds.setServerName(databaseProperties.getProperty("ServerName"));
    ds.setDatabaseName(databaseProperties.getProperty("DatabaseName"));
    ds.setUser(databaseProperties.getProperty("Login"));
    ds.setPassword(databaseProperties.getProperty("Password"));
  }

  public Connection getConnection() throws SQLServerException {
    return ds.getConnection();
  }
}
