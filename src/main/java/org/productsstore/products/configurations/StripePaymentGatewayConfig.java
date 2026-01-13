package org.productsstore.products.configurations;


import com.stripe.Stripe;
import com.stripe.StripeClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// Configuration class for setting up Stripe payment gateway–related beans.
public class StripePaymentGatewayConfig {
    @Value("${STRIPE_API_KEY}")
    // Injects the Stripe API key from application properties or environment variables.
    private String apiKey;

    @Bean
    // Creates and exposes a StripeClient bean used to interact with Stripe APIs.
    // This client will be injected wherever Stripe operations (payments, webhooks, etc.) are required.
    public StripeClient createStripeClient() {
        // Currently, the API key is hardcoded as a string literal.
        // In a production-ready setup, this should use the injected `apiKey` field.
        return new StripeClient("apiKey");
    }
}
