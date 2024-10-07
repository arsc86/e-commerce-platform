package net.project.ecommerce.msa.user.handler;

import net.project.ecommerce.dependency.api.dto.user.request.UserRequestDTO;

public interface IUserHandler {
		
	public <R> R create(UserRequestDTO data) throws Exception;
	public <R> R update(UserRequestDTO request) throws Exception;
	public void delete(UserRequestDTO request) throws Exception;
	public <R> R retrieve(UserRequestDTO request) throws Exception;		
}
