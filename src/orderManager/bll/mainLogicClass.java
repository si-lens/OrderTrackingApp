package orderManager.bll;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Observable;

import orderManager.be.IDepartment;
import orderManager.be.IProductionOrder;
import orderManager.be.IWorker;
import orderManager.dal.availableWorkersDAO;
import orderManager.dal.jsonReader;
import orderManager.dal.jsonReaderMK2;
import orderManager.dal.productionOrdersDAO;

public class mainLogicClass extends Observable {

  private static mainLogicClass mainLogic;
  private availableWorkersDAO awDAO;
  private productionOrdersDAO pDAO;
  private List<IWorker> workers;
  private List<IDepartment> departments;
  private List<IProductionOrder> productionOrders;
  private List<IProductionOrder> departmentContent;
  private mainLogicClass mlc;
  private boolean isRunning = true;

  public mainLogicClass() throws IOException, SQLException {
    awDAO = new availableWorkersDAO();
    pDAO = new productionOrdersDAO();
    setWorkers();
    setDepartments();
  }

  public static mainLogicClass getInstance() throws IOException, SQLException {
    if(mainLogic == null)
      mainLogic = new mainLogicClass();
    return mainLogic;
  }

  public void readFile(String path) throws IOException, SQLException {
    jsonReaderMK2.readFile(path);
  }

  public void setWorkers() throws SQLException {
    workers = awDAO.getWorkers();
  }

  public List<IWorker> getWorkers() {
    return workers;
  }

  public void setDepartments() throws SQLException {
    departments = pDAO.getDepartments();
  }

  public List<IDepartment> getDepartments() {
    return departments;
  }

  public void setDepartmentContent(IDepartment department) throws SQLException {
    departmentContent = pDAO.getDepartmentContent(department);
  }

  public List<IProductionOrder> getDepartmentContent(){return departmentContent;}

  /*
  public void getProducionOrdersByDepartment(IDepartment department) throws SQLException {
        productionOrders = pDAO.getProdutcionOrders(department);
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
