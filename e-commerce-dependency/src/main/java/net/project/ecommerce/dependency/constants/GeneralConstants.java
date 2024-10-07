package net.project.ecommerce.dependency.constants;

public abstract class GeneralConstants {
	private GeneralConstants() {
	}
	
	public static final String TIMEZONE_DATE = "America/Guayaquil";

	public static final int CODE_OK            = 200;
	public static final int FAILURE_CODE_DEFAULT = 1;
	
	//default responses
	public static final String SUCCESS_STATUS_DEFAULT  = "OK";
	public static final String FAILURE_STATUS_DEFAULT  = "ERROR";
	public static final String SUCCESS_MESSAGE_DEFAULT = "The processing was completed successfully";		
	public static final String FAILURE_MESSAGE_DEFAULT = "An error had ocurred";
	public static final String MSG_ERROR_GRPC_REQUEST_PROCESING = "An error ocurred while processing GRPC request with option ";	
	
	//Exception messages
	public static final String MISSING_FIELD      = "Missing input fields";
	public static final String FORMAT_ERROR_FIELD = "Format input fields error";
	public static final String NO_ROLES_TO_ACCESS = "User doesn't has roles to access";
	public static final String NO_RESOURCES       = "Missing resource path params";

	public static final String NO_EXIST_STATUS   = "Do not exist";
	public static final String VALUE_LESS_STATUS = "Hasn't value";
	public static final String NOT_PARAMS_EXIST  = "";
	
	public static final String JWT_SERVICE     = "JWT";
	
	public static final int GRPC_MAX_MSG_BYTES = 5242880;
	
	//Beans Producers Constants
	public static final String MESSAGE_PRODUCER       = "MessageProducer";
	public static final String GRPC_MESSAGE_PRODUCER  = "GRPC";
	public static final String KAKFA_MESSAGE_PRODUCER = "KAFKA";
	
	
}
