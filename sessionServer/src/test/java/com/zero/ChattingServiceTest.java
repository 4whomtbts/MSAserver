/*
package com.zero;

import com.zero.dto.ChattingMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChattingServiceTest {

    @Value("${local.server.port}")
    private int port;
    private String URL;

    private static final String SEND_MESSAGE = "/pub/message";
    private static final String SUBSCRIBE_MESSAGE = "/sub/message";
    private CompletableFuture<ChattingMessage> completableFuture;

    @Before
    public void setup() {
        URL = "ws://localhost:"+port+"/ws-stomp";
    }

    @Test
    public void testCreateChattingRoomEndpoint() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
        String uuid = UUID.randomUUID().toString();
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        stompSession.subscribe(SUBSCRIBE_MESSAGE + uuid, new CreateChattingStompHandler());
        stompSession.send(SEND_MESSAGE + uuid, new ChattingMessage());
        ChattingMessage message = completableFuture.get(5, SECONDS);
        assertNotNull(message);
    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    private class CreateChattingStompHandler implements StompFrameHandler {

        @Override
        public Type getPayloadType(StompHeaders headers) {
            System.out.println(headers.toString());
            return ChattingMessage.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            System.out.println(payload);
            completableFuture.complete((ChattingMessage)payload);
        }
    }
}
*/