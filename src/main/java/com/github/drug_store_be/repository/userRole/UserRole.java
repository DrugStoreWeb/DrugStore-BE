package com.github.drug_store_be.repository.userRole;
import com.github.drug_store_be.repository.role.Role;
import com.github.drug_store_be.repository.user.User;
import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="userRoleId")
@Builder
@Entity
@Table(name="user_role")
public class UserRole{
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_role_id")
    private Integer userRoleId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

}