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
import orderManager.be.Department;
import orderManager.be.DepartmentTask;
import orderManager.be.ProductionOrder;
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
      int deliveryID = loadDeliveries((JSONObject) rec.get("Delivery"));
      String orderNumber = (String)((JSONObject)rec.get("Order")).get("OrderNumber");
      if (loadProductionOrders(customerID, deliveryID, orderNumber) == 1)
      loadDepartmentTasks((JSONArray) rec.get("DepartmentTasks"), orderNumber);
    }
  }

  public static int loadCustomers(JSONObject customer) throws SQLException {
    String name = (String) customer.get("Name");

    String sql =
        "INSERT INTO Customers (Name) SELECT (?) WHERE NOT EXISTS (SELECT Name from Customers WHERE Name = ?)"
            + " SELECT * FROM Customers WHERE Name = ?";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setString(1, name);
    ppst.setString(2, name);
    ppst.setString(3, name);
    ResultSet rs = ppst.executeQuery();
    rs.next();
    return rs.getInt(1);
  }


  public static int loadDeliveries(JSONObject delivery) throws SQLException {
    String date = (String) delivery.get("DeliveryTime");
    date = date.replace("/Date(", "").replace("+0200)/", "");
    Date formattedDate = new Date(Long.valueOf(date));

    String sql =
        "INSERT INTO Deliveries (DeliveryTime) SELECT ? WHERE NOT EXISTS (SELECT DeliveryTime from Deliveries WHERE DeliveryTime = ?)"
            + " SELECT ID FROM Deliveries WHERE DeliveryTime = ?";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setDate(1, formattedDate);
    ppst.setDate(2, formattedDate);
    ppst.setDate(3, formattedDate);
    ResultSet rs = ppst.executeQuery();
    rs.next();
    return rs.getInt(1);
  }

  public static int loadProductionOrders(int customerID, int deliveryID, String orderNumber)
      throws SQLException {
    String sql = "INSERT INTO ProductionOrders(CustomerID, DeliveryID, OrderNumber) SELECT ?,?,? WHERE NOT EXISTS (SELECT OrderNumber from ProductionOrders WHERE OrderNumber = ?)";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setInt(1, customerID);
    ppst.setInt(2, deliveryID);
    ppst.setString(3, orderNumber);
    ppst.setString(4, orderNumber);
    return ppst.executeUpdate();
  }

  public static void loadDepartmentTasks(JSONArray departmentTasks, String orderNumber) throws SQLException {
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

      String sql = "INSERT INTO DepartmentTasks(OrderNumber, DepartmentID, StartDate, EndDate, FinishedOrder) VALUES (?, ?, ?, ?, ?)";
      PreparedStatement ppst = con.prepareStatement(sql);
      ppst.setString(1, orderNumber);
      ppst.setInt(2, departmentID);
      ppst.setDate(3, start);
      ppst.setDate(4, end);
      ppst.setBoolean(5, (Boolean) departmentTask.get("FinishedOrder"));
      ppst.execute();
    }
  }

  public static int loadDepartment(JSONObject department) throws SQLException {
    String name = (String) department.get("Name");
    String sql =
        "INSERT INTO Departments (Name) SELECT ? WHERE NOT EXISTS (SELECT Name from Departments WHERE Name = ?)"
            + "SELECT ID FROM Departments WHERE Name = ?";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setString(1, name);
    ppst.setString(2, name);
    ppst.setString(3, name);
    ResultSet rs = ppst.executeQuery();
    rs.next();
    return rs.getInt(1);
  }

  public static void resetTable(String table) throws SQLException {
    String del = "DELETE FROM " + table + " DBCC CHECKIDENT (" + table + ", RESEED, 0)";
    PreparedStatement ppst = con.prepareStatement(del);
    ppst.execute();
  }
}
