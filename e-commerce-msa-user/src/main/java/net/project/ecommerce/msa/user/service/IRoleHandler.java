package net.project.ecommerce.msa.user.service;

import net.project.ecommerce.dependency.api.dto.user.request.RoleRequestDTO;

public interface IRoleHandler {
	
	public <R> R create(RoleRequestDTO data) throws Exception;	
	public <R> R update(RoleRequestDTO request) throws Exception;
	public void delete(RoleRequestDTO request) throws Exception;
	public <R> R retrieve(RoleRequestDTO request) throws Exception;

}
