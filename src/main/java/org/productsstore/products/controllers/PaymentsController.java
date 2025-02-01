package org.productsstore.products.controllers;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import org.productsstore.products.Dtos.PaymentGatewayRequestDto;
import org.productsstore.products.services.PaymentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentsController {

    PaymentService paymentService;

    public PaymentsController( @Qualifier("razorpayPaymentGateway") PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public String getPaymentLink(@RequestBody PaymentGatewayRequestDto paymentGatewayRequestDto) {
        try {
            return paymentService.generatePaymentLink(paymentGatewayRequestDto.orderId, paymentGatewayRequestDto.amount);
        } catch (RazorpayException | StripeException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleWebhookURL(@RequestBody PaymentGatewayRequestDto paymentGatewayRequestDto) {
        System.out.println("Webhook URL triggered");
    }
}
