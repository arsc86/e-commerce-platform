package net.project.ecommerce.msa.catalog.service;

import net.project.ecommerce.dependency.api.dto.catalog.request.ProviderRequestDTO;

public interface IProviderCatalogHandler {
	
	public <R> R create(ProviderRequestDTO data) throws Exception;	
	public <R> R update(ProviderRequestDTO request) throws Exception;
	public void delete(ProviderRequestDTO request) throws Exception;
	public <R> R retrieve(ProviderRequestDTO request) throws Exception;
}
