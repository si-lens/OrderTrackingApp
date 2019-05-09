package orderManager.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import orderManager.dal.Connection.ConnectionProvider;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class jsonReaderMK2 {

  static JSONParser parser;
  static ConnectionProvider cp;
  static Connection con;
  static SimpleDateFormat sdf;

  public static void connect() throws SQLServerException, IOException {
    parser = new JSONParser();
    cp = new ConnectionProvider();
    con = cp.getConnection();
    sdf = new SimpleDateFormat("dd-MM-yyyy");
  }

  public static void readFile(String json) throws SQLException, IOException {
    connect();
    resetTable("DepartmentTasks");
    resetTable("Departments");
    resetTable("ProductionOrders");
    resetTable("AvailableWorkers");
    resetTable("Customers");
    resetTable("Deliveries");
    resetTable("Orders");


    try {
      JSONObject object = (JSONObject) parser.parse(new FileReader(json));
      loadData(object);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void loadWorkers(JSONObject object) throws SQLException {
    JSONArray jArray = (JSONArray) object.get("AvailableWorkers");
    for (int i = 0; i < jArray.size(); i++) {
      JSONObject rec = (JSONObject) jArray.get(i);
      String sql = "INSERT INTO AvailableWorkers(Name,Initials,SalaryNumber) VALUES (?,?,?)";
      PreparedStatement ppst = con.prepareStatement(sql);
      ppst.setString(1, (String) rec.get("Name"));
      ppst.setString(2, (String) rec.get("Initials"));
      ppst.setLong(3, (long) rec.get("SalaryNumber"));
      ppst.execute();

    }
  }

  public static void loadData(JSONObject object) throws SQLException {
    loadWorkers(object);
    JSONArray jArray = (JSONArray) object.get("ProductionOrders");
    for (int i = 0; i < jArray.size(); i++) {
      JSONObject rec = (JSONObject) jArray.get(i);
      int customerID = loadCustomers((JSONObject) rec.get("Customer"));
      int orderID = loadOrders((JSONObject) rec.get("Order"));
      int deliveryID = loadDeliveries((JSONObject) rec.get("Delivery"));

      loadProductionOrders(i + 1, customerID, orderID, deliveryID);
      loadDepartmentTasks((JSONArray) rec.get("DepartmentTasks"), i + 1);
    }
  }

  public static int loadCustomers(JSONObject customer) throws SQLException {
    String name = (String) customer.get("Name");
    String sql = "INSERT INTO Customers (Name) SELECT (?) WHERE NOT EXISTS (SELECT Name from Customers WHERE Name = (?))";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setString(1, name);
    ppst.setString(2, name);
    ppst.execute();

    String sql2 = "SELECT * FROM Customers WHERE Name = ?";
    PreparedStatement ppst2 = con.prepareStatement(sql2);
    ppst2.setString(1, name);
    ResultSet rs = ppst2.executeQuery();
    rs.next();
    return rs.getInt(1);
  }

  public static int loadOrders(JSONObject order) throws SQLException {
    String orderNumber = (String) order.get("OrderNumber");
    String sql = "INSERT INTO Orders (OrderNumber) SELECT ? WHERE NOT EXISTS (SELECT OrderNumber from Orders WHERE OrderNumber = ?)";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setString(1, orderNumber);
    ppst.setString(2, orderNumber);
    ppst.execute();

    String sql2 = "SELECT ID FROM Orders WHERE OrderNumber = ?";
    PreparedStatement ppst2 = con.prepareStatement(sql2);
    ppst2.setString(1, orderNumber);
    ResultSet rs = ppst2.executeQuery();
    rs.next();
    return rs.getInt(1);
  }

  public static int loadDeliveries(JSONObject delivery) throws SQLException {
    String date = (String) delivery.get("DeliveryTime");
    date = date.replace("/Date(", "").replace("+0200)/", "");
    Date formattedDate = new Date(Long.valueOf(date));

    String sql = "INSERT INTO Deliveries (DeliveryTime) SELECT ? WHERE NOT EXISTS (SELECT DeliveryTime from Deliveries WHERE DeliveryTime = ?)";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setDate(1, formattedDate);
    ppst.setDate(2, formattedDate);
    ppst.execute();

    String sql2 = "SELECT ID FROM Deliveries WHERE DeliveryTime = ?";
    PreparedStatement ppst2 = con.prepareStatement(sql2);
    ppst2.setDate(1, formattedDate);
    ResultSet rs = ppst2.executeQuery();
    rs.next();
    return rs.getInt(1);
  }

  public static void loadProductionOrders(int ID, int customerID, int orderID, int deliveryID)
      throws SQLException {
    String sql = "INSERT INTO ProductionOrders(ID, CustomerID, OrderID, DeliveryID) VALUES (?,?,?,?)";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setInt(1, ID);
    ppst.setInt(2, customerID);
    ppst.setInt(3, orderID);
    ppst.setInt(4, deliveryID);
    ppst.execute();
  }

  public static void loadDepartmentTasks(JSONArray departmentTasks, int id) throws SQLException {

    for (int i = 0; i < departmentTasks.size(); i++) {
      JSONObject departmentTask = (JSONObject) departmentTasks.get(i);
      JSONObject department = (JSONObject) departmentTask.get("Department");
      int departmentID = loadDepartment(department);

      String startDate = (String) departmentTask.get("StartDate");
      startDate = startDate.replace("/Date(", "").replace("+0200)/", "");
      Date start = new Date(Long.valueOf(startDate));

      String endDate = (String) departmentTask.get("EndDate");
      endDate = endDate.replace("/Date(", "").replace("+0200)/", "");
      Date end = new Date(Long.valueOf(endDate));

      String sql = "INSERT INTO DepartmentTasks(ProductionOrderID, DepartmentID, StartDate, EndDate, FinishedOrder ) VALUES(?, ?, ?, ?, ?)";
      PreparedStatement ppst = con.prepareStatement(sql);
      ppst.setInt(1, id);
      ppst.setInt(2, departmentID);
      ppst.setDate(3, start);
      ppst.setDate(4, end);
      ppst.setBoolean(5, (Boolean) departmentTask.get("FinishedOrder"));
      ppst.execute();
    }
  }

  public static int loadDepartment(JSONObject department) throws SQLException {
    String name = (String) department.get("Name");
    String sql = "INSERT INTO Departments (Name) SELECT ? WHERE NOT EXISTS (SELECT Name from Departments WHERE Name = ?)";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setString(1, name);
    ppst.setString(2, name);
    ppst.execute();

    String sql2 = "SELECT ID FROM Departments WHERE Name = ?";
    PreparedStatement ppst2 = con.prepareStatement(sql2);
    ppst2.setString(1, name);
    ResultSet rs = ppst2.executeQuery();
    rs.next();
    return rs.getInt(1);
  }

  public static void resetTable(String table) throws SQLException {
    String del = "DELETE FROM " + table + " DBCC CHECKIDENT (" + table + ", RESEED, 0)";
    PreparedStatement ppst = con.prepareStatement(del);
    ppst.execute();
  }
}
