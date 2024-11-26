package net.project.ecommerce.dependency.api.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateEmailRequestDTO {
	
	@NotBlank(message = "Email is required")
	@Email(message = "The email format is invalid")
	private String email;	
	private String username;

}
