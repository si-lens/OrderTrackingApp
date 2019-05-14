package orderManager.be;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductionOrder extends RecursiveTreeObject<ProductionOrder> implements IProductionOrder {

  private int id;
  private IOrder order;
  private IDelivery delivery;
  private ICustomer customer;
  private List<IDepartmentTask> departmentTaskList;

  public ProductionOrder(int id, ICustomer customer, IDelivery delivery, IOrder order) {
    this.id = id;
    this.delivery = delivery;
    this.customer = customer;
    this.order = order;
    departmentTaskList = new ArrayList<>();
  }

  @Override
  public IOrder getOrder() {
    return order;
  }

  @Override
  public IDelivery getDelivery() {
    return delivery;
  }

  @Override
  public ICustomer getCustomer() {
    return customer;
  }

  @Override
  public List<IDepartmentTask> getDepartmentTasks() {
    return departmentTaskList;
  }

  @Override
  public void addDepartmentTask(IDepartmentTask task) {
    departmentTaskList.add(task);
  }

  @Override
  public void removeDepartmentTask(IDepartmentTask task) {
    departmentTaskList.remove(task);
  }

  public String getOrderNumber() {
    return order.getOrderNumber();
  }

  public String getCustomerName(){return customer.getName();}

  public Date getDeliveryDate(){return delivery.getDeliveryTime();}
}
