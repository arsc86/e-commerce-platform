package net.project.ecommerce.msa.api.bff.handler.impl.user;

import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import net.project.ecommerce.dependency.api.constants.EcommerceConstants;
import net.project.ecommerce.dependency.api.constants.user.UserMethodConstants;
import net.project.ecommerce.dependency.api.dto.user.CredentialDTO;
import net.project.ecommerce.dependency.api.dto.user.request.AuthenticationRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.request.ChangePasswordRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.request.RecoveryPasswordRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.request.RefreshTokenRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.request.UserRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.response.AuthenticationTokenResponseDTO;
import net.project.ecommerce.dependency.api.dto.user.response.UserResponseDTO;
import net.project.ecommerce.dependency.api.enums.EnumTokenType;
import net.project.ecommerce.dependency.api.interfaces.ICrudHandler;
import net.project.ecommerce.dependency.constants.GeneralConstants;
import net.project.ecommerce.dependency.dto.response.GenericResponseDTO;
import net.project.ecommerce.dependency.enums.EnumChannelType;
import net.project.ecommerce.dependency.interfaces.IGenericProducer;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.dependency.util.Utils;
import net.project.ecommerce.dependency.vo.GenericProducerVO;
import net.project.ecommerce.msa.api.bff.cons.ApiBffConstants;
import net.project.ecommerce.msa.api.bff.enums.EnumChangePasswordType;
import net.project.ecommerce.msa.api.bff.util.EcommerceUtils;

@Service(ApiBffConstants.USER_CRUD_BEAN)
@Primary
public class UserHandlerImpl implements IUserHandler,ICrudHandler{
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Value("${security.token.recovery.password.expiration.time:}")
	private Long recoveryExpirationTime;
	
	@Value("${security.token.expiration.time:}")
	private Long accessTokenExpirationTime;
	
	@Value("${security.token.refresh.expiration.time:}")
	private Long refreshTokenExpirationTime;
	
	@Autowired
	private EcommerceUtils utils;
	
	@Autowired
	private BeanFactory factory;
	
	private IGenericProducer producer; 
	
	@Autowired
	private PasswordEncoder passwordEncoder;
		
	@Override
	public <R, T> R authenticate(T data) throws Exception {
		AuthenticationRequestDTO user = Format.objectMapping(data, AuthenticationRequestDTO.class);			
		String refreshToken     = null;
		String accessToken      = null;
		try
		{					
			Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));			
			UserDetails userDetal = (UserDetails) auth.getPrincipal();
			refreshToken = utils.generateToken(userDetal, refreshTokenExpirationTime, EnumTokenType.REFRESH_TOKEN);
			accessToken  = utils.generateToken(userDetal,accessTokenExpirationTime, EnumTokenType.ACCESS_TOKEN);
			
			return getAuthenticationToken(UserRequestDTO.builder().username(user.getUsername()).build(), accessToken, refreshToken, true);
		}
		catch(Exception e)
		{
			log.error("Error in authentication : {}",e.getMessage());
			
			if(e.getClass().getCanonicalName().contains(AccountExpiredException.class.getCanonicalName()))
			{
				//getting user
				producer = (IGenericProducer) factory.getBean(GeneralConstants.MESSAGE_PRODUCER);
				GenericResponseDTO<?> response = producer.process(GenericProducerVO.<UserRequestDTO>builder()
		                .transactionId(UUID.randomUUID().toString())
		                .channelType(EnumChannelType.GRPC)                
		                .option(UserMethodConstants.USER_RETRIEVE_METHOD)          
		                .service(EcommerceConstants.USER_MESSAGE_PRODUCER)
		                .payload(UserRequestDTO.builder().username(user.getUsername()).build())
		                .build()); 
		        UserResponseDTO userResponse = Format.listMapping(response.getPayload(), UserResponseDTO.class).get(0);
		        
		        refreshToken = utils.generateToken(userResponse,refreshTokenExpirationTime, EnumTokenType.REFRESH_TOKEN);
		        accessToken  = utils.generateToken(userResponse,accessTokenExpirationTime, EnumTokenType.ACCESS_TOKEN);
		        		       
		        return getAuthenticationToken(UserRequestDTO.builder().username(user.getUsername()).build(),accessToken,refreshToken,false);
			}
			
			throw new Exception(e.getMessage());	       
		}					
	}
	
	@SuppressWarnings("unchecked")
	private <R,T> R getAuthenticationToken(UserRequestDTO user, String accessToken, String refreshToken, boolean isValidAccount) 
			throws Exception
	{
		//Save refresh token
		producer = (IGenericProducer) factory.getBean(GeneralConstants.MESSAGE_PRODUCER);
		GenericResponseDTO<?> response =producer.process(GenericProducerVO.<UserRequestDTO>builder()
                .transactionId(UUID.randomUUID().toString())
                .channelType(EnumChannelType.GRPC)                
                .option(UserMethodConstants.UPDATE_REFRESH_TOKEN_METHOD)          
                .service(EcommerceConstants.USER_MESSAGE_PRODUCER)
                .payload(UserRequestDTO.builder()
                		.username(user.getUsername())
                		.refreshToken(refreshToken)
                		.build()
                		)
                .build());  
		
		if(response.getStatus().equalsIgnoreCase(GeneralConstants.FAILURE_STATUS_DEFAULT))
		{
			throw new Exception(response.getMessage());
		}
        
		return (R) AuthenticationTokenResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isValidAccount(isValidAccount)
                .build();	
	}
	
	@Override
	public <R, T> R create(T data) throws Exception {		
		UserRequestDTO request = Format.objectMapping(data, UserRequestDTO.class);
		request.setPassword(passwordEncoder.encode(request.getPassword()));
		producer = (IGenericProducer) factory.getBean(GeneralConstants.MESSAGE_PRODUCER);
        return producer.process(GenericProducerVO.<UserRequestDTO>builder()
                .transactionId(UUID.randomUUID().toString())
                .channelType(EnumChannelType.GRPC)                
                .option(UserMethodConstants.USER_CREATE_METHOD)          
                .service(EcommerceConstants.USER_MESSAGE_PRODUCER)
                .payload(request)
                .build());       
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R, T> R update(T data) throws Exception {
		UserRequestDTO user = Format.objectMapping(data, UserRequestDTO.class);		
		producer = (IGenericProducer) factory.getBean(GeneralConstants.MESSAGE_PRODUCER);
		GenericResponseDTO<?> response =  producer.process(GenericProducerVO.<UserRequestDTO>builder()
                .transactionId(UUID.randomUUID().toString())
                .channelType(EnumChannelType.GRPC)                
                .option(UserMethodConstants.USER_UPDATE_METHOD)          
                .service(EcommerceConstants.USER_MESSAGE_PRODUCER)
                .payload(user)
                .build());  
		return (R) response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R, T> R retrieve(T data) throws Exception {
		UserRequestDTO request = Format.objectMapping(data, UserRequestDTO.class);
		 producer = (IGenericProducer) factory.getBean(GeneralConstants.MESSAGE_PRODUCER);
        GenericResponseDTO<?> response = producer.process(GenericProducerVO.<UserRequestDTO>builder()
                .transactionId(UUID.randomUUID().toString())
                .channelType(EnumChannelType.GRPC)                
                .option(UserMethodConstants.USER_RETRIEVE_METHOD)          
                .service(EcommerceConstants.USER_MESSAGE_PRODUCER)
                .payload(request)
                .build());
        return (R) response.getPayload();
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public <R, T> R recoveryPassword(T data) throws Exception {
		// Get de user from the ms-user		
		RecoveryPasswordRequestDTO user = Format.objectMapping(data, RecoveryPasswordRequestDTO.class);
		producer = (IGenericProducer) factory.getBean(GeneralConstants.MESSAGE_PRODUCER);
		GenericResponseDTO<List<UserResponseDTO>> response = producer.process(GenericProducerVO.<UserRequestDTO>builder()
                .transactionId(UUID.randomUUID().toString())
                .channelType(EnumChannelType.GRPC)                
                .option(UserMethodConstants.USER_RETRIEVE_METHOD)          
                .service(EcommerceConstants.USER_MESSAGE_PRODUCER)
                .payload(UserRequestDTO.builder().username(user.getUsername()).build())
                .build());
		
		if(response.getPayload().size()==0)
		{
			throw new Exception("The username doesn't exists");
		}
		//Getting the user
		UserResponseDTO userResponse = Format.listMapping(response.getPayload(), UserResponseDTO.class).get(0);
		if(userResponse!=null)
		{
			//Generate validation token and to user mail
			String validationToken = utils.generateToken(userResponse,recoveryExpirationTime,EnumTokenType.ACCESS_TOKEN);
			log.info(validationToken);
			String message = "Recovery mail was sent, please follow the steps to recover your password";
			return (R) message;
		}
		return (R) ((R) GeneralConstants.FAILURE_MESSAGE_DEFAULT+" , please try again");
	}


	@SuppressWarnings("unchecked")
	@Override
	public <R, T> R changePassword(T data,EnumChangePasswordType type,HttpServletRequest request) throws Exception {
		try
		{
			log.info("Changing password");
			ChangePasswordRequestDTO changePassword = Format.objectMapping(data, ChangePasswordRequestDTO.class);
			if(changePassword.getNewPassword().equalsIgnoreCase(changePassword.getOldPassword()))
			{
				throw new Exception("The new password must be different from the old one");
			}
			String token = type.equals(EnumChangePasswordType.NON_SESSION)?changePassword.getToken():request.getHeader("Authorization");
			if(token.isEmpty())
			{
				throw new Exception("You must send token to change password");
			}
			//Validate token and return token subject [user]
			String user  = utils.getValidenTokenUser(token);			
			
			if(user != null)
			{
				//getting last passwords
				producer = (IGenericProducer) factory.getBean(GeneralConstants.MESSAGE_PRODUCER);
		        GenericResponseDTO<?> response = producer.process(GenericProducerVO.<UserRequestDTO>builder()
		                .transactionId(UUID.randomUUID().toString())
		                .channelType(EnumChannelType.GRPC)                
		                .option(UserMethodConstants.LAST_PASSORD_METHOD)          
		                .service(EcommerceConstants.USER_MESSAGE_PRODUCER)
		                .payload(UserRequestDTO.builder().username(user).build())
		                .build());
		        
		        List<CredentialDTO> credentialList = Format.listMapping(response.getPayload(), CredentialDTO.class);
		        
		        log.info("Validating password historial");
		        for(CredentialDTO credential : credentialList)
		        {
		        	if(passwordEncoder.matches(changePassword.getNewPassword(), credential.getPassword()))
		        	{
		        		throw new Exception("The password must be different from the existing ones");
		        	}
		        }
		        
		        //updating password
		        producer.process(GenericProducerVO.<UserRequestDTO>builder()
		                .transactionId(UUID.randomUUID().toString())
		                .channelType(EnumChannelType.GRPC)                
		                .option(UserMethodConstants.UPDATE_PASSWORD_METHOD)          
		                .service(EcommerceConstants.USER_MESSAGE_PRODUCER)
		                .payload(UserRequestDTO.builder()
		                		.username(user)
		                		.password(passwordEncoder.encode(changePassword.getNewPassword()))
		                		.build())
		                .build());	
		        
		        //deleting refreshtoken
		        producer = (IGenericProducer) factory.getBean(GeneralConstants.MESSAGE_PRODUCER);
				producer.process(GenericProducerVO.<UserRequestDTO>builder()
		                .transactionId(UUID.randomUUID().toString())
		                .channelType(EnumChannelType.GRPC)                
		                .option(UserMethodConstants.UPDATE_REFRESH_TOKEN_METHOD)          
		                .service(EcommerceConstants.USER_MESSAGE_PRODUCER)
		                .payload(UserRequestDTO.builder().username(user).build())
		                .build());
			}
			else
			{
				throw new Exception("An error had ocurred changing the user password, problem getting user");
			}
		}
		catch(Exception e)
		{
			log.error("Error changing user password : {}",e.getMessage());
			throw new Exception(e.getMessage());
		}
				
		return (R) "Password was changed successfully";
	}


	@Override
	public <T> void validateRecoveryToken(T data) throws Exception {
		log.info("Validating recovery Token");
		utils.validateToken(data.toString());			
	}
	
	@Override
	public <R, T> R delete(T data) throws Exception {
		UserRequestDTO user = Format.objectMapping(data, UserRequestDTO.class);		
		producer = (IGenericProducer) factory.getBean(GeneralConstants.MESSAGE_PRODUCER);
		return  producer.process(GenericProducerVO.<UserRequestDTO>builder()
                .transactionId(UUID.randomUUID().toString())
                .channelType(EnumChannelType.GRPC)                
                .option(UserMethodConstants.USER_DELETE_METHOD)          
                .service(EcommerceConstants.USER_MESSAGE_PRODUCER)
                .payload(user)
                .build()); 
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R, T> R refreshToken(HttpServletRequest request) throws Exception {
		log.info("Refreshing Access Token");		
		String refreshToken = Utils.getTokenByHttpRequest(request,"refreshToken");
		if(refreshToken==null || refreshToken.isEmpty())
		{
			throw new Exception("Refresh token doesn't exists, please go to login");
		}
		
		utils.validateToken(refreshToken);
		String accessToken = request.getHeader("Authorization");
		if(accessToken==null)
		{
			throw new Exception("You have to be logged in first to get a new access token");
		}
		String username         = utils.getTokenSubject(accessToken);
		
		//getting refresh token persisted
		producer = (IGenericProducer) factory.getBean(GeneralConstants.MESSAGE_PRODUCER);
		GenericResponseDTO<?> response = producer.process(GenericProducerVO.<UserRequestDTO>builder()
                .transactionId(UUID.randomUUID().toString())
                .channelType(EnumChannelType.GRPC)                
                .option(UserMethodConstants.GET_REFRESH_TOKEN_METHOD)          
                .service(EcommerceConstants.USER_MESSAGE_PRODUCER)
                .payload(UserRequestDTO.builder().username(username).build())
                .build());
		
		RefreshTokenRequestDTO refresh = Format.objectMapping(response.getPayload(), RefreshTokenRequestDTO.class);
		
		//Validating is refreshToken exists in database to compare
		if(refresh.getRefreshToken()==null || refresh.getRefreshToken().isEmpty())
		{
			throw new Exception("Refresh token doesn't exists, please go to login");
		}
		if(!refresh.getRefreshToken().equalsIgnoreCase(refreshToken))
		{
			throw new Exception("Refresh token is invalid, please go to login");
		}
		
		//getting user to generate new access token
		response = producer.process(GenericProducerVO.<UserRequestDTO>builder()
                .transactionId(UUID.randomUUID().toString())
                .channelType(EnumChannelType.GRPC)                
                .option(UserMethodConstants.USER_RETRIEVE_METHOD)          
                .service(EcommerceConstants.USER_MESSAGE_PRODUCER)
                .payload(UserRequestDTO.builder().username(username).build())
                .build());
		
		UserResponseDTO userResponse = Format.listMapping(response.getPayload(), UserResponseDTO.class).get(0);
		
		return (R) AuthenticationTokenResponseDTO.builder()
                .accessToken(utils.generateToken(userResponse, accessTokenExpirationTime, EnumTokenType.ACCESS_TOKEN))
                .refreshToken(refreshToken)
                .isValidAccount(true)
                .build();
	}
	
	@Override
	public void logout(HttpServletRequest request) throws Exception{
		String refreshToken = Utils.getTokenByHttpRequest(request,"refreshToken");
		if(refreshToken==null || refreshToken.isEmpty())
		{
			throw new Exception("You are already logged out");
		}
		String accessToken = request.getHeader("Authorization");
		if(accessToken==null)
		{
			throw new Exception("You are already logged out");
		}
		String username    = utils.getTokenSubject(accessToken);
		
		producer = (IGenericProducer) factory.getBean(GeneralConstants.MESSAGE_PRODUCER);
		producer.process(GenericProducerVO.<UserRequestDTO>builder()
                .transactionId(UUID.randomUUID().toString())
                .channelType(EnumChannelType.GRPC)                
                .option(UserMethodConstants.UPDATE_REFRESH_TOKEN_METHOD)          
                .service(EcommerceConstants.USER_MESSAGE_PRODUCER)
                .payload(UserRequestDTO.builder().username(username).build())
                .build());
	}
}
