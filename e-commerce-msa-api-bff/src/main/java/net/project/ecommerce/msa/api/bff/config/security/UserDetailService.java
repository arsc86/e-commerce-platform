package net.project.ecommerce.msa.api.bff.config.security;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import net.project.ecommerce.dependency.api.constants.EcommerceConstants;
import net.project.ecommerce.dependency.api.constants.user.UserMethodConstants;
import net.project.ecommerce.dependency.api.dto.user.RoleDTO;
import net.project.ecommerce.dependency.api.dto.user.request.UserRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.response.AuthenticationResponseDTO;
import net.project.ecommerce.dependency.constants.GeneralConstants;
import net.project.ecommerce.dependency.dto.response.GenericResponseDTO;
import net.project.ecommerce.dependency.enums.EnumChannelType;
import net.project.ecommerce.dependency.interfaces.IGenericProducer;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.dependency.vo.GenericProducerVO;

@Service
public class UserDetailService implements UserDetailsService {
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private BeanFactory factory;
	
	private IGenericProducer producer; 
	
	public UserDetails loadUserByUsername(String username, BeanFactory factory) throws UsernameNotFoundException {
		this.factory      = factory;		
		return loadUserByUsername(username);
	}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	    	
    	producer = (IGenericProducer) factory.getBean(GeneralConstants.MESSAGE_PRODUCER);
    	
		try 
		{
			log.info("Getting user detail: {{}}",username);
			GenericResponseDTO<?> response = producer.process(GenericProducerVO.<UserRequestDTO>builder()
									                .transactionId(UUID.randomUUID().toString())
									                .channelType(EnumChannelType.GRPC)                
									                .option(UserMethodConstants.AUTHENTICATE_METHOD)          
									                .service(EcommerceConstants.USER_MESSAGE_PRODUCER)
									                .context(EcommerceConstants.CONTEXT_USER)
									                .payload(UserRequestDTO.builder()
									            			.username(username)	
									            			.build())
									                .build());
			
			AuthenticationResponseDTO authResponse = Format.objectMapping(response.getPayload(), AuthenticationResponseDTO.class);
						
			String[] roles = null;
			
			if(authResponse.getRole()!=null && !authResponse.getRole().isEmpty())
			{
				roles = authResponse.getRole()
	                    .stream()
	                    .map(RoleDTO::getName)
	                    .toArray(String[]::new); 
			}
						
			return User
	                .withUsername(username!=null?username:authResponse.getUsername())	               
	                .password(authResponse.getPassword())
	                .roles(roles)    
	                .accountExpired(authResponse.isPasswordExpired())
	                .build();   
		} 
		catch (Exception e) 
		{
			log.error("Error loading username for login : {}",e.getMessage());
			throw new UsernameNotFoundException(e.getMessage());
		}  		                          
    }
}
