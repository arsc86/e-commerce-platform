package net.project.ecommerce.msa.catalog.service;

import net.project.ecommerce.dependency.api.dto.catalog.request.WarehouseRequestDTO;

public interface IWarehouseCatalogHandler {
	
	public <R> R create(WarehouseRequestDTO data) throws Exception;	
	public <R> R update(WarehouseRequestDTO request) throws Exception;
	public void delete(WarehouseRequestDTO request) throws Exception;
	public <R> R retrieve(WarehouseRequestDTO request) throws Exception;
}
