package orderManager.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import orderManager.be.IDepartment;
import orderManager.gui.model.Model;
import orderManager.windowOpener;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class loginWindowController implements Initializable {

    public Button loginButton;
    public ComboBox departmentSelection;
    private List<IDepartment> departmentList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Model model = Model.getInstance();
            departmentList = model.getDepartments();
            loadDepartments();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadDepartments(){
        for(int i=0; i<departmentList.size(); i++){
            departmentSelection.getItems().add(departmentList.get(i));
        }
    }


    public void logIn(ActionEvent actionEvent) throws IOException {
        IDepartment department = (IDepartment) departmentSelection.getSelectionModel().getSelectedItem();
        if(department!=null) {
            Model.getInstance().setDepartment(department);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
            new windowOpener("gui/view/mainWindow.fxml", 651, 496, true);
        }
    }
}
