package net.project.ecommerce.msa.catalog.util;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.msa.catalog.config.AppConfiguration;

@Service
public class CatalogUtils {
	
	@Autowired
	private AppConfiguration configuration;
	
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH   = 7;
	
	public static String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }
	
	public String getHATEOASUri(String type, Object... params) {
		String baseUrl = configuration.getApiBaseUrl();		
		String path    = null;
		switch (type) {
			case CatalogConstants.FILE: {
				path = configuration.getFilePath();
				break;
			}
			case CatalogConstants.PRICE: {
				path = configuration.getPricePath();
				break;			
			}
			case CatalogConstants.DISCOUNT: {
				path = configuration.getDiscountPath();
				break;			
			}
			case CatalogConstants.INVENTORY: {
				path = configuration.getInventoryPath();
				break;	
			}
			case CatalogConstants.CHARACT: {
				path = configuration.getCharacteristicPath();
				break;	
			}
			default:
				break;
		}
		
		return baseUrl + Format.replacePlaceholderByParams(path, params);
	}
}
