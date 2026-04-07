package nlu.fit.crm_system.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InteractionRequest {
    private Long customerId;
    private String interactionType; // CALL, EMAIL, MEETING, OTHER
    private String summary;
    private LocalDateTime interactionDate;
}
