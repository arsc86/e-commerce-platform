package net.project.ecommerce.msa.catalog.service;

import net.project.ecommerce.dependency.api.dto.catalog.ProductCharacteristicDTO;

public interface IProductPriceDiscCatalogHandler {
		
	public <R> R create(ProductCharacteristicDTO data) throws Exception;
	public void delete(ProductCharacteristicDTO request) throws Exception;
	public <R> R retrieve(ProductCharacteristicDTO request) throws Exception;		
}
