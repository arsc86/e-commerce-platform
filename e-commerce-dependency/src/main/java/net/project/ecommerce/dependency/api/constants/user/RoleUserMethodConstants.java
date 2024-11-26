package net.project.ecommerce.dependency.api.constants.user;

import static java.util.Map.entry;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Getter;
import net.project.ecommerce.dependency.api.constants.EcommerceConstants;
import net.project.ecommerce.dependency.api.constants.catalog.IConstantDefinition;
import net.project.ecommerce.dependency.api.dto.user.request.RoleRequestDTO;

@Component(EcommerceConstants.CONTEXT_ROLE_USER)
public class RoleUserMethodConstants implements IConstantDefinition{
	
	public static final String ROLE_SERVICE_BEAN      = "RoleServiceImpl";		
	public static final String ROLE_CREATE_METHOD     = "create";
	public static final String ROLE_RETRIEVE_METHOD   = "retrieve";
	public static final String ROLE_UPDATE_METHOD     = "update";
	public static final String ROLE_DELETE_METHOD     = "delete";
	
	@Getter
	private static final Map<String,Map<String,Object>> ROLE_METHOD_MAP = Map.ofEntries(						
		entry(
				ROLE_CREATE_METHOD, 
					Map.ofEntries(entry(ROLE_SERVICE_BEAN,  
							Map.ofEntries(entry(ROLE_CREATE_METHOD, RoleRequestDTO.class ))))
            ),
		entry(
				ROLE_RETRIEVE_METHOD, 
					Map.ofEntries(entry(ROLE_SERVICE_BEAN,  
							Map.ofEntries(entry(ROLE_RETRIEVE_METHOD, RoleRequestDTO.class ))))
            ),
		entry(
				ROLE_UPDATE_METHOD, 
					Map.ofEntries(entry(ROLE_SERVICE_BEAN,  
							Map.ofEntries(entry(ROLE_UPDATE_METHOD, RoleRequestDTO.class ))))
            ),
		entry(
				ROLE_DELETE_METHOD, 
					Map.ofEntries(entry(ROLE_SERVICE_BEAN,  
							Map.ofEntries(entry(ROLE_DELETE_METHOD, RoleRequestDTO.class ))))
            )
    );	

	@Override
	public Map<String, Map<String, Object>> getContextMap() {
		return getROLE_METHOD_MAP();
	}

}
