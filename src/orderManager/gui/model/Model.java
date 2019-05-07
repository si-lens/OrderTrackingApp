package orderManager.gui.model;

import orderManager.be.IDepartment;

public class Model {

  IDepartment department;
  static Model model;

  public static Model getInstance() {
    if(model == null)
      model = new Model();
    return model;
  }

  public void setDepartment(IDepartment department){
    this.department = department;
  }

  public IDepartment getDepartment(){
    return department;
  }
}
