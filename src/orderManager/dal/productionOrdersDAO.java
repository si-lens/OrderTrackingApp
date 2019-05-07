package orderManager.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import orderManager.be.Customer;
import orderManager.be.Delivery;
import orderManager.be.Department;
import orderManager.be.DepartmentTask;
import orderManager.be.ICustomer;
import orderManager.be.IDelivery;
import orderManager.be.IDepartment;
import orderManager.be.IDepartmentTask;
import orderManager.be.IOrder;
import orderManager.be.IProductionOrder;
import orderManager.be.Order;
import orderManager.be.ProductionOrder;
import orderManager.dal.Connection.ConnectionPool;
import orderManager.dal.Connection.ConnectionProvider;

public class productionOrdersDAO {

  ConnectionPool cp;
  Connection con = null;

  public productionOrdersDAO() throws SQLServerException, IOException {
    cp = new ConnectionPool();
  }

  public List<IDepartment> getDepartments() throws SQLException {
    con = cp.checkOut();
    List<IDepartment> departments = new ArrayList<>();
    String sql = "SELECT DISTINCT Name FROM Departments";
    Statement st = con.createStatement();
    ResultSet rs = st.executeQuery(sql);
    while (rs.next()) {
      String name = rs.getString("Name");
      IDepartment d = new Department(name);
      departments.add(d);
    }
    cp.checkIn(con);
    return departments;
  }

  public List<IProductionOrder> getProdutcionOrders() throws SQLException {
    con = cp.checkOut();
    List<IProductionOrder> pOrders = new ArrayList<>();
    String sql = "SELECT * FROM ProductionOrders";
    Statement st = con.createStatement();
    ResultSet rs = st.executeQuery(sql);
    while (rs.next()) {
      int id = rs.getInt("ID");
      IProductionOrder po = new ProductionOrder(id);
      pOrders.add(po);
    }
    cp.checkIn(con);
    return pOrders;
  }

  public List<ICustomer> getCustomers() throws SQLException {
    con = cp.checkOut();
    List<ICustomer> customers = new ArrayList<>();
    String sql = "SELECT * FROM Customers";
    Statement st = con.createStatement();
    ResultSet rs = st.executeQuery(sql);
    while (rs.next()) {
      String name = rs.getString("Name");
      int id = rs.getInt("ProductionOrderID");
      ICustomer c = new Customer(name, id);
      customers.add(c);
    }
    cp.checkIn(con);
    return customers;
  }

  public List<IDelivery> getDeliveries() throws SQLException {
    con = cp.checkOut();
    List<IDelivery> delivery = new ArrayList<>();
    String sql = "SELECT * FROM Delivery";
    Statement st = con.createStatement();
    ResultSet rs = st.executeQuery(sql);
    while (rs.next()) {
      Date deliveryTime = rs.getDate("DeliveryTime");
      int id = rs.getInt("ProductionOrderID");
      IDelivery d = new Delivery(id, deliveryTime);
      delivery.add(d);
    }
    cp.checkIn(con);
    return delivery;
  }

  public List<IDepartmentTask> getDepartmentTasks() throws SQLException {
    con = cp.checkOut();
    List<IDepartmentTask> departmentTasks = new ArrayList<>();
    String sql = "SELECT * FROM DepartmentTasks";
    Statement st = con.createStatement();
    ResultSet rs = st.executeQuery(sql);
    while (rs.next()) {
      int id = rs.getInt("ID");
      Date endDate = rs.getDate("EndDate");
      Date startDate = rs.getDate("StartDate");
      boolean finishedOrder = rs.getBoolean("FinishedOrder");
      int productionOrderID = rs.getInt("ProductionOrderID");
      IDepartmentTask department = new DepartmentTask(id, productionOrderID, finishedOrder,
          startDate, endDate);
      departmentTasks.add(department);
    }
    cp.checkIn(con);
    return departmentTasks;
  }

  public List<IOrder> getOrders() throws SQLException {
    con = cp.checkOut();
    List<IOrder> orders = new ArrayList<>();
    String sql = "SELECT * FROM Orders";
    Statement st = con.createStatement();
    ResultSet rs = st.executeQuery(sql);

    while (rs.next()) {
      int id = rs.getInt("ProductionOrderID");
      String orderNumber = rs.getString("OrderNumber");
      IOrder order = new Order(orderNumber, id);
      orders.add(order);
    }
    cp.checkIn(con);
    return orders;
  }


}
