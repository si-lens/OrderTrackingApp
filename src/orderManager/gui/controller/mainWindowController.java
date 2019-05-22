package orderManager.gui.controller;

import com.jfoenix.controls.*;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import javax.swing.JFrame;

import javafx.stage.Stage;
import orderManager.be.Department;
import orderManager.be.DepartmentTask;
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
    public Menu changeDepartmentMenuItem;
    public JFXSpinner spinner;
    public MenuBar menuBar;
    public ImageView logo;


    private ScheduledExecutorService executor;
    private ObservableList<Worker> observableWorkers;
    private ObservableList<ProductionOrder> observableOrders;
    private IDepartment chosenDepartment;
    private Model model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startSpinning();
        model = Model.getInstance();
        chosenDepartment = model.getDepartment();
        departmentBtn.setText(chosenDepartment.getName());
        initialLoad();
    }

    private void initialLoad() {

        Thread t = new Thread(() -> {
            System.out.println("thread dziala");
            try {
                observableOrders = (FXCollections.observableArrayList((List<ProductionOrder>) (List) model.getProductionOrdersFromDB()));
            } catch (SQLException | ParseException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                prepareOrdersTable();
                loadDepartmentsItems();
                endSpinning();
            });

        });
        t.start();
    }


    private void startSpinning() {
        departmentBtn.setVisible(false);
        ordersTab.setVisible(false);
        menuBar.setVisible(false);
    }

    private void endSpinning() {
        departmentBtn.setVisible(true);
        ordersTab.setVisible(true);
        menuBar.setVisible(true);
        spinner.setVisible(false);
        logo.setVisible(false);
    }

/*
  private void refresh() {
    Runnable runnable = () -> Platform.runLater(() -> {
      try {
        observableOrders = (FXCollections.observableArrayList((List<ProductionOrder>) (List) model.getProductionOrdersFromDB()));
        prepareOrdersTable();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
    ScheduledExecutorService ses = Executors.newScheduledThreadPool(2);
    ses.scheduleWithFixedDelay(runnable, 30, 6, TimeUnit.SECONDS);

  }
*/


    public void prepareOrdersTable() {
        if (ordersTab.getColumns().isEmpty()) {

            JFXTreeTableColumn<ProductionOrder, String> orderNumber = new JFXTreeTableColumn<>("Order Number");
            prepareColumn(orderNumber, "orderNumber", 145);

            JFXTreeTableColumn<ProductionOrder, String> customerName = new JFXTreeTableColumn<>("Customer Name");
            prepareColumn(customerName, "customerName", 145);

            JFXTreeTableColumn<ProductionOrder, Date> deliveryDate = new JFXTreeTableColumn<>("Delivery Date");
            prepareColumn(deliveryDate, "deliveryDate", 145);

            JFXTreeTableColumn<ProductionOrder, ProgressBar> progress = new JFXTreeTableColumn<>("Progress");
            prepareColumn(progress, "progressBar", 94);

            ordersTab.getColumns().addAll(orderNumber, customerName, deliveryDate, progress);

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void doubleClickDetails(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2) {
            RecursiveTreeItem<ProductionOrder> od = (RecursiveTreeItem<ProductionOrder>) ordersTab.getSelectionModel().getSelectedItem();
            model.setSelectedProductionOrder(od.getValue());
            new windowOpener("gui/view/taskWindow.fxml", 804, 513, true);
        }
    }

    private void loadDepartmentsItems() {
        for (IDepartment d : model.getDepartments()) {
            if (!(d.getName().equals(chosenDepartment.getName()))) {
                MenuItem mi = new MenuItem();
                mi.setText(d.getName());
                mi.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        IDepartment d = new Department(mi.getText());
                        Model.getInstance().setDepartment(d);
                        Stage stage = (Stage) ordersTab.getScene().getWindow();
                        stage.close();
                        try {
                            new windowOpener("gui/view/mainWindow.fxml", 651, 496, true);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                changeDepartmentMenuItem.getItems().add(mi);
            }
        }
    }
}