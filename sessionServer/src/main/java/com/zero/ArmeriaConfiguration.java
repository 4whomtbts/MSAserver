package com.zero;

import com.fasterxml.jackson.databind.JsonSerializable;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.client.circuitbreaker.CircuitBreaker;
import com.linecorp.armeria.client.circuitbreaker.CircuitBreakerHttpClient;
import com.linecorp.armeria.client.circuitbreaker.CircuitBreakerStrategy;
import com.linecorp.armeria.client.logging.LoggingClient;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.healthcheck.HealthCheckService;
import com.linecorp.armeria.server.healthcheck.HealthChecker;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import com.zero.dto.ChattingMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ArmeriaConfiguration {

    @Bean
    public ArmeriaServerConfigurator armeriaServerConfigurator() {
        return builder -> {
            builder.serviceUnder("/docs", new DocService());

            HealthChecker memoryBasedHealthChecker = () -> {
                long systemTotalMemory = Runtime.getRuntime().totalMemory();
                long systemFreeMemory = Runtime.getRuntime().freeMemory();
                long systemUsingMemory = systemTotalMemory - systemFreeMemory;
                double systemMemoryOccupancyRate = (double) (systemUsingMemory / systemTotalMemory);

                if(systemMemoryOccupancyRate > 0.1) {
                    return false;
                }

                return true;
            };

            HealthChecker diskUsagedBaseHealthChecker = () -> {
                return true;
            };

            builder.serviceUnder("/healthcheck",
                  HealthCheckService.of(memoryBasedHealthChecker));
            builder.serviceUnder("/unavailable", (ctx, req) -> HttpResponse.of(HttpStatus.SERVICE_UNAVAILABLE));
        };
    }

    @Bean
    public WebClient webClient() {
        CircuitBreakerStrategy strategy = CircuitBreakerStrategy.onServerErrorStatus();

        CircuitBreaker circuitBreaker = CircuitBreaker
                .builder("test-circuit-breaker")
                .counterSlidingWindow(Duration.ofSeconds(10))
                .circuitOpenWindow(Duration.ofSeconds(5))
                .failureRateThreshold(0.5)
                .minimumRequestThreshold(5)
                .trialRequestInterval(Duration.ofSeconds(3))
                .build();

        return WebClient
                .builder("http://localhost:8080")
                .decorator(LoggingClient.newDecorator())
                .decorator(CircuitBreakerHttpClient.newDecorator(circuitBreaker, strategy))
                .build();
    }
}