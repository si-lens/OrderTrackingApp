package orderManager.gui.model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import orderManager.be.IDepartment;
import orderManager.be.IProductionOrder;
import orderManager.be.IWorker;
import orderManager.be.ProductionOrder;
import orderManager.bll.mainLogicClass;

public class Model {

  private static Model model;
  private IDepartment department;
  private mainLogicClass mlc;
  private ProductionOrder po;
  private List<IProductionOrder> productionOrders;
  private List<IWorker> workers;

  private Model() {
    try {
      mlc = new mainLogicClass();
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



  public IDepartment getDepartment() {
    return department;
  }

  public void setDepartment(IDepartment department) {
    this.department = department;
  }

  public void setSelectedProductionOrder(ProductionOrder po) {
    this.po=po;
  }
}