package orderManager.be;

import javafx.concurrent.Worker;
import javafx.event.EventHandler;

import java.util.Date;
import java.util.List;

public class DepartmentTask implements IDepartmentTask {

    private  int id;
    private int productionOrderID;
    private Department department;
    private boolean orderState;
    private Date startDate;
    private Date endDate;
    private List<Worker> listOfWorkers;

    public DepartmentTask(int id, int productionOrderID, boolean orderState, Date startDate, Date endDate){
        this.id = id;
        this.productionOrderID = productionOrderID;
        this.orderState = orderState;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public IDepartment getDepartment() {
        return department;
    }

    @Override
    public Boolean isOrderFinished() {
       return orderState;
    }

    @Override
    public Date getStartTime() {
       return startDate;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public void addWorker(Worker worker) {
    listOfWorkers.add(worker);
    }

    @Override
    public void removeWorker(Worker worker) {
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
    public List<Worker> getActiveWorkers() {
        return listOfWorkers;
    }
}
