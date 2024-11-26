package net.project.ecommerce.dependency.api.constants.catalog;

import static java.util.Map.entry;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Getter;
import net.project.ecommerce.dependency.api.constants.EcommerceConstants;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductRequestDTO;

@Component(EcommerceConstants.CONTEXT_CATALOG_PRODUCT)
public class CatalogProductMethodConstants implements IConstantDefinition{
	
	public static final String PRODUCT_SERVICE_BEAN      = "ProductServiceImpl";		
	public static final String PRODUCT_CREATE_METHOD     = "create";
	public static final String PRODUCT_RETRIEVE_METHOD   = "retrieve";
	public static final String PRODUCT_UPDATE_METHOD     = "update";
	public static final String PRODUCT_DELETE_METHOD     = "delete";
	
	@Getter
	private static final Map<String,Map<String,Object>> PRODUCT_METHOD_MAP = Map.ofEntries(						
		entry(
				PRODUCT_CREATE_METHOD, 
					Map.ofEntries(entry(PRODUCT_SERVICE_BEAN,  
							Map.ofEntries(entry(PRODUCT_CREATE_METHOD, ProductRequestDTO.class ))))
            ),
		entry(
				PRODUCT_RETRIEVE_METHOD, 
					Map.ofEntries(entry(PRODUCT_SERVICE_BEAN,  
							Map.ofEntries(entry(PRODUCT_RETRIEVE_METHOD, ProductRequestDTO.class ))))
            ),
		entry(
				PRODUCT_UPDATE_METHOD, 
					Map.ofEntries(entry(PRODUCT_SERVICE_BEAN,  
							Map.ofEntries(entry(PRODUCT_UPDATE_METHOD, ProductRequestDTO.class ))))
            ),
		entry(
				PRODUCT_DELETE_METHOD, 
					Map.ofEntries(entry(PRODUCT_SERVICE_BEAN,  
							Map.ofEntries(entry(PRODUCT_DELETE_METHOD, ProductRequestDTO.class ))))
            )
    );	

	@Override
	public Map<String, Map<String, Object>> getContextMap() {
		return getPRODUCT_METHOD_MAP();
	}

}
