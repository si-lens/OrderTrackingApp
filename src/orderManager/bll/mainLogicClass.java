package orderManager.bll;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import javafx.application.Platform;
import orderManager.be.IDepartment;
import orderManager.be.Worker;
import orderManager.dal.IDAODetails;
import orderManager.dal.availableWorkersDAO;
import orderManager.dal.jsonReader;
import orderManager.dal.productionOrdersDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class mainLogicClass extends Observable {
private IDAODetails awDAO;
private productionOrdersDAO pDAO;
private boolean isRunning = true;

    public mainLogicClass() throws IOException, SQLServerException {
       awDAO = new availableWorkersDAO();
       pDAO = new productionOrdersDAO();
    }

    public void readFile(String path) throws IOException, SQLException {
        jsonReader.readFile(path);
    }

    public List<Worker> getWorkers() throws SQLException {
    return awDAO.getDetails();
}

    public List<IDepartment> getDepartments() throws SQLException {
        return pDAO.getDepartments();
    }
    //Observable Design Pattern
    public void refreshTables()
    {
        Runnable runnable = () -> {
            while (isRunning) {
                if(awDAO.hasNewData())
                {
                    try {
                        setChanged();
                        notifyObservers(awDAO.getDetails());
                        Thread.sleep(5000);
                    } catch (SQLException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        runnable.run();
    }
    //Observable Design Pattern
    public void setIsRunning(boolean isRunning)
    {
        this.isRunning = isRunning;
    }

}
