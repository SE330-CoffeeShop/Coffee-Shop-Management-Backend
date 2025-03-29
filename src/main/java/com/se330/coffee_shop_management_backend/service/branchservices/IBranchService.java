package com.se330.coffee_shop_management_backend.service.branchservices;

import com.se330.coffee_shop_management_backend.dto.request.branch.BranchCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.branch.BranchUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.branch.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IBranchService {
    Branch findByIdBranch(UUID id);
    Page<Branch> findAllBranches(Pageable pageable);
    Branch createBranch(BranchCreateRequestDTO branchCreateRequestDTO);
    Branch updateBranch(BranchUpdateRequestDTO branchUpdateRequestDTO);
    void deleteBranch(UUID id);
}