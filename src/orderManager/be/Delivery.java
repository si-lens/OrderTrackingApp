package orderManager.be;

import java.util.Date;

public class Delivery implements IDelivery {
    private Date deliveryTime;

    @Override
    public Date getDeliveryTime() {
        return deliveryTime;
    }
}
