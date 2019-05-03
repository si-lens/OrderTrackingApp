package orderManager.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import orderManager.be.Department;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class jsonReader {

  static JSONParser parser;
  static JSONObject object;
  static ConnectionProvider cp;
  static Connection con;
  static SimpleDateFormat sdf;

  public static void connect() throws SQLServerException, IOException {
    parser = new JSONParser();
    cp = new ConnectionProvider();
    con = cp.getConnection();
    sdf = new SimpleDateFormat("dd-MM-yyyy");
  }

  public static void readFile() throws SQLException, IOException {
    connect();
    String del = "DELETE FROM AvailableWorkers DELETE FROM ProductionOrders DELETE"
        + " FROM Delivery DELETE FROM Customer";
    Statement st = con.createStatement();
    st.execute(del);

    try {
      object = (JSONObject) parser.parse(new FileReader("data.json"));
      loadWorkers();
      loadProductionOrders();
    } catch (java.text.ParseException | ParseException | IOException | SQLException e) {
      e.printStackTrace();
    }
  }

  public static void loadWorkers() throws SQLException {
    JSONArray jArray = (JSONArray) object.get("AvailableWorkers");
    for (int i = 0; i < jArray.size(); i++) {
      JSONObject rec = (JSONObject) jArray.get(i);
      String sql = "INSERT INTO AvailableWorkers(Type,Name,Initials,SalaryNumber,ID) VALUES (?,?,?,?,?)";
      PreparedStatement ppst = con.prepareStatement(sql);
      ppst.setString(1, (String) rec.get("__type"));
      ppst.setString(2, (String) rec.get("Name"));
      ppst.setString(3, (String) rec.get("Initials"));
      ppst.setLong(4, (long) rec.get("SalaryNumber"));
      ppst.setInt(5, i);
      ppst.execute();

    }
  }


  public static void loadProductionOrders() throws SQLException, java.text.ParseException {
    JSONArray jArray = (JSONArray) object.get("ProductionOrders");
    for (int i = 0; i < jArray.size(); i++) {
      JSONObject rec = (JSONObject) jArray.get(i);
      String sql = "INSERT INTO ProductionOrders(Type,ID) VALUES (?,?)";
      PreparedStatement ppst = con.prepareStatement(sql);
      ppst.setString(1, (String) rec.get("__type"));
      ppst.setInt(2, i);
      ppst.execute();
      loadCustomers((JSONObject)rec.get("Customer"),i);
      loadDelivery((JSONObject)rec.get("Delivery"),i);
    }
  }

  public static void loadCustomers(JSONObject customer, int id) throws SQLException {
    String sql = "INSERT INTO Customer(Type,Name,ID) VALUES (?,?,?)";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setString(1, (String) customer.get("__type"));
    ppst.setString(2, (String) customer.get("Name"));
    ppst.setInt(3, id);
    ppst.execute();
  }

  public static void loadDelivery(JSONObject delivery, int id) throws SQLException {
    String date = (String) delivery.get("DeliveryTime");
    date = date.replace("/Date(","").replace("+0200)/","");
    Date currentDate = new Date(Long.valueOf(date));
    String sql = "INSERT INTO Delivery(Type,DeliveryTime,ID) VALUES (?,?,?)";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setString(1, (String) delivery.get("__type"));
    ppst.setDate(2, currentDate);
    ppst.setInt(3, id);
    ppst.execute();
  }

  public static void loadDepartment(JSONObject department, int id) throws SQLException {
      String sql = "INSERT INTO Department(Type, Name, ID) VALUES(?, ?, ?)";
      PreparedStatement ppst = con.prepareStatement(sql);
      ppst.setString(1, (String) department.get("__type"));
      ppst.setString(2, (String) department.get("Name"));
      ppst.setInt(3, id);
      ppst.execute();
  }

  public static void loadOrder(JSONObject order, int id) throws SQLException {
    String sql = "INSERT INTO Order(Type, OrderNumber, ID) VALUE(?, ?, ?)";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setString(1, (String) order.get("__type"));
    ppst.setString(2, (String) order.get("OrderNumber"));
    ppst.setInt(3, id);
    ppst.execute();
  }

  public static void loadDepartmentTasks(JSONObject departmentTask, int id, int productionOrderID) throws SQLException {
    String startDate = (String) departmentTask.get("StartDate");
    startDate = startDate.replace("/Date(","").replace("+0200)/","");
    Date start = new Date(Long.valueOf(startDate));
    String finishedDate = (String) departmentTask.get("FinishedOrder");
    finishedDate = finishedDate.replace("/Date(","").replace("+0200)/","");
    Date finished = new Date(Long.valueOf(finishedDate));
    String sql = "INSERT INTO DepartmentTasks(ID, Type, EndDate, StartDate, FinishedOrder, ProductionOrderID) VALUE(?, ?, ?, ?, ?, ?)";
    PreparedStatement ppst = con.prepareStatement(sql);
    ppst.setInt(1, id);
    ppst.setString(2, (String) departmentTask.get("__type"));
    ppst.setDate(3, start);
    ppst.setDate(4, finished);
    ppst.setBoolean(5, (Boolean) departmentTask.get("FinishedOrder"));
    ppst.setInt(6, productionOrderID);
  }

}
