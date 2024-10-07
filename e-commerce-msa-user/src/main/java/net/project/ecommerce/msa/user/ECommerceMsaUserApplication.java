package net.project.ecommerce.msa.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import net.project.ecommerce.msa.user.config.GrpcServerConfiguration;

@SpringBootApplication
@ComponentScan({ "net.project" })
public class ECommerceMsaUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceMsaUserApplication.class, args)
		.getBean(ECommerceMsaUserApplication.class)
        .runGrpcServer();
	}
	
	@Autowired
   	private GrpcServerConfiguration grpcServerConfiguration;
	
	public void runGrpcServer() {    
     	grpcServerConfiguration.start();
     	grpcServerConfiguration.blockUntilShutdown();
    }
}
