package orderManager.be;

import java.util.Date;

public class Department implements IDepartment {
    private String name;

    @Override
    public String getName() {
        return name;
    }
}
