package net.project.ecommerce.msa.catalog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class AppConfiguration {

	@Value("${e-commerce.api.base-url:http://localhost:8080}")
	private String apiBaseUrl;
	
	@Value("${e-commerce.api.product.path.file:/api}")
	private String filePath;
	
	@Value("${e-commerce.api.product.path.price:/api}")
	private String pricePath;
	
	@Value("${e-commerce.api.product.path.discount:/api}")
	private String discountPath;
	
	@Value("${e-commerce.api.product.path.inventory:/api}")
	private String inventoryPath;
	
	@Value("${e-commerce.api.product.path.characteristic:/api}")
	private String characteristicPath;
}
