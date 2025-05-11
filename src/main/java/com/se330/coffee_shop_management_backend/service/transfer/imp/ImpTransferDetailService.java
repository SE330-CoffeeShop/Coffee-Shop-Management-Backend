package com.se330.coffee_shop_management_backend.service.transfer.imp;

import com.se330.coffee_shop_management_backend.dto.request.transfer.TransferDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.transfer.TransferDetailUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Ingredient;
import com.se330.coffee_shop_management_backend.entity.Transfer;
import com.se330.coffee_shop_management_backend.entity.TransferDetail;
import com.se330.coffee_shop_management_backend.repository.IngredientRepository;
import com.se330.coffee_shop_management_backend.repository.TransferDetailRepository;
import com.se330.coffee_shop_management_backend.repository.TransferRepository;
import com.se330.coffee_shop_management_backend.service.transfer.ITransferDetailService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ImpTransferDetailService implements ITransferDetailService {

    private final TransferRepository transferRepository;
    private final TransferDetailRepository transferDetailRepository;
    private final IngredientRepository ingredientRepository;

    public ImpTransferDetailService(
            TransferRepository transferRepository,
            TransferDetailRepository transferDetailRepository,
            IngredientRepository ingredientRepository
    ) {
        this.transferRepository = transferRepository;
        this.transferDetailRepository = transferDetailRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public TransferDetail findByIdTransferDetail(UUID id) {
        return transferDetailRepository.findById(id).orElse(null);
    }

    @Override
    public Page<TransferDetail> findAllTransferDetails(Pageable pageable) {
        return transferDetailRepository.findAll(pageable);
    }

    @Override
    public TransferDetail createTransferDetail(TransferDetailCreateRequestDTO transferDetailCreateRequestDTO) {
        Transfer existingTransfer = transferRepository.findById(transferDetailCreateRequestDTO.getTransferId())
                .orElseThrow(() -> new EntityNotFoundException("Transfer not found with ID: " + transferDetailCreateRequestDTO.getTransferId()));

        Ingredient existingIngredient = ingredientRepository.findById(transferDetailCreateRequestDTO.getIngredientId())
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found with ID: " + transferDetailCreateRequestDTO.getIngredientId()));

        return transferDetailRepository.save(
                TransferDetail.builder()
                        .transfer(existingTransfer)
                        .ingredient(existingIngredient)
                        .transferDetailQuantity(transferDetailCreateRequestDTO.getTransferDetailQuantity())
                        .transferDetailUnit(transferDetailCreateRequestDTO.getTransferDetailUnit())
                        .build()
        );
    }

    @Transactional
    @Override
    public TransferDetail updateTransferDetail(TransferDetailUpdateRequestDTO transferDetailUpdateRequestDTO) {
        TransferDetail existingTransferDetail = transferDetailRepository.findById(transferDetailUpdateRequestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("TransferDetail not found with ID: " + transferDetailUpdateRequestDTO.getId()));

        Transfer existingTransfer = transferRepository.findById(transferDetailUpdateRequestDTO.getTransferId())
                .orElseThrow(() -> new EntityNotFoundException("Transfer not found with ID: " + transferDetailUpdateRequestDTO.getTransferId()));

        Ingredient existingIngredient = ingredientRepository.findById(transferDetailUpdateRequestDTO.getIngredientId())
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found with ID: " + transferDetailUpdateRequestDTO.getIngredientId()));

        if (existingTransferDetail.getTransfer() != null) {
            existingTransferDetail.getTransfer().getTransferDetails().remove(existingTransferDetail);
            existingTransferDetail.setTransfer(existingTransfer);
            existingTransfer.getTransferDetails().add(existingTransferDetail);
        }

        if (existingTransferDetail.getIngredient() != null) {
            existingTransferDetail.getIngredient().getTransferDetails().remove(existingTransferDetail);
            existingTransferDetail.setIngredient(existingIngredient);
            existingIngredient.getTransferDetails().add(existingTransferDetail);
        }

        existingTransferDetail.setTransferDetailQuantity(transferDetailUpdateRequestDTO.getTransferDetailQuantity());
        existingTransferDetail.setTransferDetailUnit(transferDetailUpdateRequestDTO.getTransferDetailUnit());

        return transferDetailRepository.save(existingTransferDetail);
    }

    @Transactional
    @Override
    public void deleteTransferDetail(UUID id) {
        TransferDetail existingTransferDetail = transferDetailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TransferDetail not found with ID: " + id));

        if (existingTransferDetail.getTransfer() != null) {
            existingTransferDetail.getTransfer().getTransferDetails().remove(existingTransferDetail);
            existingTransferDetail.setTransfer(null);
        }

        if (existingTransferDetail.getIngredient() != null) {
            existingTransferDetail.getIngredient().getTransferDetails().remove(existingTransferDetail);
            existingTransferDetail.setIngredient(null);
        }

        transferDetailRepository.delete(existingTransferDetail);
    }
}
