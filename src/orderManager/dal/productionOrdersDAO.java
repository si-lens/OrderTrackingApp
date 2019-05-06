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

public class productionOrdersDAO {

  ConnectionProvider cp;
  Connection con;

  public productionOrdersDAO() throws SQLServerException, IOException {
    cp = new ConnectionProvider();
    con = cp.getConnection();
  }

  public List<IDepartment> getDepartments() throws SQLException {
    List<IDepartment> departments = new ArrayList<>();
    String sql = "SELECT DISTINCT Name FROM Departments";
    Statement st = con.createStatement();
    ResultSet rs = st.executeQuery(sql);
    while (rs.next()) {
      String name = rs.getString("Name");
      int id = rs.getInt("DepartmentTaksID");
      IDepartment d = new Department(name, id);
      departments.add(d);
    }

    return departments;
  }

  public List<IProductionOrder> getProdutcionOrders() throws SQLException {

    List<IProductionOrder> pOrders = new ArrayList<>();
    String sql = "SELECT * FROM ProductionOrders";
    Statement st = con.createStatement();
    ResultSet rs = st.executeQuery(sql);
    while (rs.next()) {
      int id = rs.getInt("ID");
      IProductionOrder po = new ProductionOrder(id);
      pOrders.add(po);
    }
    return pOrders;
  }

  public List<ICustomer> getCustomers() throws SQLException {

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
    return customers;
  }

  public List<IDelivery> getDeliveries() throws SQLException {

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
    return delivery;
  }

  public List<IDepartmentTask> getDepartmentTasks() throws SQLException {

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
    return departmentTasks;
  }

  public List<IOrder> getOrders() throws SQLException {

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
    return orders;
  }


}
