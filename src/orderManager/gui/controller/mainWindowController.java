package orderManager.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javax.swing.JFrame;
import orderManager.be.IDepartment;
import orderManager.be.ProductionOrder;
import orderManager.be.Worker;
import orderManager.gui.model.Model;
import orderManager.windowOpener;

public class mainWindowController implements Initializable, Observer {

  public AnchorPane mainPane;
  public Label dateLabel;
  public JFXProgressBar estimatedProgressBar;
  public Text estimatedProgressLabel;
  public JFXButton departmentBtn;
  public JFXTreeTableView workersTab;
  public JFXTreeTableView ordersTab;


  private ScheduledExecutorService executor;
  private ObservableList<Worker> observableWorkers;
  private ObservableList<ProductionOrder> observableOrders;
  private IDepartment chosenDepartment;
  private Model model;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    model = Model.getInstance();
    chosenDepartment = model.getDepartment();
    departmentBtn.setText(chosenDepartment.getName());
    displayTime();
    refresh();
  }

  private void refresh() {
    Runnable runnable = () -> Platform.runLater(() -> {
      try {
        observableOrders = (FXCollections.observableArrayList((List<ProductionOrder>) (List) model.getProductionOrders()));
        prepareOrdersTable();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
    ScheduledExecutorService ses = Executors.newScheduledThreadPool(2);
    ses.scheduleWithFixedDelay(runnable, 0, 600, TimeUnit.SECONDS);
  }

  public void displayTime() {
    Runnable thread = () -> setTime();
    executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(thread, 0, 1, TimeUnit.SECONDS);
  }

  public void setTime() {
    Platform.runLater(() -> dateLabel.setText(String.valueOf(Calendar.getInstance().getTime())));
  }


  public void prepareOrdersTable() {
    if (ordersTab.getColumns().isEmpty()) {

      JFXTreeTableColumn<ProductionOrder, String> orderNumber = new JFXTreeTableColumn<>("Order Number");
      prepareColumn(orderNumber, "orderNumber", 145);

      JFXTreeTableColumn<ProductionOrder, String> customerName = new JFXTreeTableColumn<>("Customer Name");
      prepareColumn(customerName, "customerName", 145);

      JFXTreeTableColumn<ProductionOrder, Date> deliveryDate = new JFXTreeTableColumn<>("Delivery Date");
      prepareColumn(deliveryDate, "deliveryDate", 145);

      ordersTab.getColumns().addAll(orderNumber, customerName, deliveryDate);

    }

    setOrdersTable();
  }

  private void setOrdersTable() {
    TreeItem<ProductionOrder> root = new RecursiveTreeItem<>(observableOrders, RecursiveTreeObject::getChildren);
    ordersTab.setRoot(root);
    ordersTab.setShowRoot(false);
  }


  public void prepareColumn(JFXTreeTableColumn colName, String attributeName, int colWidth) {
    colName.setCellValueFactory(new TreeItemPropertyValueFactory<>(attributeName));
    colName.setMinWidth(colWidth);
    colName.setStyle("-fx-alignment: CENTER");
  }

  public void calculateEstimatedProgress() throws ParseException {

    //  its for later use when we will have start and end date
    /*
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date sDate = actualDepartmentTask.getStartTime();
        Date eDate = actualDepartmentTask.getEndDate();
        Instant instant = sDate.toInstant();
        Instant instant1 = eDate.toInstant();
        LocalDate startDate = instant.atZone(defaultZoneId).toLocalDate();
        LocalDate todaysDate = LocalDate.now();
        LocalDate endDate = instant1.atZone(defaultZoneId).toLocalDate();
        long daysBetweenStartAndEnd = ChronoUnit.DAYS.between(startDate, endDate);
        long daysBetweenStartAndNow = ChronoUnit.DAYS.between(startDate, todaysDate);
        double valOne = (double)daysBetweenStartAndEnd;
        double valTwo = (double)daysBetweenStartAndNow;
        double progress = valTwo/valOne;
        estimatedProgressLabel.setText((int)(progress*100)+"%");
        estimatedProgressBar.progressProperty().set(((double)progress));
*/
    //Its for now, raw data
    String startDateS = "2019-04-23";
    String endDateS = "2019-06-03";
    LocalDate startDate = LocalDate.parse(startDateS);
    LocalDate endDate = LocalDate.parse(endDateS);
    LocalDate todaysDate = LocalDate.now();
    long daysBetweenStartAndEnd = ChronoUnit.DAYS.between(startDate, endDate);
    long daysBetweenStartAndNow = ChronoUnit.DAYS.between(startDate, todaysDate);
    double valOne = (double) daysBetweenStartAndEnd;
    double valTwo = (double) daysBetweenStartAndNow;
    double progress = valTwo / valOne;
    estimatedProgressBar.progressProperty().set(progress);

  }


  @FXML
  private void clickToPickFile(ActionEvent event)
      throws IOException, SQLException // While creating/editing a song we are using this button to pick path of the song.
  {
    FileDialog fd = new FileDialog(new JFrame());
    fd.setFile("*.json");
    fd.setVisible(true);
    File[] f = fd.getFiles();
    if (f.length > 0) {
      String fullPath = f[0].toString();
      int index = f[0].toString().lastIndexOf('\\');
      String finalPath = fullPath.substring(index + 1);
      model.getManager().readFile(finalPath);

    }
  }

  @Override
  public void update(Observable o, Object arg) {
    Platform.runLater(() -> {
      try {
        observableWorkers = (FXCollections.observableArrayList((List<Worker>) (List) model.getWorkers()));
        calculateEstimatedProgress();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  public void doubleClickDetails(MouseEvent mouseEvent) throws IOException {
    if (mouseEvent.getClickCount() == 2) {
      RecursiveTreeItem<ProductionOrder> od = (RecursiveTreeItem<ProductionOrder>) ordersTab.getSelectionModel().getSelectedItem();
      model.setSelectedProductionOrder(od.getValue());
      new windowOpener("gui/view/taskWindow.fxml", 682, 446, true);

    }
  }


}