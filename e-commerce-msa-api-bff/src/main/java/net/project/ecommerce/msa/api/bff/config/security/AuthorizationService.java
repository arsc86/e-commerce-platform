package net.project.ecommerce.msa.api.bff.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import net.project.ecommerce.dependency.interfaces.ITokenGenerate;
import net.project.ecommerce.dependency.util.Utils;

@Service
public class AuthorizationService {
	
	@Autowired
	private ITokenGenerate tokenGenerate;
		
	public String authorize(HttpServletRequest request) throws Exception
	{		
		String refreshToken = Utils.getTokenByHttpRequest(request,"refreshToken");
		if(refreshToken==null || refreshToken.isEmpty())
		{
			throw new Exception("You must be logged in to access");
		}
		
		final String authorizationHeader = request.getHeader("Authorization");

        String username = null;	        

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) 
        {
        	String[] token = authorizationHeader.split("Bearer ");
            tokenGenerate.validateToken(token[1]);
            username = tokenGenerate.getSubject(tokenGenerate.decodeCypherToken(token[1]));           
        }              
        
        return username;
	}

}
