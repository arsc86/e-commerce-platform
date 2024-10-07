package net.project.ecommerce.dependency.grpc;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;

@Configuration
public class GrpcChannelManagerConfiguration {
	
	Logger log = LogManager.getLogger(this.getClass());		
	
	@Value("#{${grpc.server.map:{}}}")
	private Map<String, String> grpcServerMap;
	
	@Value("${grpc.client.timeout.idle:5}")
	private int grpcIdleTimeout;
		
	@Value("${grpc.client.inbound.message.size:5242880}")
    private int inboundMessageSize;
	
	@Value("${grpc.client.keep.alive:60}")
	private int keepLive;
	
	@Value("${grpc.client.timeout.keep.alive:5}")
	private int keepAliveTimeout;
	
	@Autowired
	private GrpcChannelManagerMap channelManager;
	
	@PostConstruct
	public void initGrpcConnection() {			
		if(grpcServerMap!=null) 
		{
			log.info("Connecting to configurated gRPC Servers : {}",grpcServerMap);	
			ManagedChannel channel = null;
			ConnectivityState currentState;
			for (Map.Entry<String, String> entry : grpcServerMap.entrySet()) 
			{
				String serverClient = entry.getValue();
				log.info("Openning gRPC channel for : {}",serverClient);	
				channel =  ManagedChannelBuilder.forTarget(serverClient)
						.usePlaintext()
						.maxInboundMessageSize(inboundMessageSize)				
						.idleTimeout(grpcIdleTimeout, TimeUnit.MINUTES)		
						.keepAliveTime(keepLive, TimeUnit.SECONDS)
						.keepAliveTimeout(keepAliveTimeout, TimeUnit.SECONDS)						
						.build();										
				currentState = channel.getState(false);
				log.info("gRPC Channel opened for : {}, with connectivity State : {}",serverClient,currentState);
				channelManager.setManagedChannel(serverClient, channel);
			}
		}
		
	}

}
