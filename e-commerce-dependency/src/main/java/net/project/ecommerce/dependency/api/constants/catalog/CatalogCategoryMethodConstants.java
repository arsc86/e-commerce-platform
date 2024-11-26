package net.project.ecommerce.dependency.api.constants.catalog;

import static java.util.Map.entry;

import java.util.Map;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import lombok.Getter;
import net.project.ecommerce.dependency.api.constants.EcommerceConstants;
import net.project.ecommerce.dependency.api.dto.catalog.request.CategoryRequestDTO;

@Component(EcommerceConstants.CONTEXT_CATALOG_CATEGORY)
@Primary
public class CatalogCategoryMethodConstants implements IConstantDefinition{
	
	public static final String CATEGORY_SERVICE_BEAN      = "CategoryServiceImpl";		
	public static final String CATEGORY_CREATE_METHOD     = "create";
	public static final String CATEGORY_CREATE_BY_METHOD  = "createBy";
	public static final String CATEGORY_RETRIEVE_METHOD   = "retrieve";
	public static final String CATEGORY_UPDATE_METHOD     = "update";
	public static final String CATEGORY_DELETE_METHOD     = "delete";
	
	@Getter
	private static final Map<String,Map<String,Object>> CATEGORY_METHOD_MAP = Map.ofEntries(						
		entry(
				CATEGORY_CREATE_METHOD, 
					Map.ofEntries(entry(CATEGORY_SERVICE_BEAN,  
							Map.ofEntries(entry(CATEGORY_CREATE_METHOD, CategoryRequestDTO.class ))))
            ),
		entry(
				CATEGORY_CREATE_BY_METHOD, 
					Map.ofEntries(entry(CATEGORY_SERVICE_BEAN,  
							Map.ofEntries(entry(CATEGORY_CREATE_BY_METHOD, CategoryRequestDTO.class ))))
            ),
		entry(
				CATEGORY_RETRIEVE_METHOD, 
					Map.ofEntries(entry(CATEGORY_SERVICE_BEAN,  
							Map.ofEntries(entry(CATEGORY_RETRIEVE_METHOD, CategoryRequestDTO.class ))))
            ),
		entry(
				CATEGORY_UPDATE_METHOD, 
					Map.ofEntries(entry(CATEGORY_SERVICE_BEAN,  
							Map.ofEntries(entry(CATEGORY_UPDATE_METHOD, CategoryRequestDTO.class ))))
            ),
		entry(
				CATEGORY_DELETE_METHOD, 
					Map.ofEntries(entry(CATEGORY_SERVICE_BEAN,  
							Map.ofEntries(entry(CATEGORY_DELETE_METHOD, CategoryRequestDTO.class ))))
            )
    );	

	@Override
	public Map<String, Map<String, Object>> getContextMap() {
		return getCATEGORY_METHOD_MAP();
	}

}
