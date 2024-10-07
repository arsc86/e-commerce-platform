package net.project.ecommerce.dependency.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import net.project.ecommerce.dependency.constants.GeneralConstants;
import net.project.ecommerce.dependency.dto.response.GenericResponseDTO;

@ControllerAdvice
@RestController
public class ExceptionController {
	
    Logger log = LogManager.getLogger(this.getClass());       
    
    private GenericResponseDTO<Object> setResponse(Integer code, String message,Object payload) {
    	 GenericResponseDTO<Object> response = new GenericResponseDTO<>();
    	 response.setStatus(GeneralConstants.FAILURE_STATUS_DEFAULT);       
         response.setCode(code); 
         response.setMessage(message);
         response.setPayload(payload);
         return response;
    }
    
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponseDTO<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {        
        List<String> listErrors = new ArrayList<>();                
        ex.getBindingResult().getAllErrors().forEach(error -> {                    
            listErrors.add(error.getDefaultMessage());
        });       
        return new ResponseEntity<>(setResponse(HttpStatus.BAD_REQUEST.value(),GeneralConstants.MISSING_FIELD,listErrors),
        		                                HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(value = HandlerMethodValidationException.class)
    public ResponseEntity<GenericResponseDTO<?>> handleValidationExceptions(HandlerMethodValidationException ex) {        
        List<String> listErrors = new ArrayList<>();                
        ex.getAllErrors().forEach(error -> {                    
            listErrors.add(error.getDefaultMessage());
        });       
        return new ResponseEntity<>(setResponse(HttpStatus.BAD_REQUEST.value(),GeneralConstants.FORMAT_ERROR_FIELD,listErrors),
        		                                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<GenericResponseDTO<?>> handleException(Exception e) {        
    	GenericResponseDTO<Object> response;
    
        Map<String, Map<HttpStatus, String>> map = Map.of(
        	    "AuthorizationDeniedException",    Map.of(HttpStatus.FORBIDDEN,GeneralConstants.NO_ROLES_TO_ACCESS),
        	    "NoResourceFoundException",        Map.of(HttpStatus.BAD_REQUEST,GeneralConstants.NO_RESOURCES)
        	);
    	
    	String idTransaction = UUID.randomUUID().toString();
    	Map<HttpStatus, String> exceptionResult = map.get(e.getClass().getSimpleName());
    	
    	if (exceptionResult != null) 
    	{    		    		
    		HttpStatus httpCode = exceptionResult.keySet().iterator().next();                                         	 
       	 	response = setResponse(httpCode.value(),
       	 			               exceptionResult.get(httpCode),
       	 			               null
       	 			              );
        } 
    	else 
    	{        	
        	response = setResponse(HttpStatus.CONFLICT.value(),
        			               GeneralConstants.FAILURE_MESSAGE_DEFAULT + " with id_transaction = "+idTransaction,
        			               null
        			              );
        }
                               
        String packError = e.getStackTrace()[0].getClassName();
        String classError = e.getStackTrace()[0].getFileName();
        String methodError = e.getStackTrace()[0].getMethodName();
        Integer lineError = e.getStackTrace()[0].getLineNumber();      
        log.error("[{}][{}][{}][{}] - {} - {}", packError, classError, methodError, lineError, e.getMessage(), idTransaction);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }   
    
   
}
