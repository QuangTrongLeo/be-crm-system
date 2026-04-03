package nlu.fit.crm_system.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessageDTO {
    private OffsetDateTime timestamp;
    private int status;           // HTTP status code
    private String error;         // HTTP reason phrase
    private String message;       // Human-readable message
    private String path;          // Request path
    private Map<String, Object> details; // Optional details (e.g., field errors)
}
