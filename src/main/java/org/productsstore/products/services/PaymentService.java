package org.productsstore.products.services;

import com.razorpay.RazorpayException;

public interface PaymentService {

    public String generatePaymentLink(Long orderId) throws RazorpayException;

}
