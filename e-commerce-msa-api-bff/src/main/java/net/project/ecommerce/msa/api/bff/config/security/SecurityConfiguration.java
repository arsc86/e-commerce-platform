package net.project.ecommerce.msa.api.bff.config.security;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class SecurityConfiguration {
		
	@Value("#{${security.exception.uri:}}")
	private Map<String,String> authorizedUris;		
	
	@Value("${security.cors.origin:}")
	private List<String> origins;

}
