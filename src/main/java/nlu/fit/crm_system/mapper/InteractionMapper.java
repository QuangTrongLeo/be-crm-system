package nlu.fit.crm_system.mapper;

import nlu.fit.crm_system.DTO.response.InteractionResponse;
import nlu.fit.crm_system.Entities.Interaction;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class InteractionMapper {
    public static InteractionResponse toInteractionResponse(Interaction interaction) {
        if (interaction == null) return null;
        return InteractionResponse.builder()
                .id(interaction.getId())
                .customerId(interaction.getCustomer().getId())
                .customerName(interaction.getCustomer().getFirstName() + " " + interaction.getCustomer().getLastName())
                .userId(interaction.getUser().getId())
                .userName(interaction.getUser().getFullName())
                .interactionType(interaction.getInteractionType().name())
                .interactionDate(interaction.getInteractionDate())
                .summary(interaction.getSummary())
                .build();
    }

    public static List<InteractionResponse> toInteractionResponseList(List<Interaction> interactions) {
        if (interactions == null || interactions.isEmpty()) return Collections.emptyList();
        return interactions.stream().map(InteractionMapper::toInteractionResponse).toList();
    }
}
