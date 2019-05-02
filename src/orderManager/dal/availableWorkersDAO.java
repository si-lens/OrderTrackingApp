package orderManager.dal;

public class availableWorkersDAO {


    /*
     public List<Worker> getWorkers() throws SQLException {
        List<Worker> workers = new ArrayList<>();
        String sql = "SELECT * FROM AvailableWorkers";
        PreparedStatement ppst = con.prepareStatement(sql);
        ResultSet rs = ppst.executeQuery();

        while (rs.next()) {
            String type = rs.getString(1);
            String initials = rs.getString(2);
            String name = rs.getString(3);
            long salary = rs.getLong(4);
            int id = rs.getInt(5);
            Worker w = new Worker(type, initials, name, salary, id);
            workers.add(w);
        }
        System.out.println(workers.size());
        return workers;
    }

     */

}
