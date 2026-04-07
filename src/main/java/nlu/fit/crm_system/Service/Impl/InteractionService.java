package nlu.fit.crm_system.Service.Impl;

import lombok.RequiredArgsConstructor;
import nlu.fit.crm_system.DTO.request.InteractionRequest;
import nlu.fit.crm_system.DTO.response.InteractionResponse;
import nlu.fit.crm_system.Entities.Customer;
import nlu.fit.crm_system.Entities.Interaction;
import nlu.fit.crm_system.Entities.User;
import nlu.fit.crm_system.Repositories.CustomerRepo;
import nlu.fit.crm_system.Repositories.InteractionRepo;
import nlu.fit.crm_system.Service.Interfaces.IInteractionService;
import nlu.fit.crm_system.Utils.JwtUtils;
import nlu.fit.crm_system.enums.InteractionType;
import nlu.fit.crm_system.mapper.InteractionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InteractionService implements IInteractionService {

    private final InteractionRepo interactionRepo;
    private final CustomerRepo customerRepo;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public InteractionResponse createInteraction(InteractionRequest request) {
        User currentUser = jwtUtils.getCurrentUser();
        Customer customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + request.getCustomerId()));

        Interaction interaction = Interaction.builder()
                .customer(customer)
                .user(currentUser)
                .interactionType(InteractionType.valueOf(request.getInteractionType().toUpperCase()))
                .summary(request.getSummary())
                .interactionDate(request.getInteractionDate() != null ? request.getInteractionDate() : LocalDateTime.now())
                .build();

        return InteractionMapper.toInteractionResponse(interactionRepo.save(interaction));
    }

    @Override
    @Transactional
    public InteractionResponse updateInteraction(Long id, InteractionRequest request) {
        User currentUser = jwtUtils.getCurrentUser();
        Interaction interaction = interactionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin tương tác với ID: " + id));

        if (!interaction.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Bạn không có quyền chỉnh sửa thông tin tương tác của người khác!");
        }

        interaction.setInteractionType(InteractionType.valueOf(request.getInteractionType().toUpperCase()));
        interaction.setSummary(request.getSummary());
        if (request.getInteractionDate() != null) {
            interaction.setInteractionDate(request.getInteractionDate());
        }

        return InteractionMapper.toInteractionResponse(interactionRepo.save(interaction));
    }

    @Override
    @Transactional
    public void deleteInteraction(Long id) {
        User currentUser = jwtUtils.getCurrentUser();

        Interaction interaction = interactionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin tương tác với ID: " + id));

        if (!interaction.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Bạn không có quyền xóa thông tin tương tác của người khác!");
        }

        interactionRepo.delete(interaction);
    }

    @Override
    public InteractionResponse getInteractionById(Long id) {
        Interaction interaction = interactionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tương tác với ID: " + id));
        return InteractionMapper.toInteractionResponse(interaction);
    }

    @Override
    public List<InteractionResponse> getAllInteractions() {
        List<Interaction> interactions = interactionRepo.findAll();
        return InteractionMapper.toInteractionResponseList(interactions);
    }

    @Override
    public List<InteractionResponse> getInteractionByCustomer(Long customerId) {
        if (!customerRepo.existsById(customerId)) {
            throw new RuntimeException("Khách hàng không tồn tại");
        }

        List<Interaction> interactions = interactionRepo.findByCustomerIdOrderByInteractionDateDesc(customerId);
        return InteractionMapper.toInteractionResponseList(interactions);
    }
}