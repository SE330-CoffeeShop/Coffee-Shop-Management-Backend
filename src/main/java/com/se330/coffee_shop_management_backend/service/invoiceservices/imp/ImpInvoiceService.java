package com.se330.coffee_shop_management_backend.service.invoiceservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.invoice.InvoiceCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.invoice.InvoiceUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Invoice;
import com.se330.coffee_shop_management_backend.entity.Supplier;
import com.se330.coffee_shop_management_backend.entity.Warehouse;
import com.se330.coffee_shop_management_backend.repository.InvoiceRepository;
import com.se330.coffee_shop_management_backend.repository.SupplierRepository;
import com.se330.coffee_shop_management_backend.repository.WarehouseRepository;
import com.se330.coffee_shop_management_backend.service.invoiceservices.IInvoiceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImpInvoiceService implements IInvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final WarehouseRepository warehouseRepository;
    private final SupplierRepository supplierRepository;

    public ImpInvoiceService(
            InvoiceRepository invoiceRepository,
            WarehouseRepository warehouseRepository,
            SupplierRepository supplierRepository
    ) {
        this.invoiceRepository = invoiceRepository;
        this.warehouseRepository = warehouseRepository;
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Invoice findByIdInvoice(UUID id) {
        return invoiceRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Invoice> findAllInvoices(Pageable pageable) {
        return invoiceRepository.findAll(pageable);
    }

    @Override
    public Invoice createInvoice(InvoiceCreateRequestDTO invoiceCreateRequestDTO) {
        Warehouse existingWarehouse = warehouseRepository.findById(invoiceCreateRequestDTO.getWarehouseId())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with id: " + invoiceCreateRequestDTO.getWarehouseId()));

        Supplier existingSupplier = supplierRepository.findById(invoiceCreateRequestDTO.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + invoiceCreateRequestDTO.getSupplierId()));

        return invoiceRepository.save(
                Invoice.builder()
                        .warehouse(existingWarehouse)
                        .supplier(existingSupplier)
                        .invoiceDescription(invoiceCreateRequestDTO.getInvoiceDescription())
                        .invoiceTrackingNumber(invoiceCreateRequestDTO.getInvoiceTrackingNumber())
                        .invoiceTransferTotalCost(invoiceCreateRequestDTO.getInvoiceTransferTotalCost())
                        .build()
        );
    }

    @Transactional
    @Override
    public Invoice updateInvoice(InvoiceUpdateRequestDTO invoiceUpdateRequestDTO) {
        Warehouse existingWarehouse = warehouseRepository.findById(invoiceUpdateRequestDTO.getWarehouseId())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with id: " + invoiceUpdateRequestDTO.getWarehouseId()));

        Supplier existingSupplier = supplierRepository.findById(invoiceUpdateRequestDTO.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + invoiceUpdateRequestDTO.getSupplierId()));

        Invoice existingInvoice = invoiceRepository.findById(invoiceUpdateRequestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + invoiceUpdateRequestDTO.getId()));

        if (existingInvoice.getSupplier() != null) {
            existingInvoice.getSupplier().getInvoices().remove(existingInvoice);
            existingInvoice.setSupplier(existingSupplier);
            existingInvoice.getSupplier().getInvoices().add(existingInvoice);
        }

        if (existingInvoice.getWarehouse() != null) {
            existingInvoice.getWarehouse().getInvoices().remove(existingInvoice);
            existingInvoice.setWarehouse(existingWarehouse);
            existingInvoice.getWarehouse().getInvoices().add(existingInvoice);
        }

        existingInvoice.setInvoiceDescription(invoiceUpdateRequestDTO.getInvoiceDescription());
        existingInvoice.setInvoiceTrackingNumber(invoiceUpdateRequestDTO.getInvoiceTrackingNumber());
        existingInvoice.setInvoiceTransferTotalCost(invoiceUpdateRequestDTO.getInvoiceTransferTotalCost());

        return invoiceRepository.save(existingInvoice);
    }

    @Transactional
    @Override
    public void deleteInvoice(UUID id) {
        Invoice existingInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + id));

        if(existingInvoice.getSupplier() != null) {
            existingInvoice.getSupplier().getInvoices().remove(existingInvoice);
            existingInvoice.setSupplier(null);
        }

        if(existingInvoice.getWarehouse() != null) {
            existingInvoice.getWarehouse().getInvoices().remove(existingInvoice);
            existingInvoice.setWarehouse(null);
        }

        invoiceRepository.delete(existingInvoice);
    }
}
