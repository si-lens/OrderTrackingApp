package orderManager.be;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class CustomProgressBar extends StackPane {

    private Date sDate;
    private Date eDate;
    private Date currentDate;
    private Text text;
    private ProgressBar pb;
    private String color;
    ChangeListener<Date> changeListener = new ChangeListener<Date>() {
        @Override
        public void changed(ObservableValue<? extends Date> observable, Date oldValue, Date newValue) {

        }
    };

    public CustomProgressBar(Date startDate, Date endDate)
    {
        this.sDate = startDate;
        this.eDate = endDate;
        pb = new ProgressBar();
        setProgressBar();
        text = new Text();
        if (isExpired())
        {
            color = "red";
            pb.setStyle("-fx-accent: " + color);
            setTextLabel();
        } else
        {
            color = "blue";
            pb.setStyle("-fx-accent: " + color);
        }
        getChildren().addAll(pb, text);
    }

    private void setTextLabel()
    {
        LocalDate endDate = LocalDate.parse(eDate.toString());
        LocalDate cDate = LocalDate.now();
        long daysPassed = ChronoUnit.DAYS.between(endDate, cDate);
        text.setText(daysPassed+" day(s)");
    }

    public void setProgressBar()
    {
        String startDateS = sDate.toString();
        String endDateS = eDate.toString();
        LocalDate startDate = LocalDate.parse(startDateS);
        LocalDate endDate = LocalDate.parse(endDateS);
        LocalDate todaysDate = LocalDate.now();
        long daysBetweenStartAndEnd = ChronoUnit.DAYS.between(startDate, endDate);
        long daysBetweenStartAndNow = ChronoUnit.DAYS.between(startDate, todaysDate);
        double valOne = (double) daysBetweenStartAndEnd;
        double valTwo = (double) daysBetweenStartAndNow;
        double progress = valTwo / valOne;
        if(progress<0)
            progress=0;
        pb.progressProperty().set(progress);
    }

    private boolean isExpired()
    {
        LocalDate ld = LocalDate.now();
        LocalDate eD = LocalDate.parse(eDate.toString());
        long timeBetween = ChronoUnit.DAYS.between(ld, eD);
        return timeBetween < 0;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

}
