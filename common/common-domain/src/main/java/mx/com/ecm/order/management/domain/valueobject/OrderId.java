package mx.com.ecm.order.management.domain.valueobject;

import java.util.UUID;

public class OrderId extends BaseId<UUID> {

    public OrderId(UUID id) {
        super(id);
    }

}
