package net.project.ecommerce.dependency.grpc;
//package net.project.ecommerce.dependency.grpc;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.stereotype.Service;
//
//import ec.telconet.telcoboss.common.dependency.dispatcher.ICompleteResultDispatcher;
//import ec.telconet.telcoboss.common.dependency.dto.request.GenericRequestDTO;
//import ec.telconet.telcoboss.common.dependency.dto.response.GenericResponseDTO;
//import ec.telconet.telcoboss.common.dependency.exception.GenericException;
//import io.grpc.stub.StreamObserver;
//import net.project.ecommerce.dependency.grpc.configuration.GrpcRequest;
//import net.project.ecommerce.dependency.grpc.configuration.GrpcResponse;
//
//@Service("MessageGrpcConsumer")
//public class MessageGrpcConsumer{
//
//	Logger log = LogManager.getLogger(this.getClass());
//	
//	public void process(GrpcRequest request, StreamObserver<GrpcResponse> responseObserver,
//                        ICompleteResultDispatcher dispatcher) {
//		GenericRequestDTO<?> genericRequestDTO;
//		GenericResponseDTO<String> genericResponseDTO = null;		
//
//		String option = "not defined";
//		String idTransaction = "not defined";
//		try {
//			try {
//				genericRequestDTO =  GrpcUtilService.generateClassFromAnyData(request.getData().getValue().toByteArray(), GenericRequestDTO.class);
//				
//				idTransaction = genericRequestDTO.getTransactionId();
//				option        = genericRequestDTO.getOption();
//				
//				log.info("A Grpc request has been received, OP: |{}|, Transaction: |{}|", option, idTransaction);				
//
//				genericResponseDTO = dispatcher.dispatch(genericRequestDTO);
//
//				log.info("The Grpc request with option {} and ID {},  has been successfully proccessed",option, idTransaction);
//			} 
//			catch (Exception e) 
//			{
//				throw new GenericException(new StringBuilder(" An error ocurred while processing GRPC request with option ")
//								.append(option).append(" and Id ").append(idTransaction).toString(), e);
//			}
//		}catch (GenericException e) {
//			genericResponseDTO = GenericResponseDTO.<String>builder()
//					.code(e.getCodeError())
//					.status(e.getStatusError())
//					.message(e.getUserMessageError())
//					.build();
//		}
//		GrpcResponse genericResponse = GrpcResponse
//				.newBuilder()
//				.setData(GrpcUtilService.generateAnyData(genericResponseDTO, GenericResponseDTO.class))
//				.build();
//		//set the response object
//		responseObserver.onNext(genericResponse);
//		//mark process is completed
//		responseObserver.onCompleted();		
//	}
//}
