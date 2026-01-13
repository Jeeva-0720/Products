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
    // API to generate a payment link for a given order.
    // Accepts order details (orderId and amount) and delegates payment link generation to the underlying payment service (e.g., Razorpay / Stripe).
    public String getPaymentLink(@RequestBody PaymentGatewayRequestDto paymentGatewayRequestDto) {
        try {
            return paymentService.generatePaymentLink(paymentGatewayRequestDto.orderId, paymentGatewayRequestDto.amount);
        } catch (RazorpayException | StripeException e) {
            // Wraps and rethrows payment gateway–specific exceptions as a runtime exception so that global exception handling can manage the failure response
            throw new RuntimeException(e);
        }
    }

    public void handleWebhookURL(@RequestBody PaymentGatewayRequestDto paymentGatewayRequestDto) {
        System.out.println("Webhook URL triggered");
    }
}
