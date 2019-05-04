package orderManager.be;

import java.util.Date;

public class Delivery implements IDelivery {
    private Date deliveryTime;
    private int id;


    public Delivery(int id, Date deliveryTime) {
        this.deliveryTime = deliveryTime;
        this.id = id;
    }

    @Override
    public Date getDeliveryTime() {
        return deliveryTime;
    }
}
