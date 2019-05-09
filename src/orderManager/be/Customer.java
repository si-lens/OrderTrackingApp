package orderManager.be;

public class Customer implements ICustomer {
    private String name;
    private int id;


    public Customer(int id, String name) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {

        this.name = name;
    }
}
