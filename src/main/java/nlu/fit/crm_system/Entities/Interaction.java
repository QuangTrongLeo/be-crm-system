package nlu.fit.crm_system.Entities;

import jakarta.persistence.*;
import lombok.*;
import nlu.fit.crm_system.enums.InteractionType;

import java.time.LocalDateTime;

@Entity
@Table(name = "interactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "interaction_type")
    private InteractionType interactionType;

    @Column(name = "interaction_date")
    private LocalDateTime interactionDate;

    private String summary;

    @PrePersist
    protected void onCreate() {
        if (interactionDate == null) {
            interactionDate = LocalDateTime.now();
        }
    }
}
