package nlu.fit.crm_system.Controller;

import jakarta.validation.Valid;
import nlu.fit.crm_system.DTO.request.CreateInteractionRequest;
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
    public ResponseEntity<Interaction> logInteraction(@Valid @RequestBody CreateInteractionRequest request) {
        Interaction created = interactionService.logInteraction(request);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Interaction>> getInteractionsForCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(interactionService.getInteractionsForCustomer(customerId));
    }
}

