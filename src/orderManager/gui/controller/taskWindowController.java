package orderManager.gui.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import orderManager.be.DepartmentTask;
import orderManager.be.IWorker;
import orderManager.be.ProductionOrder;
import orderManager.be.Worker;
import orderManager.gui.model.Model;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class taskWindowController implements Initializable {
    public JFXButton departmentBtn;
    public JFXButton markAsDoneButt;
    public JFXComboBox addWorkersBox;
    public JFXButton orderNumberLabel;
    public JFXButton activeWorkersLabel;
    public JFXTreeTableView orderTasksTable;
    public JFXTreeTableView activeWorkersTable;
    private Model model;
    private ProductionOrder selectedOrder;
    private ObservableList<DepartmentTask> observableTasks;
    private ObservableList<Worker> observableWorkers;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        selectedOrder = model.getSelectedProductionOrder();
        try {
            observableTasks = (FXCollections.observableArrayList((List<DepartmentTask>) (List) selectedOrder.getDepartmentTasks()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            loadWorkers();
            observableWorkers = (FXCollections.observableArrayList((List<Worker>) (List) model.getWorkers()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setOrderNumber();
        prepareTasksTable();
    }

    @FXML
    private void changeStatus(ActionEvent event)
    {
        try {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to mark number '" + selectedOrder.getOrderNumber() + "' task as 'Done'?", ButtonType.YES, ButtonType.NO);
            a.showAndWait();
            if (a.getResult() == ButtonType.YES)
            {
                model.changeStatus(selectedOrder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        JFXTreeTableColumn<DepartmentTask, String> department = new JFXTreeTableColumn<>("Department");
        prepareColumn(department, "department", 94);

        JFXTreeTableColumn<DepartmentTask, Date> startDate = new JFXTreeTableColumn<>("Start Date");
        prepareColumn(startDate, "startDate", 94);

        JFXTreeTableColumn<DepartmentTask, Date> endDate = new JFXTreeTableColumn<>("End Date");
        prepareColumn(endDate, "endDate", 94);
/*
        JFXTreeTableColumn<DepartmentTask, Boolean> status = new JFXTreeTableColumn<>("Status");
        prepareColumn(status, "finishedOrder", 94);
*/
        JFXTreeTableColumn<DepartmentTask, ProgressBar> progress = new JFXTreeTableColumn<>("Progress");
        prepareColumn(progress, "progressBar", 94);

        orderTasksTable.getColumns().addAll(department, startDate, endDate,progress);

        setTaskTable();
    }

    private void setTaskTable() {
        TreeItem<DepartmentTask> root = new RecursiveTreeItem<>(observableTasks, RecursiveTreeObject::getChildren);
        orderTasksTable.setRoot(root);
        orderTasksTable.setShowRoot(false);
    }

    /*
        public void prepareWorkersTable() {

            JFXTreeTableColumn<Worker, String> idCol = new JFXTreeTableColumn<>("ID");
            prepareColumn(idCol, "id", 33);

            JFXTreeTableColumn<Worker, String> nameCol = new JFXTreeTableColumn<>("Name");
            prepareColumn(nameCol, "name", 207);

            JFXTreeTableColumn<Worker, String> initialsCol = new JFXTreeTableColumn<>("Initials");
            prepareColumn(initialsCol, "initials", 44);

            JFXTreeTableColumn<Worker, String> salaryCol = new JFXTreeTableColumn<>("SalaryNumber");
            prepareColumn(salaryCol, "salary", 93);



            activeWorkersTable.getColumns().addAll(idCol, nameCol, initialsCol, salaryCol);

            TreeItem<Worker> root = new RecursiveTreeItem<>(observableWorkers, RecursiveTreeObject::getChildren);
            activeWorkersTable.setRoot(root);
            activeWorkersTable.setShowRoot(false);
        }
    */
    public void prepareColumn(JFXTreeTableColumn colName, String attributeName, int colWidth) {
        colName.setCellValueFactory(new TreeItemPropertyValueFactory<>(attributeName));
        colName.setMinWidth(colWidth);
        colName.setStyle("-fx-alignment: CENTER");
    }

}

