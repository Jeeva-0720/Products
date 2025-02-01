package org.productsstore.products.services;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentService {

    public String generatePaymentLink(Long orderId, Long amount) throws RazorpayException, StripeException;

}
