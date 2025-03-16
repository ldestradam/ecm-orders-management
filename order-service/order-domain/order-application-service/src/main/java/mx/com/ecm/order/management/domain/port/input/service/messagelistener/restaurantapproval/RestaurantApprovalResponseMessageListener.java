package mx.com.ecm.order.management.domain.port.input.service.messagelistener.restaurantapproval;

import mx.com.ecm.order.management.domain.dto.message.RestaurantAprovalResponse;

public interface RestaurantApprovalResponseMessageListener {
    void orderApproved(RestaurantAprovalResponse restaurantAprovalResponse);
    void orderRejected(RestaurantAprovalResponse restaurantAprovalResponse);
}
