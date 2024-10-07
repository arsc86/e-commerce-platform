package net.project.ecommerce.dependency.api.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecoveryPasswordRequestDTO {
	
	@NotBlank(message = "Username is required")
	private String username;	

}
