package net.project.ecommerce.msa.user.service;

import net.project.ecommerce.dependency.api.dto.user.request.UserRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.request.ValidateEmailRequestDTO;

public interface IAuthenticationHandler {
	
	public <R> R authenticate(UserRequestDTO request) throws Exception;	
	public <R> R getLastPassword(UserRequestDTO request) throws Exception;
	public <R> R updatePassword(UserRequestDTO request) throws Exception;
	public <R> R getRefreshToken(UserRequestDTO request) throws Exception;
	public <R> R updateRefreshToken(UserRequestDTO request) throws Exception;
	public <R> R validateMailUser(ValidateEmailRequestDTO request) throws Exception;

}
