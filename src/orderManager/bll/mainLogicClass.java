package orderManager.bll;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import orderManager.be.*;
import orderManager.dal.availableWorkersDAO;
import orderManager.dal.jsonReader;
import orderManager.dal.jsonReaderMK2;
import orderManager.dal.productionOrdersDAO;

public class mainLogicClass extends Observable {

  private availableWorkersDAO awDAO;
  private productionOrdersDAO pDAO;
  private List<IDepartment> departments;
  private boolean isRunning = true;

  public mainLogicClass() throws IOException, SQLException {
    awDAO = new availableWorkersDAO();
    pDAO = new productionOrdersDAO();
    //setWorkers();
    setDepartments();
  }

  public void readFile(String path) throws IOException, SQLException {
    jsonReaderMK2.readFile(path);
  }

  /*
  public void setWorkers() throws SQLException {
    workers = awDAO.getWorkers();
  }
   */

  public List<IWorker> getWorkers() throws SQLException {
    return awDAO.getWorkers();
  }

  public void setDepartments() throws SQLException {
    departments = pDAO.getDepartments();
  }

  public List<IDepartment> getDepartments() {
    return departments;
  }

  public List<IProductionOrder> getProducionOrdersByDepartment(IDepartment department) throws SQLException {
    return pDAO.getDepartmentContent(department);
  }
/*
  public List<OrderDetails> getOrderDetail(IDepartment department) throws SQLException {
    return pDAO.getDepartmentOrders(department);
  }
*/
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