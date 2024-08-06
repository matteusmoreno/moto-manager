package com.matteusmoreno.moto_manager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customers")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Customer {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String email;
    private LocalDate birthDate;
    private Integer age;
    private String phone;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Address> address = new ArrayList<>();
    @OneToMany
    private List<Motorcycle> motorcycles = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private boolean active;

}
