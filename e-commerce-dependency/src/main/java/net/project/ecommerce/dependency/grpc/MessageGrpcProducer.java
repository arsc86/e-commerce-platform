package net.project.ecommerce.dependency.grpc;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import net.project.ecommerce.dependency.constants.GeneralConstants;
import net.project.ecommerce.dependency.dto.request.GenericRequestDTO;
import net.project.ecommerce.dependency.dto.request.InternalRequestDTO;
import net.project.ecommerce.dependency.dto.response.GenericResponseDTO;
import net.project.ecommerce.dependency.interfaces.IGenericMessageProducer;
import net.project.ecommerce.dependency.util.Validator;

/**
 * gRPC message producer class
 *
 * @author Allan Suarez <mailto:arsc86@gmailcom>
 */
@SuppressWarnings("rawtypes")
@Service(GeneralConstants.GRPC_MESSAGE_PRODUCER)
@Primary
public class MessageGrpcProducer implements IGenericMessageProducer<InternalRequestDTO, GenericResponseDTO> {
	
	Logger log = LogManager.getLogger(this.getClass());	

	@Override
	public GenericResponseDTO sendMessage(InternalRequestDTO data) throws Exception {
		
		GenericResponseDTO response=null;
		
		try {
			Validator.validateFieldObjectFromTemplate(data, Arrays.asList("transactionId", "option", "payload"));
						
			log.info("Sending grpc request with Remote Option : {{}} , with Id : {}", data.getOption(), data.getTransactionId());
						
			if (data.getBlockingStub() == null) 
			{
				throw new Exception(new StringBuilder("No grpc connection configured for the option [")
						            .append(data.getOption()).append("]").toString());
			}
			
			//Send message into a GenericRequest and it response into a GenericResponse
			response = GrpcUtil.send(data.getBlockingStub(),
									 data,
									 GenericRequestDTO.class,
									 GenericResponseDTO.class
									 );
			
			if (response.getPayload() == null)
			{
				log.error("Grpc Response Payload does not exist or is null");
				throw new Exception("Grpc Response Payload does not exist or is null");
			}														
		} 
		catch (Exception e) 
		{
			log.error("An error ocurred while sending grpc request : {}, ; {}",data.getTransactionId(),e.getMessage());
			throw new Exception(new StringBuilder("An error ocurred while sending grpc request [")
								.append(data.getOption()).append("] whit Id: ")
								.append(data.getTransactionId()).toString(),e);
		}			
		return response;
	}
}
