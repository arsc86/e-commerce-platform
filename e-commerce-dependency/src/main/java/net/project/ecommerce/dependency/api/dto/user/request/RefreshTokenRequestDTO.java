package net.project.ecommerce.dependency.api.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenRequestDTO {
	
	@NotBlank(message = "Token is required")
	private String refreshToken;

}
