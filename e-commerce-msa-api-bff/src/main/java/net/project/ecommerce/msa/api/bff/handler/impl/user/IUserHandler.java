package net.project.ecommerce.msa.api.bff.handler.impl.user;

import jakarta.servlet.http.HttpServletRequest;
import net.project.ecommerce.msa.api.bff.enums.EnumChangePasswordType;

public interface IUserHandler {
	
	public <R,T> R authenticate(T data)throws Exception;
	public void logout(HttpServletRequest request)throws Exception;
	public <R,T> R refreshToken(HttpServletRequest request) throws Exception;
	public <R,T> R recoveryPassword(T data) throws Exception;
	public <T> void validateRecoveryToken(T data) throws Exception;
	public <R,T> R changePassword(T data, EnumChangePasswordType type,HttpServletRequest request) throws Exception;	
	
}
