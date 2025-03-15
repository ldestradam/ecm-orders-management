package mx.com.ecm.order.management.domain;

import lombok.extern.slf4j.Slf4j;
import mx.com.ecm.order.management.domain.entity.Order;
import mx.com.ecm.order.management.domain.entity.Product;
import mx.com.ecm.order.management.domain.entity.Restaurant;
import mx.com.ecm.order.management.domain.event.OrderCancelledEvent;
import mx.com.ecm.order.management.domain.event.OrderCreatedEvent;
import mx.com.ecm.order.management.domain.event.OrderPaidEvent;
import mx.com.ecm.order.management.domain.exception.OrderDomainException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

    private static final String ZONE_ID = "UTC";

    @Override
    public OrderCreatedEvent validAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id: {} is initiated", order.getId().getValue());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(ZONE_ID)));
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        order.getItems().forEach(item -> {
            restaurant.getProducts().forEach(restaurantProduct -> {
                Product orderProduct = item.getProduct();
                if(orderProduct.equals(restaurantProduct)) {
                    orderProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(), restaurantProduct.getPrice());
                }
            });
        });
    }

    private void validateRestaurant(Restaurant restaurant) {
        if(!restaurant.isActive()) {
            throw new OrderDomainException("Restaurant with id: " + restaurant.getId().getValue() + " is not active");
        }
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with id: {} paid successfully", order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(ZONE_ID)));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id: {} approved successfully", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info("Order payment is cancelling for the order id: {}", order.getId().getValue());
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(ZONE_ID)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel();
        log.info("Order cancelled for the order id: {}", order.getId().getValue());
    }
}
