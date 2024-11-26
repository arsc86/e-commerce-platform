package net.project.ecommerce.dependency.api.constants.catalog;

import static java.util.Map.entry;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Getter;
import net.project.ecommerce.dependency.api.constants.EcommerceConstants;
import net.project.ecommerce.dependency.api.dto.catalog.request.WarehouseRequestDTO;

@Component(EcommerceConstants.CONTEXT_CATALOG_WAREHOUSE)
public class CatalogWarehouseMethodConstants implements IConstantDefinition{
	
	public static final String WAREHOUSE_SERVICE_BEAN      = "WarehouseServiceImpl";		
	public static final String WAREHOUSE_CREATE_METHOD     = "create";	
	public static final String WAREHOUSE_RETRIEVE_METHOD   = "retrieve";
	public static final String WAREHOUSE_UPDATE_METHOD     = "update";
	public static final String WAREHOUSE_DELETE_METHOD     = "delete";
		
	@Getter
	private static final Map<String,Map<String,Object>> WAREHOUSE_METHOD_MAP = Map.ofEntries(						
		entry(
				WAREHOUSE_CREATE_METHOD, 
					Map.ofEntries(entry(WAREHOUSE_SERVICE_BEAN,  
							Map.ofEntries(entry(WAREHOUSE_CREATE_METHOD, WarehouseRequestDTO.class ))))
            ),
		entry(
				WAREHOUSE_RETRIEVE_METHOD, 
					Map.ofEntries(entry(WAREHOUSE_SERVICE_BEAN,  
							Map.ofEntries(entry(WAREHOUSE_RETRIEVE_METHOD, WarehouseRequestDTO.class ))))
            ),
		entry(
				WAREHOUSE_UPDATE_METHOD, 
					Map.ofEntries(entry(WAREHOUSE_SERVICE_BEAN,  
							Map.ofEntries(entry(WAREHOUSE_UPDATE_METHOD, WarehouseRequestDTO.class ))))
            ),
		entry(
				WAREHOUSE_DELETE_METHOD, 
					Map.ofEntries(entry(WAREHOUSE_SERVICE_BEAN,  
							Map.ofEntries(entry(WAREHOUSE_DELETE_METHOD, WarehouseRequestDTO.class ))))
            )
    );
		
	@Override
	public Map<String, Map<String, Object>> getContextMap() {
		return getWAREHOUSE_METHOD_MAP();
	}

}
