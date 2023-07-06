package lms.commonlib;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@Slf4j
public class KafkaClient {
    
    public static final String KAFKA_PRODUCER = "kafka-producer";
    
    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;
    private final AvroConfig avroConfig;
    
    public KafkaClient(@Qualifier(KAFKA_PRODUCER) KafkaTemplate<String, SpecificRecordBase> kafkaTemplate, AvroConfig avroConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.avroConfig = avroConfig;
    }
    
    public <T extends SpecificRecordBase> void send(String topic, T data) {
        kafkaTemplate.send(topic, data);
    }
    
    public <T extends SpecificRecordBase> void subscribe(String topic, Class<T> expectedEventType, Consumer<SpecificRecordBase> consumer) {
        ConcurrentKafkaListenerContainerFactory<String, T> containerFactory = avroConfig.kafkaListenerContainerFactory(expectedEventType);
        ConcurrentMessageListenerContainer<String, T> container = containerFactory.createContainer(topic);
        container.setupMessageListener((MessageListener<String, T>) event -> {
            log.info("Receive message: {}", event.value());
            consumer.accept(event.value());
        });
        container.start();
        log.info("Subscribe to {} successfully", topic);
    }
}