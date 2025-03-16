package mx.com.ecm.order.management.domain.port.output.repository;

import mx.com.ecm.order.management.domain.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {
    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);
}
