package orderManager.gui.model;

import orderManager.be.IDepartment;
import orderManager.be.IProductionOrder;
import orderManager.be.IWorker;
import orderManager.be.ProductionOrder;
import orderManager.bll.mainLogicClass;
import orderManager.dal.Properties.PropertyReader;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public class Model {

  private static Model model;
  private IDepartment department;
  private mainLogicClass mlc;
  private ProductionOrder po;
  private PropertyReader pr;

  private Model() {
    try {
      mlc = new mainLogicClass();
      pr = new PropertyReader();
    } catch (SQLException e) {
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

  public List<IProductionOrder> getProductionOrdersFromDB() throws SQLException, ParseException {
    return mlc.getProducionOrdersByDepartment(getDepartment());
  }

  public List<IWorker> getWorkers() throws SQLException {
    return mlc.getWorkers();
  }

  public ProductionOrder getSelectedProductionOrder() {
    return po;
  }

public void changeStatus(IProductionOrder prodOrd) throws SQLException {
  mlc.changeStatus(prodOrd, department);
}

  public IDepartment getDepartment() {
    if (department == null)
    {
      return pr.read();
    } else
    {
      return department;
    }
  }

  public void setDepartment(IDepartment department) {
    this.department = department;
    pr.write(department.getName());
  }

  public void setSelectedProductionOrder(ProductionOrder po) {
    this.po=po;
  }

    public void readFile(String path) throws IOException, SQLException {
     mlc.readFile(path);
    }

  public List<IDepartment> getDepartments() {
    return mlc.getDepartments();
  }

}