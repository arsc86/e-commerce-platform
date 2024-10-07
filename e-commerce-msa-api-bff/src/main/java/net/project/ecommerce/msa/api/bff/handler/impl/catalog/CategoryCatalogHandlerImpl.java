package net.project.ecommerce.msa.api.bff.handler.impl.catalog;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.project.ecommerce.dependency.api.constants.EcommerceConstants;
import net.project.ecommerce.dependency.api.constants.catalog.CatalogCategoryMethodConstants;
import net.project.ecommerce.dependency.api.dto.catalog.request.CategoryRequestDTO;
import net.project.ecommerce.dependency.api.interfaces.ICrudComplentaryHandler;
import net.project.ecommerce.dependency.api.interfaces.ICrudHandler;
import net.project.ecommerce.dependency.constants.GeneralConstants;
import net.project.ecommerce.dependency.enums.EnumChannelType;
import net.project.ecommerce.dependency.interfaces.IGenericProducer;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.dependency.vo.GenericProducerVO;
import net.project.ecommerce.msa.api.bff.cons.ApiBffConstants;

@Component(ApiBffConstants.CATEGORY_CATALOG_BEAN)
public class CategoryCatalogHandlerImpl implements ICrudHandler,ICrudComplentaryHandler{
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private BeanFactory factory;
	
	private IGenericProducer producer;
	
	@Override
	public <R, T> R retrieve(T data) throws Exception {
		return execute(data,CatalogCategoryMethodConstants.CATEGORY_RETRIEVE_METHOD);
	}
	
	@Override
	public <R, T> R create(T data) throws Exception {
		return execute(data,CatalogCategoryMethodConstants.CATEGORY_CREATE_METHOD);			
	}

	@Override
	public <R, T> R update(T data) throws Exception {
		return execute(data,CatalogCategoryMethodConstants.CATEGORY_UPDATE_METHOD);			
	}

	@Override
	public <R, T> R delete(T data) throws Exception {
		return execute(data,CatalogCategoryMethodConstants.CATEGORY_DELETE_METHOD);		
	}

	@Override
	public <R, T> R createBy(T data) throws Exception {
		return execute(data,CatalogCategoryMethodConstants.CATEGORY_CREATE_BY_METHOD);		
	}
	
	private <R,T> R execute(T data, String op) throws Exception
	{
		CategoryRequestDTO request = Format.objectMapping(data, CategoryRequestDTO.class);		
		producer = (IGenericProducer) factory.getBean(GeneralConstants.MESSAGE_PRODUCER);
		return  producer.process(GenericProducerVO.<CategoryRequestDTO>builder()
                .transactionId(UUID.randomUUID().toString())
                .channelType(EnumChannelType.GRPC)                
                .option(op)          
                .context(EcommerceConstants.CONTEXT_CATALOG_CATEGORY)
                .service(EcommerceConstants.CATALOG_MESSAGE_PRODUCER)
                .payload(request)
                .build());
	}

}
