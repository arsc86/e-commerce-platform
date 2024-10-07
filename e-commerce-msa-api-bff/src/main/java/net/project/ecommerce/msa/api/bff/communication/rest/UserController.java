package net.project.ecommerce.msa.api.bff.communication.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import net.project.ecommerce.dependency.api.dto.user.request.AuthenticationRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.request.ChangePasswordRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.request.RecoveryPasswordRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.request.UserRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.response.AuthenticationTokenResponseDTO;
import net.project.ecommerce.dependency.api.dto.user.response.UserResponseDTO;
import net.project.ecommerce.dependency.api.enums.EnumRoleType;
import net.project.ecommerce.dependency.api.enums.EnumStates;
import net.project.ecommerce.dependency.constants.GeneralConstants;
import net.project.ecommerce.dependency.dto.response.GenericResponseDTO;
import net.project.ecommerce.msa.api.bff.enums.EnumChangePasswordType;
import net.project.ecommerce.msa.api.bff.handler.IUserHandler;

@RestController
@RequestMapping("/user")
public class UserController {
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private IUserHandler userHandler;
	
	@Value("${security.token.refresh.expiration.time:}")
	private int refreshTokenExpirationTime;
	
	@GetMapping("/login")	
	public ResponseEntity<?> login(@Valid @RequestBody AuthenticationRequestDTO authRequest, HttpServletResponse response) throws Exception
	{		
		GenericResponseDTO<AuthenticationTokenResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{	
			AuthenticationTokenResponseDTO authResponse = userHandler.authenticate(authRequest);
			genericResponse.setPayload(authResponse);
			genericResponse.setCode(HttpStatus.ACCEPTED.value());
						
	        Cookie refreshTokenCookie = new Cookie("refreshToken", authResponse.getRefreshToken());	        
	        refreshTokenCookie.setHttpOnly(true); 
	        refreshTokenCookie.setSecure(true);
	        refreshTokenCookie.setPath("/");
	        refreshTokenCookie.setMaxAge(refreshTokenExpirationTime * 60);
	        response.addCookie(refreshTokenCookie);
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.UNAUTHORIZED.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@GetMapping("/logout")	
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) throws Exception
	{		
		GenericResponseDTO<AuthenticationTokenResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{	
			userHandler.logout(request);			
			genericResponse.setCode(HttpStatus.NO_CONTENT.value());
						
	        Cookie refreshTokenCookie = new Cookie("refreshToken",null);	        
	        refreshTokenCookie.setHttpOnly(true); 
	        refreshTokenCookie.setSecure(true);
	        refreshTokenCookie.setPath("/");
	        refreshTokenCookie.setMaxAge(0);
	        response.addCookie(refreshTokenCookie);
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.UNAUTHORIZED.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@GetMapping("/refresh-token")	
	public ResponseEntity<?> refreshToken(HttpServletRequest request) throws Exception
	{		
		GenericResponseDTO<AuthenticationTokenResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{						
			AuthenticationTokenResponseDTO authResponse = userHandler.refreshToken(request);
			genericResponse.setPayload(authResponse);
			genericResponse.setCode(HttpStatus.ACCEPTED.value());			
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@GetMapping("/recovery-password")	
	public ResponseEntity<?> recoveryPassword(@Valid @RequestBody RecoveryPasswordRequestDTO request) throws Exception
	{		
		GenericResponseDTO<AuthenticationTokenResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{			
			genericResponse.setMessage(userHandler.recoveryPassword(request));
			genericResponse.setCode(HttpStatus.ACCEPTED.value());
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@GetMapping("/recovery-password/token/{token}")	
	public ResponseEntity<?> validateTokenPassordRecovery(@PathVariable String token,HttpServletResponse response) throws Exception
	{		
		GenericResponseDTO<?> genericResponse = new GenericResponseDTO<>();
		try
		{			
			userHandler.validateRecoveryToken(token);
			genericResponse.setMessage("Valid token");
			genericResponse.setCode(HttpStatus.OK.value());							
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@PutMapping("/recovery-password/change")	
	public ResponseEntity<?> changePasswordByRecovery(@Valid @RequestBody ChangePasswordRequestDTO request,
			HttpServletRequest httpRequest,HttpServletResponse response) throws Exception
	{		
		GenericResponseDTO<AuthenticationTokenResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{						
			genericResponse.setMessage(userHandler.changePassword(request,EnumChangePasswordType.NON_SESSION,httpRequest));
			genericResponse.setCode(HttpStatus.OK.value());
			
			Cookie refreshTokenCookie = new Cookie("refreshToken", null);	        
	        refreshTokenCookie.setHttpOnly(true); 
	        refreshTokenCookie.setSecure(true);
	        refreshTokenCookie.setPath("/");
	        refreshTokenCookie.setMaxAge(0);
	        response.addCookie(refreshTokenCookie);
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@PutMapping("/change-password")	
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequestDTO request,
			HttpServletRequest httpRequest,HttpServletResponse response) throws Exception
	{		
		GenericResponseDTO<AuthenticationTokenResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{						
			genericResponse.setMessage(userHandler.changePassword(request,EnumChangePasswordType.SESSION,httpRequest));
			genericResponse.setCode(HttpStatus.OK.value());
			
			Cookie refreshTokenCookie = new Cookie("refreshToken", null);	        
	        refreshTokenCookie.setHttpOnly(true); 
	        refreshTokenCookie.setSecure(true);
	        refreshTokenCookie.setPath("/");
	        refreshTokenCookie.setMaxAge(0);
	        response.addCookie(refreshTokenCookie);
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@PostMapping			
	public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDTO request) throws Exception
	{		
		GenericResponseDTO<UserResponseDTO> genericResponse = new GenericResponseDTO<>();
				
		try
		{			
			if(request.getProfile().getProviderCode()!=null && !request.getProfile().getProviderCode().isEmpty())
			{
				request.setRole(EnumRoleType.PROVIDER);
			}
			else
			{
				request.setRole(EnumRoleType.USER);
			}
			
			genericResponse = userHandler.createUser(request);
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}
			genericResponse.setCode(HttpStatus.CREATED.value());
			genericResponse.setMessage("User successfully created");
		}
		catch(Exception e)
		{			
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	
	@PatchMapping("/{id}")
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	public ResponseEntity<?> editUser(
			@PathVariable 
			@NotBlank(message = "User id is required")
			@Pattern(regexp = "\\d+", message = "The user field must contain only numbers") 
			String id ,@RequestBody UserRequestDTO request) throws Exception
	{		
		GenericResponseDTO<?> genericResponse = new GenericResponseDTO<>();
		try
		{
			request.setId(Long.valueOf(id));
			genericResponse = userHandler.updateUser(request);
			
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage("User successfully updated");
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@DeleteMapping("/{id}")
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<?> deleteUser(
			@PathVariable 
			@NotBlank(message = "User id is required")
			@Pattern(regexp = "\\d+", message = "The user field must contain only numbers") 
			String id) throws Exception
	{		
		GenericResponseDTO<?> genericResponse = new GenericResponseDTO<>();
		try
		{						
			genericResponse = userHandler.deleteUser( UserRequestDTO.builder().id(Long.valueOf(id)).build() );
			
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage("User successfully deleted");
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@GetMapping
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	public ResponseEntity<?> getAllUser() throws Exception
	{		
		GenericResponseDTO<?> genericResponse = new GenericResponseDTO<>();
		try
		{	
			genericResponse.setPayload(userHandler.retrieveUser( UserRequestDTO.builder().status(EnumStates.active.name()).build()) );
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage(GeneralConstants.SUCCESS_MESSAGE_DEFAULT);
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@GetMapping("/{id}")
	@Secured({"ROLE_USER","ROLE_PROVIDER","ROLE_ADMIN"})
	public ResponseEntity<?> getUser(			
			@PathVariable 
			@NotNull(message = "User id is required")
			@Pattern(regexp = "\\d+", message = "The user field must contain only numbers") 
			String id) throws Exception
	{		
		GenericResponseDTO<?> genericResponse = new GenericResponseDTO<>();
		try
		{
			genericResponse.setPayload(userHandler.retrieveUser( UserRequestDTO.builder().id(Long.valueOf(id)).build()) );
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage(GeneralConstants.SUCCESS_MESSAGE_DEFAULT);
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}		

}
