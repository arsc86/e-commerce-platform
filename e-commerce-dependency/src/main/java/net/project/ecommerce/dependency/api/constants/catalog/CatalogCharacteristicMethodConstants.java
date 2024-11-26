package net.project.ecommerce.dependency.api.constants.catalog;

import static java.util.Map.entry;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Getter;
import net.project.ecommerce.dependency.api.constants.EcommerceConstants;
import net.project.ecommerce.dependency.api.dto.catalog.request.CharacteristicRequestDTO;

@Component(EcommerceConstants.CONTEXT_CATALOG_CHARACT)
public class CatalogCharacteristicMethodConstants implements IConstantDefinition{
	
	public static final String CHARACTERISTIC_SERVICE_BEAN      = "CharacteristicServiceImpl";		
	public static final String CHARACTERISTIC_CREATE_METHOD     = "create";
	public static final String CHARACTERISTIC_RETRIEVE_METHOD   = "retrieve";
	public static final String CHARACTERISTIC_UPDATE_METHOD     = "update";
	public static final String CHARACTERISTIC_DELETE_METHOD     = "delete";
	
	@Getter
	private static final Map<String,Map<String,Object>> CHARACTERISTIC_METHOD_MAP = Map.ofEntries(						
		entry(
				CHARACTERISTIC_CREATE_METHOD, 
					Map.ofEntries(entry(CHARACTERISTIC_SERVICE_BEAN,  
							Map.ofEntries(entry(CHARACTERISTIC_CREATE_METHOD, CharacteristicRequestDTO.class ))))
            ),
		entry(
				CHARACTERISTIC_RETRIEVE_METHOD, 
					Map.ofEntries(entry(CHARACTERISTIC_SERVICE_BEAN,  
							Map.ofEntries(entry(CHARACTERISTIC_RETRIEVE_METHOD, CharacteristicRequestDTO.class ))))
            ),
		entry(
				CHARACTERISTIC_UPDATE_METHOD, 
					Map.ofEntries(entry(CHARACTERISTIC_SERVICE_BEAN,  
							Map.ofEntries(entry(CHARACTERISTIC_UPDATE_METHOD, CharacteristicRequestDTO.class ))))
            ),
		entry(
				CHARACTERISTIC_DELETE_METHOD, 
					Map.ofEntries(entry(CHARACTERISTIC_SERVICE_BEAN,  
							Map.ofEntries(entry(CHARACTERISTIC_DELETE_METHOD, CharacteristicRequestDTO.class ))))
            )
    );	

	@Override
	public Map<String, Map<String, Object>> getContextMap() {
		return getCHARACTERISTIC_METHOD_MAP();
	}

}
