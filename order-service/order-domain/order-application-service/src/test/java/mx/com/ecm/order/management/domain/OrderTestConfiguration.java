package mx.com.ecm.order.management.domain;

import mx.com.ecm.order.management.domain.port.output.messagepublisher.payment.OrderCancelledPaymentRequestMessagePublisher;
import mx.com.ecm.order.management.domain.port.output.messagepublisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import mx.com.ecm.order.management.domain.port.output.repository.CustomerRepository;
import mx.com.ecm.order.management.domain.port.output.repository.OrderRepository;
import mx.com.ecm.order.management.domain.port.output.repository.RestaurantRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "mx.com.ecm.order.management.domain")
public class OrderTestConfiguration {

    @Bean
    public OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher(){
        return Mockito.mock(OrderCreatedPaymentRequestMessagePublisher.class);
    }

    @Bean
    public OrderCancelledPaymentRequestMessagePublisher orderCancelledPaymentRequestMessagePublisher(){
        return Mockito.mock(OrderCancelledPaymentRequestMessagePublisher.class);
    }

    @Bean
    public OrderRepository orderRepository() {
        return Mockito.mock(OrderRepository.class);
    }

    @Bean
    public CustomerRepository customerRepository() {
        return Mockito.mock(CustomerRepository.class);
    }

    @Bean
    public RestaurantRepository restaurantRepository() {
        return Mockito.mock(RestaurantRepository.class);
    }

    public OrderDomainService orderDomainService() {
        return new OrderDomainServiceImpl();
    }

}
