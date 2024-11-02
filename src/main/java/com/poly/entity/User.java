package com.poly.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.poly.util._enum.*;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String image;
    private String phone;
    private Boolean activated;
    private LocalDateTime otpGeneratedTime;
    private String otp;

    @JsonIgnore
    private String password;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Book> books;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles = Set.of(new Role(RoleUserEnum.USER));
    @Builder.Default
    @Enumerated(EnumType.ORDINAL)
    private AuthTypeEnum authType = AuthTypeEnum.LOCAL;
    @Builder.Default
    private String authId = null;

    public String getRoleString() {
        return this.roles.stream().map(r -> r.getName()).collect(Collectors.joining(", "));
    }


}
