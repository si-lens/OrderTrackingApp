package orderManager.be;

public class Department implements IDepartment {
    private String name;
    private int departmentTaskID;

    public Department(String name, int departmentTaskID){
        this.name = name;
        this.departmentTaskID = departmentTaskID;
    }
    @Override
    public String getName() {
        return name;
    }
}
