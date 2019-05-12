package orderManager.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import orderManager.be.IDepartment;
import orderManager.be.OrderDetails;
import orderManager.bll.mainLogicClass;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Model {

  private IDepartment department;
  private static Model model;
  private mainLogicClass mlc;

  public static Model getInstance() {
    if(model == null)
      model = new Model();
    return model;
  }

  private Model()
  {
    try {
      mlc = new mainLogicClass();
    } catch (IOException | SQLException e) {
      e.printStackTrace();
    }
  }

  public void setDepartment(IDepartment department){
    this.department = department;
  }

  public IDepartment getDepartment(){
    return department;
  }

  public ObservableList<OrderDetails> obsOrdDet() throws SQLException {
    List<OrderDetails> ordDet = mlc.getOrderDetail(department);
    return FXCollections.observableArrayList(ordDet);
  }
}
