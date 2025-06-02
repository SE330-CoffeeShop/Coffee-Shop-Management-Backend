package com.se330.coffee_shop_management_backend.dto.response.paymentmethod;

import com.se330.coffee_shop_management_backend.entity.PaymentMethods;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodResponseDTO {
    private String paymentMethodId;
    private String methodType;
    private String methodDetails;
    private boolean methodIsDefault;
    private String userId;
    private List<String> orderIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PaymentMethodResponseDTO convert(PaymentMethods paymentMethod) {
        return PaymentMethodResponseDTO.builder()
                .paymentMethodId(paymentMethod.getId().toString())
                .methodType(paymentMethod.getMethodType())
                .methodDetails(paymentMethod.getMethodDetails())
                .methodIsDefault(paymentMethod.isMethodIsDefault())
                .userId(paymentMethod.getUser() != null ? paymentMethod.getUser().getId().toString() : null)
                .orderIds(paymentMethod.getOrders() != null ? paymentMethod.getOrders().stream()
                        .map(order -> order.getId().toString())
                        .collect(Collectors.toList()) : null)
                .createdAt(paymentMethod.getCreatedAt())
                .updatedAt(paymentMethod.getUpdatedAt())
                .build();
    }

    public static List<PaymentMethodResponseDTO> convert(List<PaymentMethods> paymentMethods) {
        if (paymentMethods == null || paymentMethods.isEmpty()) {
            return List.of();
        }

        return paymentMethods.stream()
                .map(PaymentMethodResponseDTO::convert)
                .collect(Collectors.toList());
    }
}