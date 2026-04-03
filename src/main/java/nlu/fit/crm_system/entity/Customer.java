package nlu.fit.crm_system.entity;

import jakarta.persistence.*;
import lombok.*;
import nlu.fit.crm_system.enums.CustomerStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    private String phone;
    private String company;

    @Enumerated(EnumType.STRING)
    private CustomerStatus status = CustomerStatus.LEAD;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // FK -> users
    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    // Quan hệ
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Note> notes;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Interaction> interactions;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
