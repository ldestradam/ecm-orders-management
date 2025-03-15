package mx.com.ecm.order.management.domain.event;

import mx.com.ecm.order.management.domain.entity.Order;
import mx.com.ecm.order.management.domain.events.DomainEvent;

import java.time.ZonedDateTime;

public class OrderCancelledEvent extends OrderEvent {

    public OrderCancelledEvent(Order order, ZonedDateTime timestamp) {
        super(order, timestamp);
    }

}
