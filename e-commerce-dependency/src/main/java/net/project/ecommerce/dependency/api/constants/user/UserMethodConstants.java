package net.project.ecommerce.dependency.api.constants.user;

import static java.util.Map.entry;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Getter;
import net.project.ecommerce.dependency.api.constants.EcommerceConstants;
import net.project.ecommerce.dependency.api.constants.catalog.IConstantDefinition;
import net.project.ecommerce.dependency.api.dto.user.request.ValidateEmailRequestDTO;

@Component(EcommerceConstants.CONTEXT_USER)
public class UserMethodConstants implements IConstantDefinition{
	
	public static final String USER_SERVICE_BEAN          = "UserHandlerImpl";		
	public static final String USER_CREATE_METHOD         = "create";
	public static final String USER_RETRIEVE_METHOD       = "retrieve";
	public static final String USER_UPDATE_METHOD         = "update";
	public static final String USER_DELETE_METHOD         = "delete";
	
	public static final String USER_ADDRESS_SERVICE_BEAN  = "UserAddressHandlerImpl";		
	public static final String USER_ADDRESS_CREATE_METHOD = "createUserAddress";
	public static final String USER_ADDRESS_RETRIEVE_METHOD = "retrieveUserAddress";
	public static final String USER_ADDRESS_UPDATE_METHOD   = "updateUserAddress";
	public static final String USER_ADDRESS_DELETE_METHOD   = "deleteUserAddress";
	
	public static final String USER_PAYMENT_SERVICE_BEAN    = "UserPaymentHandlerImpl";		
	public static final String USER_PAYMENT_CREATE_METHOD   = "createUserPayment";
	public static final String USER_PAYMENT_RETRIEVE_METHOD = "retrieveUserPayment";
	public static final String USER_PAYMENT_UPDATE_METHOD   = "updateUserPayment";
	public static final String USER_PAYMENT_DELETE_METHOD   = "deleteUserPayment";
	
	public static final String AUTH_SERVICE_BEAN          = "AuthenticationHandlerImpl";	
	public static final String AUTHENTICATE_METHOD        = "authenticate";
	public static final String LAST_PASSORD_METHOD        = "getLastPassword";
	public static final String UPDATE_PASSWORD_METHOD     = "updatePassword";
	public static final String GET_REFRESH_TOKEN_METHOD   = "getRefreshToken";
	public static final String UPDATE_REFRESH_TOKEN_METHOD= "updateRefreshToken";
	public static final String VALIDATE_MAIL_USER_METHOD  = "validateMailUser";
	
	public static final String USER_REQUEST = "net.project.ecommerce.dependency.api.dto.user.request.UserRequestDTO";	
	
	@Getter
	public static final Map<String,Map<String,Object>> USER_METHOD_MAP = Map.ofEntries(						
		entry(
				USER_CREATE_METHOD, 
					Map.ofEntries(entry(USER_SERVICE_BEAN,  
							Map.ofEntries(entry(USER_CREATE_METHOD, USER_REQUEST ))))
            ),
		entry(
				USER_RETRIEVE_METHOD, 
					Map.ofEntries(entry(USER_SERVICE_BEAN,  
							Map.ofEntries(entry(USER_RETRIEVE_METHOD, USER_REQUEST ))))
            ),
		entry(
				USER_UPDATE_METHOD, 
					Map.ofEntries(entry(USER_SERVICE_BEAN,  
							Map.ofEntries(entry(USER_UPDATE_METHOD, USER_REQUEST ))))
            ),
		entry(
				USER_DELETE_METHOD, 
					Map.ofEntries(entry(USER_SERVICE_BEAN,  
							Map.ofEntries(entry(USER_DELETE_METHOD, USER_REQUEST ))))
            ),
		//auth
		entry(
				AUTHENTICATE_METHOD, 
					Map.ofEntries(entry(AUTH_SERVICE_BEAN,  
							Map.ofEntries(entry(AUTHENTICATE_METHOD, USER_REQUEST ))))
            ),
		entry(
				LAST_PASSORD_METHOD, 
					Map.ofEntries(entry(AUTH_SERVICE_BEAN,  
							Map.ofEntries(entry(LAST_PASSORD_METHOD, USER_REQUEST ))))
            ),
		entry(
				UPDATE_PASSWORD_METHOD, 
					Map.ofEntries(entry(AUTH_SERVICE_BEAN,  
							Map.ofEntries(entry(UPDATE_PASSWORD_METHOD, USER_REQUEST ))))
            ),
		entry(
				UPDATE_REFRESH_TOKEN_METHOD, 
					Map.ofEntries(entry(AUTH_SERVICE_BEAN,  
							Map.ofEntries(entry(UPDATE_REFRESH_TOKEN_METHOD, USER_REQUEST ))))
            ),
		entry(
				GET_REFRESH_TOKEN_METHOD, 
					Map.ofEntries(entry(AUTH_SERVICE_BEAN,  
							Map.ofEntries(entry(GET_REFRESH_TOKEN_METHOD, USER_REQUEST ))))
            ),
		entry(
				VALIDATE_MAIL_USER_METHOD, 
					Map.ofEntries(entry(AUTH_SERVICE_BEAN,  
							Map.ofEntries(entry(VALIDATE_MAIL_USER_METHOD, ValidateEmailRequestDTO.class ))))
            ),
		//ADDRESS		
		entry(
				USER_ADDRESS_CREATE_METHOD, 
					Map.ofEntries(entry(USER_ADDRESS_SERVICE_BEAN,  
							Map.ofEntries(entry(USER_CREATE_METHOD, USER_REQUEST ))))
            ),
		entry(
				USER_ADDRESS_RETRIEVE_METHOD, 
					Map.ofEntries(entry(USER_ADDRESS_SERVICE_BEAN,  
							Map.ofEntries(entry(USER_RETRIEVE_METHOD, USER_REQUEST ))))
            ),
		entry(
				USER_ADDRESS_UPDATE_METHOD, 
					Map.ofEntries(entry(USER_ADDRESS_SERVICE_BEAN,  
							Map.ofEntries(entry(USER_UPDATE_METHOD, USER_REQUEST ))))
            ),
		entry(
				USER_ADDRESS_DELETE_METHOD, 
					Map.ofEntries(entry(USER_ADDRESS_SERVICE_BEAN,  
							Map.ofEntries(entry(USER_DELETE_METHOD, USER_REQUEST ))))
            ),
		//PAYMENT METHOD
		entry(
				USER_PAYMENT_CREATE_METHOD, 
					Map.ofEntries(entry(USER_PAYMENT_SERVICE_BEAN,  
							Map.ofEntries(entry(USER_CREATE_METHOD, USER_REQUEST ))))
            ),
		entry(
				USER_PAYMENT_RETRIEVE_METHOD, 
					Map.ofEntries(entry(USER_PAYMENT_SERVICE_BEAN,  
							Map.ofEntries(entry(USER_RETRIEVE_METHOD, USER_REQUEST ))))
            ),
		entry(
				USER_PAYMENT_UPDATE_METHOD, 
					Map.ofEntries(entry(USER_PAYMENT_SERVICE_BEAN,  
							Map.ofEntries(entry(USER_UPDATE_METHOD, USER_REQUEST ))))
            ),
		entry(
				USER_PAYMENT_DELETE_METHOD, 
					Map.ofEntries(entry(USER_PAYMENT_SERVICE_BEAN,  
							Map.ofEntries(entry(USER_DELETE_METHOD, USER_REQUEST ))))
            )
    );

	@Override
	public Map<String, Map<String, Object>> getContextMap() {
		return getUSER_METHOD_MAP();
	}			
}
