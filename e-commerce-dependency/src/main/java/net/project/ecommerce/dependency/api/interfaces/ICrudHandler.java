package net.project.ecommerce.dependency.api.interfaces;

public interface ICrudHandler {
	
	public <R,T> R create(T data) throws Exception;
	public <R,T> R update(T data) throws Exception;
	public <R,T> R delete(T data) throws Exception;
	public <R,T> R retrieve(T data) throws Exception;

}
