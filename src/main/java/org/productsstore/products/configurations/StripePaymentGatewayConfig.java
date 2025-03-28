package org.productsstore.products.configurations;


import com.stripe.Stripe;
import com.stripe.StripeClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripePaymentGatewayConfig {
    @Value("${STRIPE_API_KEY}")
    private String apiKey;

    @Bean
    public StripeClient createStripeClient() {
        return new StripeClient("apiKey");
    }
}
