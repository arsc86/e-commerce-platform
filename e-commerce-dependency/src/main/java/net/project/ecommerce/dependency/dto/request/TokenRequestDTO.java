package net.project.ecommerce.dependency.dto.request;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRequestDTO {
	
	private String issuer;
	private String creationUser;
	private String subject;
	private Long timeExpToken;
	private Map<String,Object> claims;
}
