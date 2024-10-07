package net.project.ecommerce.msa.api.bff.config.security;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.project.ecommerce.dependency.dto.response.GenericResponseDTO;

public class AuthorizationFilter extends OncePerRequestFilter {
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private SecurityConfiguration securityConfiguration;
	
	@Autowired
	private AuthorizationService authorizationService;
	
	@Autowired
	private UserDetailService userDetailService;
	
	@Autowired
	private BeanFactory factory;
	
	public AuthorizationFilter(SecurityConfiguration securityConfiguration,
			                   AuthorizationService  authorizationService,
			                   UserDetailService     userDetailService,
			                   BeanFactory           factory
			                  ) 
	{		
		this.securityConfiguration = securityConfiguration;
		this.authorizationService  = authorizationService;
		this.userDetailService     = userDetailService;
		this.factory               = factory;
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException 
	{				
		for (Map.Entry<String, String> path : securityConfiguration.getAuthorizedUris().entrySet()) 
		{			
			if(request.getRequestURI().contains(path.getKey()) && request.getMethod().equals(path.getValue())) 
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
			UserDetails userDetails = userDetailService.loadUserByUsername(authorizationService.authorize(request),factory);
			
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
	                new UsernamePasswordAuthenticationToken(userDetails.getUsername(), 
	                		                                userDetails.getPassword(), 
	                		                                userDetails.getAuthorities()
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
