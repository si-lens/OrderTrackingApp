package orderManager.be;

import java.util.List;

//Defines an interface of data from multiple data-sources.
//This interface acts as an adapter for BelMaker models such as Order and Customer.
public interface IProductionOrder {

  //Defines relevant data of the order/
  Order getOrder();

  //Defines relevant data of the delivery.
  Delivery getDelivery();

  //Defines data of the customer.
  Customer getCustomer();

  //Defines tasks of departments.
  List<DepartmentTask> getDepartmentTasks();

  //Adds a task to this production order.
  void addDepartmentTask(DepartmentTask Task);

  //Removes the task to this production order.
  void removeDepartmentTask(DepartmentTask task);
}
