package orderManager.be;

public class Order implements IOrder {
private String orderNumber;
private int id;

 public Order(int id, String orderNumber){
     this.orderNumber = orderNumber;
     this.id = id;
 }

    @Override
    public String getOrderNumber() {
        return  orderNumber;
    }
}
