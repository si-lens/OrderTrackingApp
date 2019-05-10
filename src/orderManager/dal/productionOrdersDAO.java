package orderManager.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
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

public class productionOrdersDAO implements IDAODetails {

  private ConnectionPool cp;
  private Connection con = null;
  private boolean hasNewData;

  public productionOrdersDAO() {
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

  public List<IProductionOrder> getProdutcionOrders(IDepartment department) throws SQLException {
    con = cp.checkOut();
    List<IProductionOrder> pOrders = new ArrayList<>();
    String sql = "SELECT ProductionOrders.ID, CustomerID, DeliveryID, OrderID FROM ProductionOrders,DepartmentTasks,Departments WHERE ProductionOrders.ID = DepartmentTasks.ProductionOrderID AND Departments.ID = DepartmentTasks.DepartmentID AND Name = ?";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setString(1, department.getName());
    ResultSet rs = ppst.executeQuery(sql);
    while (rs.next()) {
      int id = rs.getInt("ID");
      int customerID = rs.getInt("CustomerID");
      int deliveryID = rs.getInt("DeliveryID");
      int orderID = rs.getInt("OrderID");
      IProductionOrder po = new ProductionOrder(id, getCustomer(customerID),
          getDelivery(deliveryID), getOrder(orderID));
      pOrders.add(getDepartmentTasks(id, po));
    }
    cp.checkIn(con);
    return pOrders;
  }

  public ICustomer getCustomer(int customerID) throws SQLException {
    con = cp.checkOut();
    String sql = "SELECT * FROM Customers WHERE ID = ?";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setInt(1, customerID);
    ResultSet rs = ppst.executeQuery(sql);
    rs.next();
    int id = rs.getInt("ID");
    String name = rs.getString("Name");
    cp.checkIn(con);
    return new Customer(id, name);
  }

  public IDelivery getDelivery(int deliveryID) throws SQLException {
    con = cp.checkOut();
    String sql = "SELECT * FROM Deliveries WHERE ID = ?";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setInt(1, deliveryID);
    ResultSet rs = ppst.executeQuery(sql);
    rs.next();
    int id = rs.getInt("ID");
    Date date = rs.getDate("DeliveryDate");
    cp.checkIn(con);
    return new Delivery(id, date);
  }

  public IOrder getOrder(int orderID) throws SQLException {
    con = cp.checkOut();
    String sql = "SELECT * FROM Orders WHERE ID = ?";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setInt(1, orderID);
    ResultSet rs = ppst.executeQuery(sql);
    rs.next();
    int id = rs.getInt("ID");
    String orderNumber = rs.getString("OrderNumber");
    cp.checkIn(con);
    return new Order(id, orderNumber);
  }

  public IProductionOrder getDepartmentTasks(int id, IProductionOrder po) throws SQLException {
    con = cp.checkOut();
    String sql = "SELECT * FROM DepartmentTasks WHERE ProductionOrderID = ?";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setInt(1, id);
    ResultSet rs = ppst.executeQuery(sql);
    while (rs.next()) {
      Date endDate = rs.getDate("EndDate");
      Date startDate = rs.getDate("StartDate");
      boolean finishedOrder = rs.getBoolean("FinishedOrder");
      int departmentID = rs.getInt("DepartmentID");
      IDepartmentTask department = new DepartmentTask(startDate, endDate, finishedOrder,
          getDepartment(departmentID));
      po.addDepartmentTask(department);
    }
    cp.checkIn(con);
    return po;
  }

  public IDepartment getDepartment(int departmentID) throws SQLException {
    con = cp.checkOut();
    String sql = "SELECT * FROM Departments WHERE ID = ?";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setInt(1, departmentID);
    ResultSet rs = ppst.executeQuery();
    rs.next();
    IDepartment dp = new Department(rs.getString("Name"));
    cp.checkIn(con);
    return dp;
  }

  @Override
  public List getDetails() throws SQLException {
    return null;
  }

  @Override
  public boolean hasNewData() {
    return hasNewData;
  }

  @Override
  public void setHasNewData(boolean hasNewData) {

  }
}