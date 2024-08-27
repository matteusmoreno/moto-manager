package com.matteusmoreno.moto_manager.payable.service;

import com.matteusmoreno.moto_manager.exception.InvalidDueDateException;
import com.matteusmoreno.moto_manager.exception.PayableAlreadyCanceledException;
import com.matteusmoreno.moto_manager.exception.PayableAlreadyPaidException;
import com.matteusmoreno.moto_manager.payable.response.PayableDetailsResponse;
import com.matteusmoreno.moto_manager.payable.repository.PayableRepository;
import com.matteusmoreno.moto_manager.payable.PaymentStatus;
import com.matteusmoreno.moto_manager.payable.entity.Payable;
import com.matteusmoreno.moto_manager.payable.request.CreatePayableRequest;
import com.matteusmoreno.moto_manager.payable.request.UpdatePayableRequest;
import com.matteusmoreno.moto_manager.supplier.entity.Supplier;
import com.matteusmoreno.moto_manager.supplier.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PayableService {

    private final PayableRepository payableRepository;
    private final SupplierRepository supplierRepository;

    @Autowired
    public PayableService(PayableRepository payableRepository, SupplierRepository supplierRepository) {
        this.payableRepository = payableRepository;
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public Payable createPayable(CreatePayableRequest request) {
        Supplier supplier = supplierRepository.findById(request.supplierId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        if (request.dueDate().isBefore(request.issueDate())) {
            throw new InvalidDueDateException("Due date must be after issue date");
        }

        PaymentStatus status;
        if (LocalDate.now().isAfter(request.dueDate())) status = PaymentStatus.OVERDUE;
        else status = PaymentStatus.PENDING;

        Payable payable = Payable.builder()
                .supplier(supplier)
                .description(request.description())
                .value(request.value())
                .issueDate(request.issueDate())
                .dueDate(request.dueDate())
                .paymentDate(null)
                .status(status)
                .build();

        payableRepository.save(payable);
        return payable;
    }

    public Page<PayableDetailsResponse> findAllPayables(Pageable pageable) {
        return payableRepository.findAll(pageable).map(PayableDetailsResponse::new);
    }

    @Transactional
    public Payable updatePayable(UpdatePayableRequest request) {
        Payable payable = payableRepository.findById(request.payableId())
                .orElseThrow(() -> new EntityNotFoundException("Payable not found"));

        if (request.supplierId() != null) {
            Supplier supplier = supplierRepository.findById(request.supplierId())
                    .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));
            payable.setSupplier(supplier);
        }

        if (request.description() != null) payable.setDescription(request.description());
        if (request.value() != null) payable.setValue(request.value());

        if (request.issueDate() != null) {
            if (request.issueDate().isAfter(LocalDate.now())) {
                throw new InvalidDueDateException("Issue date must be before today");
            }
            payable.setIssueDate(request.issueDate());
        }

        if (request.dueDate() != null) {
            if (request.dueDate().isBefore(payable.getIssueDate())) {
                throw new InvalidDueDateException("Due date must be after issue date");
            }
            payable.setDueDate(request.dueDate());
        }

        payableRepository.save(payable);

        return payable;
    }

    @Transactional
    public Payable payPayable(Long id) {
        Payable payable = payableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payable not found"));

        if (payable.getStatus() == PaymentStatus.PAID) {
            throw new PayableAlreadyPaidException("Payable is already paid");
        }
        if (payable.getStatus() == PaymentStatus.CANCELED) {
            throw new PayableAlreadyCanceledException("Payable is canceled");
        }

        payable.setPaymentDate(LocalDate.now());
        payable.setStatus(PaymentStatus.PAID);
        payableRepository.save(payable);

        return payable;
    }

    @Transactional
    public Payable cancelPayable(Long id) {
        Payable payable = payableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payable not found"));

        if (payable.getStatus() == PaymentStatus.PAID) {
            throw new PayableAlreadyPaidException("Payable is already paid");
        }
        if (payable.getStatus() == PaymentStatus.CANCELED) {
            throw new PayableAlreadyCanceledException("Payable is canceled");
        }

        payable.setStatus(PaymentStatus.CANCELED);
        payableRepository.save(payable);

        return payable;
    }

    // Atualiza o status de payables para OVERDUE todos os dias às 00:00
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateOverduePayables() {
        List<Payable> overduePayables = payableRepository.findByStatusAndDueDateBefore(PaymentStatus.PENDING, LocalDate.now());
        for (Payable payable : overduePayables) {
            payable.setStatus(PaymentStatus.OVERDUE);
            payableRepository.save(payable);
        }
    }
}
