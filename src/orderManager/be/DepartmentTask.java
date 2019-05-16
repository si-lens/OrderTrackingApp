package orderManager.be;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;

import java.util.Date;
import java.util.List;

public class DepartmentTask extends RecursiveTreeObject<orderManager.be.DepartmentTask> implements IDepartmentTask {


    private IDepartment department;
    private boolean orderState;
    private Date startDate;
    private Date endDate;
    private List<IWorker> listOfWorkers;

    public DepartmentTask(Date startDate, Date endDate, boolean orderState, IDepartment department, List<IWorker> list){
        this.orderState = orderState;
        this.startDate = startDate;
        this.endDate = endDate;
        this.department = department;
    }
    public String getDepartmentName(){ return department.getName();}

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
