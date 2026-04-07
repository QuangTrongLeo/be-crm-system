package nlu.fit.crm_system.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InteractionResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private Long userId;
    private String userName;
    private String interactionType;
    private LocalDateTime interactionDate;
    private String summary;
}
