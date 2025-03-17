package mx.com.ecm.order.management.domain.port.output.repository;

import mx.com.ecm.order.management.domain.entity.Order;
import mx.com.ecm.order.management.domain.valueObject.TrackingId;

import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findByTrackingId(TrackingId trackingId);
}
