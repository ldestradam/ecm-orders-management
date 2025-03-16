package mx.com.ecm.order.management.domain.port.output.repository;

import mx.com.ecm.order.management.domain.entity.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Optional<Customer> findCustomer(UUID customerId);


}
