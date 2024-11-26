package net.project.ecommerce.msa.api.bff.handler.impl.catalog;

import java.util.UUID;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.project.ecommerce.dependency.api.constants.EcommerceConstants;
import net.project.ecommerce.dependency.api.constants.catalog.CatalogWarehouseMethodConstants;
import net.project.ecommerce.dependency.api.dto.catalog.request.WarehouseRequestDTO;
import net.project.ecommerce.dependency.api.interfaces.ICrudHandler;
import net.project.ecommerce.dependency.constants.GeneralConstants;
import net.project.ecommerce.dependency.enums.EnumChannelType;
import net.project.ecommerce.dependency.interfaces.IGenericProducer;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.dependency.vo.GenericProducerVO;
import net.project.ecommerce.msa.api.bff.cons.ApiBffConstants;

@Component(ApiBffConstants.WAREHOUSE_CATALOG_BEAN)
public class WarehouseCatalogHandlerImpl implements ICrudHandler{
	
	@Autowired
	private BeanFactory factory;
	
	private IGenericProducer producer;

	@Override
	public <R, T> R retrieve(T data) throws Exception {
		return execute(data,CatalogWarehouseMethodConstants.WAREHOUSE_RETRIEVE_METHOD);
	}

	@Override
	public <R, T> R create(T data) throws Exception {
		return execute(data,CatalogWarehouseMethodConstants.WAREHOUSE_CREATE_METHOD);
	}

	@Override
	public <R, T> R update(T data) throws Exception {
		return execute(data,CatalogWarehouseMethodConstants.WAREHOUSE_UPDATE_METHOD);
	}

	@Override
	public <R, T> R delete(T data) throws Exception {
		return execute(data,CatalogWarehouseMethodConstants.WAREHOUSE_DELETE_METHOD);
	}
	
	private <R,T> R execute(T data, String op) throws Exception
	{
		WarehouseRequestDTO request = Format.objectMapping(data, WarehouseRequestDTO.class);		
		producer = (IGenericProducer) factory.getBean(GeneralConstants.MESSAGE_PRODUCER);
		return  producer.process(GenericProducerVO.<WarehouseRequestDTO>builder()
                .transactionId(UUID.randomUUID().toString())
                .channelType(EnumChannelType.GRPC)                
                .option(op)      
                .context(EcommerceConstants.CONTEXT_CATALOG_WAREHOUSE)
                .service(EcommerceConstants.CATALOG_MESSAGE_PRODUCER)
                .payload(request)
                .build());
	}

}
