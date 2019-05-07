package orderManager.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import orderManager.be.Worker;
import orderManager.dal.Connection.ConnectionPool;
import orderManager.dal.Connection.ConnectionProvider;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class availableWorkersDAO {
    ConnectionPool cp;
    Connection con = null;

    public availableWorkersDAO() throws IOException, SQLServerException {
        cp = new ConnectionPool();
    }

    public List<Worker> getWorkers() throws SQLException {
        con = cp.checkOut();
        List<Worker> workers = new ArrayList<>();
        String sql = "SELECT * FROM AvailableWorkers";
        Statement st = con.createStatement();
        ResultSet rs =  st.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String initials = rs.getString(3);
            long salary = rs.getLong(4);
            Worker w = new Worker(id,name,initials,salary);
            workers.add(w);
        }
        cp.checkIn(con);
        System.out.println(workers.size());
        return workers;
    }


}
