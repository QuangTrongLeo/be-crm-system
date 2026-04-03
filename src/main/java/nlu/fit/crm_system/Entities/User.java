package nlu.fit.crm_system.Entities;

import jakarta.persistence.*;
import lombok.*;
import nlu.fit.crm_system.enums.Role;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role = Role.SALES;

    private LocalDateTime createdAt;

//    // Quan hệ
//    @OneToMany(mappedBy = "assignedUser")
//    private List<Customer> customers;
//
//    @OneToMany(mappedBy = "user")
//    private List<Note> notes;
//
//    @OneToMany(mappedBy = "user")
//    private List<Interaction> interactions;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
