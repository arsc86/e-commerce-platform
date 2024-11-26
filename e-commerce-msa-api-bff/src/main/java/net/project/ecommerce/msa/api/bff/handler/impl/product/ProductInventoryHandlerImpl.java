package net.project.ecommerce.msa.api.bff.handler.impl.product;

import java.util.UUID;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.project.ecommerce.dependency.api.constants.EcommerceConstants;
import net.project.ecommerce.dependency.api.constants.catalog.CatalogProductInventoryMethodConstants;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductInventoryRequestDTO;
import net.project.ecommerce.dependency.constants.GeneralConstants;
import net.project.ecommerce.dependency.enums.EnumChannelType;
import net.project.ecommerce.dependency.interfaces.IGenericProducer;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.dependency.vo.GenericProducerVO;
import net.project.ecommerce.msa.api.bff.cons.ApiBffConstants;

@Component(ApiBffConstants.PRODUCT_INVENTORY_BEAN)
public class ProductInventoryHandlerImpl implements IInventoryCatalog {
	
	@Autowired
	private BeanFactory factory;
	
	private IGenericProducer producer;

	@Override
	public <R, T> R stockInOut(T data) throws Exception {
		return execute(data,CatalogProductInventoryMethodConstants.PRODUCT_INVENTORY_STOCKING_METHOD);
	}

	@Override
	public <R, T> R retrieve(T data) throws Exception {
		return execute(data,CatalogProductInventoryMethodConstants.PRODUCT_INVENTORY_RETRIEVE_METHOD);
	}
	
	private <R,T> R execute(T data, String op) throws Exception
	{
		ProductInventoryRequestDTO request = Format.objectMapping(data, ProductInventoryRequestDTO.class);		
		producer = (IGenericProducer) factory.getBean(GeneralConstants.MESSAGE_PRODUCER);
		return  producer.process(GenericProducerVO.<ProductInventoryRequestDTO>builder()
                .transactionId(UUID.randomUUID().toString())
                .channelType(EnumChannelType.GRPC)                
                .option(op)          
                .context(EcommerceConstants.CONTEXT_CATALOG_PRODUCT_INVENTORY)
                .service(EcommerceConstants.CATALOG_MESSAGE_PRODUCER)
                .payload(request)
                .build());
	}

}
