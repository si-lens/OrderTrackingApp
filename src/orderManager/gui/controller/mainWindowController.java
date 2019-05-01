package orderManager.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.time.temporal.ChronoUnit;

import com.jfoenix.controls.JFXProgressBar;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import orderManager.be.Department;
import orderManager.be.DepartmentTask;
import orderManager.dal.jsonReader;

import static java.time.temporal.ChronoUnit.DAYS;

public class mainWindowController implements Initializable {

    public AnchorPane mainPane;
    public Label dateLabel;
    public JFXProgressBar estimatedProgressBar;
    public Text estimatedProgressLabel;
    private ScheduledExecutorService executor;
    private DepartmentTask actualDepartmentTask;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayTime();
        try {
            jsonReader.readFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLServerException e) {
            e.printStackTrace();
        }
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
