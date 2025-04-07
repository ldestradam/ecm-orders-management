package mx.com.ecm.order.management.domain;

import mx.com.ecm.order.management.domain.dto.create.CreateOrderCommand;
import mx.com.ecm.order.management.domain.dto.create.CreateOrderResponse;
import mx.com.ecm.order.management.domain.dto.create.OrderAddress;
import mx.com.ecm.order.management.domain.dto.create.OrderItem;
import mx.com.ecm.order.management.domain.entity.Customer;
import mx.com.ecm.order.management.domain.entity.Order;
import mx.com.ecm.order.management.domain.entity.Product;
import mx.com.ecm.order.management.domain.entity.Restaurant;
import mx.com.ecm.order.management.domain.exception.OrderDomainException;
import mx.com.ecm.order.management.domain.mapper.OrderDataMapper;
import mx.com.ecm.order.management.domain.port.input.service.OrderApplicationService;
import mx.com.ecm.order.management.domain.port.output.repository.CustomerRepository;
import mx.com.ecm.order.management.domain.port.output.repository.OrderRepository;
import mx.com.ecm.order.management.domain.port.output.repository.RestaurantRepository;
import mx.com.ecm.order.management.domain.valueobject.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
public class OrderApplicationServiceTest {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private OrderDataMapper orderDataMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWrongPrice;
    private CreateOrderCommand createOrderCommandWrongProductPrice;
    private final UUID CUSTOMER_ID = UUID.fromString("58f85ab0-af46-4fb5-add5-5fc9351e3124");
    private final UUID RESTAURANT_ID = UUID.fromString("c305070a-1485-4bed-a94d-c93582c3527a");
    private final UUID PRODUCT_ID = UUID.fromString("cf82fb40-40fc-4ac6-846e-18644e555385");
    private final UUID ORDER_ID = UUID.fromString("70ca82ce-5ae0-46f6-ae7c-7d136291a4aa");
    private final BigDecimal PRICE = new BigDecimal("200.00");

    @BeforeEach
    public void init(){
        createOrderCommand = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(
                        OrderAddress.builder()
                                .street("12345")
                                .postalCode("12345")
                                .city("12345")
                                .build()
                )
                .price(PRICE)
                .items(
                        List.of(
                                OrderItem.builder()
                                        .productId(PRODUCT_ID)
                                        .quantity(1)
                                        .price(new BigDecimal("50.00"))
                                        .subTotal(new BigDecimal("50.00"))
                                        .build(),
                                OrderItem.builder()
                                        .productId(PRODUCT_ID)
                                        .quantity(3)
                                        .price(new BigDecimal("50.00"))
                                        .subTotal(new BigDecimal("150.00"))
                                        .build()
                        )
                )
                .build();
        createOrderCommandWrongPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(
                        OrderAddress.builder()
                                .street("12345")
                                .postalCode("12345")
                                .city("12345")
                                .build()
                )
                .price(new BigDecimal("250.00"))
                .items(
                        List.of(
                                OrderItem.builder()
                                        .productId(PRODUCT_ID)
                                        .quantity(1)
                                        .price(new BigDecimal("50.00"))
                                        .subTotal(new BigDecimal("50.00"))
                                        .build(),
                                OrderItem.builder()
                                        .productId(PRODUCT_ID)
                                        .quantity(3)
                                        .price(new BigDecimal("50.00"))
                                        .subTotal(new BigDecimal("150.00"))
                                        .build()
                        )
                )
                .build();
        createOrderCommandWrongProductPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(
                        OrderAddress.builder()
                                .street("12345")
                                .postalCode("12345")
                                .city("12345")
                                .build()
                )
                .price(new BigDecimal("210.00"))
                .items(
                        List.of(
                                OrderItem.builder()
                                        .productId(PRODUCT_ID)
                                        .quantity(1)
                                        .price(new BigDecimal("60.00"))
                                        .subTotal(new BigDecimal("60.00"))
                                        .build(),
                                OrderItem.builder()
                                        .productId(PRODUCT_ID)
                                        .quantity(3)
                                        .price(new BigDecimal("50.00"))
                                        .subTotal(new BigDecimal("150.00"))
                                        .build()
                        )
                )
                .build();
        Customer customer = new Customer();
        customer.setId(new CustomerId(CUSTOMER_ID));
        Restaurant restaurant = Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(List.of(
                        new Product(new ProductId(PRODUCT_ID), "Product-1", new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID), "Product-2", new Money(new BigDecimal("50.00")))
                ))
                .active(true)
                .build();
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderId(ORDER_ID));

        when(customerRepository.findCustomer(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(restaurantRepository.findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
            .thenReturn(Optional.of(restaurant));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
    }

    @Test
    public void shouldCreateOrder() {
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
        assertEquals(OrderStatus.PENDING, createOrderResponse.getOrderStatus());
        assertEquals("Order created successfully", createOrderResponse.getMessage());
        assertNotNull(createOrderResponse.getOrderTrackingId());
    }

    @Test
    public void shouldCreateOrderWrongTotalPrice() {
        OrderDomainException ex = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongPrice));
        assertEquals("Total price: 200.00 is not equal to Order items total: 250.00!", ex.getMessage());
    }

    @Test
    public void shouldCreateOrderWrongProductPrice() {
        OrderDomainException ex = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongProductPrice));
        assertEquals("Order item price: 60.00 is not valid for product " + PRODUCT_ID, ex.getMessage());
    }

    @Test
    public void shouldCreateOrderWithPassiveRestaurant(){
        Restaurant restaurant = Restaurant.builder()
            .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
            .products(List.of(
                    new Product(new ProductId(PRODUCT_ID), "Product-1", new Money(new BigDecimal("50.00"))),
                    new Product(new ProductId(PRODUCT_ID), "Product-2", new Money(new BigDecimal("50.00")))
            ))
            .active(false)
            .build();
        when(restaurantRepository.findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
            .thenReturn(Optional.of(restaurant));
        OrderDomainException ex = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommand));
        assertEquals(ex.getMessage(), "Restaurant with id: " +  RESTAURANT_ID + " is not active");
    }


}
