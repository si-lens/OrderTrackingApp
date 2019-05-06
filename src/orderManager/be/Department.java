package orderManager.be;

import java.util.Date;

public class Department implements IDepartment {
    private String name;
    private int departmentTaskID;

    public Department(String name){
        this.name = name;
    }
    @Override
    public String getName() {
        return name;
    }
}
