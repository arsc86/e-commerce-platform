package net.project.ecommerce.msa.api.bff.handler;

public interface ICatalogHandler {
		
	public <R,T> R getBy(T data) throws Exception;	
	public <R,T> R create(T data) throws Exception;
	public <R,T> R update(T data) throws Exception;
	public <R,T> R delete(T data) throws Exception;
	

}
