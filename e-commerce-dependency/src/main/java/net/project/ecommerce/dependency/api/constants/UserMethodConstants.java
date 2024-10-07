package net.project.ecommerce.dependency.api.constants;

import static java.util.Map.entry;

import java.util.Map;

public class UserMethodConstants {
	
	public static final String USER_SERVICE_BEAN          = "UserHandlerImpl";		
	public static final String USER_CREATE_METHOD         = "create";
	public static final String USER_RETRIEVE_METHOD       = "retrieve";
	public static final String USER_UPDATE_METHOD         = "update";
	public static final String USER_DELETE_METHOD         = "delete";
	
	public static final String AUTH_SERVICE_BEAN          = "AuthenticationHandlerImpl";	
	public static final String AUTHENTICATE_METHOD        = "authenticate";
	public static final String LAST_PASSORD_METHOD        = "getLastPassword";
	public static final String UPDATE_PASSWORD_METHOD     = "updatePassword";
	public static final String GET_REFRESH_TOKEN_METHOD   = "getRefreshToken";
	public static final String UPDATE_REFRESH_TOKEN_METHOD= "updateRefreshToken";
	
	public static final String USER_REQUEST = "net.project.ecommerce.dependency.api.dto.user.request.UserRequestDTO";	
	
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
            )
    );
	
	//Producer
	public static final String USER_MESSAGE_PRODUCER  = "user";
}
