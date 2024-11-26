package net.project.ecommerce.dependency.api.dto.user.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.api.dto.user.RoleDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponseDTO {
	
	private String username;
	private String password;
	private boolean isPasswordExpired;
	private List<RoleDTO> role;

}
