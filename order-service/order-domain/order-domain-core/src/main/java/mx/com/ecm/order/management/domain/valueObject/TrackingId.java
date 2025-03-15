package mx.com.ecm.order.management.domain.valueObject;

import mx.com.ecm.order.management.domain.valueobject.BaseId;

import java.util.UUID;

public class TrackingId extends BaseId<UUID> {

    public TrackingId(UUID id) {
        super(id);
    }

}
