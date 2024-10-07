package net.project.ecommerce.dependency.kafka.configuration;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.project.ecommerce.dependency.dto.request.GenericRequestDTO;

public class KafkaRequestDeserializer implements Deserializer<GenericRequestDTO<?>> {
    Logger log = LogManager.getLogger(this.getClass());

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Do nothing because of X and Y.
    }

    @Override
    public void close() {
        // Do nothing because of X and Y.
    }

    @Override
    public GenericRequestDTO<?> deserialize(String topic, byte[] data) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        GenericRequestDTO<?> request = null;
        try {
            request = mapper.readValue(data, new TypeReference<GenericRequestDTO<?>>() {});
        } catch (Exception e) {
            log.error("Error deserializing KafkaRequest: {}", e.getLocalizedMessage());
        }
        return request;
    }
}

