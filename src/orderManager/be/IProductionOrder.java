package orderManager.be;

//Defines an interface of data from multiple data-sources.
//This interface acts as an adapter for BelMaker models such as Order and Customer.
public interface IProductionOrder {

  //Defines relevant data of the order/
  IOrder getOrder();

  //Defines relevant data of the delivery.
  IDelivery getDelivery();

  //Defines data of the customer.
  ICustomer getCustomer();

  //Defines tasks of departments.
  IDepartmentTask[] getDepartmentTasks();

  //Adds a task to this production order.
  void addDepartmentTask(IDepartmentTask Task);

  //Removes the task to this production order.
  void removeDepartmentTask(IDepartmentTask task);
}
