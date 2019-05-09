package orderManager.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;

import orderManager.dal.Connection.ConnectionProvider;
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

    public static void readFile(String json) throws SQLException, IOException {
        connect();
        String del = "DELETE FROM AvailableWorkers " +
                "DELETE FROM ProductionOrders " +
        "DELETE FROM DEPARTMENTS";
        /// Database has cascade on deleting so if we delete tables with PK, the rest is also cleaned.

        Statement st = con.createStatement();
        st.execute(del);

        try {
            object = (JSONObject) parser.parse(new FileReader(json));
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
            String sql = "INSERT INTO AvailableWorkers(ID,Name,Initials,SalaryNumber) VALUES (?,?,?,?)";
            PreparedStatement ppst = con.prepareStatement(sql);
            ppst.setInt(1, i + 1);
            ppst.setString(2, (String) rec.get("Name"));
            ppst.setString(3, (String) rec.get("Initials"));
            ppst.setLong(4, (long) rec.get("SalaryNumber"));

            ppst.execute();

        }
    }


    public static void loadProductionOrders() throws SQLException, java.text.ParseException {
        JSONArray jArray = (JSONArray) object.get("ProductionOrders");
        for (int i = 0; i < jArray.size(); i++) {
            JSONObject rec = (JSONObject) jArray.get(i);
            String sql = "INSERT INTO ProductionOrders(ID) VALUES (?)";
            PreparedStatement ppst = con.prepareStatement(sql);
            ppst.setInt(1, i + 1);
            ppst.execute();
            loadDepartmentTasks((JSONArray) rec.get("DepartmentTasks"), i + 1);
            loadCustomers((JSONObject) rec.get("Customer"), i + 1);
            loadDelivery((JSONObject) rec.get("Delivery"), i + 1);
            loadOrder((JSONObject) rec.get("Order"), i + 1);
        }
    }

    public static void loadDepartmentTasks(JSONArray departmentTasks, int id) throws SQLException {

        for (int i = 0; i < departmentTasks.size(); i++) {

            JSONObject departmentTask = (JSONObject) departmentTasks.get(i);
            JSONObject department = (JSONObject) departmentTask.get("Department");
            int topAvailableID = getDepartmentTasksTopID();
            String startDate = (String) departmentTask.get("StartDate");
            startDate = startDate.replace("/Date(", "").replace("+0200)/", "");
            Date start = new Date(Long.valueOf(startDate));
            String endDate = (String) departmentTask.get("EndDate");
            endDate = endDate.replace("/Date(", "").replace("+0200)/", "");
            Date end = new Date(Long.valueOf(endDate));
            String sql = "INSERT INTO DepartmentTasks(ID,ProductionOrderID, StartDate, EndDate, FinishedOrder ) VALUES(?,?, ?, ?, ?)";
            PreparedStatement ppst = con.prepareStatement(sql);
            System.out.println("ID: "+id);
            ppst.setInt(1, topAvailableID);
            ppst.setInt(2, id);
            ppst.setDate(3, start);
            ppst.setDate(4, end);
            ppst.setBoolean(5, (Boolean) departmentTask.get("FinishedOrder"));
            ppst.execute();

            loadDepartment(department,topAvailableID);

        }
    }



    public static void loadDepartment(JSONObject department, int id) throws SQLException {
            String sql = "INSERT INTO Departments(ID,Name) VALUES(?, ?)";
            PreparedStatement ppst = con.prepareStatement(sql);
            ppst.setInt(1, id);
            ppst.setString(2, (String) department.get("Name"));
            ppst.execute();
    }


    public static void loadCustomers(JSONObject customer, int id) throws SQLException {
        String sql = "INSERT INTO Customers(ProductionOrderID,Name) VALUES (?,?)";
        PreparedStatement ppst = con.prepareStatement(sql);
        ppst.setInt(1, id);
        ppst.setString(2, (String) customer.get("Name"));
        ppst.execute();
    }

    public static void loadDelivery(JSONObject delivery, int id) throws SQLException {
        String date = (String) delivery.get("DeliveryTime");
        date = date.replace("/Date(", "").replace("+0200)/", "");
        Date currentDate = new Date(Long.valueOf(date));
        String sql = "INSERT INTO Deliveries(ProductionOrderID,DeliveryTime) VALUES (?,?)";
        PreparedStatement ppst = con.prepareStatement(sql);
        ppst.setInt(1, id);
        ppst.setDate(2, currentDate);
        ppst.execute();
    }




    public static void loadOrder(JSONObject order, int id) throws SQLException {
        String sql = "INSERT INTO Orders(ProductionOrderID,OrderNumber) VALUES(?, ?)";
        PreparedStatement ppst = con.prepareStatement(sql);
        ppst.setInt(1, id);
        ppst.setString(2, (String) order.get("OrderNumber"));
        ppst.execute();
    }

    public static int getDepartmentTasksTopID() throws SQLException {
        String sql = "SELECT ID FROM DepartmentTasks ORDER BY ID DESC";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        if(!rs.next())
            return 1;
        else return rs.getInt(1)+1;
    }

}
