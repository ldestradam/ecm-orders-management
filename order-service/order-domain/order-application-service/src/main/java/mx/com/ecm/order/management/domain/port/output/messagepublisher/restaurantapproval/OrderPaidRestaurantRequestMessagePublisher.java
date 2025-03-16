package mx.com.ecm.order.management.domain.port.output.messagepublisher.restaurantapproval;

import mx.com.ecm.order.management.domain.event.OrderPaidEvent;
import mx.com.ecm.order.management.domain.events.publisher.DomainEventPublisher;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
