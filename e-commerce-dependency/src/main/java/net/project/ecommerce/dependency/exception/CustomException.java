package net.project.ecommerce.dependency.exception;

public class CustomException extends Exception{
	
	private static final long serialVersionUID = -3406323657783324045L;

	public CustomException(String message) {
        super(message);
    }
	
	public CustomException(Exception e) {		
		super(e);  
    }	
}
