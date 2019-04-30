package orderManager.gui.controller;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import orderManager.dal.jsonReader;

public class mainWindowController implements Initializable {

  public AnchorPane mainPane;
  public Label dateLabel;
  private ScheduledExecutorService executor;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    displayTime();
    jsonReader.readFile();
  }

  public void displayTime() {
    Runnable thread = () -> setTime();
    executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(thread, 0, 1, TimeUnit.SECONDS);
  }

  public void setTime() {
    Platform.runLater(() -> dateLabel.setText(String.valueOf(Calendar.getInstance().getTime())));

  }


}
