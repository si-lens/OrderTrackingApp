package orderManager.be;

public class Department implements IDepartment {
    private String name;

    public Department(String name){
        this.name = name;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
