package mx.com.ecm.order.management.domain;

import lombok.extern.slf4j.Slf4j;
import mx.com.ecm.order.management.domain.dto.create.CreateOrderCommand;
import mx.com.ecm.order.management.domain.dto.create.CreateOrderResponse;
import mx.com.ecm.order.management.domain.entity.Customer;
import mx.com.ecm.order.management.domain.entity.Order;
import mx.com.ecm.order.management.domain.entity.Restaurant;
import mx.com.ecm.order.management.domain.event.OrderCreatedEvent;
import mx.com.ecm.order.management.domain.exception.OrderDomainException;
import mx.com.ecm.order.management.domain.mapper.OrderDataMapper;
import mx.com.ecm.order.management.domain.port.output.repository.CustomerRepository;
import mx.com.ecm.order.management.domain.port.output.repository.OrderRepository;
import mx.com.ecm.order.management.domain.port.output.repository.RestaurantRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    public OrderCreateCommandHandler(OrderDomainService orderDomainService,
                                     OrderRepository orderRepository,
                                     CustomerRepository customerRepository,
                                     RestaurantRepository restaurantRepository,
                                     OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand command) {
        checkCustomerExists(command.getCustomerId());
        Restaurant restaurant = checkRestaurant(command);
        Order order = orderDataMapper.createOrderCommandToOrder(command);
        OrderCreatedEvent event = orderDomainService.validAndInitiateOrder(order, restaurant);
        Order orderCreated = saveOrder(order);
        log.info("Order created with id {}", orderCreated.getId().getValue());
        return orderDataMapper.orderToCreateOrderResponse(orderCreated);
    }

    private void checkCustomerExists(UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomer(customerId);
        if (customer.isEmpty()) {
            log.warn("Customer with id {} not found", customerId);
            throw new OrderDomainException("Customer with id " + customerId + " not found");
        }
    }

    private Restaurant checkRestaurant(CreateOrderCommand command) {
        Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(command);
        Optional<Restaurant> optRestaurant = restaurantRepository.findRestaurantInformation(restaurant);
        if (optRestaurant.isEmpty()) {
            log.warn("Restaurant with id {} not found", command.getRestaurantId());
            throw new OrderDomainException("Restaurant with id " + command.getRestaurantId() + " not found");
        }
        return optRestaurant.get();
    }

    private Order saveOrder(Order order) {
        Order orderCreated = orderRepository.save(order);
        if (orderCreated == null) {
            throw new OrderDomainException("Could not save order");
        }
        log.info("Order with id {} saved", orderCreated.getId());
        return orderCreated;
    }

}
