package net.project.ecommerce.dependency.util;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import net.project.ecommerce.dependency.constants.GeneralConstants;
import net.project.ecommerce.dependency.exception.CustomException;
import net.project.ecommerce.dependency.grpc.GrpcChannelManagerMap;
import net.project.ecommerce.dependency.grpc.configuration.GrpcEcommerceGenericServiceGrpc;
import net.project.ecommerce.dependency.interfaces.ITokenGenerate;

@SuppressWarnings("unchecked")
@Component
public class Utils {

	static Logger log = LogManager.getLogger(Utils.class);
	
	@Autowired
	private ITokenGenerate tokenGenerate;
		
	private static GrpcChannelManagerMap channelManager;
	
	public Utils(GrpcChannelManagerMap manager) {
		channelManager = manager;
	}


	public static <T, R> R executeMethod(T clazz, String methodName, Object... params)
			throws Exception {
		Method method = null;
		R objectResult = null;
		try {
			try {
				Class<?>[] paramTypes = getObjectsType(params);
				if (paramTypes != null && paramTypes.length > 0 && paramTypes[0] != null) {
					method = clazz.getClass().getDeclaredMethod(methodName, paramTypes);
				} else {
					method = clazz.getClass().getDeclaredMethod(methodName);
				}
				if (method != null) {
					method.setAccessible(true);
					if (paramTypes != null && paramTypes.length > 0) {
						objectResult = (R) method.invoke(clazz, params);
					} else {
						objectResult = (R) method.invoke(clazz);
					}
				} else {
					throw new Exception("His method has not been found ");	
				}
				return objectResult;
			} 
			catch (InvocationTargetException e) 
			{				
				 Throwable cause = e.getCause();
				 log.error("Internal Error executing method : {}",cause.getMessage());
				 if (cause instanceof CustomException) 
				 {		                
					 throw new RuntimeException(cause.getMessage());
		         }
				
				if(e.getCause() != null && e.getCause().getClass() == Exception.class) {
					throw (Exception) e.getCause();
				}else {					
					throw new Exception(cause.getMessage());
				}
				
			} catch (Exception e) {
				
				if(e.getCause() != null && e.getCause().getClass() == Exception.class) {
					throw (Exception) e.getCause();
				}else {
					throw (e instanceof Exception) ? (Exception) e : new Exception(e.getMessage());
				}
				
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static <R> R executeMethodFromBean(String serviceBeanName, BeanFactory factory, String methodName, Object... params)
			throws Exception {
		Object service = factory.getBean(serviceBeanName);
		return (R) executeMethod(service, methodName, params);
	}


	public static Class<?>[] getObjectsType(Object... params) throws Exception {
		try {
			Class<?>[] arrayClass = null;
			if (params != null && params.length > 0 && params[0] != null && params[0] != "none") {
				arrayClass = new Class<?>[params.length];
				for (int i = 0; i < params.length; i++) {
					arrayClass[i] = params[i].getClass();
				}
			}
			return arrayClass;
		} catch (Exception e) {
			log.error("An error ocurred while getting object types from {}", Arrays.toString(params));
			throw new Exception(e.getMessage());
		}
	}

	
	public static <R> R buildBean(BeanFactory factory, String ... params)
			throws Exception {
		StringBuilder beanName = new StringBuilder("");
		try {
			if (params != null && params.length > 0) {
				for (String param : params) {
					beanName.append(param);
				}
			} else {
				beanName.append("not defined");
			}
			return (R) factory.getBean(beanName.toString());
		} catch (Exception e) {
			log.error("The requested bean {} could not be instantiated", beanName);
			throw new Exception(e.getMessage());
		}
	}


	public static GrpcEcommerceGenericServiceGrpc.GrpcEcommerceGenericServiceBlockingStub getBlockingStub (String serverClient,
																						 int grpcIdleTimeout,
																						 int grpcDeadlineTimeout){
		return channelConfiguration(serverClient,
				                    grpcIdleTimeout,
				                    grpcDeadlineTimeout,
				                    GeneralConstants.GRPC_MAX_MSG_BYTES,
				                    GeneralConstants.GRPC_MAX_MSG_BYTES);
	}
	
	public static GrpcEcommerceGenericServiceGrpc.GrpcEcommerceGenericServiceBlockingStub getBlockingStub (String serverClient,
																						 int grpcIdleTimeout,
																						 int grpcDeadlineTimeout,
																						 int inboundMessageSize,
																						 int outboundMessageSize){
		return channelConfiguration(serverClient,
				                    grpcIdleTimeout,
				                    grpcDeadlineTimeout,
				                    inboundMessageSize,
				                    outboundMessageSize);
	}
	
	private static GrpcEcommerceGenericServiceGrpc.GrpcEcommerceGenericServiceBlockingStub channelConfiguration(String serverClient,
																							 int grpcIdleTimeout,
																							 int grpcDeadlineTimeout,
																							 int inboundMessageSize,
																							 int outboundMessageSize)
	{							
		if(serverClient!=null && !serverClient.isEmpty())
		{
			ManagedChannel channel = channelManager.getMannagedChannel(serverClient);
				
			if(channel==null) 
			{			
				channel = grpcInitConnection(inboundMessageSize,grpcIdleTimeout,serverClient);
			}			
			else
			{
				ConnectivityState currentState = channel.getState(false);	
													
				if(currentState.toString().equalsIgnoreCase(ConnectivityState.SHUTDOWN.toString()) || channel.isShutdown())
				{
					log.info("Channel gRPC for server : {} was closed...",serverClient);	
					channel = grpcInitConnection(inboundMessageSize,grpcIdleTimeout,serverClient);
				}
			}
						
			return GrpcEcommerceGenericServiceGrpc.newBlockingStub(channel)
					.withWaitForReady()
					.withMaxOutboundMessageSize(outboundMessageSize)
					.withMaxInboundMessageSize(inboundMessageSize)
					.withDeadlineAfter(grpcDeadlineTimeout, TimeUnit.SECONDS);					
		}
		
		return null;
		
	}
	
	private static ManagedChannel grpcInitConnection(int inboundMessageSize, int grpcIdleTimeout, String serverClient) {
		ManagedChannel channel = null;
		ConnectivityState currentState;
		log.info("Openning gRPC channel for server : {}",serverClient);	
		channel =  ManagedChannelBuilder.forTarget(serverClient)
				.usePlaintext()
				.maxInboundMessageSize(inboundMessageSize)				
				.idleTimeout(grpcIdleTimeout, TimeUnit.MINUTES)		
				.keepAliveTime(30, TimeUnit.SECONDS)
			    .keepAliveTimeout(5, TimeUnit.SECONDS)
				.build();						
		currentState = channel.getState(false);
		log.info("Channel opened for server : {}, with status : {}",serverClient,currentState);
		channelManager.setManagedChannel(serverClient, channel);
		return channel;
	}
	
	
	public static <R,T> R getExecutionMethodResponseFromMap(Map<String, Object> mapServices, 
					                                        T payload, 
					                                        String option, 
					                                        String idTransaction,
					                                        BeanFactory factory) throws Exception
	{
		R response = null;
						
		try
		{
			if(mapServices!=null)
			{
				if(mapServices.size()==1)
				{
					String beanService   = "";
					String method        = "";					
					Class<?> payloadCass = null;										
										
					for (Map.Entry<String, Object> entrada : mapServices.entrySet()) 
					{
						beanService = entrada.getKey();//ServiceBean
						
						Map<String, Object> classMap = (Map<String, Object>) entrada.getValue();
						
						if(classMap.size()==1)
						{
							//return Method/Payload Class
							for (Map.Entry<String, Object> methodInput : classMap.entrySet()) 
							{																					
								method = methodInput.getKey();//Method
								
								//Validate if params are not required
								if(methodInput.getValue() != null && !methodInput.getValue().toString().isBlank())
								{
									//payload Class
									if(methodInput.getValue() instanceof String)
									{
										payloadCass = Class.forName(methodInput.getValue().toString());
									}
									else
									{
										payloadCass = (Class<?>) methodInput.getValue();
									}	
								}								
					        }
						}
						else
						{							
							throw new Exception("There cannot be more than one Method to execute for the option sent.");
							
						}
			        }
					
					if(!method.isEmpty() && !beanService.isEmpty())
					{
						payload  = payloadCass!=null?(T) Format.objectMapping(payload, payloadCass):(T) payloadCass;						
						response = Utils.executeMethodFromBean(beanService, factory, method, payload);
					}
					else
					{
						throw new Exception("Unable to obtain the class type of the payload, execution method or bean service");
					}					
				}
				else
				{
					throw new Exception("There cannot be more than one ServiceBean to execute for the option sent.");
				}
			}
			else
			{
				throw new Exception("The method HasMap does not exist configured");
			}
		}	
		catch(Exception e)
		{				
			throw new Exception(e.getMessage());
		}
				
		return response;
	}


	public static <T> List<String> getClassAttributeNames(Class<T> clazz) {
		return Arrays.asList(clazz.getDeclaredFields()).stream()
				.map(Field::getName)
				.toList();
	}
	
	public static String getClearMessage(String message) {
        String messageCustom;
        messageCustom = message.replaceAll("\\\\", "");
        if(messageCustom.indexOf("{") >= 0){
            messageCustom = messageCustom.substring(messageCustom.indexOf("{"));
            messageCustom = messageCustom.substring(0,messageCustom.lastIndexOf("}")+1);
        }     
        return messageCustom;
    }


	
	public static String buildGenericSimpleToken(Integer size, Boolean allowUpperCase, Boolean allowLowerCase,
												 Boolean allowNumbers,Boolean allowSpecialCharacters,
												 List<Character> excludeCharacters) {
		StringBuilder genericToken = new StringBuilder(size);

		StringBuilder gToken = new StringBuilder();
		StringBuilder filteredToken = new StringBuilder();
		if (Boolean.TRUE.equals(allowUpperCase)) {
			gToken.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		}
		if (Boolean.TRUE.equals(allowLowerCase)) {
			gToken.append("abcdefghijklmnopqrstuvwxyz");
		}
		if (Boolean.TRUE.equals(allowNumbers)) {
			gToken.append("0123456789");
		}
		if (Boolean.TRUE.equals(allowSpecialCharacters)) {
			gToken.append("º/ª¿?!¡$€~&^{}Ç<>.,*-+_%#/=()");
		}
		if (gToken.length() == 0) {
			gToken.append("0123456789");
		}
		if (excludeCharacters != null && !excludeCharacters.isEmpty()) {
			gToken.chars().mapToObj(c -> (char) c)
					.filter(c -> !excludeCharacters.contains(c))
					.forEach(filteredToken::append);
		} else {
			filteredToken = gToken;
		}

		char[] chars = filteredToken.toString().toCharArray();
		for (int i = 0; i < size; i++) {
			genericToken.append(chars[new SecureRandom().nextInt(chars.length)]);
		}
		return genericToken.toString();
	}
	
	/**
	 * Get token from http Header or Cookie
	 * 
	 * @author Allan Suarez <mailto:arsc86@gmail.com>
	 * 
	 * @param request {@link HttpServletRequest}
	 * @param httpHeaderKey {@link String}
	 * @return {@link String}
	 */
	public static String getTokenByHttpRequest(HttpServletRequest request, String httpHeaderKey)
	{		
		String token = request.getHeader(httpHeaderKey);
		if (token == null) 
		{
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie auxCoo : cookies) {
					if (auxCoo.getName().equals(httpHeaderKey)) {
						token = auxCoo.getValue();
						break;
					}
				}
			}
		}
		
		return token;
	}
	
	public String getSessionUserByHttpRequest(HttpServletRequest request) throws Exception
	{
		final String authorizationHeader = request.getHeader("Authorization");

        String username    = null;	   
        
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) 
        {
        	String[] token = authorizationHeader.split("Bearer ");        	          
            return tokenGenerate.getSubject(tokenGenerate.decodeCypherToken(token[1]));           
        }    
        
        return username;
	}

}