package mx.com.ecm.order.management.domain.event;

import mx.com.ecm.order.management.domain.entity.Order;
import mx.com.ecm.order.management.domain.events.DomainEvent;

import java.time.ZonedDateTime;

public abstract class OrderEvent implements DomainEvent<Order> {

    private final Order order;
    private final ZonedDateTime timestamp;

    public OrderEvent(Order order, ZonedDateTime timestamp) {
        this.order = order;
        this.timestamp = timestamp;
    }

    public Order getOrder() {
        return order;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
}
