package net.project.ecommerce.msa.api.bff.handler.impl.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import net.project.ecommerce.dependency.api.dto.product.response.CategoryResponseDTO;
import net.project.ecommerce.msa.api.bff.cons.ApiBffConstants;
import net.project.ecommerce.msa.api.bff.handler.ICatalogHandler;

@Component(ApiBffConstants.PRODUCT_CATALOG_BEAN)
@Primary
public class ProductCatalogHandlerImpl implements ICatalogHandler{
	
	Logger log = LogManager.getLogger(this.getClass());

	@SuppressWarnings("unchecked")
	@Override
	public <R, T> R getBy(T data) throws Exception {
		
		List<CategoryResponseDTO> list = new ArrayList<>();
		list.add(CategoryResponseDTO.builder()
				.id(1L)
				.name("Category A")
				.provider("Provider A")
				.status("active")
				.createdBy("user")
				.creationAt(new Date())
				.build());
		return (R) list ;
	}
	
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

	


}
