package net.project.ecommerce.dependency.kafka.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.scheduling.annotation.EnableAsync;

import net.project.ecommerce.dependency.dto.response.GenericResponseDTO;

@Configuration
@EnableKafka
@EnableAsync
public class KafkaConsumerConfig {
	
    @Value("${spring.kafka.bootstrap-servers:localhost}")
    private String bootstrapAddress;

    @Value("${kafka.request.max.byte:26214400}")
    private Integer maxByteRequestKafka;

    @Value("${kafka.request.max.poll.records:500}")
    private Integer maxPollRecordKafka;

    @Value("${kafka.request.max.interval.ms.config:900000}")
    private Integer maxIntervalMsKafka;

    private Map<String, Object> defaultConfig() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaRequestDeserializer.class);
        configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaRequestDeserializer.class);
        configMap.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxByteRequestKafka);        
        configMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecordKafka);
        configMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxIntervalMsKafka);
        return configMap;
    }

    @Bean
    <T> ConcurrentKafkaListenerContainerFactory<String, GenericResponseDTO<T>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, GenericResponseDTO<T>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(defaultConfig()));
        return factory;
    }

    @Bean
    KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

}
