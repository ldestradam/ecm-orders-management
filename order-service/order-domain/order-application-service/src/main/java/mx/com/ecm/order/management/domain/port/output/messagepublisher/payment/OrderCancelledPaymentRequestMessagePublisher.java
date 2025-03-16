package mx.com.ecm.order.management.domain.port.output.messagepublisher.payment;

import mx.com.ecm.order.management.domain.event.OrderCancelledEvent;
import mx.com.ecm.order.management.domain.events.publisher.DomainEventPublisher;

public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {
}
