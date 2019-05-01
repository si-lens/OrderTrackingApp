package orderManager.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class jsonReader {

  static JSONParser parser;
  static JSONObject object;
  static ConnectionProvider cp;
  static Connection con;

  public static void readFile() throws IOException, SQLServerException {
    parser = new JSONParser();
    cp = new ConnectionProvider();
    con = cp.getConnection();

    try {
      object = (JSONObject) parser.parse(new FileReader("data.json"));
      getWorkers();
      getOrders();
    } catch (ParseException | IOException | SQLException e) {
      e.printStackTrace();
    }
  }

  public static void getWorkers() throws SQLException {
    JSONArray jArray = (JSONArray) object.get("AvailableWorkers");
    for (int i = 0; i < jArray.size(); i++) {
      JSONObject rec = (JSONObject) jArray.get(i);
      String sql = "INSERT INTO AvailableWorkers(Type,Name,Initials,SalaryNumber,ID) VALUES (?,?,?,?,?)";
      PreparedStatement pps = con.prepareStatement(sql);
      pps.setString(1, (String) rec.get("__type"));
      pps.setString(2, (String) rec.get("Name"));
      pps.setString(3, (String) rec.get("Initials"));
      pps.setLong(4, (long) rec.get("SalaryNumber"));
      pps.setInt(5, i);
      pps.execute();
    }
  }

  public static void getOrders() {
    JSONArray jArray = (JSONArray) object.get("ProductionOrders");
    for (int i = 0; i < jArray.size(); i++) {
      JSONObject rec = (JSONObject) jArray.get(i);
      System.out.println("Type: " + rec.get("__type"));
    }

  }

}
