package orderManager.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import orderManager.be.IDepartmentTask;
import orderManager.be.IWorker;
import orderManager.be.ProductionOrder;
import orderManager.gui.model.Model;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class taskWindowController implements Initializable {


    public JFXButton departmentBtn;
    public JFXButton markAsDoneButt;
    public JFXComboBox addWorkersBox;
    public JFXButton orderNumberLabel;
    public JFXButton activeWorkersLabel;
    public TableView orderTasksTable;
    public TableView activeWorkersTable;
    private Model model;
    private ProductionOrder selectedOrder;
    private ObservableList<IDepartmentTask> observableTasks;
    private TableColumn<IDepartmentTask, String> statusCol;
    private TableColumn<IDepartmentTask, String> departmentCol;
    private TableColumn<IDepartmentTask, String> progressCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        selectedOrder = model.getSelectedProductionOrder();
        observableTasks = (FXCollections.observableArrayList(selectedOrder.getDepartmentTasks()));
        try {
            loadWorkers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setOrderNumber();
        prepareTasksTable();
    }

    private void setOrderNumber() {
        orderNumberLabel.setText("Order: " + selectedOrder.getOrderNumber());
    }


    private void loadWorkers() throws SQLException {
        for (IWorker w : model.getWorkers()) {
            addWorkersBox.getItems().add(w.getName());
        }
    }

    public void prepareTasksTable() {
       // statusCol.setCellValueFactory(new PropertyValueFactory<>());
       // departmentCol.setCellValueFactory(new PropertyValueFactory<>());
       // progressCol.setCellValueFactory(new PropertyValueFactory<>());
        //orderTasksTable.getColumns().clear();
       // orderTasksTable.setItems(observableTasks);
        //orderTasksTable.getColumns().addAll(statusCol, departmentCol, progressCol);


    }
}