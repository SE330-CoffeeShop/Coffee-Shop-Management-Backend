package com.se330.coffee_shop_management_backend.service.paymentservices.imp.other;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaypalService {
    private final APIContext apiContext;
    
    @Value("${SUCCESS_REDIRECT_URL}")
    private String successRedirectUrl;
    
    @Value("${CANCELED_REDIRECT_URL}")
    private String canceledRedirectUrl;

    @Transactional
    public Payment createPaymentWithPayPal(
            Double total,
            String orderId) throws PayPalRESTException {

        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription("Thanh toán cho hóa đơn mã số " + orderId);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(Constants.PaymentMethodEnum.PAYPAL.getValue());

        Payment payment = new Payment();
        payment.setIntent("SALE"); // "sale" cho thanh toán ngay
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(canceledRedirectUrl); // URL khi người dùng hủy thanh toán
        redirectUrls.setReturnUrl(successRedirectUrl); // URL khi người dùng hoàn tất thanh toán
        
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    // Xác nhận thanh toán sau khi người dùng hoàn tất trên PayPal
    @Transactional
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecution);
    }
}
