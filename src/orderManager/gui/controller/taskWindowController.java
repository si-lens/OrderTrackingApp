package orderManager.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
import javafx.stage.Stage;
import orderManager.be.*;
import orderManager.gui.model.Model;
import org.controlsfx.control.CheckComboBox;

public class taskWindowController implements Initializable {

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
    private ScheduledExecutorService s;
    private int clickedTaskIndex;
    private boolean selectionDone;
    private boolean initialLoadDone;
    private RecursiveTreeItem<DepartmentTask> dt;
    private boolean taskCanBeMarked;
    private Stage stage;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        selectedOrder = model.getSelectedProductionOrder();
        setOrderNumber();
        try {
            observableWorkers = (FXCollections.observableArrayList((List<Worker>) (List) model.getWorkers()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadWorkers();
        refresh();
        orderTasksTable.widthProperty().addListener((observable, oldValue, newValue) -> orderTasksTable.setStyle("-fx-font-size: " + newValue.doubleValue()/32));
        activeWorkersTable.widthProperty().addListener((observable, oldValue, newValue) -> activeWorkersTable.setStyle("-fx-font-size: " + newValue.doubleValue()/30));

    }

    public void setStage(Stage stage){
        this.stage=stage;
        stage.setOnCloseRequest(event -> s.shutdown());
    }



    @FXML
    private void changeStatus(ActionEvent event) {
        try {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to mark number '" + selectedOrder.getOrderNumber() + "' task as 'Done'?", ButtonType.YES, ButtonType.NO);
            a.showAndWait();
            if (a.getResult() == ButtonType.YES) {
                dt.getValue().setProgressBar(CustomProgressBar.Status.DONE);
                model.changeStatus(selectedOrder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        s = Executors.newSingleThreadScheduledExecutor();
        s.scheduleAtFixedRate(() -> {
            try {
                System.out.println("task refresh leap");
                observableTasks = (FXCollections.observableArrayList((List<DepartmentTask>) (List) selectedOrder.getDepartmentTasks()));
                selectUsefulTasks();
                observableWorkers = (FXCollections.observableArrayList((List<Worker>) (List) model.getWorkers()));
                Platform.runLater(() -> {
                    prepareTasksTable();
                    if(!initialLoadDone)setInitialWorkersTable();
                    markAsDoneButt.setDisable(isProgressBarClickable());
                });
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, 0, 2500, TimeUnit.MILLISECONDS);
    }

    private void selectUsefulTasks() {
        for (int i = 0; i < observableTasks.size(); i++)
        {
            if (observableTasks.get(i).getDepartmentName().equals(model.getDepartment().getName()))
            {
                observableTasks.remove(i+1, observableTasks.size());
            }
        }
    }

    private void setOrderNumber() {
        orderNumberLabel.setText("Order: " + selectedOrder.getOrderNumber());
    }

    private void loadWorkers() {
        checkComboBox = new CheckComboBox();
        checkComboBox.setMaxWidth(140);
        for (IWorker w : observableWorkers) {
            checkComboBox.getItems().add(w);
        }
        comboBoxPane.getChildren().add(checkComboBox);
    }

    public void prepareTasksTable() {
        if(orderTasksTable.getColumns().isEmpty()) {
            JFXTreeTableColumn<DepartmentTask, String> department = new JFXTreeTableColumn<>("Department");
            prepareColumn(department, "department", 94);

            JFXTreeTableColumn<DepartmentTask, Date> startDate = new JFXTreeTableColumn<>("Start Date");
            prepareColumn(startDate, "startDate", 94);

            JFXTreeTableColumn<DepartmentTask, Date> endDate = new JFXTreeTableColumn<>("End Date");
            prepareColumn(endDate, "endDate", 94);

            JFXTreeTableColumn<DepartmentTask, ProgressBar> progress = new JFXTreeTableColumn<>("Progress");
            prepareColumn(progress, "progressBar", 94);

            orderTasksTable.getColumns().addAll(department, startDate, endDate, progress);
        }
        setTaskTable();
    }

    private void setTaskTable() {
        TreeItem<DepartmentTask> root = new RecursiveTreeItem<>(observableTasks, RecursiveTreeObject::getChildren);
        orderTasksTable.setRoot(root);
        orderTasksTable.setShowRoot(false);
        orderTasksTable.getSelectionModel().select(clickedTaskIndex);
    }

    public void prepareColumn(JFXTreeTableColumn colName, String attributeName, int colWidth) {
        colName.setCellValueFactory(new TreeItemPropertyValueFactory<>(attributeName));
        colName.setMinWidth(colWidth);
        colName.setStyle("-fx-alignment: CENTER");
    }

    @FXML
    private void loadClickedContent(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() > 0 && orderTasksTable.getSelectionModel()!=null) {
            dt = (RecursiveTreeItem<DepartmentTask>) orderTasksTable.getSelectionModel().getSelectedItem();
            clickedTaskIndex= orderTasksTable.getSelectionModel().getSelectedIndex();
            if (dt.getValue().getDepartment().getName().equals(model.getDepartment().getName())) {
                disableFunctionality(false);
                loadActiveWorkers(dt, false);
            } else {
                disableFunctionality(true);
                loadActiveWorkers(dt, true);
            }
        }
    }

    private void setInitialWorkersTable()
    {
        dt = (RecursiveTreeItem<DepartmentTask>) orderTasksTable.getTreeItem(observableTasks.size()-1);
        clickedTaskIndex = observableTasks.size()-1;
        orderTasksTable.getSelectionModel().select(clickedTaskIndex);
        loadActiveWorkers(dt, false);
        initialLoadDone=true;
    }

    private void loadActiveWorkers(RecursiveTreeItem<DepartmentTask> dt, boolean disabled) {
        observableActiveWorkers = (FXCollections.observableArrayList((List<Worker>) (List) dt.getValue().getActiveWorkers()));
        prepareWorkersTable(observableActiveWorkers);
        if (observableActiveWorkers != null && disabled == false) {
            loadComboBoxChecks();
        }
    }

    private void disableFunctionality(boolean b) {
        checkComboBox.setDisable(b);
        addWorkersButton.setDisable(b);
        if(taskCanBeMarked)
            markAsDoneButt.setDisable(b);
    }

    public void loadComboBoxChecks() {
        checkComboBox.getCheckModel().clearChecks();
        for (int i = 0; i < observableWorkers.size(); i++) {
            for (Worker w : observableActiveWorkers) {
                if (w.getId() == observableWorkers.get(i).getId()) {
                    checkComboBox.getCheckModel().check(i);
                }
            }
        }
    }

    public void prepareWorkersTable(ObservableList<Worker> ow) {

        if(activeWorkersTable.getColumns().isEmpty()){
            JFXTreeTableColumn<Worker, String> idCol = new JFXTreeTableColumn<>("ID");
            prepareColumn(idCol, "id", 33);

            JFXTreeTableColumn<Worker, String> nameCol = new JFXTreeTableColumn<>("Name");
            prepareColumn(nameCol, "name", 187);

            JFXTreeTableColumn<Worker, String> initialsCol = new JFXTreeTableColumn<>("Initials");
            prepareColumn(initialsCol, "initials", 54);

            JFXTreeTableColumn<Worker, String> salaryCol = new JFXTreeTableColumn<>("SalaryNumber");
            prepareColumn(salaryCol, "salary", 103);

            activeWorkersTable.getColumns().clear();
            activeWorkersTable.getColumns().addAll(idCol, nameCol, initialsCol, salaryCol);
        }

        setWorkersTable(ow);
    }

    private void setWorkersTable(ObservableList<Worker> ow) {
        TreeItem<Worker> root = new RecursiveTreeItem<>(ow, RecursiveTreeObject::getChildren);
        activeWorkersTable.setRoot(root);
        activeWorkersTable.setShowRoot(false);
        orderTasksTable.getSelectionModel().select(clickedTaskIndex);
    }

    public void addRemoveButton(ActionEvent actionEvent) throws SQLException {
        if (orderTasksTable.getSelectionModel().getSelectedItem() != null) {
            RecursiveTreeItem<DepartmentTask> dt = (RecursiveTreeItem<DepartmentTask>) orderTasksTable.getSelectionModel().getSelectedItem();

            List<IWorker> checkList = checkComboBox.getCheckModel().getCheckedItems();
            List<IWorker> activeWorkers = dt.getValue().getActiveWorkers();
            List<IWorker> allWorkers = model.getWorkers();
            List<IWorker> toRemove = new ArrayList<>();
            List<IWorker> toAdd = new ArrayList<>();
            removeActiveWorkers(dt, activeWorkers, toRemove);
            addActiveWorkers(dt, allWorkers, checkList, toAdd);
            loadActiveWorkers(dt, false);
        }
    }

    private void addActiveWorkers(RecursiveTreeItem<DepartmentTask> dt, List<IWorker> allWorkers, List<IWorker> checkList, List<IWorker> toAdd) throws SQLException {
        for (IWorker w : checkList) {
            for (IWorker w1 : allWorkers) {

                if (w.getId() == w1.getId()) {
                    toAdd.add(w);
                }
            }
        }
        if (toAdd.size() > 0) {
            for (IWorker w : toAdd) {
                dt.getValue().addWorker(w);
            }
        }
    }

    private void removeActiveWorkers(RecursiveTreeItem<DepartmentTask> dt, List<IWorker> workers, List<IWorker> toRemove) throws SQLException {
        for (IWorker w : workers) {
            toRemove.add(w);
        }

        if (toRemove.size() > 0) {
            for (IWorker w : toRemove) {
                dt.getValue().removeWorker(w);
            }
        }
    }

    private Boolean isProgressBarClickable()
    {
        Boolean b = null;
        CustomProgressBar.Status previousOrderStatus = null;
        CustomProgressBar.Status currentOrderStatus = observableTasks.get(observableTasks.size() - 1).getProgressBar().getStatus();
        if (observableTasks.size() == 1)
        {
            if(currentOrderStatus!= CustomProgressBar.Status.DONE)
                b = false;
            else
                b = true;
        }
        else if (observableTasks.size() > 1) {
            previousOrderStatus = observableTasks.get(observableTasks.size() - 2).getProgressBar().getStatus();
            if(currentOrderStatus != CustomProgressBar.Status.DONE && previousOrderStatus == CustomProgressBar.Status.DONE)
                b = false;
            else
                b = true;
        }
        return b;
    }
}

