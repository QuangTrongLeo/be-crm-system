package nlu.fit.crm_system.Service.Interfaces;

import nlu.fit.crm_system.DTO.request.CreateInteractionRequest;
import nlu.fit.crm_system.Entities.Interaction;
import java.util.List;

public interface IInteractionService {
    Interaction logInteraction(CreateInteractionRequest request);
    List<Interaction> getInteractionsForCustomer(Long customerId);
}

