package orderManager.gui.controller;

import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import orderManager.be.DepartmentTask;
import orderManager.be.Worker;
import orderManager.bll.mainLogicClass;
import orderManager.dal.jsonReader;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class mainWindowController implements Initializable {

    public AnchorPane mainPane;
    public Label dateLabel;
    public JFXProgressBar estimatedProgressBar;
    public Text estimatedProgressLabel;
    public JFXTreeTableView workersTable;
    private ScheduledExecutorService executor;
    private DepartmentTask actualDepartmentTask;
    private mainLogicClass mainLogic;
    private List<Worker> workerList;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainLogic = new mainLogicClass();
        displayTime();
    //    loadWorkersTable();
        try {
            jsonReader.readFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLServerException e) {
            e.printStackTrace();
        }
        /*
        try {
            workerList = mainLogic.getWorkers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
*/
        try {
            calculateEstimatedProgress();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void displayTime() {
        Runnable thread = () -> setTime();
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(thread, 0, 1, TimeUnit.SECONDS);
    }

    public void setTime() {
        Platform.runLater(() -> dateLabel.setText(String.valueOf(Calendar.getInstance().getTime())));
    }
/*
    public void loadWorkersTable(){
        JFXTreeTableColumn<Worker, String>  type = new JFXTreeTableColumn<>("Type");
        JFXTreeTableColumn<Worker, String> initials = new JFXTreeTableColumn<>("Initial");
        JFXTreeTableColumn<Worker, String>  name = new JFXTreeTableColumn<>("Name");
        JFXTreeTableColumn<Worker, Long>  salary = new JFXTreeTableColumn<>("Salary");
        JFXTreeTableColumn<Worker, Integer>  id = new JFXTreeTableColumn<>("ID");

        type.setCellValueFactory(new TreeItemPropertyValueFactory<>("type"));
        initials.setCellValueFactory(new TreeItemPropertyValueFactory<>("initials"));
        name.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        salary.setCellValueFactory(new TreeItemPropertyValueFactory<>("salary"));
        id.setCellValueFactory(new TreeItemPropertyValueFactory<>("id"));

        workersTable.getColumns().addAll(type,initials,name,salary,id);

    }
*/
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
        String startDateS = "2019-04-29";
        String endDateS = "2019-06-01";
        LocalDate startDate = LocalDate.parse(startDateS);
        LocalDate endDate = LocalDate.parse(endDateS);
        LocalDate todaysDate = LocalDate.now();
        long daysBetweenStartAndEnd = ChronoUnit.DAYS.between(startDate, endDate);
        long daysBetweenStartAndNow = ChronoUnit.DAYS.between(startDate, todaysDate);
        double valOne = (double)daysBetweenStartAndEnd;
        double valTwo = (double)daysBetweenStartAndNow;
        double progress = valTwo/valOne;
        estimatedProgressLabel.setText((int)(progress*100)+"%");
        estimatedProgressBar.progressProperty().set(progress);

    }


}
