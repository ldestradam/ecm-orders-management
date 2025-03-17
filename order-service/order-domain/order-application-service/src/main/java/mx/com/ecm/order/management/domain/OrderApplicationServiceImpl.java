package mx.com.ecm.order.management.domain;

import lombok.extern.slf4j.Slf4j;
import mx.com.ecm.order.management.domain.dto.create.CreateOrderCommand;
import mx.com.ecm.order.management.domain.dto.create.CreateOrderResponse;
import mx.com.ecm.order.management.domain.dto.track.TrackOrderQuery;
import mx.com.ecm.order.management.domain.dto.track.TrackOrderResponse;
import mx.com.ecm.order.management.domain.port.input.service.OrderApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
public class OrderApplicationServiceImpl implements OrderApplicationService {

    private final OrderCreateCommandHandler orderCreateCommandHandler;
    private final OrderTrackCommandHandler orderTrackCommandHandler;

    public OrderApplicationServiceImpl(OrderCreateCommandHandler orderCreateCommandHandler, OrderTrackCommandHandler orderTrackCommandHandler) {
        this.orderCreateCommandHandler = orderCreateCommandHandler;
        this.orderTrackCommandHandler = orderTrackCommandHandler;
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        return orderCreateCommandHandler.createOrder(createOrderCommand);
    }

    @Override
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        return orderTrackCommandHandler.trackOrder(trackOrderQuery);
    }
}
