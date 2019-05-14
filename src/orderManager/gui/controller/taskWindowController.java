package orderManager.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import orderManager.be.IWorker;
import orderManager.be.Worker;
import orderManager.gui.model.Model;

import java.net.URL;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class taskWindowController implements Initializable {


    public JFXButton departmentBtn;
    public JFXButton markAsDoneButt;
    public JFXComboBox addWorkersBox;
    public TreeTableView activeWorkersTable;
    public TreeTableColumn idCol;
    public TreeTableColumn nameCol;
    public TreeTableColumn initialsCol;
    public TreeTableColumn salaryCol;
    public TreeTableView orderTasksTable;
    public TreeTableColumn statusCol;
    public TreeTableColumn departmentCol;
    public TreeTableColumn progressCol;
    public JFXButton orderNumberLabel;
    public JFXButton activeWorkersLabel;
    private Model model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        try {
            loadWorkers();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        setOrderNumber();
    }

    private void setOrderNumber() {
        String orderNum = model.getSelectedOrderNumber();
        orderNumberLabel.setText(orderNum);
    }


    private void loadWorkers() throws SQLException {
        for(IWorker w:model.getWorkers()){
            addWorkersBox.getItems().add(w.getName());
        }
    }


}