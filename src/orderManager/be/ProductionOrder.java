package orderManager.be;

import java.util.List;

public class ProductionOrder implements IProductionOrder {
    private int id;
    private String type;
    private Order order;
    private Delivery delivery;
    private Customer customer;
    private List<DepartmentTask> departmentTaskList;

    public ProductionOrder(int id, String type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public Order getOrder() {
        return order;
    }

    @Override
    public Delivery getDelivery() {
        return delivery;
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public List<DepartmentTask> getDepartmentTasks() {
        return departmentTaskList;
    }

    @Override
    public void addDepartmentTask(DepartmentTask task) {
     departmentTaskList.add(task);
    }

    @Override
    public void removeDepartmentTask(DepartmentTask task) {
    departmentTaskList.remove(task);
    }
}
