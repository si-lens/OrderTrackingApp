package orderManager.gui.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import orderManager.be.*;
import orderManager.gui.model.Model;
import orderManager.windowOpener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.*;

public class mainWindowController implements Initializable, Observer {

    public AnchorPane mainPane;
    public JFXButton departmentBtn;
    public JFXTreeTableView ordersTab;
    public Menu changeDepartmentMenuItem;
    public JFXSpinner spinner;
    public MenuBar menuBar;
    public ImageView logo;

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
            try {
                observableOrders = (FXCollections.observableArrayList((List<ProductionOrder>) (List) model.getProductionOrdersFromDB()));
                setIndication();
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

    private void setIndication() throws ParseException {
        for (ProductionOrder p : observableOrders) {
            for (IDepartmentTask depTask : p.getDepartmentTasks())
            {
                if (depTask.getDepartment().getName().equals(chosenDepartment.getName()))
                {
                    p.setIndication(depTask);
                }
            }
        }
    }

    private void prepareOrdersTable() {
        if (ordersTab.getColumns().isEmpty()) {

            JFXTreeTableColumn<ProductionOrder, Label> indication = new JFXTreeTableColumn<>("Progress");
            prepareColumn(indication, "indication", 45);

            JFXTreeTableColumn<ProductionOrder, String> orderNumber = new JFXTreeTableColumn<>("Order Number");
            prepareColumn(orderNumber, "orderNumber", 145);

            JFXTreeTableColumn<ProductionOrder, String> customerName = new JFXTreeTableColumn<>("Customer Name");
            prepareColumn(customerName, "customerName", 145);

            JFXTreeTableColumn<ProductionOrder, Date> deliveryDate = new JFXTreeTableColumn<>("Delivery Date");
            prepareColumn(deliveryDate, "deliveryDate", 145);

            ordersTab.getColumns().addAll(indication, orderNumber, customerName, deliveryDate);

        }

        setOrdersTable();
    }

    private void setOrdersTable() {
        TreeItem<ProductionOrder> root = new RecursiveTreeItem<>(observableOrders, RecursiveTreeObject::getChildren);
        ordersTab.setRoot(root);
        ordersTab.setShowRoot(false);
    }


    private void prepareColumn(JFXTreeTableColumn colName, String attributeName, int colWidth) {
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