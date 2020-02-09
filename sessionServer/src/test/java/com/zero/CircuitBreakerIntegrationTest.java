package com.zero;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.client.circuitbreaker.FailFastException;
import com.linecorp.armeria.common.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.invoke.MethodHandles;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CircuitBreakerIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    WebClient client;

    @Test
    public void TestCircuitBreakerOpening() throws InterruptedException {

        for(int i=0; i < 6; i++) {
            assertThat(client.get("/unavailable/").aggregate().join().status())
                    .isSameAs(HttpStatus.SERVICE_UNAVAILABLE);
            Thread.sleep(1000);
        }

        assertThatThrownBy(() -> client.get("/unavailable/").aggregate().join())
                .hasCauseExactlyInstanceOf(FailFastException.class);
    }

    @Test
    public void TestCircuitBreakerOpeningExceptionCatch() throws InterruptedException {
        boolean failfasted = false;
        for(int i=0; i < 6; i++) {
            assertThat(client.get("/unavailable/").aggregate().join().status())
                    .isSameAs(HttpStatus.SERVICE_UNAVAILABLE);
            Thread.sleep(1000);
        }

        try {
            client.get("/unavailable/").aggregate().join();
        }catch(Exception e) {
            failfasted = true;
        }

        assertThat(failfasted).isTrue();
    }
}