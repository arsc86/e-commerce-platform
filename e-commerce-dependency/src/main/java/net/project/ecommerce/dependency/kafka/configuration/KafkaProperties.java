package net.project.ecommerce.dependency.kafka.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 *  Properties Kafka Class
 *  @author Allan Suarez
 */
@Data
@Component
@ConfigurationProperties(prefix = "kafka.ms", ignoreUnknownFields = false, ignoreInvalidFields = true)
public class KafkaProperties {

	private Logger log = LogManager.getLogger(this.getClass());
	
	private String topicGroup;
	private Collection<String> topic = new ArrayList<>();

	public KafkaProperties setProperties(String group, String topic) {
		try {
			setTopicGroup(group);
			log.info("Kafka Group configured : {}", getTopicGroup());
			Collection<String> colKafkaTopicAync = Collections.singletonList(topic);
			setTopic(colKafkaTopicAync);
			log.info("Topic kafka configured: {}", getTopic()::toString);
			return this;
		}catch (Exception e)
		{
			log.error("Error configuration Kafka properties : {}",e.getMessage());
			return null;
		}
	}
}
