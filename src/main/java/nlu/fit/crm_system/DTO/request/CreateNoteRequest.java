package nlu.fit.crm_system.DTO.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateNoteRequest {
    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Content is required")
    private String content;

    private Boolean isImportant = false;
}

