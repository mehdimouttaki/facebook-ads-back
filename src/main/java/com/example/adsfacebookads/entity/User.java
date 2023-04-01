package com.example.adsfacebookads.entity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Set;

@Entity
@Table(name = "user_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private Long userId;
    @Column(name="first_name")
    private String firstName ;
    @Column(name="last_name")
    private String lastName ;
    @Column(name="email_name")
    private String email ;
    @Column(name="user_name")
    @NotNull
    @NotEmpty
    @Size(min = 2, max = 10, message = "Username should be min 2 characters and max 10 characters")
    private String username;
    @NotNull
    @NotEmpty
    @Column(length = 100)
//    @Size(min = 2, max = 10, message = "Password should be min 2 characters and max 10 characters")
    private String password;
    @Column(name="admin_id")
    @NotNull
    private Long adminId;
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User(String firstName, String lastName, String email, String username, String password, Set<Role> roles,Long adminId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.adminId=adminId;
    }
}
