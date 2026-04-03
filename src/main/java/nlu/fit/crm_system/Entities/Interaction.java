//package nlu.fit.crm_system.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import nlu.fit.crm_system.enums.InteractionType;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "interactions")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Interaction {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Enumerated(EnumType.STRING)
//    private InteractionType interactionType;
//
//    private LocalDateTime interactionDate;
//
//    private String summary;
//
//    // FK
//    @ManyToOne
//    @JoinColumn(name = "customer_id", nullable = false)
//    private Customer customer;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @PrePersist
//    public void prePersist() {
//        this.interactionDate = LocalDateTime.now();
//    }
//}
