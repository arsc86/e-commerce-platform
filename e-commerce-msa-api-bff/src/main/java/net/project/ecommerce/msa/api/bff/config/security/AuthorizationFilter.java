package net.project.ecommerce.msa.api.bff.config.security;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.project.ecommerce.dependency.dto.response.GenericResponseDTO;
import net.project.ecommerce.msa.api.bff.dto.UserDTO;

public class AuthorizationFilter extends OncePerRequestFilter {
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private SecurityConfiguration securityConfiguration;
	
	@Autowired
	private AuthorizationService authorizationService;
		
	public AuthorizationFilter(SecurityConfiguration securityConfiguration,
			                   AuthorizationService  authorizationService			                  
			                  ) 
	{		
		this.securityConfiguration = securityConfiguration;
		this.authorizationService  = authorizationService;		
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException 
	{		
		AntPathMatcher pathMatcher = new AntPathMatcher();
		
		for (Map.Entry<String, String> path : securityConfiguration.getAuthorizedUris().entrySet()) 
		{			
			if(pathMatcher.match(path.getKey(),request.getRequestURI()) && request.getMethod().equals(path.getValue())) 
			{			
				log.info("Authorized path {} method {}",path.getValue(),path.getKey());
				return true;									
			}
		}
		return false;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException 
	{
		GenericResponseDTO<Object> data=new GenericResponseDTO<Object>();
		
		try
		{		
			log.info("Validating authorization to path : {}",request.getRequestURI());
			
			//Validate token
			UserDTO user = authorizationService.authorize(request);

			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(user.getUsername(), 
            										user.getPassword(), 
            		                                user.getRoles()
            		                               );
			
	        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	        
	        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
	        
	        filterChain.doFilter(request, response);
		}
		catch(Exception e)
		{
			log.error("Unauthorized path method : {{}} with error : {}",request.getRequestURI(),e.getMessage());
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			data.setCode(HttpServletResponse.SC_UNAUTHORIZED);
			data.setStatus("ERROR");
			data.setMessage("You must be logged in to access");
			new ObjectMapper().writeValue(response.getOutputStream(), data);
		}				
	}
}
