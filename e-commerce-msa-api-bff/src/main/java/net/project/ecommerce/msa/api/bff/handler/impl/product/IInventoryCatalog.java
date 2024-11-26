package net.project.ecommerce.msa.api.bff.handler.impl.product;

public interface IInventoryCatalog {
	
	public <R,T> R stockInOut(T data) throws Exception;
	public <R,T> R retrieve(T data) throws Exception;

}
