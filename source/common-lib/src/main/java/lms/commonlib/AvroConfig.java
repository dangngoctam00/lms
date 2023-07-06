package lms.commonlib;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

import static lms.commonlib.KafkaClient.KAFKA_PRODUCER;

@Configuration
@EnableKafka
@ComponentScan
@PropertySource("classpath:kafka.properties")
@RequiredArgsConstructor
@EnableAutoConfiguration
public class AvroConfig {
    
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    
    @Value("${spring.kafka.producer.max-request-size}")
    private String maxRequestSize;
    
    @Bean
    public Map<String, Object> kafkaProducerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, maxRequestSize);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroSerializer.class);
        return props;
    }
    
    @Bean
    public Map<String, Object> kafkaConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, AvroDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "lms");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
        return props;
    }
    
    @Bean
    public ProducerFactory<String, SpecificRecordBase> producerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProducerConfigs());
    }
    
    @Bean(name = KAFKA_PRODUCER)
    public KafkaTemplate<String, SpecificRecordBase> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    
    public <T extends SpecificRecordBase> ConsumerFactory<String, T> consumerFactory(Class<T> clazz) {
        return new DefaultKafkaConsumerFactory<>(kafkaConsumerConfigs(), new StringDeserializer(),
                                                 new AvroDeserializer<>(clazz));
    }
    
    public <T extends SpecificRecordBase> ConcurrentKafkaListenerContainerFactory<String, T> kafkaListenerContainerFactory(Class<T> clazz) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(clazz));
        return factory;
    }
}
