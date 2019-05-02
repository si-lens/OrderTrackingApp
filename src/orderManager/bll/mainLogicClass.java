package orderManager.bll;

import orderManager.be.Worker;
import orderManager.dal.availableWorkersDAO;
import orderManager.dal.jsonReader;

import java.sql.SQLException;
import java.util.List;

public class mainLogicClass {
private availableWorkersDAO awDAO;

    public mainLogicClass() {
       awDAO = new availableWorkersDAO();
    }
/*
    public List<Worker> getWorkers() throws SQLException {
    return awDAO.getWorkers();
}
*/
}
