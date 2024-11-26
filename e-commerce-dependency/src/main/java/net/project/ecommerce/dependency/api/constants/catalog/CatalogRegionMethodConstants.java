package net.project.ecommerce.dependency.api.constants.catalog;

import static java.util.Map.entry;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Getter;
import net.project.ecommerce.dependency.api.constants.EcommerceConstants;
import net.project.ecommerce.dependency.api.dto.catalog.request.RegionRequestDTO;

@Component(EcommerceConstants.CONTEXT_CATALOG_REGION)
public class CatalogRegionMethodConstants implements IConstantDefinition{
	
	public static final String REGION_SERVICE_BEAN      = "RegionServiceImpl";		
	public static final String REGION_CREATE_METHOD     = "create";	
	public static final String REGION_RETRIEVE_METHOD   = "retrieve";
	public static final String REGION_UPDATE_METHOD     = "update";
	public static final String REGION_DELETE_METHOD     = "delete";
		
	@Getter
	private static final Map<String,Map<String,Object>> REGION_METHOD_MAP = Map.ofEntries(						
		entry(
				REGION_CREATE_METHOD, 
					Map.ofEntries(entry(REGION_SERVICE_BEAN,  
							Map.ofEntries(entry(REGION_CREATE_METHOD, RegionRequestDTO.class ))))
            ),
		entry(
				REGION_RETRIEVE_METHOD, 
					Map.ofEntries(entry(REGION_SERVICE_BEAN,  
							Map.ofEntries(entry(REGION_RETRIEVE_METHOD, RegionRequestDTO.class ))))
            ),
		entry(
				REGION_UPDATE_METHOD, 
					Map.ofEntries(entry(REGION_SERVICE_BEAN,  
							Map.ofEntries(entry(REGION_UPDATE_METHOD, RegionRequestDTO.class ))))
            ),
		entry(
				REGION_DELETE_METHOD, 
					Map.ofEntries(entry(REGION_SERVICE_BEAN,  
							Map.ofEntries(entry(REGION_DELETE_METHOD, RegionRequestDTO.class ))))
            )
    );
		
	@Override
	public Map<String, Map<String, Object>> getContextMap() {
		return getREGION_METHOD_MAP();
	}

}
