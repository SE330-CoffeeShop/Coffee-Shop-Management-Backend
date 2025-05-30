package com.se330.coffee_shop_management_backend.service.paymentmethodservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.paymentmethod.PaymentMethodCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.paymentmethod.PaymentMethodUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.PaymentMethods;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.repository.PaymentMethodsRepository;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.service.paymentmethodservices.IPaymentMethodService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ImpPaymentMethodService implements IPaymentMethodService {

    private final PaymentMethodsRepository paymentMethodsRepository;
    private final UserRepository userRepository;

    public ImpPaymentMethodService(PaymentMethodsRepository paymentMethodsRepository, UserRepository userRepository) {
        this.paymentMethodsRepository = paymentMethodsRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PaymentMethods findByIdPaymentMethod(UUID id) {
        return paymentMethodsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found with id: " + id));
    }

    @Override
    public Page<PaymentMethods> findAllPaymentMethods(Pageable pageable) {
        return paymentMethodsRepository.findAll(pageable);
    }

    @Override
    public PaymentMethods createPaymentMethod(PaymentMethodCreateRequestDTO paymentMethodCreateRequestDTO) {
        User user = null;
        if (paymentMethodCreateRequestDTO.getUserId() != null) {
            user = userRepository.findById(paymentMethodCreateRequestDTO.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + paymentMethodCreateRequestDTO.getUserId()));
        }

        return paymentMethodsRepository.save(
                PaymentMethods.builder()
                        .methodType(paymentMethodCreateRequestDTO.getMethodType())
                        .methodDetails(paymentMethodCreateRequestDTO.getMethodDetails())
                        .methodIsDefault(paymentMethodCreateRequestDTO.isMethodIsDefault())
                        .user(user)
                        .build()
        );
    }

    @Override
    public PaymentMethods updatePaymentMethod(PaymentMethodUpdateRequestDTO paymentMethodUpdateRequestDTO) {
        PaymentMethods existingPaymentMethod = paymentMethodsRepository.findById(paymentMethodUpdateRequestDTO.getPaymentMethodId())
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found with id: " + paymentMethodUpdateRequestDTO.getPaymentMethodId()));

        User user = null;
        if (paymentMethodUpdateRequestDTO.getUserId() != null) {
            user = userRepository.findById(paymentMethodUpdateRequestDTO.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + paymentMethodUpdateRequestDTO.getUserId()));
        }

        existingPaymentMethod.setMethodType(paymentMethodUpdateRequestDTO.getMethodType());
        existingPaymentMethod.setMethodDetails(paymentMethodUpdateRequestDTO.getMethodDetails());
        existingPaymentMethod.setMethodIsDefault(paymentMethodUpdateRequestDTO.isMethodIsDefault());
        existingPaymentMethod.setUser(user);

        return paymentMethodsRepository.save(existingPaymentMethod);
    }

    @Override
    @Transactional
    public void deletePaymentMethod(UUID id) {
        PaymentMethods paymentMethod = paymentMethodsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found with id: " + id));

        User owner = paymentMethod.getUser();
        if (owner != null) {
            owner.getPaymentMethods().remove(paymentMethod);
            userRepository.save(owner);
        }

        paymentMethodsRepository.delete(paymentMethod);
    }
}
