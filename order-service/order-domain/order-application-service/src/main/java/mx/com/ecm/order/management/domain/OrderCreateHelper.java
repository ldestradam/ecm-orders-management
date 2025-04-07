package mx.com.ecm.order.management.domain;

import lombok.extern.slf4j.Slf4j;
import mx.com.ecm.order.management.domain.dto.create.CreateOrderCommand;
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

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreateHelper {
    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final CustomerRepository customerRepository;
    private final OrderDataMapper orderDataMapper;

    public OrderCreateHelper(OrderDomainService orderDomainService,
                             OrderRepository orderRepository,
                             RestaurantRepository restaurantRepository,
                             CustomerRepository customerRepository,
                             OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.customerRepository = customerRepository;
        this.orderDataMapper = orderDataMapper;
    }

    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {
        checkCustomerExists(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreatedEvent event = orderDomainService.validAndInitiateOrder(order, restaurant);
        saveOrder(order);
        return event;
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

    private void saveOrder(Order order) {
        Order orderCreated = orderRepository.save(order);
        if (orderCreated == null) {
            throw new OrderDomainException("Could not save order");
        }
    }
}
