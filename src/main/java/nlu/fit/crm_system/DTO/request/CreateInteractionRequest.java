package nlu.fit.crm_system.DTO.request;

import lombok.Data;
import nlu.fit.crm_system.enums.InteractionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class CreateInteractionRequest {
    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Interaction type is required")
    private InteractionType interactionType;

    private LocalDateTime interactionDate;

    @NotBlank(message = "Summary is required")
    private String summary;
}

