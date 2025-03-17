package mx.com.ecm.order.management.domain.mapper;

import mx.com.ecm.order.management.domain.dto.create.CreateOrderCommand;
import mx.com.ecm.order.management.domain.dto.create.CreateOrderResponse;
import mx.com.ecm.order.management.domain.dto.create.OrderAddress;
import mx.com.ecm.order.management.domain.entity.Order;
import mx.com.ecm.order.management.domain.entity.OrderItem;
import mx.com.ecm.order.management.domain.entity.Product;
import mx.com.ecm.order.management.domain.entity.Restaurant;
import mx.com.ecm.order.management.domain.valueObject.StreetAddress;
import mx.com.ecm.order.management.domain.valueobject.CustomerId;
import mx.com.ecm.order.management.domain.valueobject.Money;
import mx.com.ecm.order.management.domain.valueobject.ProductId;
import mx.com.ecm.order.management.domain.valueobject.RestaurantId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {

    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(createOrderCommand.getItems().stream().map(orderItem -> {
                    return new Product(new ProductId(orderItem.getProductId()));
                }).collect(Collectors.toList()))
                .build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .items(orderItemsToOrderItemsEntities(createOrderCommand.getItems()))
                .build();
    }

    public CreateOrderResponse orderToCreateOrderResponse(Order order) {
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .build();
    }

    private List<OrderItem> orderItemsToOrderItemsEntities(List<mx.com.ecm.order.management.domain.dto.create.OrderItem> orderItems) {
        return orderItems.stream().map(orderItem -> {
            return OrderItem.builder()
                    .product(new Product(new ProductId(orderItem.getProductId())))
                    .price(new Money(orderItem.getPrice()))
                    .quantity(orderItem.getQuantity())
                    .subTotal(new Money(orderItem.getSubTotal()))
                    .build();
        }).collect(Collectors.toList());
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddress address) {
        return new StreetAddress(UUID.randomUUID(), address.getStreet(), address.getPostalCode(), address.getCity());
    }

}
