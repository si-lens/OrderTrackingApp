package orderManager.gui.model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import orderManager.be.*;
import orderManager.bll.mainLogicClass;
import orderManager.dal.Properties.PropertyReader;

public class Model {

  private static Model model;
  private IDepartment department;
  private mainLogicClass mlc;
  private ProductionOrder po;
  private List<IProductionOrder> productionOrders;
  private List<IWorker> workers;
  private PropertyReader pr;

  private Model() {
    try {
      mlc = new mainLogicClass();
      pr = new PropertyReader();
    } catch (IOException | SQLException e) {
      e.printStackTrace();
    }
  }

  public static Model getInstance() {
    if (model == null) {
      model = new Model();
    }
    return model;
  }

  public mainLogicClass getManager(){
    return mlc;
  }

  public List<IProductionOrder> getProductionOrders() throws SQLException {
    return mlc.getProducionOrdersByDepartment(department);
  }

  public List<IWorker> getWorkers() throws SQLException {
    return mlc.getWorkers();
  }

  public ProductionOrder getSelectedProductionOrder() {
    return po;
  }

public void changeStatus(IProductionOrder prodOrd) throws SQLException {
  mlc.changeStatus(prodOrd);
}

  public String getDepartment() {
    if (department == null)
    {
      return pr.read();
    } else
    {
      return department.getName();
    }
  }

  public void setDepartment(IDepartment department) {
    this.department = department;
    pr.write(department.getName());
  }

  public void setSelectedProductionOrder(ProductionOrder po) {
    this.po=po;
  }

    public void setSelectedOrderNumber(IOrder order) throws SQLException {
      mlc.getDepartmentTaskByOrderNumber(order);
    }
}