package orderManager.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import orderManager.be.DepartmentTask;
import orderManager.be.IDepartmentTask;
import orderManager.be.IWorker;
import orderManager.be.ProductionOrder;
import orderManager.be.Worker;
import orderManager.gui.model.Model;

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
    observableTasks = (FXCollections.observableArrayList((List<DepartmentTask>) (List) selectedOrder.getDepartmentTasks()));
    try {
      loadWorkers();
      observableWorkers = (FXCollections.observableArrayList((List<Worker>) (List) model.getWorkers()));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    setOrderNumber();
    prepareTasksTable();
    prepareWorkersTable();
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
    prepareColumn(department, "department", 115);

    JFXTreeTableColumn<DepartmentTask, Date> startDate = new JFXTreeTableColumn<>("Start Date");
    prepareColumn(startDate, "startDate", 115);

    JFXTreeTableColumn<DepartmentTask, Date> endDate = new JFXTreeTableColumn<>("End Date");
    prepareColumn(endDate, "endDate", 115);

    JFXTreeTableColumn<DepartmentTask, Boolean> finishedOrder = new JFXTreeTableColumn<>("Finished Order");
    prepareColumn(finishedOrder, "finishedOrder", 36);

    orderTasksTable.getColumns().addAll(department, startDate, endDate, finishedOrder);

    TreeItem<DepartmentTask> root = new RecursiveTreeItem<>(observableTasks, RecursiveTreeObject::getChildren);
    orderTasksTable.setRoot(root);
    orderTasksTable.setShowRoot(false);
  }

  public void prepareWorkersTable() {
    JFXTreeTableColumn<Worker, String> initialsCol = new JFXTreeTableColumn<>("Initials");
    prepareColumn(initialsCol, "initials", 145);

    JFXTreeTableColumn<Worker, String> nameCol = new JFXTreeTableColumn<>("Name");
    prepareColumn(nameCol, "name", 231);

    JFXTreeTableColumn<Worker, String> salaryCol = new JFXTreeTableColumn<>("SalaryNumber");
    prepareColumn(salaryCol, "salary", 115);

    JFXTreeTableColumn<Worker, String> idCol = new JFXTreeTableColumn<>("ID");
    prepareColumn(idCol, "id", 36);

    activeWorkersTable.getColumns().addAll(idCol, nameCol, initialsCol, salaryCol);

    TreeItem<Worker> root = new RecursiveTreeItem<>(observableWorkers, RecursiveTreeObject::getChildren);
    activeWorkersTable.setRoot(root);
    activeWorkersTable.setShowRoot(false);
  }

  public void prepareColumn(JFXTreeTableColumn colName, String attributeName, int colWidth) {
    colName.setCellValueFactory(new TreeItemPropertyValueFactory<>(attributeName));
    colName.setMinWidth(colWidth);
    colName.setStyle("-fx-alignment: CENTER");
  }

}