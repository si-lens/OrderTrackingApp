package orderManager.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import orderManager.be.Worker;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        PreparedStatement ppst = con.prepareStatement(sql);
        ResultSet rs = ppst.executeQuery();

        while (rs.next()) {
            String type = rs.getString(1);
            String initials = rs.getString(2);
            String name = rs.getString(3);
            long salary = rs.getLong(4);
            //int salary = rs.getInt(4);
            int id = rs.getInt(5);
            Worker w = new Worker(type, initials, name, salary, id);
            System.out.println("salary "+ salary );
            workers.add(w);
        }
        System.out.println(workers.size());
        return workers;
    }


}
