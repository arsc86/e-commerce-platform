package net.project.ecommerce.msa.catalog.service;

import net.project.ecommerce.dependency.api.dto.catalog.request.CharacteristicRequestDTO;

public interface ICharcteristicCatalogHandler {
	
	public <R> R create(CharacteristicRequestDTO data) throws Exception;
	public <R> R update(CharacteristicRequestDTO request) throws Exception;
	public void delete(CharacteristicRequestDTO request) throws Exception;
	public <R> R retrieve(CharacteristicRequestDTO request) throws Exception;	

}
