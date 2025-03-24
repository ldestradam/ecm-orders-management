package mx.com.ecm.order.management.domain;

import lombok.extern.slf4j.Slf4j;
import mx.com.ecm.order.management.domain.dto.create.CreateOrderCommand;
import mx.com.ecm.order.management.domain.dto.create.CreateOrderResponse;
import mx.com.ecm.order.management.domain.event.OrderCreatedEvent;
import mx.com.ecm.order.management.domain.mapper.OrderDataMapper;
import mx.com.ecm.order.management.domain.port.output.messagepublisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class OrderCreateCommandHandler {
    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;
    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;

    public OrderCreateCommandHandler(OrderCreateHelper orderCreateHelper,
                                     OrderDataMapper orderDataMapper,
                                     OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher) {
        this.orderCreateHelper = orderCreateHelper;
        this.orderDataMapper = orderDataMapper;
        this.orderCreatedPaymentRequestMessagePublisher = orderCreatedPaymentRequestMessagePublisher;
    }

    public CreateOrderResponse createOrder(CreateOrderCommand command) {
        OrderCreatedEvent event = orderCreateHelper.persistOrder(command);
        log.info("Order is created with id: {}", event.getOrder().getId().getValue());
        orderCreatedPaymentRequestMessagePublisher.publish(event);
        return orderDataMapper.orderToCreateOrderResponse(event.getOrder(), "Order created successfully");
    }

}
