package com.maveric.ce.entity;


import com.maveric.ce.userenum.Gender;
import com.maveric.ce.userenum.RolesEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class CustomerDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    private String firstName;

    private String lastName;

    private String username;


    private String password;

    @Column(unique = true)
    private String email;

    private String phoneNumber;

    private LocalDateTime createdAt;

//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdatedAt;

    @Column(name = "role", columnDefinition = "varchar(20)")
    @Enumerated(EnumType.STRING)
    private RolesEnum rolesENum;

    @Column(name = "gender", columnDefinition = "varchar(20)")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "customer",cascade= CascadeType.REMOVE, fetch = FetchType.EAGER)
//    @JoinColumn(name="account_id")
    private List<AccountDetails> accountDetails;

    @Override
    public String toString() {
        return "CustomerDetails{" +
                "customerId=" + customerId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", rolesENum=" + rolesENum +
                '}';
    }
}
