package net.project.ecommerce.msa.user.service;

import net.project.ecommerce.dependency.api.dto.user.request.UserRequestDTO;

public interface IAuthenticationHandler {
	
	public <R> R authenticate(UserRequestDTO request) throws Exception;	
	public <R> R getLastPassword(UserRequestDTO request) throws Exception;
	public <R> R updatePassword(UserRequestDTO request) throws Exception;
	public <R> R getRefreshToken(UserRequestDTO request) throws Exception;
	public <R> R updateRefreshToken(UserRequestDTO request) throws Exception;

}
