package net.project.ecommerce.dependency.api.constants.catalog;

import static java.util.Map.entry;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Getter;
import net.project.ecommerce.dependency.api.constants.EcommerceConstants;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductFileRequestDTO;

@Component(EcommerceConstants.CONTEXT_CATALOG_PRODUCT_FILE)
public class CatalogProductFileMethodConstants implements IConstantDefinition{
	
	public static final String PRODUCT_FILE_SERVICE_BEAN      = "ProductFileCatalogHandlerImpl";		
	public static final String PRODUCT_FILE_CREATE_METHOD     = "create";
	public static final String PRODUCT_FILE_RETRIEVE_METHOD   = "retrieve";
	public static final String PRODUCT_FILE_UPDATE_METHOD     = "update";
	public static final String PRODUCT_FILE_DELETE_METHOD     = "delete";
	
	@Getter
	private static final Map<String,Map<String,Object>> PRODUCT_FILE_METHOD_MAP = Map.ofEntries(						
		entry(
				PRODUCT_FILE_CREATE_METHOD, 
					Map.ofEntries(entry(PRODUCT_FILE_SERVICE_BEAN,  
							Map.ofEntries(entry(PRODUCT_FILE_CREATE_METHOD, ProductFileRequestDTO.class ))))
            ),
		entry(
				PRODUCT_FILE_RETRIEVE_METHOD, 
					Map.ofEntries(entry(PRODUCT_FILE_SERVICE_BEAN,  
							Map.ofEntries(entry(PRODUCT_FILE_RETRIEVE_METHOD, ProductFileRequestDTO.class ))))
            ),
		entry(
				PRODUCT_FILE_UPDATE_METHOD, 
					Map.ofEntries(entry(PRODUCT_FILE_SERVICE_BEAN,  
							Map.ofEntries(entry(PRODUCT_FILE_UPDATE_METHOD, ProductFileRequestDTO.class ))))
            ),
		entry(
				PRODUCT_FILE_DELETE_METHOD, 
					Map.ofEntries(entry(PRODUCT_FILE_SERVICE_BEAN,  
							Map.ofEntries(entry(PRODUCT_FILE_DELETE_METHOD, ProductFileRequestDTO.class ))))
            )
    );	

	@Override
	public Map<String, Map<String, Object>> getContextMap() {
		return getPRODUCT_FILE_METHOD_MAP();
	}

}
