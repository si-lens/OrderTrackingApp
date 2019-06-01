package orderManager.be;

import java.sql.Date;

//Defines the delivery terms.
public interface IDelivery {
//Defines the time of which the order is to be delivered.
Date getDeliveryTime();
}
