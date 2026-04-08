package nlu.fit.crm_system.Controller;

import jakarta.validation.Valid;
import nlu.fit.crm_system.DTO.request.CreateInteractionRequest;
import nlu.fit.crm_system.DTO.response.ApiResponse;
import nlu.fit.crm_system.Entities.Interaction;
import nlu.fit.crm_system.Service.Interfaces.IInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.crm-system-url}/interactions")
public class InteractionController {
    @Autowired
    private IInteractionService interactionService;

    @PostMapping
    public ResponseEntity<ApiResponse<Interaction>> logInteraction(@Valid @RequestBody CreateInteractionRequest request) {
        Interaction created = interactionService.logInteraction(request);
        return ResponseEntity.status(201).body(
            ApiResponse.<Interaction>builder()
                .code(201)
                .message("Lưu tương tác thành công")
                .data(created)
                .build()
        );
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<Interaction>>> getInteractionsForCustomer(@PathVariable Long customerId) {
        List<Interaction> interactions = interactionService.getInteractionsForCustomer(customerId);
        return ResponseEntity.ok(
            ApiResponse.<List<Interaction>>builder()
                .code(200)
                .message("Lấy danh sách tương tác thành công")
                .data(interactions)
                .build()
        );
    }
}
