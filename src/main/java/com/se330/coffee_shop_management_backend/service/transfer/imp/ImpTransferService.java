package com.se330.coffee_shop_management_backend.service.transfer.imp;

import com.se330.coffee_shop_management_backend.dto.request.transfer.TransferCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.transfer.TransferUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Branch;
import com.se330.coffee_shop_management_backend.entity.Transfer;
import com.se330.coffee_shop_management_backend.entity.Warehouse;
import com.se330.coffee_shop_management_backend.repository.BranchRepository;
import com.se330.coffee_shop_management_backend.repository.TransferRepository;
import com.se330.coffee_shop_management_backend.repository.WarehouseRepository;
import com.se330.coffee_shop_management_backend.service.transfer.ITransferService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImpTransferService implements ITransferService {

    private final TransferRepository transferRepository;
    private final WarehouseRepository warehouseRepository;
    private final BranchRepository branchRepository;

    public ImpTransferService(
            TransferRepository transferRepository,
            WarehouseRepository warehouseRepository,
            BranchRepository branchRepository
    ) {
        this.transferRepository = transferRepository;
        this.warehouseRepository = warehouseRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public Transfer findByIdTransfer(UUID id) {
        return transferRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Transfer> findAllTransfers(Pageable pageable) {
        return transferRepository.findAll(pageable);
    }

    @Override
    public Transfer createTransfer(TransferCreateRequestDTO transferCreateRequestDTO) {
        Warehouse existingWarehouse = warehouseRepository.findById(transferCreateRequestDTO.getWarehouseId())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: " + transferCreateRequestDTO.getWarehouseId()));

        Branch existingBranch = branchRepository.findById(transferCreateRequestDTO.getBranchId())
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with ID: " + transferCreateRequestDTO.getBranchId()));

        return transferRepository.save(
                Transfer.builder()
                        .warehouse(existingWarehouse)
                        .branch(existingBranch)
                        .transferDescription(transferCreateRequestDTO.getTransferDescription())
                        .transferTrackingNumber(transferCreateRequestDTO.getTransferTrackingNumber())
                        .transferTotalCost(transferCreateRequestDTO.getTransferTotalCost())
                        .build()
        );
    }

    @Transactional
    @Override
    public Transfer updateTransfer(TransferUpdateRequestDTO transferUpdateRequestDTO) {
        Transfer existingTransfer = transferRepository.findById(transferUpdateRequestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Transfer not found with ID: " + transferUpdateRequestDTO.getId()));

        Warehouse existingWarehouse = warehouseRepository.findById(transferUpdateRequestDTO.getWarehouseId())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: " + transferUpdateRequestDTO.getWarehouseId()));

        Branch existingBranch = branchRepository.findById(transferUpdateRequestDTO.getBranchId())
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with ID: " + transferUpdateRequestDTO.getBranchId()));

        if (existingTransfer.getWarehouse() != null) {
            existingTransfer.getWarehouse().getTransfers().remove(existingTransfer);
            existingTransfer.setWarehouse(existingWarehouse);
            existingWarehouse.getTransfers().add(existingTransfer);
        }

        if (existingTransfer.getBranch() != null) {
            existingTransfer.getBranch().getTransfers().remove(existingTransfer);
            existingTransfer.setBranch(existingBranch);
            existingBranch.getTransfers().add(existingTransfer);
        }

        existingTransfer.setTransferDescription(transferUpdateRequestDTO.getTransferDescription());
        existingTransfer.setTransferTrackingNumber(transferUpdateRequestDTO.getTransferTrackingNumber());
        existingTransfer.setTransferTotalCost(transferUpdateRequestDTO.getTransferTotalCost());

        return transferRepository.save(existingTransfer);
    }

    @Transactional
    @Override
    public void deleteTransfer(UUID id) {
        Transfer existingTransfer = transferRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transfer not found with ID: " + id));

        if(existingTransfer.getWarehouse() != null) {
            existingTransfer.getWarehouse().getTransfers().remove(existingTransfer);
            existingTransfer.setWarehouse(null);
        }

        if(existingTransfer.getBranch() != null) {
            existingTransfer.getBranch().getTransfers().remove(existingTransfer);
            existingTransfer.setBranch(null);
        }

        transferRepository.delete(existingTransfer);
    }
}
