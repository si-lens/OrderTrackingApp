package orderManager.bll;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
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

    public List<DepartmentTask> getDepartmentTaskByOrderNumber(IOrder order) throws SQLException, ParseException {
        return pDAO.getDepartmentTasksByOrderNumber(order);
    }

    public List<IDepartment> getDepartments() {
        return departments;
    }

    public List<IProductionOrder> getProducionOrdersByDepartment(IDepartment department) throws SQLException, ParseException {
        return pDAO.getDepartmentContent(department);
    }

    public void changeStatus(IProductionOrder prodOrd) throws SQLException, ParseException {
        pDAO.changeStatus(prodOrd);
    }

    /*
      public List<OrderDetails> getOrderDetail(IDepartment department) throws SQLException {
        return pDAO.getDepartmentOrders(department);
      }
    */
/*
  //Observable Design Pattern
  public void refreshTables()
  {
    Runnable runnable = () -> {
      while (isRunning) {
        if(pDAO.hasNewData())
        {
          try {
            setChanged();
            notifyObservers(pDAO.getDepartment());
            Thread.sleep(5000);
          } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    };
    runnable.run();
  }

 */
    //Observable Design Pattern
    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }


    public double calculateEstimatedProgress(Date sD, Date eD) throws ParseException {
        String startDateS = sD.toString();
        String endDateS = eD.toString();
        LocalDate startDate = LocalDate.parse(startDateS);
        LocalDate endDate = LocalDate.parse(endDateS);
        LocalDate todaysDate = LocalDate.now();
        long daysBetweenStartAndEnd = ChronoUnit.DAYS.between(startDate, endDate);
        long daysBetweenStartAndNow = ChronoUnit.DAYS.between(startDate, todaysDate);
        double valOne = (double) daysBetweenStartAndEnd;
        double valTwo = (double) daysBetweenStartAndNow;
        double progress = valTwo / valOne;
        if(progress<0)
            progress=0;
        return progress;
    }

}