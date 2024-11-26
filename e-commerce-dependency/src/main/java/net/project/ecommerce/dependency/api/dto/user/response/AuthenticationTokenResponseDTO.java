package net.project.ecommerce.dependency.api.dto.user.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationTokenResponseDTO {
	
	private String accessToken;
	@JsonIgnore
	private String refreshToken;
	private Boolean isValidAccount;

}
