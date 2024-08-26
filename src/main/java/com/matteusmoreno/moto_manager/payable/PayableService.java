package com.matteusmoreno.moto_manager.payable;

import com.matteusmoreno.moto_manager.exception.InvalidDueDateException;
import com.matteusmoreno.moto_manager.supplier.entity.Supplier;
import com.matteusmoreno.moto_manager.supplier.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
}
