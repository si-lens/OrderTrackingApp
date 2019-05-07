package orderManager.dal.Connection;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool extends ObjectPool<Connection> {

    private Connection con;
    private ConnectionProvider conProv;

    public ConnectionPool()
    {
        super();
        try {
            conProv = new ConnectionProvider();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Connection create() {
        try {
            con = conProv.getConnection();
        } catch (SQLServerException e) {
            e.printStackTrace();
        }
        return con;
    }

    @Override
    public boolean validate(Connection o) {
        try {
            return !o.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void expire(Connection o) {
        try {
            o.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
