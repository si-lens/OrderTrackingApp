package orderManager.be;

public class Order implements IOrder {
private String orderNumber;
private int id;

 public Order(String orderNumber, int id){
     this.orderNumber = orderNumber;
     this.id = id;
 }

    @Override
    public String getOrderNumber() {
        return  orderNumber;
    }
}
