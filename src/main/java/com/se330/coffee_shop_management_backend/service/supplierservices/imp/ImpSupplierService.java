package com.se330.coffee_shop_management_backend.service.supplierservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.supplier.SupplierCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.supplier.SupplierUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Supplier;
import com.se330.coffee_shop_management_backend.repository.SupplierRepository;
import com.se330.coffee_shop_management_backend.service.supplierservices.ISupplierService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImpSupplierService implements ISupplierService {

    private final SupplierRepository supplierRepository;

    public ImpSupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Supplier findByIdSupplier(UUID id) {
        return supplierRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Supplier> findAllSuppliers(Pageable pageable) {
        return supplierRepository.findAll(pageable);
    }

    @Override
    public Supplier createSupplier(SupplierCreateRequestDTO supplierCreateRequestDTO) {
        return supplierRepository.save(
                Supplier.builder()
                        .supplierName(supplierCreateRequestDTO.getSupplierName())
                        .supplierPhone(supplierCreateRequestDTO.getSupplierPhone())
                        .supplierEmail(supplierCreateRequestDTO.getSupplierEmail())
                        .supplierAddress(supplierCreateRequestDTO.getSupplierAddress())
                        .build()
        );
    }

    @Override
    public Supplier updateSupplier(SupplierUpdateRequestDTO supplierUpdateRequestDTO) {
        Supplier existingSupplier = supplierRepository.findById(supplierUpdateRequestDTO.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with ID: " + supplierUpdateRequestDTO.getSupplierId()));

        existingSupplier.setSupplierName(supplierUpdateRequestDTO.getSupplierName());
        existingSupplier.setSupplierPhone(supplierUpdateRequestDTO.getSupplierPhone());
        existingSupplier.setSupplierEmail(supplierUpdateRequestDTO.getSupplierEmail());
        existingSupplier.setSupplierAddress(supplierUpdateRequestDTO.getSupplierAddress());

        return supplierRepository.save(existingSupplier);
    }

    @Override
    public void deleteSupplier(UUID id) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with ID: " + id));

        supplierRepository.delete(existingSupplier);
    }
}
