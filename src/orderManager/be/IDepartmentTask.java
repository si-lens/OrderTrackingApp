package orderManager.be;

import java.util.Date;
import javafx.event.EventHandler;

//Defines a task for a given department.
public interface IDepartmentTask {

  //Defines the name of the department.
  IDepartment getDepartment();

  //Defines if the order has been finished.
  Boolean isOrderFinished();

  //Defines start date for this deprtment.
  Date getStartTime();

  //Defines the end date for this department.
  Date getEndDate();

  //Adds a worker as an active worker on this department task.
  void addWorker(IWorker worker);

  //Removes a worker form this department task active workers.
  void removeWorker(IWorker worker);

  //Raised when a worker has been added to this order.
  EventHandler workerAdded();

  //Raised when a worker has been removed from this order.
  EventHandler workerRemoved();

  //Defines the active workers on this given production order.
  IWorker[] getActiveWorkers();
}
