package orderManager.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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
            loadWorkers();
            getOrders();
        } catch (ParseException | IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadWorkers() throws SQLException {
        JSONArray jArray = (JSONArray) object.get("AvailableWorkers");
        String del = "DELETE FROM AvailableWorkers";
        Statement st = con.createStatement();
        st.execute(del);
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



    public static void getOrders() {
        JSONArray jArray = (JSONArray) object.get("ProductionOrders");
        for (int i = 0; i < jArray.size(); i++) {
            JSONObject rec = (JSONObject) jArray.get(i);
         //   System.out.println("Type: " + rec.get("__type"));

        }

    }

}
