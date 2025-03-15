package mx.com.ecm.order.management.domain.dto.track;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import mx.com.ecm.order.management.domain.valueobject.OrderStatus;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class TrackOrderResponse {
    @NotNull
    private final UUID orderTrackerId;
    @NotNull
    private final OrderStatus orderStatus;
    private final List<String> failureMessages;

}
