package com.matteusmoreno.moto_manager.finance.receivable.service;

import com.matteusmoreno.moto_manager.exception.PaymentStatusException;
import com.matteusmoreno.moto_manager.finance.constant.PaymentStatus;
import com.matteusmoreno.moto_manager.finance.receivable.entity.Receivable;
import com.matteusmoreno.moto_manager.finance.receivable.repository.ReceivableRepository;
import com.matteusmoreno.moto_manager.finance.receivable.response.ReceivableDetailsResponse;
import com.matteusmoreno.moto_manager.service_order.entity.ServiceOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ReceivableService {

    private final ReceivableRepository receivableRepository;

    @Autowired
    public ReceivableService(ReceivableRepository receivableRepository) {
        this.receivableRepository = receivableRepository;
    }

    @Transactional
    public void createReceivable(ServiceOrder serviceOrder) {
        Receivable receivable = Receivable.builder()
                .serviceOrder(serviceOrder)
                .value(serviceOrder.getTotalCost())
                .issueDate(serviceOrder.getCreatedAt().toLocalDate())
                .paymentDate(null)
                .status(PaymentStatus.PENDING)
                .build();

        receivableRepository.save(receivable);
    }

    public Page<ReceivableDetailsResponse> findAllReceivables(Pageable pageable) {
        return  receivableRepository.findAll(pageable).map(ReceivableDetailsResponse::new);
    }

    @Transactional
    public Receivable payReceivable(Long receivableId) {
        Receivable receivable = receivableRepository.findById(receivableId)
                .orElseThrow(() -> new RuntimeException("Receivable not found"));

        if (receivable.getStatus() != PaymentStatus.PENDING) throw new PaymentStatusException("Receivable is not pending");

        receivable.setStatus(PaymentStatus.PAID);
        receivable.setPaymentDate(LocalDate.now());
        receivableRepository.save(receivable);

        return receivable;
    }

    @Transactional
    public void cancelReceivable(ServiceOrder serviceOrder) {
        Receivable receivable = receivableRepository.findByServiceOrder(serviceOrder);
        receivable.setStatus(PaymentStatus.CANCELED);
        receivableRepository.save(receivable);
    }
}
