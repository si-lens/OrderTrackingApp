package orderManager.bll;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import orderManager.be.Department;
import orderManager.be.IDepartment;
import orderManager.be.Worker;
import orderManager.dal.availableWorkersDAO;
import orderManager.dal.jsonReader;
import orderManager.dal.productionOrdersDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class mainLogicClass {
private availableWorkersDAO awDAO;
private productionOrdersDAO pDAO;

    public mainLogicClass() throws IOException, SQLServerException {
       awDAO = new availableWorkersDAO();
       pDAO = new productionOrdersDAO();
    }

    public void readFile(String path) throws IOException, SQLException {
        jsonReader.readFile(path);
    }

    public List<Worker> getWorkers() throws SQLException {
    return awDAO.getWorkers();
}

    public List<IDepartment> getDepartments() throws SQLException {
        return pDAO.getDepartments();
    }

}
