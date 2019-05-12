package orderManager.be;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.util.Date;

public class OrderDetails extends RecursiveTreeObject<OrderDetails> {

    private String orderNumber;
    private boolean orderState;
    private Date startDate;
    private Date endDate;

    public OrderDetails(String orderNumber, Date startDate, Date endDate, boolean orderState)
    {
        this.orderNumber = orderNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.orderState = orderState;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public boolean isOrderState() {
        return orderState;
    }

    public void setOrderState(boolean orderState) {
        this.orderState = orderState;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
