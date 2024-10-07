package net.project.ecommerce.dependency.api.dto.user.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.api.dto.user.AddressDTO;
import net.project.ecommerce.dependency.api.dto.user.PaymentMethodDTO;
import net.project.ecommerce.dependency.api.dto.user.ProfileDTO;
import net.project.ecommerce.dependency.api.dto.user.RoleDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
	
	private Long id;
	private String username;
	private String firstName;
	private String lastName;
	private String status;	
	private List<RoleDTO> roles;
	private ProfileDTO profile;	
	private List<AddressDTO> addresses;
	private List<PaymentMethodDTO> paymentMethods;

}
