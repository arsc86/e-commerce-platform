package net.project.ecommerce.dependency.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class SecurityTokenKeysConfig {
	
	@Value("${security.cipher.key:GENERICENCRIPTIONKEY}")
	private String cypherKey;
	
	@Value("${security.token.key:GENERICENCRIPTIONKEY}")
	private String tokenKey;
	
	@Value("${security.token.expiration.time:30}")//MINUTES
	private Long expirationTime;
}
