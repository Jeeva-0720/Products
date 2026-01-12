package org.productsstore.products.configurations;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced   //To achieve Client Side load balancing
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
