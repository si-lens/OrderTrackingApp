package orderManager.be;

public class Worker implements IWorker {
    private String type;
    private String initials;
    private String name;
    private long salary;
    private int id;


    public Worker(String type, String initials, String name, long salary, int id) {
        this.type = type;
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
    public long getSalaryNumber() {
        return salary;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }


}
