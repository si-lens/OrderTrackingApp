package orderManager.be;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import orderManager.dal.availableWorkersDAO;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class DepartmentTask extends RecursiveTreeObject<orderManager.be.DepartmentTask> implements IDepartmentTask {


  private IDepartment department;
  private int id;
  private boolean orderState;
  private Date startDate;
  private Date endDate;
  private List<IWorker> listOfWorkers;
  private CustomProgressBar progressBar;

  public DepartmentTask(int id, Date startDate, Date endDate, boolean orderState, IDepartment department, List<IWorker> list) {
    this.id = id;
    this.orderState = orderState;
    this.startDate = startDate;
    this.endDate = endDate;
    this.department = department;
    listOfWorkers = list;
  }

  public String getDepartmentName() {
    return department.getName();
  }

  @Override
  public CustomProgressBar getProgressBar() {
    return progressBar;
  }

  @Override
  public void setProgressBar(CustomProgressBar.Status status)
  {
    this.progressBar = new CustomProgressBar(startDate, endDate, status);
  }

  @Override
  public IDepartment getDepartment() {
    return department;
  }

  @Override
  public Boolean getFinishedOrder() {
    return orderState;
  }

  @Override
  public Date getStartDate() {
    return startDate;
  }

  @Override
  public Date getEndDate() {
    return endDate;
  }

  @Override
  public void addWorker(IWorker worker) throws SQLException {
    listOfWorkers.add(worker);
    workerAdded(worker);
  }
  @Override
  public void removeWorker(IWorker worker) throws SQLException {
    listOfWorkers.remove(worker);
    workerRemoved(worker);
  }

  @Override
  public void workerAdded(IWorker worker) throws SQLException {
    availableWorkersDAO.addWorkerToDepartmentTask(id, worker.getId());
  }

  @Override
  public void workerRemoved(IWorker worker) throws SQLException {
    availableWorkersDAO.removeWorkerFromDepartmentTask(id, worker.getId());
  }

  @Override
  public List<IWorker> getActiveWorkers() {
    return listOfWorkers;
  }

}
