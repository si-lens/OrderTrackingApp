package orderManager.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import orderManager.be.DepartmentTask;
import orderManager.be.IWorker;
import orderManager.be.ProductionOrder;
import orderManager.be.Worker;
import orderManager.gui.model.Model;
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
            for (int i = 0; i < observableTasks.size(); i++)
            {
                if (observableTasks.get(i).getDepartmentName().equals(model.getDepartment().getName()))
                {
                    observableTasks.remove(i+1, observableTasks.size());
                }
            }
            observableWorkers = (FXCollections.observableArrayList((List<Worker>) (List) model.getWorkers()));
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
        }
        loadWorkers();
        setOrderNumber();
        prepareTasksTable();
    }

    @FXML
    private void changeStatus(ActionEvent event) {
        try {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to mark number '" + selectedOrder.getOrderNumber() + "' task as 'Done'?", ButtonType.YES, ButtonType.NO);
            a.showAndWait();
            if (a.getResult() == ButtonType.YES) {
                model.changeStatus(selectedOrder);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        orderTasksTable.getColumns().addAll(department, startDate, endDate, progress);

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
            if (dt.getValue().getDepartment().getName().equals(model.getDepartment().getName())) {
                disableFunctionality(false);
                loadActiveWorkers(dt, false);
            } else {
                disableFunctionality(true);
                loadActiveWorkers(dt, true);
            }
        }
    }

    private void loadActiveWorkers(RecursiveTreeItem<DepartmentTask> dt, boolean disabled) {
        observableActiveWorkers = (FXCollections.observableArrayList((List<Worker>) (List) dt.getValue().getActiveWorkers()));
        prepareWorkersTable(observableActiveWorkers);
        if (observableActiveWorkers != null && disabled == false) {
            loadComboBoxChecks();
        }
    }

    private void disableFunctionality(boolean b) {
        markAsDoneButt.setDisable(b);
        checkComboBox.setDisable(b);
        addWorkersButton.setDisable(b);
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

        JFXTreeTableColumn<Worker, String> idCol = new JFXTreeTableColumn<>("ID");
        prepareColumn(idCol, "id", 33);

        JFXTreeTableColumn<Worker, String> nameCol = new JFXTreeTableColumn<>("Name");
        prepareColumn(nameCol, "name", 207);

        JFXTreeTableColumn<Worker, String> initialsCol = new JFXTreeTableColumn<>("Initials");
        prepareColumn(initialsCol, "initials", 44);

        JFXTreeTableColumn<Worker, String> salaryCol = new JFXTreeTableColumn<>("SalaryNumber");
        prepareColumn(salaryCol, "salary", 93);

        activeWorkersTable.getColumns().clear();
        activeWorkersTable.getColumns().addAll(idCol, nameCol, initialsCol, salaryCol);

        setWorkersTable(ow);
    }

    private void setWorkersTable(ObservableList<Worker> ow) {
        TreeItem<Worker> root = new RecursiveTreeItem<>(ow, RecursiveTreeObject::getChildren);
        activeWorkersTable.setRoot(root);
        activeWorkersTable.setShowRoot(false);
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

/*
  public void addActiveWorkers(ActionEvent actionEvent) throws SQLException {
    if (orderTasksTable.getSelectionModel().getSelectedItem() != null) {
      RecursiveTreeItem<DepartmentTask> dt = (RecursiveTreeItem<DepartmentTask>) orderTasksTable.getSelectionModel().getSelectedItem();

      List<Worker> checkList = checkComboBox.getCheckModel().getCheckedItems();
      List<Worker> workers = (List<Worker>) (List) dt.getValue().getActiveWorkers();

      try {
        loop1:
        for (Worker w : workers) {
          for (Worker w1 : checkList) {
            if (w.getId() == w1.getId()) {
              continue loop1;
            }
          }

          System.out.println("Remove " + w.getId());
          dt.getValue().removeWorker(w);
          System.out.println("Workers list: "+workers.size());
          System.out.println("CheckList: "+checkList.size());

        }

        loop2:
        for (Worker w : checkList) {
          for (Worker w1 : workers) {
            if (w.getId() == w1.getId()) {
              continue loop2;
            }
          }
          System.out.println("Add " + w.getId());
          dt.getValue().addWorker(w);
        }
      } catch (Exception e) {
          e.printStackTrace();
      }
      loadActiveWorkers(dt, false);
    }
  }
 */
}

