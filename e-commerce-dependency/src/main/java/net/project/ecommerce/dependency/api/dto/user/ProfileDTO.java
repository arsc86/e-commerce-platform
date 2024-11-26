package net.project.ecommerce.dependency.api.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDTO {
	
	@NotBlank(message = "Profile Email information is required")
	@Email(message = "Email should be valid")
	private String email;	
	private String phone;		
	private String birthday;
	private String providerCode;	
	private String imageProfile;	

}
