package mx.com.ecm.order.management.domain.event;

import mx.com.ecm.order.management.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderPaidEvent extends OrderEvent {
    public OrderPaidEvent(Order order, ZonedDateTime timestamp) {
        super(order, timestamp);
    }
}
