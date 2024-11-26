package net.project.ecommerce.msa.catalog.service;

import net.project.ecommerce.dependency.api.dto.catalog.request.ProductRequestDTO;

public interface IProductCatalogHandler {
		
	public <R> R create(ProductRequestDTO data) throws Exception;	
	public <R> R update(ProductRequestDTO request) throws Exception;
	public void delete(ProductRequestDTO request) throws Exception;
	public <R> R retrieve(ProductRequestDTO request) throws Exception;		
}
