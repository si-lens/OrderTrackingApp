package orderManager.be;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.scene.control.Label;

import java.text.ParseException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class ProductionOrder extends RecursiveTreeObject<ProductionOrder> implements IProductionOrder {

  private IOrder order;
  private IDelivery delivery;
  private ICustomer customer;
  private List<IDepartmentTask> departmentTaskList;
  private Label label;

  public ProductionOrder(ICustomer customer, IDelivery delivery, IOrder order) {
    this.delivery = delivery;
    this.customer = customer;
    this.order = order;
    departmentTaskList = new ArrayList<>();
  }

  public Label getIndication() {
    return label;
  }

  public void setIndication(IDepartment dep) throws ParseException {
    for (IDepartmentTask dT : departmentTaskList) {
      if(dT.getDepartment().getName().equals(dep.getName())) {
        label = dT.getProgressBar().getIndication();
      }
    }
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
