package orderManager.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import orderManager.be.Worker;
import orderManager.dal.Connection.ConnectionProvider;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class availableWorkersDAO {
    ConnectionProvider cp;
    Connection con;

    public availableWorkersDAO() throws IOException, SQLServerException {
        cp = new ConnectionProvider();
        con = cp.getConnection();
    }

    public List<Worker> getWorkers() throws SQLException {
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
        System.out.println(workers.size());
        return workers;
    }


}
