package net.project.ecommerce.msa.api.bff.handler.impl.user;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.project.ecommerce.dependency.api.constants.EcommerceConstants;
import net.project.ecommerce.dependency.api.constants.user.UserMethodConstants;
import net.project.ecommerce.dependency.api.dto.user.request.UserRequestDTO;
import net.project.ecommerce.dependency.api.interfaces.ICrudHandler;
import net.project.ecommerce.dependency.constants.GeneralConstants;
import net.project.ecommerce.dependency.dto.response.GenericResponseDTO;
import net.project.ecommerce.dependency.enums.EnumChannelType;
import net.project.ecommerce.dependency.interfaces.IGenericProducer;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.dependency.vo.GenericProducerVO;
import net.project.ecommerce.msa.api.bff.cons.ApiBffConstants;

@Component(ApiBffConstants.USER_ADDRESS_CRUD_BEAN)
public class UserAddressHandlerImpl implements ICrudHandler{
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private BeanFactory factory;
	
	private IGenericProducer producer; 

	@Override
	public <R, T> R create(T data) throws Exception {
		log.info("Adding address to customer");
		UserRequestDTO request = Format.objectMapping(data, UserRequestDTO.class);		
		producer = (IGenericProducer) factory.getBean(GeneralConstants.MESSAGE_PRODUCER);
        return producer.process(GenericProducerVO.<UserRequestDTO>builder()
                .transactionId(UUID.randomUUID().toString())
                .channelType(EnumChannelType.GRPC)                
                .option(UserMethodConstants.USER_ADDRESS_CREATE_METHOD)          
                .service(EcommerceConstants.USER_MESSAGE_PRODUCER)
                .payload(request)
                .build());
	}

	@Override
	public <R, T> R update(T data) throws Exception {
		log.info("Updating customer address");
		UserRequestDTO request = Format.objectMapping(data, UserRequestDTO.class);		
		producer = (IGenericProducer) factory.getBean(GeneralConstants.MESSAGE_PRODUCER);
        return producer.process(GenericProducerVO.<UserRequestDTO>builder()
                .transactionId(UUID.randomUUID().toString())
                .channelType(EnumChannelType.GRPC)                
                .option(UserMethodConstants.USER_ADDRESS_UPDATE_METHOD)          
                .service(EcommerceConstants.USER_MESSAGE_PRODUCER)
                .payload(request)
                .build());
	}

	@Override
	public <R, T> R delete(T data) throws Exception {
		log.info("Deleting customer address");
		UserRequestDTO request = Format.objectMapping(data, UserRequestDTO.class);		
		producer = (IGenericProducer) factory.getBean(GeneralConstants.MESSAGE_PRODUCER);
        return producer.process(GenericProducerVO.<UserRequestDTO>builder()
                .transactionId(UUID.randomUUID().toString())
                .channelType(EnumChannelType.GRPC)                
                .option(UserMethodConstants.USER_ADDRESS_DELETE_METHOD)          
                .service(EcommerceConstants.USER_MESSAGE_PRODUCER)
                .payload(request)
                .build());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R, T> R retrieve(T data) throws Exception {
		log.info("Retrieving customer addresses");
		UserRequestDTO request = Format.objectMapping(data, UserRequestDTO.class);		
		producer = (IGenericProducer) factory.getBean(GeneralConstants.MESSAGE_PRODUCER);
        GenericResponseDTO<?> response = producer.process(GenericProducerVO.<UserRequestDTO>builder()
                .transactionId(UUID.randomUUID().toString())
                .channelType(EnumChannelType.GRPC)                
                .option(UserMethodConstants.USER_ADDRESS_RETRIEVE_METHOD)          
                .service(EcommerceConstants.USER_MESSAGE_PRODUCER)
                .payload(request)
                .build());
        return (R) response.getPayload();
	}

}
