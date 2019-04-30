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

public class mainWindowController implements Initializable {

    public AnchorPane mainPane;
    public Label dateLabel;
    private ScheduledExecutorService executor;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.displayTime();
    }

    public void displayTime() {
        final Runnable thread = () -> {
            this.setTime();
        };
        this.executor = Executors.newScheduledThreadPool(1);
        this.executor.scheduleAtFixedRate(thread, 0, 1, TimeUnit.SECONDS);
    }

    public void setTime() {
        Platform.runLater(() -> {
            this.dateLabel.setText(String.valueOf(Calendar.getInstance().getTime()));
        });

    }


}
