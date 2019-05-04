package orderManager.be;

public class Worker implements IWorker {
    private String initials;
    private String name;
    private long salary;
    private int id;


    public Worker(int id, String name, String initials, long salary) {
        this.initials = initials;
        this.name = name;
        this.salary = salary;
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getInitials() {
        return initials;
    }

    @Override
    public long getSalary() {
        return salary;
    }

    @Override
    public int getId() {
        return id;
    }


}
