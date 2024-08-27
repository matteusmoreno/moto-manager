package com.matteusmoreno.moto_manager.finance.payable.entity;

import com.matteusmoreno.moto_manager.finance.constant.PaymentStatus;
import com.matteusmoreno.moto_manager.supplier.entity.Supplier;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payables")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Payable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Supplier supplier;
    private String description;
    private BigDecimal value;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
