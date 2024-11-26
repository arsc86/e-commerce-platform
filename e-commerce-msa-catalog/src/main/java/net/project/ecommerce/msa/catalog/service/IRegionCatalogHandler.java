package net.project.ecommerce.msa.catalog.service;

import net.project.ecommerce.dependency.api.dto.catalog.request.RegionRequestDTO;

public interface IRegionCatalogHandler {
	
	public <R> R create(RegionRequestDTO data) throws Exception;	
	public <R> R update(RegionRequestDTO request) throws Exception;
	public void delete(RegionRequestDTO request) throws Exception;
	public <R> R retrieve(RegionRequestDTO request) throws Exception;
}
