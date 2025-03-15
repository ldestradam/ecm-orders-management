package mx.com.ecm.order.management.domain.entity;

import mx.com.ecm.order.management.domain.valueobject.*;
import mx.com.ecm.order.management.domain.exception.OrderDomainException;
import mx.com.ecm.order.management.domain.valueObject.OrderItemId;
import mx.com.ecm.order.management.domain.valueObject.StreetAddress;
import mx.com.ecm.order.management.domain.valueObject.TrackingId;

import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot<OrderId> {

    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress streetAddress;
    private final Money price;
    private final List<OrderItem> items;
    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        streetAddress = builder.streetAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public void initializeOrder() {
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItem();
    }

    private void initializeOrderItem() {
        long itemId = 1;
        for (OrderItem item : items) {
            item.initializeOrderItem(super.getId(), new OrderItemId(itemId));
        }
    }

    public void validateOrder(){
        validateInitialOrder();
        validateTotalPrice();
        validateItemPrice();
    }

    private void validateInitialOrder() {
        if (orderStatus != null || getId() != null) {
            throw new OrderDomainException("Order is not in correct state for initialization!");
        }
    }

    private void validateTotalPrice() {
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Price must be greater than zero!");
        }
    }

    private void validateItemPrice() {
        Money orderItemsTotal = items.stream().map(orderItem -> {
            validateItemPrice(orderItem);
            return orderItem.getSubTotal();
        }).reduce(Money.ZERO, Money::add);
        if(!price.equals(orderItemsTotal)) {
            throw new OrderDomainException("Total price: "
                    + orderItemsTotal + " is not equal to Order items total: " + price + "!");
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if(orderItem.isPriceValid()){
            throw new OrderDomainException("Order item price: " + orderItem.getPrice() + " is not valid!");
        }
    }

    public void pay(){
        if (orderStatus != OrderStatus.PENDING) {
            throw new OrderDomainException("Order is not in correct state for payment!");
        }
        orderStatus = OrderStatus.PAID;
    }

    public void approve(){
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for approve!");
        }
        orderStatus = OrderStatus.APPROVED;
        updateFailureMessages(failureMessages);
    }

    public void initCancel(List<String> failureMessages){
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for cancellation!");
        }
        orderStatus = OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }

    public void cancel(){
        if (!(orderStatus == OrderStatus.CANCELLING || orderStatus == OrderStatus.PENDING)) {
            throw new OrderDomainException("Order is not in correct state for cancellation!");
        }
        orderStatus = OrderStatus.CANCELLED;
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if(this.failureMessages != null && failureMessages != null) {
            this.failureMessages.addAll(failureMessages.stream().filter(message -> !message.isEmpty()).toList());
        }
        if(this.failureMessages == null) {
            this.failureMessages = failureMessages;
        }
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getStreetAddress() {
        return streetAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress streetAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder streetAddress(StreetAddress val) {
            streetAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
