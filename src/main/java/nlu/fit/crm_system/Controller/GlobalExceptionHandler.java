package nlu.fit.crm_system.Controller;

import jakarta.servlet.http.HttpServletRequest;
import nlu.fit.crm_system.DTO.response.ResponseMessageDTO;
import nlu.fit.crm_system.Utils.LogObj;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final LogObj log = LogObj.defaultLog;

    private ResponseEntity<ResponseMessageDTO> build(HttpStatus status, String message, HttpServletRequest request, Map<String, Object> details) {
        ResponseMessageDTO body = ResponseMessageDTO.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request != null ? request.getRequestURI() : null)
                .details(details)
                .build();
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseMessageDTO> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Bad request: " + ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<ResponseMessageDTO> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        log.info("Not found: " + ex.getMessage());
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseMessageDTO> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, Object> details = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();
        for (var error : ex.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError fe) {
                fieldErrors.put(fe.getField(), fe.getDefaultMessage());
            } else {
                fieldErrors.put(error.getObjectName(), error.getDefaultMessage());
            }
        }
        details.put("fieldErrors", fieldErrors);
        log.warn("Validation failed: " + fieldErrors);
        return build(HttpStatus.BAD_REQUEST, "Validation failed", request, details);
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<ResponseMessageDTO> handleBindException(BindException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> fieldErrors.put(err.getField(), err.getDefaultMessage()));
        Map<String, Object> details = new HashMap<>();
        details.put("fieldErrors", fieldErrors);
        log.warn("Bind error: " + fieldErrors);
        return build(HttpStatus.BAD_REQUEST, "Binding failed", request, details);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ResponseMessageDTO> handleUnreadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("Malformed JSON request: " + ex.getMostSpecificCause().getMessage());
        return build(HttpStatus.BAD_REQUEST, "Malformed JSON request", request, null);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<ResponseMessageDTO> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
        Map<String, Object> details = new HashMap<>();
        details.put("parameter", ex.getParameterName());
        log.warn("Missing request parameter: " + ex.getParameterName());
        return build(HttpStatus.BAD_REQUEST, "Missing request parameter", request, details);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<ResponseMessageDTO> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        Map<String, Object> details = new HashMap<>();
        details.put("cause", ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage());
        log.warn("Data integrity violation: " + details.get("cause"));
        return build(HttpStatus.CONFLICT, "Data integrity violation", request, details);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessageDTO> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception: " + ex.getMessage());
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request, null);
    }
}
