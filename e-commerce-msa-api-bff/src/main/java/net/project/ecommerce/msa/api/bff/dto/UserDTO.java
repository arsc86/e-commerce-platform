package net.project.ecommerce.msa.api.bff.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
	
	private String username;
	private String password;
	private Collection<GrantedAuthority> roles;

}
