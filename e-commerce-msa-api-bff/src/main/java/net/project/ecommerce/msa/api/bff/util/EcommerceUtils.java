package net.project.ecommerce.msa.api.bff.util;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import net.project.ecommerce.dependency.api.dto.user.RoleDTO;
import net.project.ecommerce.dependency.api.dto.user.response.UserResponseDTO;
import net.project.ecommerce.dependency.api.enums.EnumTokenType;
import net.project.ecommerce.dependency.dto.request.TokenRequestDTO;
import net.project.ecommerce.dependency.interfaces.ITokenGenerate;

@Service
public class EcommerceUtils {
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private ITokenGenerate generate;
	
	public String generateToken(UserDetails detailService, Long expirationTime, EnumTokenType tokenType) throws Exception{		
		return generate.generateToken(TokenRequestDTO.builder()
				.creationUser(detailService.getUsername())
				.issuer(detailService.getUsername())				
				.subject(detailService.getUsername())	
				.claims(tokenType.equals(EnumTokenType.ACCESS_TOKEN)? 
						Map.of("roles", detailService.getAuthorities().stream()
						.map(GrantedAuthority::getAuthority)
						.collect(Collectors.toList()))
						:null
					    )
				.timeExpToken(expirationTime)
				.build()).getToken();
	}
	
	public String generateToken(UserResponseDTO detailService, Long expirationTime, EnumTokenType tokenType) throws Exception{		
		return generate.generateToken(TokenRequestDTO.builder()
				.creationUser(detailService.getUsername())
				.issuer(detailService.getUsername())				
				.subject(detailService.getUsername())	
				.claims(tokenType.equals(EnumTokenType.ACCESS_TOKEN)? 
						Map.of("roles", detailService.getRoles().stream()
						.map(RoleDTO::getName)
						.collect(Collectors.toList()))
						:null
					   )
				.timeExpToken(expirationTime)
				.build()).getToken();
	}		
	
	public void validateToken(String token) throws Exception{		
		generate.validateToken(token);		
	}
	
	public String getValidenTokenUser(String token) throws Exception{			
		validateToken(getJwtToken(token));
		return getTokenSubject(token);
	}
	
	public String getTokenSubject(String token) throws Exception{		
		return generate.getSubject(generate.decodeCypherToken(getJwtToken(token)));	
	}	
	
	private String getJwtToken(String cypherToken)
	{
		if (cypherToken != null && cypherToken.startsWith("Bearer ")) 
        {        	
			cypherToken = cypherToken.split("Bearer ")[1];        	           
        }
		
		return cypherToken;
	}

}
