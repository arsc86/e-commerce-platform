package net.project.ecommerce.msa.api.bff.config.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import net.project.ecommerce.dependency.interfaces.ITokenGenerate;
import net.project.ecommerce.dependency.util.Utils;
import net.project.ecommerce.msa.api.bff.dto.UserDTO;
import net.project.ecommerce.msa.api.bff.util.EcommerceUtils;

@Service
public class AuthorizationService {
	
	@Autowired
	private ITokenGenerate tokenGenerate;
	
	@Autowired
	private EcommerceUtils utils;
		
	@SuppressWarnings("unchecked")
	public UserDTO authorize(HttpServletRequest request) throws Exception
	{		
		String refreshToken = Utils.getTokenByHttpRequest(request,"refreshToken");
		if(refreshToken==null || refreshToken.isEmpty())
		{
			throw new Exception("You must be logged in to access");
		}
		
		final String authorizationHeader = request.getHeader("Authorization");

        String username    = null;	   
        String accessToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) 
        {
        	String[] token = authorizationHeader.split("Bearer ");
        	accessToken    = tokenGenerate.decodeCypherToken(token[1]);
            tokenGenerate.validateToken(token[1]);
            username = tokenGenerate.getSubject(accessToken);           
        }         
        
        //Getting claim roles
        Map<String,Object> claimMap = utils.getTokenClaim(accessToken, "roles");
       
        List<String> roles = claimMap!=null?(List<String>) claimMap.get("roles"):null;

        Collection<GrantedAuthority> authorities = roles!=null?
        		roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList()):
                null; 
               
        UserDTO user = UserDTO.builder()
        		.username(username)
        		.password(null)
        		.roles(authorities)
        		.build();
        
        
        return user;
	}

}
