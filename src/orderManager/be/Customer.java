package orderManager.be;

public class Customer implements ICustomer {
    private String name;


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {

        this.name = name;
    }
}
