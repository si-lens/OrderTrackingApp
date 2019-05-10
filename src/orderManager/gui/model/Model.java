package orderManager.gui.model;

import orderManager.be.IDepartment;
import orderManager.be.IProductionOrder;
import orderManager.be.IWorker;
import orderManager.bll.mainLogicClass;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Model {

  IDepartment department;
  static Model model;
  private mainLogicClass mainLogic;

  public Model() throws IOException, SQLException {
    mainLogic = mainLogicClass.getInstance();
  }

  public static Model getInstance() throws IOException, SQLException {
    if(model == null)
      model = new Model();
    return model;
  }


  public void setDepartment(IDepartment department) throws SQLException {
    mainLogic.setDepartmentContent(department);
    this.department = department;
  }

  public IDepartment getDepartment(){
    return department;
  }

  public List<IProductionOrder> getDepartmentContent(){return mainLogic.getDepartmentContent();}

  public List<IWorker> getWorkers(){ return mainLogic.getWorkers();}



}
