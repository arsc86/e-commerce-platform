package net.project.ecommerce.dependency.grpc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import io.grpc.ManagedChannel;

@Component
public class GrpcChannelManagerMap {
	
	private final Map<String, ManagedChannel> grpcChannelMap = new ConcurrentHashMap<>();
	
	public void setManagedChannel(String server, ManagedChannel channel) {
		grpcChannelMap.put(server, channel);
	}
	
	public ManagedChannel getMannagedChannel(String server) {
		return grpcChannelMap.get(server);
	}

}
