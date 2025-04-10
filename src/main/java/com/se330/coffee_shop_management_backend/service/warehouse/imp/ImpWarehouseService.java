package com.se330.coffee_shop_management_backend.service.warehouse.imp;

import com.se330.coffee_shop_management_backend.dto.request.warehouse.WarehouseCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.warehouse.WarehouseUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Warehouse;
import com.se330.coffee_shop_management_backend.repository.WarehouseRepository;
import com.se330.coffee_shop_management_backend.service.warehouse.IWarehouseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImpWarehouseService implements IWarehouseService {

    private final WarehouseRepository warehouseRepository;

    public ImpWarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public Warehouse findByIdWarehouse(UUID id) {
        return warehouseRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Warehouse> findAllWarehouses(Pageable pageable) {
        return warehouseRepository.findAll(pageable);
    }

    @Override
    public Warehouse createWarehouse(WarehouseCreateRequestDTO warehouseCreateRequestDTO) {
        return warehouseRepository.save(Warehouse.builder()
                .warehouseName(warehouseCreateRequestDTO.getWarehouseName())
                .warehousePhone(warehouseCreateRequestDTO.getWarehousePhone())
                .warehouseEmail(warehouseCreateRequestDTO.getWarehouseEmail())
                .warehouseAddress(warehouseCreateRequestDTO.getWarehouseAddress())
                .build()
        );
    }

    @Override
    public Warehouse updateWarehouse(WarehouseUpdateRequestDTO warehouseUpdateRequestDTO) {
        Warehouse existingWarehouse = warehouseRepository.findById(warehouseUpdateRequestDTO.getWarehouseId())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: " + warehouseUpdateRequestDTO.getWarehouseId()));

        existingWarehouse.setWarehouseName(warehouseUpdateRequestDTO.getWarehouseName());
        existingWarehouse.setWarehousePhone(warehouseUpdateRequestDTO.getWarehousePhone());
        existingWarehouse.setWarehouseEmail(warehouseUpdateRequestDTO.getWarehouseEmail());
        existingWarehouse.setWarehouseAddress(warehouseUpdateRequestDTO.getWarehouseAddress());

        return warehouseRepository.save(existingWarehouse);
    }

    @Transactional
    @Override
    public void deleteWarehouse(UUID id) {
        Warehouse existingWarehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: " + id));

        warehouseRepository.delete(existingWarehouse);
    }
}
