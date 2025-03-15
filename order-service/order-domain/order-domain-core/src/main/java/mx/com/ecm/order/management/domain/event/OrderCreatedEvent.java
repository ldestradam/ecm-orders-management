package mx.com.ecm.order.management.domain.event;

import mx.com.ecm.order.management.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCreatedEvent extends OrderEvent {

    public OrderCreatedEvent(Order order, ZonedDateTime timestamp) {
        super(order, timestamp);
    }
}
