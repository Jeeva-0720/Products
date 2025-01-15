package org.productsstore.products.configurations;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorpayPaymentGatewayConfig {
    @Value("${razorpay.client.id}")
    private String apiKey;
    @Value("${razorpay.client.secret}")
    private String apiSecret;

    @Bean
    public RazorpayClient createRazorpayClient() throws RazorpayException {
        return new RazorpayClient("apiKey", "apiSecret");
    }
}
