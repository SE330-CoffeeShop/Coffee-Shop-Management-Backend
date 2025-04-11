package com.se330.coffee_shop_management_backend.service.transfer.imp;

import com.se330.coffee_shop_management_backend.dto.request.transfer.TransferDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.transfer.TransferDetailUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.TransferDetail;
import com.se330.coffee_shop_management_backend.service.transfer.ITransferDetailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImpTransferDetailService implements ITransferDetailService {
    @Override
    public TransferDetail findByIdTransferDetail(UUID id) {
        return null;
    }

    @Override
    public Page<TransferDetail> findAllTransferDetails(Pageable pageable) {
        return null;
    }

    @Override
    public TransferDetail createTransferDetail(TransferDetailCreateRequestDTO transferDetailCreateRequestDTO) {
        return null;
    }

    @Override
    public TransferDetail updateTransferDetail(TransferDetailUpdateRequestDTO transferDetailUpdateRequestDTO) {
        return null;
    }

    @Override
    public void deleteTransferDetail(UUID id) {

    }
}
