package orderManager.gui.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import orderManager.be.*;
import orderManager.gui.model.Model;
import javafx.scene.control.ComboBox;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import org.controlsfx.control.CheckComboBox;

public class taskWindowController implements Initializable {
    public JFXButton departmentBtn;
    public JFXButton markAsDoneButt;
    public JFXButton orderNumberLabel;
    public JFXButton activeWorkersLabel;
    public JFXTreeTableView orderTasksTable;
    public JFXTreeTableView activeWorkersTable;
    public StackPane comboBoxPane;
    public JFXButton addWorkersButton;
    private Model model;
    private ProductionOrder selectedOrder;
    private ObservableList<DepartmentTask> observableTasks;
    private ObservableList<IWorker> observableWorkers;
    private ObservableList<Worker> observableActiveWorkers;
    private CheckComboBox checkComboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        selectedOrder = model.getSelectedProductionOrder();
        try {
            observableTasks = (FXCollections.observableArrayList((List<DepartmentTask>) (List) selectedOrder.getDepartmentTasks()));
            observableWorkers = (FXCollections.observableArrayList(model.getWorkers()));
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
        }
        try {
            loadWorkers();
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
        checkComboBox = new CheckComboBox();
        checkComboBox.setMaxWidth(140);
        for (IWorker w : model.getWorkers()) {
            checkComboBox.getItems().add(w);
        }
        comboBoxPane.getChildren().add(checkComboBox);
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

    public void prepareColumn(JFXTreeTableColumn colName, String attributeName, int colWidth) {
        colName.setCellValueFactory(new TreeItemPropertyValueFactory<>(attributeName));
        colName.setMinWidth(colWidth);
        colName.setStyle("-fx-alignment: CENTER");
    }

    public void loadClickedContent(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() > 0) {
            RecursiveTreeItem<DepartmentTask> dt = (RecursiveTreeItem<DepartmentTask>) orderTasksTable.getSelectionModel().getSelectedItem();
            if(dt.getValue().getDepartment().getName().equals(model.getDepartment().getName())){
                disableFunctionality(false);
                loadActiveWorkers();
        }else{
                disableFunctionality(true);
                observableActiveWorkers = (FXCollections.observableArrayList((List<Worker>) (List) dt.getValue().getActiveWorkers()));
                prepareWorkersTable(observableActiveWorkers);
            }
    }
    }

    private void loadActiveWorkers(){
        RecursiveTreeItem<DepartmentTask> dt = (RecursiveTreeItem<DepartmentTask>) orderTasksTable.getSelectionModel().getSelectedItem();
        if (dt.getValue().getActiveWorkers() != null) {
            observableActiveWorkers = (FXCollections.observableArrayList((List<Worker>) (List) dt.getValue().getActiveWorkers()));
        } else observableActiveWorkers = null;
        prepareWorkersTable(observableActiveWorkers);
        if (observableActiveWorkers != null) {
            loadComboBoxChecks();
        }
    }

    private void disableFunctionality(boolean b) {
        markAsDoneButt.setDisable(b);
        checkComboBox.setDisable(b);
        addWorkersButton.setDisable(b);
    }

    public void loadComboBoxChecks(){
        checkComboBox.getCheckModel().clearChecks();
        for (int i = 0; i < observableWorkers.size(); i++) {
            if (observableActiveWorkers.contains(checkComboBox.getItems().get(i)))
                checkComboBox.getCheckModel().check(i);
        }
    }



    public void prepareWorkersTable(ObservableList<Worker> ow) {

        JFXTreeTableColumn<Worker, String> idCol = new JFXTreeTableColumn<>("ID");
        prepareColumn(idCol, "id", 33);

        JFXTreeTableColumn<Worker, String> nameCol = new JFXTreeTableColumn<>("Name");
        prepareColumn(nameCol, "name", 207);

        JFXTreeTableColumn<Worker, String> initialsCol = new JFXTreeTableColumn<>("Initials");
        prepareColumn(initialsCol, "initials", 44);

        JFXTreeTableColumn<Worker, String> salaryCol = new JFXTreeTableColumn<>("SalaryNumber");
        prepareColumn(salaryCol, "salary", 93);

        activeWorkersTable.getColumns().addAll(idCol, nameCol, initialsCol, salaryCol);

        setWorkersTable(ow);
    }

    private void setWorkersTable(ObservableList<Worker> ow) {
        TreeItem<Worker> root = new RecursiveTreeItem<>(ow, RecursiveTreeObject::getChildren);
        activeWorkersTable.setRoot(root);
        activeWorkersTable.setShowRoot(false);
    }


    public void addActiveWorkers(ActionEvent actionEvent) {
        if(orderTasksTable.getSelectionModel().getSelectedItem()!=null){
            RecursiveTreeItem<DepartmentTask> dt = (RecursiveTreeItem<DepartmentTask>) orderTasksTable.getSelectionModel().getSelectedItem();
            observableActiveWorkers = checkComboBox.getCheckModel().getCheckedItems();
            for(Worker w: observableActiveWorkers){
                if(!(dt.getValue().getActiveWorkers().contains(w)))
                    dt.getValue().addWorker(w);
            }
            prepareWorkersTable(observableActiveWorkers);
        }
    }
}

