package mx.com.ecm.order.management.domain;

import lombok.extern.slf4j.Slf4j;
import mx.com.ecm.order.management.domain.dto.track.TrackOrderQuery;
import mx.com.ecm.order.management.domain.dto.track.TrackOrderResponse;
import mx.com.ecm.order.management.domain.entity.Order;
import mx.com.ecm.order.management.domain.exception.OrderNotFoundException;
import mx.com.ecm.order.management.domain.mapper.OrderDataMapper;
import mx.com.ecm.order.management.domain.port.output.repository.OrderRepository;
import mx.com.ecm.order.management.domain.valueObject.TrackingId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class OrderTrackCommandHandler {

    private final OrderDataMapper orderDataMapper;
    private final OrderRepository orderRepository;

    public OrderTrackCommandHandler(OrderDataMapper orderDataMapper, OrderRepository orderRepository) {
        this.orderDataMapper = orderDataMapper;
        this.orderRepository = orderRepository;
    }

    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        Optional<Order> order = orderRepository.findByTrackingId(new TrackingId(trackOrderQuery.getOrderTrackingId()));
        if (order.isEmpty()) {
            log.warn("Could not find order with tracking id: {}", trackOrderQuery.getOrderTrackingId());
            throw new OrderNotFoundException("Could not find order with tracking id: " + trackOrderQuery.getOrderTrackingId());
        }
        return orderDataMapper.orderToTrackOrderResponse(order.get());
    }
}
