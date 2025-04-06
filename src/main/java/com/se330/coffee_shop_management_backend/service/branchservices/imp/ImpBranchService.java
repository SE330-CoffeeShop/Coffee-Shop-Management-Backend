package com.se330.coffee_shop_management_backend.service.branchservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.branch.BranchCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.branch.BranchUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Branch;
import com.se330.coffee_shop_management_backend.repository.BranchRepository;
import com.se330.coffee_shop_management_backend.service.branchservices.IBranchService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImpBranchService implements IBranchService {

    private final BranchRepository branchRepository;

    public ImpBranchService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @Override
    public Branch findByIdBranch(UUID id) {
        return branchRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Branch> findAllBranches(Pageable pageable) {
        return branchRepository.findAll(pageable);
    }

    @Override
    public Branch createBranch(BranchCreateRequestDTO branchCreateRequestDTO) {
        return branchRepository.save(
                Branch.builder()
                        .branchName(branchCreateRequestDTO.getBranchName())
                        .branchAddress(branchCreateRequestDTO.getBranchAddress())
                        .branchPhone(branchCreateRequestDTO.getBranchPhone())
                        .branchEmail(branchCreateRequestDTO.getBranchEmail())
                        .build()
        );
    }

    @Override
    public Branch updateBranch(BranchUpdateRequestDTO branchUpdateRequestDTO) {
        Branch existingBranch = branchRepository.findById(branchUpdateRequestDTO.getBranchId())
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with ID: " + branchUpdateRequestDTO.getBranchId()));

        existingBranch.setBranchName(branchUpdateRequestDTO.getBranchName());
        existingBranch.setBranchAddress(branchUpdateRequestDTO.getBranchAddress());
        existingBranch.setBranchPhone(branchUpdateRequestDTO.getBranchPhone());
        existingBranch.setBranchEmail(branchUpdateRequestDTO.getBranchEmail());

        return branchRepository.save(existingBranch);
    }

    @Override
    public void deleteBranch(UUID id) {
        branchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with ID: " + id));

        branchRepository.deleteById(id);
    }
}