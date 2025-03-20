package mx.com.ecm.order.management.domain;

import lombok.extern.slf4j.Slf4j;
import mx.com.ecm.order.management.domain.dto.message.RestaurantAprovalResponse;
import mx.com.ecm.order.management.domain.port.input.service.messagelistener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
public class RestaurantApprovalResponseMessageListenerImpl implements RestaurantApprovalResponseMessageListener {

    @Override
    public void orderApproved(RestaurantAprovalResponse restaurantAprovalResponse) {

    }

    @Override
    public void orderRejected(RestaurantAprovalResponse restaurantAprovalResponse) {

    }
}
