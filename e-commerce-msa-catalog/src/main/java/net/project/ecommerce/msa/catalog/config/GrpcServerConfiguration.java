package net.project.ecommerce.msa.catalog.config;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import net.project.ecommerce.msa.catalog.communication.grpc.Consumer;


@Component
public class GrpcServerConfiguration {
	
	private Server server;

	@Value("${grpc.server.port}")
	private int port;		
	
	@Value("${grpc.server.keep.alive:60}")
	private int keepLive;
	
	@Value("${grpc.server.timeout.keep.alive:30}")
	private int keepAliveTimeout;
	
	@Autowired
	private Consumer grpcConsumer;

	Logger log = LogManager.getLogger(this.getClass());

	public void start() {    	    	
	    	
    try {
	        server = ServerBuilder.forPort(port)
	        		.addService(grpcConsumer)	        		
	        		.keepAliveTime(keepLive, TimeUnit.SECONDS)
	        		.keepAliveTimeout(keepAliveTimeout, TimeUnit.SECONDS)
	        		.permitKeepAliveWithoutCalls(true)
	                .build()
	                .start();
	
	        log.info("gRPC server started on port: {}",port);           
	
	        Runtime.getRuntime().addShutdownHook(new Thread(() -> {                
	            log.info("Shutting down gRPC server");
	            stop();
	        }));
	    } catch (IOException e) {
	    	log.info("error en GrpcServerConfiguration");
	    }
	}
	public void stop() {
	    if (server != null) {
	        server.shutdown();
	    }
	}
	public void blockUntilShutdown() {
	    try 
	    {
	        if (server != null) {
	            server.awaitTermination();
	        }
	    }
	    catch (InterruptedException e) 
	    {
	    	 log.error("gRPC server started ERROR: {}",e.getMessage()); 
	    	 Thread.currentThread().interrupt();
	    }
	}
}
