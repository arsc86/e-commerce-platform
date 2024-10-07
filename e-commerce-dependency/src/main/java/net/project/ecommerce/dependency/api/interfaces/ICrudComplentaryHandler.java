package net.project.ecommerce.dependency.api.interfaces;

public interface ICrudComplentaryHandler {
	
	public <R,T> R createBy(T data) throws Exception;

}
