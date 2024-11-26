package net.project.ecommerce.dependency.api.constants.catalog;

import static java.util.Map.entry;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Getter;
import net.project.ecommerce.dependency.api.constants.EcommerceConstants;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductInventoryRequestDTO;

@Component(EcommerceConstants.CONTEXT_CATALOG_PRODUCT_INVENTORY)
public class CatalogProductInventoryMethodConstants implements IConstantDefinition{
	
	public static final String PRODUCT_INVENTORY_SERVICE_BEAN    = "ProductInventoryServiceImpl";		
	public static final String PRODUCT_INVENTORY_STOCKING_METHOD = "stockInOut";
	public static final String PRODUCT_INVENTORY_RETRIEVE_METHOD = "retrieve";
	
	@Getter
	private static final Map<String,Map<String,Object>> PRODUCT_INVENTORY_METHOD_MAP = Map.ofEntries(						
		entry(
				PRODUCT_INVENTORY_STOCKING_METHOD, 
					Map.ofEntries(entry(PRODUCT_INVENTORY_SERVICE_BEAN,  
							Map.ofEntries(entry(PRODUCT_INVENTORY_STOCKING_METHOD, ProductInventoryRequestDTO.class ))))
            ),
		entry(
				PRODUCT_INVENTORY_RETRIEVE_METHOD, 
					Map.ofEntries(entry(PRODUCT_INVENTORY_SERVICE_BEAN,  
							Map.ofEntries(entry(PRODUCT_INVENTORY_RETRIEVE_METHOD, ProductInventoryRequestDTO.class ))))
            )
    );	

	@Override
	public Map<String, Map<String, Object>> getContextMap() {
		return getPRODUCT_INVENTORY_METHOD_MAP();
	}

}
