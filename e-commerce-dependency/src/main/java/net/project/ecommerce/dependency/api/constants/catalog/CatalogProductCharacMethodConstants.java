package net.project.ecommerce.dependency.api.constants.catalog;

import static java.util.Map.entry;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Getter;
import net.project.ecommerce.dependency.api.constants.EcommerceConstants;
import net.project.ecommerce.dependency.api.dto.catalog.ProductCharacteristicDTO;

@Component(EcommerceConstants.CONTEXT_CATALOG_PRODUCT_CHARACT)
public class CatalogProductCharacMethodConstants implements IConstantDefinition{
	
	public static final String PRODUCT_CHARAC_SERVICE_BEAN      = "ProductCharacteristicServiceImpl";		
	public static final String PRODUCT_CHARAC_CREATE_METHOD     = "create";
	public static final String PRODUCT_CHARAC_RETRIEVE_METHOD   = "retrieve";
	public static final String PRODUCT_CHARAC_DELETE_METHOD     = "delete";
	
	@Getter
	private static final Map<String,Map<String,Object>> PRODUCT_CHARAC_METHOD_MAP = Map.ofEntries(						
		entry(
				PRODUCT_CHARAC_CREATE_METHOD, 
					Map.ofEntries(entry(PRODUCT_CHARAC_SERVICE_BEAN,  
							Map.ofEntries(entry(PRODUCT_CHARAC_CREATE_METHOD, ProductCharacteristicDTO.class ))))
            ),
		entry(
				PRODUCT_CHARAC_RETRIEVE_METHOD, 
					Map.ofEntries(entry(PRODUCT_CHARAC_SERVICE_BEAN,  
							Map.ofEntries(entry(PRODUCT_CHARAC_RETRIEVE_METHOD, ProductCharacteristicDTO.class ))))
            ),
		entry(
				PRODUCT_CHARAC_DELETE_METHOD, 
					Map.ofEntries(entry(PRODUCT_CHARAC_SERVICE_BEAN,  
							Map.ofEntries(entry(PRODUCT_CHARAC_DELETE_METHOD, ProductCharacteristicDTO.class ))))
            )
    );	

	@Override
	public Map<String, Map<String, Object>> getContextMap() {
		return getPRODUCT_CHARAC_METHOD_MAP();
	}

}
