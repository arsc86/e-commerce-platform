package net.project.ecommerce.msa.user.service;

import net.project.ecommerce.dependency.api.dto.user.request.UserRequestDTO;

public interface IUserCrudHandler {

	public <R> R create(UserRequestDTO data) throws Exception;
	public <R> R update(UserRequestDTO data) throws Exception;
	public void delete(UserRequestDTO data) throws Exception;
	public <R> R retrieve(UserRequestDTO data) throws Exception;
}
