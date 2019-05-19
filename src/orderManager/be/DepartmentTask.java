package orderManager.be;

import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import orderManager.gui.model.Model;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class DepartmentTask extends RecursiveTreeObject<orderManager.be.DepartmentTask> implements IDepartmentTask {


    private IDepartment department;
    private boolean orderState;
    private Date startDate;
    private Date endDate;
    private List<IWorker> listOfWorkers;
    private ProgressBar progressBar;
    private ProgressIndicator progressIndicator;
    private Double progressBarValue;
    private Model model;

    public DepartmentTask(Date startDate, Date endDate, boolean orderState, IDepartment department, List<IWorker> list) throws ParseException {
        this.orderState = orderState;
        this.startDate = startDate;
        this.endDate = endDate;
        this.department = department;
        model=Model.getInstance();
        setProgressBar();

    }
    public String getDepartmentName(){ return department.getName();}

    public void setProgressBar() throws ParseException {
        progressBarValue = model.getEstimatedProgress(this.startDate,this.endDate);
        progressBar = new ProgressBar();
        progressBar.progressProperty().set(progressBarValue);
    }


    public ProgressBar getProgressBar(){
        return progressBar;
    }

    @Override
    public IDepartment getDepartment() {
        return department;
    }

    @Override
    public Boolean getFinishedOrder() {
       return orderState;
    }

    @Override
    public Date getStartDate() {
       return startDate;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public void addWorker(IWorker worker) {
    listOfWorkers.add(worker);
    }

    @Override
    public void removeWorker(IWorker worker) {
        listOfWorkers.remove(worker);
    }

    @Override
    public EventHandler workerAdded() {
        // There will be a method which adds a worker to the table in UI  and database
        return null;
    }

    @Override
    public EventHandler workerRemoved() {
        // There will be a method which removes a worker from the table in UI  and database
        return null;
    }

    @Override
    public List<IWorker> getActiveWorkers() {
        return listOfWorkers;
    }

}
