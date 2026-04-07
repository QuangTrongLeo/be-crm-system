package nlu.fit.crm_system.DTO.request;

import lombok.Data;

@Data
public class NoteRequest {
    private Long customerId;
    private Long userId;
    private String content;
    private boolean isImportant;
}
