package orderManager.be;

public class Order implements IOrder {
private String orderNumber;

 public Order(String orderNumber){
     this.orderNumber = orderNumber;
 }

    @Override
    public String getOrderNumber() {
        return  orderNumber;
    }
}
