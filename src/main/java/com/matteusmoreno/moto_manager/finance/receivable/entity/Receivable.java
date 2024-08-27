package com.matteusmoreno.moto_manager.finance.receivable.entity;

import com.matteusmoreno.moto_manager.finance.constant.PaymentStatus;
import com.matteusmoreno.moto_manager.service_order.entity.ServiceOrder;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "receivables")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Receivable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private ServiceOrder serviceOrder;
    private BigDecimal value;
    private LocalDate issueDate;
    private LocalDate paymentDate;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
