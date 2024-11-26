package net.project.ecommerce.dependency.api.dto.user.request;


import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.api.dto.user.AddressDTO;
import net.project.ecommerce.dependency.api.dto.user.PaymentMethodDTO;
import net.project.ecommerce.dependency.api.dto.user.ProfileDTO;
import net.project.ecommerce.dependency.api.enums.EnumRoleType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {
			
	private Long id;
	@NotBlank(message = "Username is required")
	private String username;
	//@NotBlank(message = "Password is required")
	private String password;
	@NotBlank(message = "First name is required")
	private String firstName;
	@NotBlank(message = "Last name is required")
	private String lastName;	
	@Valid
	@NotNull(message = "Profile information is needed")
	private ProfileDTO profile;	
	private List<AddressDTO> address;
	private List<PaymentMethodDTO> paymentMethods;
	private EnumRoleType role;
	private String status;
	private String refreshToken;
	private boolean byAdmin;

}
