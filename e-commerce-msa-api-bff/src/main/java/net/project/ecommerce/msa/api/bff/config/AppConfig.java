package net.project.ecommerce.msa.api.bff.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.Data;

@Configuration
@Data
public class AppConfig {
	@Value("#{${grpc.client.server.map:{}}}")
	private Map<String, String> grpcServerMap;
	@Value("${grpc.client.timeout.idle:0}")
	private int grpcIdleTimeout;
	@Value("${grpc.client.timeout.deadline:0}")
	private int grpcDeadlineTimeout;
	@Value("${grpc.client.inbound.message.size:5242880}")
    private int inboundMessageSize;
    @Value("${grpc.client.outbound.message.size:5242880}")
    private int outboundMessageSize;
	@Value("#{${kafka.client.topic.map}}")
	private Map<String, String> KafkaTopicMap;

	@PostConstruct
	private void setDefaultValues() {
		KafkaTopicMap = (KafkaTopicMap == null) ? new HashMap<>() : KafkaTopicMap;
		grpcServerMap = (grpcServerMap == null) ? new HashMap<>() : grpcServerMap;
	}

}
