package orderManager.be;

public class Order implements IOrder {
private String orderNumber;
    @Override
    public String getOrderNumber() {
        return  orderNumber;
    }
}
