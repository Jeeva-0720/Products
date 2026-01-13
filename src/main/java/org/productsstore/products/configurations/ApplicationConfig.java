package org.productsstore.products.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.client.RestTemplate;

@Configuration
// Central application configuration class for defining common infrastructure beans.
public class ApplicationConfig {
    @Bean
    // Configures and exposes a RestTemplate bean for making HTTP calls to external services (e.g., FakeStore API, payment gateways).
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    // Configures and exposes a RedisTemplate bean for interacting with Redis. This template is used for caching and key-value storage operations.
    public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // Uses String serialization for Redis keys for readability
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // Uses JSON serialization for Redis hash values to store complex objects
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}
