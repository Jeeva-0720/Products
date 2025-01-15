package org.productsstore.products.services;

import com.razorpay.RazorpayException;
import org.springframework.stereotype.Service;


@Service("stripePaymentGateway")
public class StripePaymentGateway implements PaymentService {

    @Override
    public String generatePaymentLink(Long orderId) throws RazorpayException {
        return "";
    }
}
