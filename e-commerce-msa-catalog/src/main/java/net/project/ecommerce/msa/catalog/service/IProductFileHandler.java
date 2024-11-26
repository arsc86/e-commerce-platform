package net.project.ecommerce.msa.catalog.service;

import net.project.ecommerce.dependency.api.dto.catalog.request.ProductFileRequestDTO;

public interface IProductFileHandler {
	
	public <R> R create(ProductFileRequestDTO data) throws Exception;
	public void delete(ProductFileRequestDTO request) throws Exception;
	public <R> R retrieve(ProductFileRequestDTO request) throws Exception;	

}
