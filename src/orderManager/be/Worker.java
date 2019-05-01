package orderManager.be;

public class Worker implements IWorker {
    private String name;
    private String initials;
    private int salary;
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getInitials() {
        return initials;
    }

    @Override
    public int getSalaryNumber() {
        return salary;
    }
}
