package orderManager.be;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

//Defines a task for a given department.
public interface IDepartmentTask {

  //Defines the name of the department.
  IDepartment getDepartment();

  //Defines if the order has been finished.
  Boolean getFinishedOrder();

  //Defines start date for this deprtment.
  Date getStartDate();

  //Defines the end date for this department.
  Date getEndDate();

  //Adds a worker as an active worker on this department task.
  void addWorker(IWorker worker) throws SQLException;

  //Removes a worker form this department task active workers.
  void removeWorker(IWorker worker) throws SQLException;

  //Raised when a worker has been added to this order.
  void workerAdded(IWorker worker) throws SQLException;

  //Raised when a worker has been removed from this order.
  void workerRemoved(IWorker worker) throws SQLException;

  //Defines the active workers on this given production order.
  List<IWorker> getActiveWorkers();

  String getDepartmentName();

  CustomProgressBar getProgressBar() throws ParseException;
}
