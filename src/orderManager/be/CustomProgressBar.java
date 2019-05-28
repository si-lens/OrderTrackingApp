package orderManager.be;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class CustomProgressBar extends StackPane {

    private Date sDate;
    private Date eDate;
    private Text text;
    private ProgressBar pb;
    private Status status;

    public enum Status
    {
        DONE, BEHIND, NOT_STARTED, ALL_GOOD, DEFAULT
    }

    CustomProgressBar(Date startDate, Date endDate, Status status)
    {
        sDate = startDate;
        eDate = endDate;
        this.status = status;
        pb = new ProgressBar();
        pb.setMaxWidth(200);
        pb.setMaxHeight(20);
        text = new Text();
        setProgressBar();
        setStatus(status);
        getChildren().addAll(pb, text);
    }

    private void setTextLabel()
    {
        LocalDate endDate = LocalDate.parse(eDate.toString());
        LocalDate cDate = LocalDate.now();
        long daysPassed = ChronoUnit.DAYS.between(endDate, cDate);
        text.setText(daysPassed+" day(s)");
    }

    private void setProgressBar()
    {
        String startDateS = sDate.toString();
        String endDateS = eDate.toString();
        LocalDate startDate = LocalDate.parse(startDateS);
        LocalDate endDate = LocalDate.parse(endDateS);
        LocalDate todaysDate = LocalDate.now();
        long daysBetweenStartAndEnd = ChronoUnit.DAYS.between(startDate, endDate);
        long daysBetweenStartAndNow = ChronoUnit.DAYS.between(startDate, todaysDate);
        double valOne = daysBetweenStartAndEnd;
        double valTwo = daysBetweenStartAndNow;
        double progress = valTwo / valOne;
        if(progress<0)
            progress=0;
        pb.progressProperty().set(progress);
    }

    private void setStatus(Status status)
    {
        switch (status)
        {
            case DONE:
                pb.setStyle("-fx-accent: #53E958");
                break;
            case NOT_STARTED:
                pb.setStyle("-fx-accent: #E8D528");
                break;
            case BEHIND:
                pb.setStyle("-fx-accent: #F32B2B");
                setTextLabel();
                break;
            case ALL_GOOD:
                pb.setStyle("-fx-accent: #41A0F3");
                break;
            case DEFAULT:
                break;
        }
    }

    public Status getStatus()
    {
        return status;
    }

    Label getIndication()
    {
        Label label = new Label();
        switch (status)
        {
            case DONE:
                label.setText("\u2713");
                label.setStyle("-fx-text-fill: #53E958; -fx-alignment: center; -fx-font-size: 40; -fx-background-size: 94");
                break;
            case ALL_GOOD:
                label.setText("O");
                label.setStyle("-fx-text-fill: #41A0F3; -fx-alignment: center; -fx-font-size: 40; -fx-background-size: 94");
                break;
            case NOT_STARTED:
                label.setText("?");
                label.setStyle("-fx-text-fill: #E8D528; -fx-alignment: center; -fx-font-size: 40; -fx-background-size: 94");
                break;
            case BEHIND:
                label.setText("!");
                label.setStyle("-fx-text-fill: #F32B2B; -fx-alignment: center; -fx-font-size: 40; -fx-background-size: 94");
                break;
        }
        return label;
    }
}
