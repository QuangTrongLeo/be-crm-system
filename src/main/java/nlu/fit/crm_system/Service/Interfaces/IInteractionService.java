package nlu.fit.crm_system.Service.Interfaces;

import nlu.fit.crm_system.DTO.request.InteractionRequest;
import nlu.fit.crm_system.DTO.response.InteractionResponse;

import java.util.List;

public interface IInteractionService {
    InteractionResponse createInteraction(InteractionRequest request);
    InteractionResponse updateInteraction(Long id, InteractionRequest request);
    void deleteInteraction(Long id);
    InteractionResponse getInteractionById(Long id);
    List<InteractionResponse> getAllInteractions();
    List<InteractionResponse> getInteractionByCustomer(Long customerId);
}
