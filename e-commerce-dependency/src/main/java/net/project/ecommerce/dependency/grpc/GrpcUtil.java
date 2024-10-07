package net.project.ecommerce.dependency.grpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.protobuf.Any;
import com.google.protobuf.Any.Builder;
import com.google.protobuf.ByteString;

import net.project.ecommerce.dependency.grpc.configuration.GrpcEcommerceGenericServiceGrpc.GrpcEcommerceGenericServiceBlockingStub;
import net.project.ecommerce.dependency.grpc.configuration.GrpcRequest;
import net.project.ecommerce.dependency.grpc.configuration.GrpcResponse;

public class GrpcUtil {
	
	static ObjectMapper objectMapper;
	
	static Logger logger = Logger.getLogger(GrpcUtil.class.getName());
			
	@SuppressWarnings("deprecation")
	public static <T> Builder generateAnyData(T data, Class<T> clazz) {
		
		objectMapper = new ObjectMapper();
				
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		try 
		{	
			objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);	
        	objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			objectMapper.writeValue(byteArrayOutputStream,data);
			return Any.newBuilder().setTypeUrl(clazz.getCanonicalName())
					               .setValue(ByteString.copyFrom(byteArrayOutputStream.toByteArray()));
		} 
		catch (IOException e) 
		{
			logger.log(Level.SEVERE, "Error generating Generic (ANY) data Type for gRPC", e);
		}
		
		return null;		
	}
		
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static <T,U> U generateClassFromAnyData(byte[] genericByteArray,Class<T> classType)
	{		
		objectMapper = new ObjectMapper();
		
        try 
        {        	
        	objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);	
        	objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        	return (U) objectMapper.readValue(genericByteArray, classType);			
		} 
        catch (IOException e) 
        {
        	logger.log(Level.SEVERE, "Error generating Generic Class from gRPC byteArray (ByteString)", e);
		}
		
		return null;
		
	}
	
	/**
	 * Execute generic grpc communication
	 *
	 * @param @GrpcEcommerceGenericServiceBlockingStub blockingStub
	 * @param <T> data
	 * @param @Class<T> inputClass
	 * @param @Class<U> ouputClass
	 *
	 * @return U
	 */
	@SuppressWarnings("unchecked")
	public static <T, U> U send(GrpcEcommerceGenericServiceBlockingStub blockingStub,
			                    T data, 
                                Class<T> inputClass,
                                Class<U> ouputClass) 
	{
		 GrpcResponse grpcResponse = blockingStub.getData(GrpcRequest
														  .newBuilder()
														  .setData(generateAnyData(data,inputClass))
														  .build()
														 );			 	

		 return (U) generateClassFromAnyData(grpcResponse.getData().getValue().toByteArray(),ouputClass); 
                                         
	}
	
	public static Class<?> getGrpcTypeClass(GrpcRequest request) throws ClassNotFoundException
	{
		return Class.forName(request.getData().getTypeUrl());		
	}		

}
