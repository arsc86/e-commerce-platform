package net.project.ecommerce.msa.api.bff.communication.rest.user;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import net.project.ecommerce.dependency.api.dto.user.AddressDTO;
import net.project.ecommerce.dependency.api.dto.user.PaymentMethodDTO;
import net.project.ecommerce.dependency.api.dto.user.request.CreatePaymentRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.request.AuthenticationRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.request.ChangePasswordRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.request.CreateAddressesRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.request.RecoveryPasswordRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.request.UserRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.response.AuthenticationTokenResponseDTO;
import net.project.ecommerce.dependency.api.dto.user.response.UserResponseDTO;
import net.project.ecommerce.dependency.api.enums.EnumRoleType;
import net.project.ecommerce.dependency.api.enums.EnumStates;
import net.project.ecommerce.dependency.api.interfaces.ICrudHandler;
import net.project.ecommerce.dependency.constants.GeneralConstants;
import net.project.ecommerce.dependency.dto.response.GenericResponseDTO;
import net.project.ecommerce.msa.api.bff.cons.ApiBffConstants;
import net.project.ecommerce.msa.api.bff.enums.EnumChangePasswordType;
import net.project.ecommerce.msa.api.bff.handler.impl.user.IUserHandler;

@RestController
@RequestMapping("/user")
public class UserController {
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private IUserHandler userHandler;
	
	@Autowired
	private ICrudHandler crudHandler;
	
	@Autowired
	private BeanFactory factory;
	
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
			crudHandler = factory.getBean(ApiBffConstants.USER_CRUD_BEAN,ICrudHandler.class);
			if(request.getProfile().getProviderCode()!=null && !request.getProfile().getProviderCode().isEmpty())
			{
				request.setRole(EnumRoleType.PROVIDER);
			}
			else
			{
				request.setRole(EnumRoleType.USER);
			}
			
			genericResponse = crudHandler.create(request);
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
			crudHandler = factory.getBean(ApiBffConstants.USER_CRUD_BEAN,ICrudHandler.class);
			request.setId(Long.valueOf(id));
			genericResponse = crudHandler.update(request);
			
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
			crudHandler = factory.getBean(ApiBffConstants.USER_CRUD_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.delete( UserRequestDTO.builder().id(Long.valueOf(id)).build() );
			
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
			crudHandler = factory.getBean(ApiBffConstants.USER_CRUD_BEAN,ICrudHandler.class);
			genericResponse.setPayload(crudHandler.retrieve( UserRequestDTO.builder().status(EnumStates.active.name()).build()) );
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
			crudHandler = factory.getBean(ApiBffConstants.USER_CRUD_BEAN,ICrudHandler.class);
			genericResponse.setPayload(crudHandler.retrieve( UserRequestDTO.builder().id(Long.valueOf(id)).build()) );
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
	
	//Address
	
	@PostMapping("/{id}/address")	
	@Secured({"ROLE_USER","ROLE_PROVIDER","ROLE_ADMIN"})
	public ResponseEntity<?> createUserAddress(
			@PathVariable 
			@NotBlank(message = "User id is required")
			@Pattern(regexp = "\\d+", message = "The user field must contain only numbers") 
			String id,
			@Valid @RequestBody CreateAddressesRequestDTO request) throws Exception
	{		
		GenericResponseDTO<UserResponseDTO> genericResponse = new GenericResponseDTO<>();
				
		try
		{						
			crudHandler = factory.getBean(ApiBffConstants.USER_ADDRESS_CRUD_BEAN,ICrudHandler.class);			
			genericResponse = crudHandler.create(UserRequestDTO.builder()
															   .id(Long.valueOf(id))
															   .address(request.getAddresses())
															   .build()
											    );
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}
			genericResponse.setCode(HttpStatus.CREATED.value());
			genericResponse.setMessage("User Address successfully added");
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
	
	@PatchMapping("/{id}/address/{addressId}")	
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	public ResponseEntity<?> updateUserAddress(
			@PathVariable 
			@NotBlank(message = "User id is required")
			@Pattern(regexp = "\\d+", message = "The user field must contain only numbers") 
			String id,
			@PathVariable 
			@NotBlank(message = "User address id is required")
			@Pattern(regexp = "\\d+", message = "The user address field must contain only numbers") 
			String addressId,
			@Valid @RequestBody AddressDTO request) throws Exception
	{		
		GenericResponseDTO<UserResponseDTO> genericResponse = new GenericResponseDTO<>();
				
		try
		{						
			crudHandler = factory.getBean(ApiBffConstants.USER_ADDRESS_CRUD_BEAN,ICrudHandler.class);			
			request.setId(Long.valueOf(addressId));
			genericResponse = crudHandler.update(UserRequestDTO.builder()
															   .id(Long.valueOf(id))
															   .address(Arrays.asList(request))
															   .build()
											    );
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}
			genericResponse.setCode(HttpStatus.CREATED.value());
			genericResponse.setMessage("User Address successfully updated");
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
	
	@DeleteMapping("/{id}/address/{addressId}")
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	public ResponseEntity<?> deleteUserAddress(
			@PathVariable 
			@NotBlank(message = "User id is required")
			@Pattern(regexp = "\\d+", message = "The user field must contain only numbers") 
			String id,
			@PathVariable 
			@NotBlank(message = "User address id is required")
			@Pattern(regexp = "\\d+", message = "The user address field must contain only numbers") 
			String addressId) throws Exception
	{		
		GenericResponseDTO<?> genericResponse = new GenericResponseDTO<>();
		try
		{						
			crudHandler = factory.getBean(ApiBffConstants.USER_ADDRESS_CRUD_BEAN,ICrudHandler.class);
			AddressDTO address = AddressDTO.builder().id(Long.valueOf(addressId)).build();
			genericResponse = crudHandler.delete( UserRequestDTO.builder()
																.id(Long.valueOf(id))
																.address(Arrays.asList(address))
																.build() 
											    );
			
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage("User Address successfully deleted");
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
	
	@GetMapping("/{id}/addresses")
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	public ResponseEntity<?> getUserAddressBy(			
			@PathVariable 
			@NotNull(message = "User id is required")
			@Pattern(regexp = "\\d+", message = "The user field must contain only numbers") 
			String id,	
			@Pattern(regexp = "^[YN]$", message = "isDefault must be 'Y' or 'N'")
			@RequestParam(required = false)
			String isDefault,
			@RequestParam(required = false)
		    @Pattern(regexp = "^[a-zA-Z]+$", message = "The status field must have only letters")			
			String status,
			@RequestParam Map<String, String> allParams) throws Exception
	{		
		GenericResponseDTO<?> genericResponse = new GenericResponseDTO<>();
		try
		{
			Set<String> allowedParams = Set.of("status", "isDefault");
			for (String param : allParams.keySet()) 
			{
	            if (!allowedParams.contains(param)) 
	            {
	                throw new Exception("Parameter "+param+" is not allowed");
	            }
	        }
			AddressDTO addressDTO = AddressDTO.builder()					
												.status(status)
												.isDefault(isDefault)
												.build();
			crudHandler = factory.getBean(ApiBffConstants.USER_ADDRESS_CRUD_BEAN,ICrudHandler.class);
			genericResponse.setPayload(crudHandler.retrieve( UserRequestDTO.builder()
					.id(Long.valueOf(id))
					.address(Arrays.asList(addressDTO))
					.build()) );
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage(GeneralConstants.SUCCESS_MESSAGE_DEFAULT);
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
	
	//payments
	
	@PostMapping("/{id}/payment-method")	
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	public ResponseEntity<?> createUserPaymentMethod(
			@PathVariable 
			@NotBlank(message = "User id is required")
			@Pattern(regexp = "\\d+", message = "The user field must contain only numbers") 
			String id,
			@Valid @RequestBody CreatePaymentRequestDTO request) throws Exception
	{		
		GenericResponseDTO<UserResponseDTO> genericResponse = new GenericResponseDTO<>();
				
		try
		{		
			crudHandler = factory.getBean(ApiBffConstants.USER_PAYMENT_CRUD_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.create(UserRequestDTO.builder()
															   .id(Long.valueOf(id))
															   .paymentMethods(request.getPayments())
															   .build());
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
	
	@PatchMapping("/{id}/payment-method/{paymentId}")	
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	public ResponseEntity<?> updateUserPaymentMethod(
			@PathVariable 
			@NotBlank(message = "User id is required")
			@Pattern(regexp = "\\d+", message = "The user field must contain only numbers") 
			String id,
			@PathVariable 
			@NotBlank(message = "User payment method id is required")
			@Pattern(regexp = "\\d+", message = "The user payment method field must contain only numbers") 
			String paymentId,
			@Valid @RequestBody PaymentMethodDTO request) throws Exception
	{		
		GenericResponseDTO<UserResponseDTO> genericResponse = new GenericResponseDTO<>();
				
		try
		{						
			crudHandler = factory.getBean(ApiBffConstants.USER_PAYMENT_CRUD_BEAN,ICrudHandler.class);			
			request.setId(Long.valueOf(paymentId));
			genericResponse = crudHandler.update(UserRequestDTO.builder()
															   .id(Long.valueOf(id))
															   .paymentMethods(Arrays.asList(request))
															   .build()
											    );
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}
			genericResponse.setCode(HttpStatus.CREATED.value());
			genericResponse.setMessage("User Payment method successfully updated");
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
	
	@DeleteMapping("/{id}/payment-method/{paymentId}")
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	public ResponseEntity<?> deleteUserPaymentMethod(
			@PathVariable 
			@NotBlank(message = "User id is required")
			@Pattern(regexp = "\\d+", message = "The user field must contain only numbers") 
			String id,
			@PathVariable 
			@NotBlank(message = "User payment method id is required")
			@Pattern(regexp = "\\d+", message = "The user payment method field must contain only numbers") 
			String paymentId) throws Exception
	{		
		GenericResponseDTO<?> genericResponse = new GenericResponseDTO<>();
		try
		{						
			crudHandler = factory.getBean(ApiBffConstants.USER_PAYMENT_CRUD_BEAN,ICrudHandler.class);
			PaymentMethodDTO request = PaymentMethodDTO.builder().id(Long.valueOf(paymentId)).build();
			genericResponse = crudHandler.delete( UserRequestDTO.builder()
																.id(Long.valueOf(id))
																.paymentMethods(Arrays.asList(request))
																.build() 
											    );
			
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage("User Payment Method successfully deleted");
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
	
	@GetMapping("/{id}/payment-methods")
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	public ResponseEntity<?> getUserPaymentMethodsBy(			
			@PathVariable 
			@NotNull(message = "User id is required")
			@Pattern(regexp = "\\d+", message = "The user field must contain only numbers") 
			String id,	
			@Pattern(regexp = "^[YN]$", message = "isDefault must be 'Y' or 'N'")
			@RequestParam(required = false)
			String isDefault,
			@RequestParam(required = false)
		    @Pattern(regexp = "^[a-zA-Z]+$", message = "The status field must have only letters")			
			String status,
			@RequestParam(required = false)		    	
			String type,
			@RequestParam Map<String, String> allParams) throws Exception
	{		
		GenericResponseDTO<?> genericResponse = new GenericResponseDTO<>();
		try
		{
			Set<String> allowedParams = Set.of("status", "isDefault", "type");
			for (String param : allParams.keySet()) 
			{
	            if (!allowedParams.contains(param)) 
	            {
	                throw new Exception("Parameter "+param+" is not allowed");
	            }
	        }
			PaymentMethodDTO payment = PaymentMethodDTO.builder()					
												.status(status)
												.isDefault(isDefault)
												.build();
			crudHandler = factory.getBean(ApiBffConstants.USER_PAYMENT_CRUD_BEAN,ICrudHandler.class);
			genericResponse.setPayload(crudHandler.retrieve( UserRequestDTO.builder()
					.id(Long.valueOf(id))
					.paymentMethods(Arrays.asList(payment))
					.build()) );
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage(GeneralConstants.SUCCESS_MESSAGE_DEFAULT);
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

}
