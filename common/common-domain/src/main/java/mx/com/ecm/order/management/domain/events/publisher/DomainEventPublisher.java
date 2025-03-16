package mx.com.ecm.order.management.domain.events.publisher;

import mx.com.ecm.order.management.domain.events.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {
    void publish(T domainEvent);

}
