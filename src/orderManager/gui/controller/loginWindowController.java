package orderManager.gui.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import orderManager.be.Department;
import orderManager.be.IDepartment;
import orderManager.bll.mainLogicClass;
import orderManager.dal.jsonReaderMK2;
import orderManager.gui.model.Model;
import orderManager.windowOpener;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class loginWindowController implements Initializable {

    public Button loginButton;
    public ComboBox departmentSelection;
    private mainLogicClass mainLogic;
    private List<IDepartment> departmentList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            mainLogic = new mainLogicClass();
            departmentList = mainLogic.getDepartments();
            loadDepartments();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadDepartments(){
        for(int i=0; i<departmentList.size(); i++){
            departmentSelection.getItems().add(departmentList.get(i));
        }
    }


    public void logIn(ActionEvent actionEvent) throws IOException {
        IDepartment department = (IDepartment) departmentSelection.getSelectionModel().getSelectedItem();
        Model.getInstance().setDepartment(department);
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
        new windowOpener("gui/view/mainWindow.fxml");
    }
}
