package orderManager.bll;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import orderManager.be.Worker;
import orderManager.dal.availableWorkersDAO;
import orderManager.dal.jsonReader;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class mainLogicClass {
private availableWorkersDAO awDAO;

    public mainLogicClass() throws IOException, SQLServerException {
       awDAO = new availableWorkersDAO();
    }

    public List<Worker> getWorkers() throws SQLException {
    return awDAO.getWorkers();
}

}
