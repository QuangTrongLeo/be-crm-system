package nlu.fit.crm_system.Service.Impl;

import nlu.fit.crm_system.DTO.request.CreateInteractionRequest;
import nlu.fit.crm_system.Entities.Customer;
import nlu.fit.crm_system.Entities.Interaction;
import nlu.fit.crm_system.Entities.User;
import nlu.fit.crm_system.Repositories.CustomerRepo;
import nlu.fit.crm_system.Repositories.InteractionRepo;
import nlu.fit.crm_system.Repositories.UserRepository;
import nlu.fit.crm_system.Service.Interfaces.IInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InteractionService implements IInteractionService {
    @Autowired
    private InteractionRepo interactionRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Interaction logInteraction(CreateInteractionRequest request) {
        Customer customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Interaction interaction = Interaction.builder()
                .customer(customer)
                .user(user)
                .interactionType(request.getInteractionType())
                .summary(request.getSummary())
                .interactionDate(request.getInteractionDate() != null ? request.getInteractionDate() : LocalDateTime.now())
                .build();

        return interactionRepo.save(interaction);
    }

    @Override
    public List<Interaction> getInteractionsForCustomer(Long customerId) {
        return interactionRepo.findByCustomerId(customerId);
    }
}

