package com.zero;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.internals.Sender;
import org.apache.kafka.streams.StreamsConfig;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasPartition;

public class KafkaTest {

        private static final String TEMPLATE_TOPIC = "templateTopic";

        @ClassRule
        public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, TEMPLATE_TOPIC);

        @Test
        public void testTemplate() throws Exception {
                Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testT", "false",
                        embeddedKafka.getEmbeddedKafka());
                DefaultKafkaConsumerFactory<Integer, String> cf =
                        new DefaultKafkaConsumerFactory<Integer, String>(consumerProps);
                ContainerProperties containerProperties = new ContainerProperties(TEMPLATE_TOPIC);
                KafkaMessageListenerContainer<Integer, String> container =
                        new KafkaMessageListenerContainer<>(cf, containerProperties);
                final BlockingQueue<ConsumerRecord<Integer, String>> records = new LinkedBlockingQueue<>();
                container.setupMessageListener(new MessageListener<Integer, String>() {

                        @Override
                        public void onMessage(ConsumerRecord<Integer, String> record) {
                                System.out.println(record);
                                records.add(record);
                        }

                });
                container.setBeanName("templateTests");
                container.start();
                ContainerTestUtils.waitForAssignment(container,
                        embeddedKafka.getEmbeddedKafka().getPartitionsPerTopic());
                Map<String, Object> producerProps =
                        KafkaTestUtils.producerProps(embeddedKafka.getEmbeddedKafka());
                ProducerFactory<Integer, String> pf =
                        new DefaultKafkaProducerFactory<Integer, String>(producerProps);
                KafkaTemplate<Integer, String> template = new KafkaTemplate<>(pf);
                template.setDefaultTopic(TEMPLATE_TOPIC);
                template.sendDefault("foo");

        }

}
/*
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        topics = {"test"})
public class KafkaTest {

    private static final Logger logger = LoggerFactory.getLogger(KafkaTest.class);
    private static final String TOPIC_NAME = "ChattingMessage";
    @Autowired
    private KafkaMessageProducerService kafkaMessageProducerService;

    @Autowired
    EmbeddedKafkaBr

    private KafkaMessageListenerContainer<String, ChattingMessage> container;
    private BlockingQueue<ConsumerRecord<String, String>> consumerRecords;

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, TOPIC_NAME);

    @Before
    public void setup() {
        consumerRecords = new LinkedBlockingDeque<>();
        ContainerProperties containerProperties = new ContainerProperties(TOPIC_NAME);
        Map<String, Object> consumerProperties =
                KafkaTestUtils.consumerProps("sender", "false", embeddedKafka.getEmbeddedKafka());
        DefaultKafkaConsumerFactory<String, ChattingMessage> consumer =
                new DefaultKafkaConsumerFactory<>(consumerProperties);
        container = new KafkaMessageListenerContainer<>(consumer, containerProperties);
        container.setupMessageListener((MessageListener<String, String>) record -> {
            logger.debug("Listened message='{}'", record.toString());
            consumerRecords.add(record);
        });
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafka.getEmbeddedKafka().getPartitionsPerTopic());
    }

    @After
    public void tearDown() {
        container.stop();
    }

    @Test
    public void TestChattingEvent() throws InterruptedException, IOException {
        ChattingMessage chattingMessage =
                new ChattingMessage(ChattingMessage.MessageType.ENTER, "1234", "linus", "hello world!");
        kafkaMessageProducerService.send(chattingMessage);
        ConsumerRecord<String, String> received = consumerRecords.poll(10, TimeUnit.SECONDS);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(chattingMessage);
        assertThat(received, hasValue(json));
        assertThat(received).has(key(null));
    }
}

*/