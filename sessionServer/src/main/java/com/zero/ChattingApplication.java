package com.zero;

import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.healthcheck.HealthCheckService;
import com.linecorp.armeria.server.healthcheck.HealthChecker;
import com.zero.dto.ChattingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
//@PropertySource("classpath:application.properties")
public class ChattingApplication {
    public static Logger logger = LoggerFactory.getLogger(ChattingApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(ChattingApplication.class, args);
        /*
        ServerBuilder sb = Server.builder();
        sb.port(9090, SessionProtocol.HTTP);
        sb.serviceUnder("/docs", new DocService());
        sb.serviceUnder("/healthcheck",
                HealthCheckService.of((HealthChecker) () -> false));
        Server server = sb.build();

        CompletableFuture<Void> future = server.start();
        future.join();
        */
    }


}
