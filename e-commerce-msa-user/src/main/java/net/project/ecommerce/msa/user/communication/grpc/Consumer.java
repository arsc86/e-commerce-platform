package net.project.ecommerce.msa.user.communication.grpc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.grpc.stub.StreamObserver;
import net.project.ecommerce.dependency.api.constants.catalog.IConstantDefinition;
import net.project.ecommerce.dependency.constants.GeneralConstants;
import net.project.ecommerce.dependency.dto.request.GenericRequestDTO;
import net.project.ecommerce.dependency.dto.response.GenericResponseDTO;
import net.project.ecommerce.dependency.grpc.GrpcUtil;
import net.project.ecommerce.dependency.grpc.configuration.GrpcEcommerceGenericServiceGrpc.GrpcEcommerceGenericServiceImplBase;
import net.project.ecommerce.dependency.grpc.configuration.GrpcRequest;
import net.project.ecommerce.dependency.grpc.configuration.GrpcResponse;
import net.project.ecommerce.dependency.util.Utils;

@Service
public class Consumer extends GrpcEcommerceGenericServiceImplBase {
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	BeanFactory beanFactory;
	
	@Autowired
	private IConstantDefinition constantDefinition;

	@Override
	public void getData(GrpcRequest request, StreamObserver<GrpcResponse> responseObserver) {
		
		GenericResponseDTO<?> genericResponseDTO = null;
		GenericRequestDTO<?> genericRequestDTO   = null;
		String option        = null;		
		String idTransaction = null;
		String context       = null;
		
		try 
		{
			//Getting the information sent via gRPC
			genericRequestDTO =  GrpcUtil.generateClassFromAnyData(request.getData().getValue().toByteArray(),GenericRequestDTO.class);
				
			option        = genericRequestDTO.getOption();		
			idTransaction = genericRequestDTO.getTransactionId();	
			context       = genericRequestDTO.getContext();
			
			log.info("A Grpc request has been received, context: {{}} , method: {{}}, Transaction: {}",context, option, idTransaction);	
			
			constantDefinition = beanFactory.getBean(context,IConstantDefinition.class);
			
			//Applying strategy to consume services
			Object response = Utils.getExecutionMethodResponseFromMap(constantDefinition.getContextMap().get(option), 
					                                                  genericRequestDTO.getPayload(), 
					                                                  genericRequestDTO.getOption(), 
					                                                  genericRequestDTO.getTransactionId(),
					                                                  beanFactory
					                                                  );																					
			genericResponseDTO = GenericResponseDTO.<Object>builder()
					.code(GeneralConstants.CODE_OK)
					.status(GeneralConstants.SUCCESS_STATUS_DEFAULT)
					.message(GeneralConstants.SUCCESS_MESSAGE_DEFAULT)
					.payload(response!=null?response:new Object())
					.build();
			
			log.info("The Grpc request with method {{}} and ID {},  has been successfully proccessed",option, idTransaction);
		}
		catch (Exception e) 
		{
			genericResponseDTO = GenericResponseDTO.<Object>builder()
					.code(GeneralConstants.FAILURE_CODE_DEFAULT)
					.status(GeneralConstants.FAILURE_STATUS_DEFAULT)
					.message(e.getMessage())
					.payload(new Object())
					.build();
			
			log.error(GeneralConstants.MSG_ERROR_GRPC_REQUEST_PROCESING+"{{}} and error : {}",option,e);
		}
		
		GrpcResponse genericResponse = GrpcResponse
				.newBuilder()
				.setData(GrpcUtil.generateAnyData(genericResponseDTO, GenericResponseDTO.class))
				.build();
		//set the response object
		responseObserver.onNext(genericResponse);
		//mark process is completed
		responseObserver.onCompleted();
	}
}
