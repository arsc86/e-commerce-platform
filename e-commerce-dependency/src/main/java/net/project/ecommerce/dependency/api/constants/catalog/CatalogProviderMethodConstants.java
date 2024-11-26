package net.project.ecommerce.dependency.api.constants.catalog;

import static java.util.Map.entry;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Getter;
import net.project.ecommerce.dependency.api.constants.EcommerceConstants;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProviderRequestDTO;

@Component(EcommerceConstants.CONTEXT_CATALOG_PROVIDER)
public class CatalogProviderMethodConstants implements IConstantDefinition{
	
	public static final String PROVIDER_SERVICE_BEAN      = "ProviderServiceImpl";		
	public static final String PROVIDER_CREATE_METHOD     = "create";	
	public static final String PROVIDER_RETRIEVE_METHOD   = "retrieve";
	public static final String PROVIDER_UPDATE_METHOD     = "update";
	public static final String PROVIDER_DELETE_METHOD     = "delete";
	
	@Getter
	private static final Map<String,Map<String,Object>> PROVIDER_METHOD_MAP = Map.ofEntries(						
		entry(
				PROVIDER_CREATE_METHOD, 
					Map.ofEntries(entry(PROVIDER_SERVICE_BEAN,  
							Map.ofEntries(entry(PROVIDER_CREATE_METHOD, ProviderRequestDTO.class ))))
            ),
		entry(
				PROVIDER_RETRIEVE_METHOD, 
					Map.ofEntries(entry(PROVIDER_SERVICE_BEAN,  
							Map.ofEntries(entry(PROVIDER_RETRIEVE_METHOD, ProviderRequestDTO.class ))))
            ),
		entry(
				PROVIDER_UPDATE_METHOD, 
					Map.ofEntries(entry(PROVIDER_SERVICE_BEAN,  
							Map.ofEntries(entry(PROVIDER_UPDATE_METHOD, ProviderRequestDTO.class ))))
            ),
		entry(
				PROVIDER_DELETE_METHOD, 
					Map.ofEntries(entry(PROVIDER_SERVICE_BEAN,  
							Map.ofEntries(entry(PROVIDER_DELETE_METHOD, ProviderRequestDTO.class ))))
            )
    );
		
	@Override
	public Map<String, Map<String, Object>> getContextMap() {
		return getPROVIDER_METHOD_MAP();
	}

}
