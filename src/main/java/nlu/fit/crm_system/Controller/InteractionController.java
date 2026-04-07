package nlu.fit.crm_system.Controller;

import lombok.RequiredArgsConstructor;
import nlu.fit.crm_system.DTO.request.InteractionRequest;
import nlu.fit.crm_system.DTO.response.ApiResponse;
import nlu.fit.crm_system.DTO.response.InteractionResponse;
import nlu.fit.crm_system.Service.Interfaces.IInteractionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.crm-system-url}/interactions")
@RequiredArgsConstructor
public class InteractionController {

    private final IInteractionService interactionService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
    @PostMapping
    public ResponseEntity<ApiResponse<InteractionResponse>> createInteraction(@RequestBody InteractionRequest request) {
        InteractionResponse data = interactionService.createInteraction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<InteractionResponse>builder()
                        .code(201)
                        .message("Ghi nhận tương tác thành công")
                        .data(data)
                        .build()
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InteractionResponse>> updateInteraction(
            @PathVariable Long id,
            @RequestBody InteractionRequest request) {
        InteractionResponse data = interactionService.updateInteraction(id, request);
        return ResponseEntity.ok(
                ApiResponse.<InteractionResponse>builder()
                        .code(200)
                        .message("Cập nhật tương tác thành công")
                        .data(data)
                        .build()
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInteraction(@PathVariable Long id) {
        interactionService.deleteInteraction(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .message("Xóa tương tác thành công")
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InteractionResponse>> getInteractionById(@PathVariable Long id) {
        InteractionResponse data = interactionService.getInteractionById(id);
        return ResponseEntity.ok(
                ApiResponse.<InteractionResponse>builder()
                        .code(200)
                        .message("Lấy thông tin tương tác thành công")
                        .data(data)
                        .build()
        );
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<InteractionResponse>>> getAllInteractions() {
        List<InteractionResponse> data = interactionService.getAllInteractions();
        return ResponseEntity.ok(
                ApiResponse.<List<InteractionResponse>>builder()
                        .code(200)
                        .message("Lấy danh sách tất cả tương tác thành công")
                        .data(data)
                        .build()
        );
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<InteractionResponse>>> getInteractionByCustomer(@PathVariable Long customerId) {
        List<InteractionResponse> data = interactionService.getInteractionByCustomer(customerId);
        return ResponseEntity.ok(
                ApiResponse.<List<InteractionResponse>>builder()
                        .code(200)
                        .message("Lấy lịch sử tương tác của khách hàng thành công")
                        .data(data)
                        .build()
        );
    }
}