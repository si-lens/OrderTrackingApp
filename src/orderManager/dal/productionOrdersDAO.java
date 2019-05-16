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

import orderManager.be.*;
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

  public List<IProductionOrder> getDepartmentContent(IDepartment department) throws SQLException {
    con = cp.checkOut();
    List<IProductionOrder> pOrders = new ArrayList<>();
    String sql = "SELECT ProductionOrders.OrderNumber, CustomerID, DeliveryID FROM ProductionOrders,DepartmentTasks,Departments WHERE ProductionOrders.OrderNumber = DepartmentTasks.OrderNumber AND Departments.ID = DepartmentTasks.DepartmentID AND Departments.Name = ?";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setString(1, department.getName());
    ResultSet rs = ppst.executeQuery();
    while (rs.next()) {
      int customerID = rs.getInt("CustomerID");
      int deliveryID = rs.getInt("DeliveryID");
      String orderNumber = rs.getString("OrderNumber");
      IProductionOrder po = new ProductionOrder(getCustomer(customerID), getDelivery(deliveryID), new Order(orderNumber));
      pOrders.add(getDepartmentTasks(orderNumber, po));
    }
    cp.checkIn(con);
    return pOrders;
  }





  public ICustomer getCustomer(int customerID) throws SQLException {
    con = cp.checkOut();
    String sql = "SELECT * FROM Customers WHERE ID = ?";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setInt(1, customerID);
    ResultSet rs = ppst.executeQuery();
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
    ResultSet rs = ppst.executeQuery();
    rs.next();
    int id = rs.getInt("ID");
    Date date = rs.getDate("DeliveryTime");
    cp.checkIn(con);
    return new Delivery(id, date);
  }


  public IProductionOrder getDepartmentTasks(String orderNumber, IProductionOrder po) throws SQLException {
    con = cp.checkOut();
    String sql = "SELECT * FROM DepartmentTasks WHERE OrderNumber = ?";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setString(1, orderNumber);
    ResultSet rs = ppst.executeQuery();
    while (rs.next()) {
      int id = rs.getInt("ID");
      Date endDate = rs.getDate("EndDate");
      Date startDate = rs.getDate("StartDate");
      boolean finishedOrder = rs.getBoolean("FinishedOrder");
      int departmentID = rs.getInt("DepartmentID");
      IDepartmentTask department = new DepartmentTask(startDate, endDate, finishedOrder, getDepartment(departmentID), getWorkers(id));
      po.addDepartmentTask(department);
    }
    cp.checkIn(con);
    return po;
  }
/*
  public List<OrderDetails> getDepartmentOrders(IDepartment department) throws SQLException {
    con = cp.checkOut();
    List<OrderDetails> od = new ArrayList<>();
    String sql = "SELECT OrderNumber, StartDate, EndDate, FinishedOrder FROM DepartmentTasks join Departments on DepartmentTasks.DepartmentID=Departments.ID join ProductionOrders on DepartmentTasks.ProductionOrderID=ProductionOrders.ID join Orders on ProductionOrderID=Orders.ID WHERE Departments.Name = ?";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setString(1, department.getName());
    ResultSet rs = ppst.executeQuery();
    while (rs.next()) {
      String orderNumber = rs.getString("OrderNumber");
      Date startDate = rs.getDate("StartDate");
      Date endDate = rs.getDate("EndDate");
      boolean finishedOrder = rs.getBoolean("FinishedOrder");
      OrderDetails ordDet = new OrderDetails(orderNumber, startDate, endDate, finishedOrder);
      od.add(ordDet);
    }
    cp.checkIn(con);
    return od;
  }
*/
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

  public List<IWorker> getWorkers(int id) throws SQLException {
    con = cp.checkOut();
    List<IWorker> workers = new ArrayList<>();
    String sql = "SELECT * FROM AvailableWorkers,TaskWorkers WHERE AvailableWorkers.ID = TaskWorkers.WorkerID AND TaskWorkers.DepartmentTaskID = ?";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setInt(1, id);
    ResultSet rs =  ppst.executeQuery();
    while (rs.next()) {
      int workerID = rs.getInt(1);
      String name = rs.getString(2);
      String initials = rs.getString(3);
      long salary = rs.getLong(4);
      IWorker w = new Worker(workerID,name,initials,salary);
      workers.add(w);
    }
    cp.checkIn(con);
    return workers;
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