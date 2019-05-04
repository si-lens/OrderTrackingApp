package orderManager.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import orderManager.be.Customer;
import orderManager.be.Delivery;
import orderManager.be.ProductionOrder;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class productionOrdersDAO {
    ConnectionProvider cp;
    Connection con;

    public productionOrdersDAO() throws SQLServerException, IOException {
        cp = new ConnectionProvider();
        con = cp.getConnection();
    }

/*
    public List<ProductionOrder> getProdutcionOrders() throws SQLException {

        List<ProductionOrder> pOrders = new ArrayList<>();
        String sql = "SELECT * FROM ProductionOrders";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt("ID");
            String type = rs.getString("Type");
            ProductionOrder po = new ProductionOrder(id,type);
            pOrders.add(po);
        }
        return pOrders;
    }

    public List<Customer> getCustomers() throws SQLException {

        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customers";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            String type = rs.getString("Type");
            String name = rs.getString("Name");
            int id = rs.getInt("ID");
            Customer c = new Customer(type,name,id);
            customers.add(c);
        }
        return customers;
    }

    public List<Delivery> getDeliveries() throws SQLException {

        List<Delivery> delivery = new ArrayList<>();
        String sql = "SELECT * FROM Delivery";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            String type = rs.getString("Type");
            Date deliveryTime = rs.getDate("DeliveryTime");
            int id = rs.getInt("ID");
            Delivery d = new Delivery(type,deliveryTime,id);
            delivery.add(d);
        }
        return delivery;
    }

/*
    public List<DepartmentTask> getDepartmentTasks() throws SQLException {

        List<DepartmentTask> departmentTasks = new ArrayList<>();
        String sql = "SELECT * FROM DepartmentTasks";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt("ID");
            String type = rs.getString("Type");
            Date endDate = rs.getDate("EndDate");
            Date startDate = rs.getDate("StartDate");
            boolean finishedOrder = rs.getBoolean("FinishedOrder");
            int productionOrderID = rs.getInt("ProductionOrderID");

        }
        return
    }
*/





}
