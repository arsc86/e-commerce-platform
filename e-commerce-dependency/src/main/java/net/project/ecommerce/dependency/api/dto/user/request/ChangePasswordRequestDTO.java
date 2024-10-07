package net.project.ecommerce.dependency.api.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequestDTO {
	
	@NotBlank(message = "New password is required")
	private String newPassword;
	@NotBlank(message = "Old password is required")
	private String oldPassword;
	private String token;	

}
