package com.matteusmoreno.moto_manager.entity;

import com.matteusmoreno.moto_manager.constant.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "employees")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Employee {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private Integer age;
    private String cpf;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToOne
    private Address address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Boolean active;
}
