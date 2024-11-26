package net.project.ecommerce.msa.catalog.service;

import net.project.ecommerce.dependency.api.dto.catalog.request.ProductInventoryRequestDTO;

public interface IProductInventoryHandler {
	
	public <R> R stockInOut(ProductInventoryRequestDTO data) throws Exception;	
	public <R> R retrieve(ProductInventoryRequestDTO request) throws Exception;	

}
