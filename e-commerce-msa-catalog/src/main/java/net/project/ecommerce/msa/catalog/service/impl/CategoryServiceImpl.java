package net.project.ecommerce.msa.catalog.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import net.project.ecommerce.dependency.api.constants.user.UserMethodConstants;
import net.project.ecommerce.dependency.api.dto.product.request.CategoryRequestDTO;
import net.project.ecommerce.msa.catalog.service.ICategoryCatalogHandler;

@Service(UserMethodConstants.USER_SERVICE_BEAN)
@Primary
public class CategoryServiceImpl implements ICategoryCatalogHandler{
	
	Logger log = LogManager.getLogger(this.getClass());

	@Override
	public <R> R create(CategoryRequestDTO data) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <R> R update(CategoryRequestDTO request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(CategoryRequestDTO request) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <R> R retrieve(CategoryRequestDTO request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}		
	
	
	
	
}
