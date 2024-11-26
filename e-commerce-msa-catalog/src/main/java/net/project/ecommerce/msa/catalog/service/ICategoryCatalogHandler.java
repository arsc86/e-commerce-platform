package net.project.ecommerce.msa.catalog.service;

import net.project.ecommerce.dependency.api.dto.catalog.request.CategoryRequestDTO;

public interface ICategoryCatalogHandler {
		
	public <R> R create(CategoryRequestDTO data) throws Exception;
	public <R> R createBy(CategoryRequestDTO data) throws Exception;
	public <R> R update(CategoryRequestDTO request) throws Exception;
	public void delete(CategoryRequestDTO request) throws Exception;
	public <R> R retrieve(CategoryRequestDTO request) throws Exception;		
}
