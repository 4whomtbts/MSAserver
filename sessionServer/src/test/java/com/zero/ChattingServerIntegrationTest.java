package com.zero;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.zero.controller.ChattingController;
import com.zero.controller.WebSocketConfig;
import com.zero.dto.ChattingMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ChattingServerIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @LocalServerPort
    int port;

    private WebSocketStompClient stompClient1;
    private StompSession stompSession1;

    private WebSocketStompClient stompClient2;
    private StompSession stompSession2;

    private WsTestUtils wsTestUtils = new WsTestUtils();

    private WebClient client;

    @Before
    public void initClient() {
        if (client == null) {
            client = WebClient.of("http://127.0.0.1:8080");
        }
    }

    @Before
    public void setUp() throws Exception {
        String wsUrl = "ws://127.0.0.1:" + port + WebSocketConfig.ENDPOINT_CONNECT;
        stompClient1 = wsTestUtils.createWebSocketClient();
        stompSession1 = stompClient1.connect(wsUrl, new ClientSessionHandler()).get();

        stompClient2 = wsTestUtils.createWebSocketClient();
        stompSession2 = stompClient2.connect(wsUrl, new ClientSessionHandler()).get();
    }

    @After
    public void tearDown() throws Exception {
        stompSession1.disconnect();
        stompClient1.stop();

        stompSession2.disconnect();
        stompClient2.stop();
    }

    @Test
    public void TestPublicChatting() throws Exception {
        ChattingMessage mockMessage = new ChattingMessage(ChattingMessage.MessageType.ENTER,
                "1234",
                "knuth",
                "hello world!");
        BlockingQueue<ChattingMessage> queue1 = new LinkedBlockingDeque<>();
        BlockingQueue<ChattingMessage> userQueue1 = new LinkedBlockingDeque<>();
        stompSession1.subscribe(ChattingController.ENDPOINT_SUBSCRIBE_CHAT + mockMessage.getRoomId(),
                new ClientFrameHandler((payload) -> {
                    queue1.offer(payload);
                }));

        Thread.currentThread().sleep(100);

        BlockingQueue<ChattingMessage> queue2 = new LinkedBlockingDeque<>();
        BlockingQueue<ChattingMessage> userQueue2 = new LinkedBlockingDeque<>();
        stompSession2.subscribe(ChattingController.ENDPOINT_SUBSCRIBE_CHAT + mockMessage.getRoomId(),
                new ClientFrameHandler((payload) -> {
                    queue2.offer(payload);
                }));

        Thread.currentThread().sleep(100);


        for(int i=0; i < 1000; i++) {
            stompSession1.send(ChattingController.ENDPOINT_PUBLISH_CHAT, mockMessage);
        }

        Thread.currentThread().sleep(100);
        assertEquals(1000, queue1.size());
        assertEquals(1000, queue2.size());
        assertEquals(mockMessage.getSender() +"님이 입장하셨습니다", queue1.poll().getMessage());
        assertEquals(mockMessage.getSender() +"님이 입장하셨습니다", queue2.poll().getMessage());
    }

    @Test
    public void TestHealthCheck() throws Exception {
        final AggregatedHttpResponse res =
                client.get("/healthcheck/")
                      .aggregate()
                      .join();
        assertThat(res.status()).isEqualTo(HttpStatus.OK);
        assertThat(res.contentUtf8()).isEqualTo("{\"healthy\":true}");
    }

    @Test
    public void TestCircuitBreaker() {
        assertThat(client.get("/unavailable/").aggregate().join().status())
                .isSameAs(HttpStatus.SERVICE_UNAVAILABLE);
    }
}

class WsTestUtils {

    public WebSocketStompClient createWebSocketClient() {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        return stompClient;
    }
}

class ClientSessionHandler extends StompSessionHandlerAdapter {

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        String username = connectedHeaders.get("user-name").iterator().next();
        super.afterConnected(session, connectedHeaders);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        super.handleException(session, command, headers, payload, exception);
    }
}

class ClientFrameHandler implements StompFrameHandler {
    private final Consumer<ChattingMessage> frameHandler;

    public ClientFrameHandler(Consumer<ChattingMessage> frameHandler) {
        this.frameHandler = frameHandler;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ChattingMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        frameHandler.accept((ChattingMessage)payload);
    }
}

/*
    @Test
    public void TestCircuitBreakerFailFast() {

        final AtomicLong ticker = new AtomicLong();
        CircuitBreakerStrategy strategy = CircuitBreakerStrategy.onServerErrorStatus();
        CircuitBreaker circuitBreaker =
                CircuitBreaker.builder()
                              .counterSlidingWindow(Duration.ofSeconds(10))
                              .circuitOpenWindow(Duration.ofSeconds(5))
                              .failureRateThreshold(0.3)
                              .minimumRequestThreshold(1)
                              .trialRequestInterval(Duration.ofSeconds(3))
                              .build();
        client = WebClient.builder("http://127.0.0.1:8080")
                          .decorator(CircuitBreakerHttpClient.newDecorator(circuitBreaker, strategy))
                          .build();

        for(int i=0; i < 6; i++) {
            final long currentTime = ticker.get();
            assertThat(client.get("/unavailable/").aggregate().join().status())
            .isSameAs(HttpStatus.SERVICE_UNAVAILABLE);
            await().until(() -> currentTime != ticker.get());
        }

      //  assertThat(circuitBreaker.canRequest()).isFalse();
        assertThatThrownBy(() -> client.get("/unavailable/").aggregate().join())
                .hasCauseExactlyInstanceOf(FailFastException.class);
    }
    */