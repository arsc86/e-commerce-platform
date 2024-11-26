package net.project.ecommerce.msa.api.bff.handler.impl.catalog;

import java.util.UUID;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.project.ecommerce.dependency.api.constants.EcommerceConstants;
import net.project.ecommerce.dependency.api.constants.catalog.CatalogRegionMethodConstants;
import net.project.ecommerce.dependency.api.dto.catalog.request.RegionRequestDTO;
import net.project.ecommerce.dependency.api.interfaces.ICrudHandler;
import net.project.ecommerce.dependency.constants.GeneralConstants;
import net.project.ecommerce.dependency.enums.EnumChannelType;
import net.project.ecommerce.dependency.interfaces.IGenericProducer;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.dependency.vo.GenericProducerVO;
import net.project.ecommerce.msa.api.bff.cons.ApiBffConstants;

@Component(ApiBffConstants.REGION_CATALOG_BEAN)
public class RegionCatalogHandlerImpl implements ICrudHandler{
	
	@Autowired
	private BeanFactory factory;
	
	private IGenericProducer producer;

	@Override
	public <R, T> R retrieve(T data) throws Exception {
		return execute(data,CatalogRegionMethodConstants.REGION_RETRIEVE_METHOD);
	}

	@Override
	public <R, T> R create(T data) throws Exception {
		return execute(data,CatalogRegionMethodConstants.REGION_CREATE_METHOD);
	}

	@Override
	public <R, T> R update(T data) throws Exception {
		return execute(data,CatalogRegionMethodConstants.REGION_UPDATE_METHOD);
	}

	@Override
	public <R, T> R delete(T data) throws Exception {
		return execute(data,CatalogRegionMethodConstants.REGION_DELETE_METHOD);
	}
	
	private <R,T> R execute(T data, String op) throws Exception
	{
		RegionRequestDTO request = Format.objectMapping(data, RegionRequestDTO.class);		
		producer = (IGenericProducer) factory.getBean(GeneralConstants.MESSAGE_PRODUCER);
		return  producer.process(GenericProducerVO.<RegionRequestDTO>builder()
                .transactionId(UUID.randomUUID().toString())
                .channelType(EnumChannelType.GRPC)                
                .option(op)      
                .context(EcommerceConstants.CONTEXT_CATALOG_REGION)
                .service(EcommerceConstants.CATALOG_MESSAGE_PRODUCER)
                .payload(request)
                .build());
	}

}
