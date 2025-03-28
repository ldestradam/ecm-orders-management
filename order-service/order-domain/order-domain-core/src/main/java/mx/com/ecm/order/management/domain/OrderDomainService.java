package mx.com.ecm.order.management.domain;

import mx.com.ecm.order.management.domain.entity.Order;
import mx.com.ecm.order.management.domain.entity.Restaurant;
import mx.com.ecm.order.management.domain.event.OrderCancelledEvent;
import mx.com.ecm.order.management.domain.event.OrderCreatedEvent;
import mx.com.ecm.order.management.domain.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {

    OrderCreatedEvent validAndInitiateOrder(Order order, Restaurant restaurant );
    OrderPaidEvent payOrder(Order order);
    void approveOrder(Order order);
    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);
    void cancelOrder(Order order, List<String> failureMessages);

}
