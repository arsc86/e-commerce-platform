package net.project.ecommerce.msa.user.config;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;

@Component
public class HealthIndicatorConfig extends AbstractHealthIndicator{
	
	Logger log = LogManager.getLogger(this.getClass());
	
	private static final double MEMORY_THRESHOLD = 0.8;
	
	@Override
	protected void doHealthCheck(Builder builder) throws Exception {
		log.info("Evaluating microservice health, date {}", new Date());
		isMemoryUsageHealthy(builder);
	}

	private void isMemoryUsageHealthy(Builder builder) {  
		log.info("Validating memory");
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;        
        double usedMemoryPercentage = (double) usedMemory / totalMemory;
        log.info("Used memory percentage : {}",usedMemoryPercentage);
        if(usedMemoryPercentage < MEMORY_THRESHOLD)
        {
        	builder.up().withDetail("Memory", "Healthy").build();
        }
        else
        {
        	builder.down().withDetail("Memory", "High usage detected").build();
        }        
    }

	

}
