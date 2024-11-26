package net.project.ecommerce.msa.api.bff.handler.impl.catalog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import net.project.ecommerce.dependency.api.interfaces.ICrudHandler;
import net.project.ecommerce.msa.api.bff.cons.ApiBffConstants;

@Component(ApiBffConstants.PRODUCT_CATALOG_BEAN)
public class ProductCatalogHandlerImpl implements ICrudHandler{
	
	Logger log = LogManager.getLogger(this.getClass());

	@Override
	public <R, T> R create(T data) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <R, T> R update(T data) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <R, T> R delete(T data) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <R, T> R retrieve(T data) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	

}
