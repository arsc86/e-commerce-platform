package net.project.ecommerce.msa.catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import net.project.ecommerce.msa.catalog.config.GrpcServerConfiguration;

@SpringBootApplication
@ComponentScan({ "net.project" })
public class ECommerceMsaCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceMsaCatalogApplication.class, args)
		.getBean(ECommerceMsaCatalogApplication.class)
        .runGrpcServer();
	}
	
	@Autowired
   	private GrpcServerConfiguration grpcServerConfiguration;
	
	public void runGrpcServer() {    
     	grpcServerConfiguration.start();
     	grpcServerConfiguration.blockUntilShutdown();
    }
}
