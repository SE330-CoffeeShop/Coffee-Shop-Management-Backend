package com.se330.coffee_shop_management_backend.service.paymentmethodservices;

import com.se330.coffee_shop_management_backend.dto.request.paymentmethod.PaymentMethodCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.paymentmethod.PaymentMethodUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.PaymentMethods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IPaymentMethodService {
    PaymentMethods findByIdPaymentMethod(UUID id);
    Page<PaymentMethods> findAllPaymentMethodsByUserId(UUID userId, Pageable pageable);
    Page<PaymentMethods> findAllPaymentMethods(Pageable pageable);
    PaymentMethods createPaymentMethod(PaymentMethodCreateRequestDTO paymentMethodCreateRequestDTO);
    PaymentMethods updatePaymentMethod(PaymentMethodUpdateRequestDTO paymentMethodUpdateRequestDTO);
    void deletePaymentMethod(UUID id);
}
