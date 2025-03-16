package mx.com.ecm.order.management.domain.port.input.service.messagelistener.payment;

import mx.com.ecm.order.management.domain.dto.message.PaymentResponse;

public interface PaymentResponseMessageListener {
    void paymentCompleted(PaymentResponse paymentResponse);
    void paymentCancelled(PaymentResponse paymentResponse);
}
