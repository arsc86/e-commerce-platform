package net.project.ecommerce.dependency.api.dto.user.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.api.dto.user.RoleDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationTokenResponseDTO {
	
	@JsonIgnore
	private String accessToken;	
	@JsonIgnore
	private String refreshToken;
	private Boolean _exp;
	private Boolean _val;	
	private LoginUserData _dat;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class LoginUserData{
		private String username;
		private String firstName;
		private String lastName; 
		private List<RoleDTO> roles;
	}

}
