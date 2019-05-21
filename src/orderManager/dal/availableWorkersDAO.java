package orderManager.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import orderManager.be.IDepartment;
import orderManager.be.IDepartmentTask;
import orderManager.be.IWorker;
import orderManager.be.Worker;
import orderManager.dal.Connection.ConnectionPool;
import orderManager.dal.Connection.ConnectionProvider;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class availableWorkersDAO implements IDAODetails {
    private static ConnectionPool cp;
    private static Connection con;
    private boolean hasNewData;

    public availableWorkersDAO() {
        cp = new ConnectionPool();
    }

    public static List<IWorker> getWorkers() throws SQLException {
        con = cp.checkOut();
        List<IWorker> workers = new ArrayList<>();
        String sql = "SELECT * FROM AvailableWorkers";
        Statement st = con.createStatement();
        ResultSet rs =  st.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String initials = rs.getString(3);
            long salary = rs.getLong(4);
            IWorker w = new Worker(id,name,initials,salary);
            workers.add(w);
        }
        cp.checkIn(con);
        System.out.println(workers.size());
        return workers;
    }

    public static void addWorkerToDepartmentTask(int departmentID, int workerID) throws SQLException {
        con = cp.checkOut();
        String sql = "INSERT INTO TaskWorkers(DepartmentTaskID,WorkerID) SELECT ?,? WHERE NOT EXISTS(SELECT * FROM TaskWorkers WHERE DepartmentTaskID = ? AND WorkerID = ?)";
        PreparedStatement ppst = con.prepareStatement(sql);
        ppst.setInt(1, departmentID);
        ppst.setInt(2, workerID);
        ppst.setInt(3, departmentID);
        ppst.setInt(4, workerID);
        ppst.execute();
    }

    public static void removeWorkerFromDepartmentTask(int departmentID, int workerID) throws SQLException {
        con = cp.checkOut();
        String sql = "DELETE FROM TaskWorkers WHERE DepartmentTaskID = ? AND WorkerID = ?";
        PreparedStatement ppst = con.prepareStatement(sql);
        ppst.setInt(1, departmentID);
        ppst.setInt(2, workerID);
        ppst.execute();
    }

    @Override
    public List getDetails() throws SQLException {
        return getWorkers();
    }

    @Override
    public boolean hasNewData() {
        return hasNewData;
    }

    @Override
    public void setHasNewData(boolean hasNewData) {

    }
}
