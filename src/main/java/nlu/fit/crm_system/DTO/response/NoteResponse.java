package nlu.fit.crm_system.DTO.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NoteResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private Long userId;
    private String userName;
    private String content;
    private boolean isImportant;
    private LocalDateTime createdAt;
}