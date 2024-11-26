package net.project.ecommerce.msa.api.bff.communication.rest.catalog;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import net.project.ecommerce.dependency.api.dto.catalog.ProductCharacteristicDTO;
import net.project.ecommerce.dependency.api.dto.catalog.request.CharacteristicRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductFileRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductInventoryRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductPriceDiscountRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductRequestDTO.ProductCharacteristicRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.response.CharacteristicResponseDTO;
import net.project.ecommerce.dependency.api.dto.catalog.response.ProductCatalogResponseDTO;
import net.project.ecommerce.dependency.api.dto.catalog.response.ProductFileResponseDTO.FileDTO;
import net.project.ecommerce.dependency.api.enums.EnumFileType;
import net.project.ecommerce.dependency.api.enums.EnumProductValueType;
import net.project.ecommerce.dependency.api.interfaces.ICrudHandler;
import net.project.ecommerce.dependency.dto.response.GenericResponseDTO;
import net.project.ecommerce.dependency.exception.CustomException;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.dependency.util.Utils;
import net.project.ecommerce.msa.api.bff.cons.ApiBffConstants;
import net.project.ecommerce.msa.api.bff.handler.impl.product.IInventoryCatalog;

@RestController
@RequestMapping("/catalog")
public class ProductCatalogController {
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private ICrudHandler crudHandler;
	
	@Autowired
	private IInventoryCatalog inventoryCatalog;
	
	@Autowired
	private BeanFactory factory;
	
	@Autowired
	private Utils utils;
	
	@GetMapping("/characteristic")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> getCharacteristic(
			@RequestParam(required = false) 
			@Pattern(regexp = "\\d+", message = "The id field must contain only numbers")
			String id,
			@RequestParam(required = false, defaultValue = "active")
			@Pattern(regexp = "^[a-zA-Z]+$", message = "The status field must have only letters")
			String status,
			@RequestParam(required = false) 
			String name,
			@RequestParam Map<String, String> allParams) throws Exception
	{
		GenericResponseDTO<List<CharacteristicResponseDTO>> genericResponse = new GenericResponseDTO<>();
		try
		{			
			Set<String> allowedParams = Set.of("name", "status","id");
			for (String param : allParams.keySet()) 
			{
	            if (!allowedParams.contains(param)) 
	            {
	                throw new Exception("Parameter "+param+" is not allowed");
	            }
	        }
			crudHandler = factory.getBean(ApiBffConstants.CHARACT_CATALOG_BEAN,ICrudHandler.class);			
			CharacteristicRequestDTO request = CharacteristicRequestDTO.builder()
					.id(id!=null?Long.valueOf(id):null)
					.name(name)					
					.status(status)
					.build();
			genericResponse = crudHandler.retrieve(request);	
			genericResponse.setCode(HttpStatus.OK.value());		
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@PostMapping("/characteristic")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> createCharacteristic(@Valid @RequestBody CharacteristicRequestDTO request,HttpServletRequest httpRequest) throws Exception
	{
		GenericResponseDTO<CharacteristicResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{							
			crudHandler = factory.getBean(ApiBffConstants.CHARACT_CATALOG_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.create(request);
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}			
			genericResponse.setCode(HttpStatus.CREATED.value());
			genericResponse.setMessage("Characteristic created successfully");
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@PatchMapping("/characteristic/{id}")
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> updateCharacteristic(
			@PathVariable 
			@NotBlank(message = "Characteristic id is required")
			@Pattern(regexp = "\\d+", message = "The category field must contain only numbers") String id,
			@RequestBody CharacteristicRequestDTO request,
			HttpServletRequest httpRequest) throws Exception
	{
		GenericResponseDTO<CharacteristicResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{						
			crudHandler = factory.getBean(ApiBffConstants.CHARACT_CATALOG_BEAN,ICrudHandler.class);			
			request.setId(Long.valueOf(id));
			genericResponse = crudHandler.update(request);
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new CustomException(genericResponse.getMessage());
			}
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage("Characteristic updated successfully");
		}
		catch(Exception e)
		{			
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@DeleteMapping("/characteristic/{id}")
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> deleteCharacteristic(
			@PathVariable 
			@NotBlank(message = "Characteristic id is required")
			@Pattern(regexp = "\\d+", message = "The id field must contain only numbers") String id) throws Exception
	{
		GenericResponseDTO<String> genericResponse = new GenericResponseDTO<>();
		try
		{							
			crudHandler = factory.getBean(ApiBffConstants.CHARACT_CATALOG_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.delete( CharacteristicRequestDTO.builder().id(Long.valueOf(id)).build());
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage("Characteristic successfully deleted");
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	//Product
	
	@GetMapping("/product")	
	public ResponseEntity<?> getProduct(
			@RequestParam(required = false) 
			@Pattern(regexp = "\\d+", message = "The id field must contain only numbers")
			String id,
			@RequestParam(required = false, defaultValue = "active")
			@Pattern(regexp = "^[a-zA-Z]+$", message = "The status field must have only letters")
			String status,
			@RequestParam(required = false) 
			String description,
			@RequestParam(required = false) 
			String name,
			@RequestParam(required = false) 
			String code,
			@RequestParam(required = false) 
			String warehouseId,
			@RequestParam(required = false) 
			String providerName,
			@RequestParam(required = false) 
			@Pattern(regexp = "\\d+", message = "The category field must contain only numbers")
			String categoryId,
			@RequestParam(required = false) 
			@Pattern(regexp = "\\d+", message = "The page field must contain only numbers")
			String page,
			@RequestParam(required = false) 
			@Pattern(regexp = "\\d+", message = "The page size field must contain only numbers")
			String pageSize,
			@RequestParam(required = false) 
			boolean pagination,
			@RequestParam Map<String, String> allParams) throws Exception
	{
		GenericResponseDTO<List<ProductCatalogResponseDTO>> genericResponse = new GenericResponseDTO<>();
		try
		{			
			Set<String> allowedParams = Set.of("description", "status","id","code","warehouseId","providerName","page","pageSize","pagination","categoryId");
			 
			for (String param : allParams.keySet()) 
			{
	            if (!allowedParams.contains(param)) 
	            {
	                throw new Exception("Parameter "+param+" is not allowed");
	            }
	        }
			
			crudHandler = factory.getBean(ApiBffConstants.PRODUCT_CATALOG_BEAN,ICrudHandler.class);			
			ProductRequestDTO request = ProductRequestDTO.builder()
					.id(id!=null?Long.valueOf(id):null)
					.name(name)			
					.warehouseId(warehouseId!=null?Long.valueOf(warehouseId):null)
					.providerName(providerName)
					.categoryId(categoryId!=null?Long.valueOf(categoryId):null)
					.pageNumber(page==null?Integer.parseInt(ApiBffConstants.DEFAULT_PAGE_NUMBER):Integer.parseInt(page))
					.pageSize(pageSize==null?Integer.parseInt(ApiBffConstants.DEFAULT_PAGE_SIZE):Integer.parseInt(pageSize))
					.status(status)
					.pagination(pagination)
					.code(code)
					.build();
			genericResponse = crudHandler.retrieve(request);	
			genericResponse.setCode(HttpStatus.OK.value());		
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@PostMapping("/product")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequestDTO request,HttpServletRequest httpRequest) throws Exception
	{
		GenericResponseDTO<ProductRequestDTO> genericResponse = new GenericResponseDTO<>();
		try
		{							
			request.setCreatedBy(utils.getSessionUserByHttpRequest(httpRequest));
			crudHandler = factory.getBean(ApiBffConstants.PRODUCT_CATALOG_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.create(request);
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}			
			genericResponse.setCode(HttpStatus.CREATED.value());
			genericResponse.setMessage("Product created successfully");
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@PutMapping("/product/{id}")
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> updateProduct(
			@PathVariable 
			@NotBlank(message = "Product id is required")
			@Pattern(regexp = "\\d+", message = "The product field must contain only numbers") String id,
			@RequestBody ProductRequestDTO request,
			HttpServletRequest httpRequest) throws Exception
	{
		GenericResponseDTO<ProductCatalogResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{						
			crudHandler = factory.getBean(ApiBffConstants.PRODUCT_CATALOG_BEAN,ICrudHandler.class);			
			request.setId(Long.valueOf(id));
			genericResponse = crudHandler.update(request);
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new CustomException(genericResponse.getMessage());
			}
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage("Product updated successfully");
		}
		catch(Exception e)
		{			
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@DeleteMapping("/product/{id}")
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> deleteProduct(
			@PathVariable 
			@NotBlank(message = "Product id is required")
			@Pattern(regexp = "\\d+", message = "The id field must contain only numbers") String id) throws Exception
	{
		GenericResponseDTO<String> genericResponse = new GenericResponseDTO<>();
		try
		{							
			crudHandler = factory.getBean(ApiBffConstants.PRODUCT_CATALOG_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.delete( ProductRequestDTO.builder().id(Long.valueOf(id)).build());
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage("Product successfully deleted");
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	//Product Characteristic
	
	@GetMapping("/product/{id}/characteristic")		
	public ResponseEntity<?> retrieveProductCharact(
			@PathVariable 
			@NotBlank(message = "Product id is required")
			@Pattern(regexp = "\\d+", message = "The product field must contain only numbers") String id,
			@RequestParam Map<String, String> allParams) throws Exception
	{
		GenericResponseDTO<Object> genericResponse = new GenericResponseDTO<>();
		try
		{						
			for (String param : allParams.keySet()) 
			{
				throw new Exception("Parameter "+param+" is not allowed");	            
	        }
			ProductCharacteristicDTO characteristicDTO = ProductCharacteristicDTO.builder()
					.productId(Long.valueOf(id))					
					.build();
			crudHandler = factory.getBean(ApiBffConstants.PRODUCT_CHARACT_CATALOG_BEAN,ICrudHandler.class);			
			genericResponse = crudHandler.retrieve(characteristicDTO);	
			genericResponse.setCode(HttpStatus.OK.value());	
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@PostMapping("/product/{id}/characteristic")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> createProductCharact(
			@PathVariable 
			@NotBlank(message = "Product id is required")
			@Pattern(regexp = "\\d+", message = "The product field must contain only numbers") String id,
			@Valid @RequestBody List<ProductCharacteristicRequestDTO> request) throws Exception
	{
		GenericResponseDTO<Object> genericResponse = new GenericResponseDTO<>();
		try
		{						
			ProductCharacteristicDTO characteristicDTO = ProductCharacteristicDTO.builder()
					.productId(Long.valueOf(id))
					.characteristics(request)
					.build();
			crudHandler = factory.getBean(ApiBffConstants.PRODUCT_CHARACT_CATALOG_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.create(characteristicDTO);
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}			
			genericResponse.setCode(HttpStatus.CREATED.value());
			genericResponse.setMessage("Characteristic added to product successfully");
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@DeleteMapping("/product/{id}/characteristic")
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> deleteProductCharacteristic(
			@PathVariable 
			@NotBlank(message = "Product id is required")
			@Pattern(regexp = "\\d+", message = "The id field must contain only numbers") String id,
			@RequestBody List<ProductCharacteristicRequestDTO> request) throws Exception
	{
		GenericResponseDTO<String> genericResponse = new GenericResponseDTO<>();
		try
		{							
			crudHandler = factory.getBean(ApiBffConstants.PRODUCT_CHARACT_CATALOG_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.delete( ProductCharacteristicDTO.builder()
					                                                     .productId(Long.valueOf(id))
					                                                     .characteristics(request)
					                                                     .build()
					                                                     );
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage("Characteristics were successfully deleted from product");
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	//upload file to characteristic
	
	@PostMapping(value="/product/characteristic/{id}/upload",
			     consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, 
			     produces = {"application/json"})	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> createProductFile(
			@PathVariable 
			@NotBlank(message = "Product id is required")
			@Pattern(regexp = "\\d+", message = "The id field must contain only numbers") String id,
			@RequestParam("file") MultipartFile file,
			@NotBlank(message = "File type is required")
			@RequestParam("type") String type,
			HttpServletRequest httpRequest) throws Exception
	{
		GenericResponseDTO<Object> genericResponse = new GenericResponseDTO<>();
		try
		{			
			if (file.isEmpty()) {
				throw new Exception("Please select a file to upload...");
	        }
			
			byte[] fileBytes = file.getBytes();
	        String base64Encoded = Base64.getEncoder().encodeToString(fileBytes);

			ProductFileRequestDTO fileRequestDTO = ProductFileRequestDTO.builder()
					.valueId(id)
					.type(EnumFileType.valueOf(type))
					.fileName(file.getOriginalFilename())
					.base64File(base64Encoded)
					.createdBy(utils.getSessionUserByHttpRequest(httpRequest))
					.build();
			
			crudHandler = factory.getBean(ApiBffConstants.PRODUCT_FILE_CATALOG_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.create(fileRequestDTO );
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}			
			genericResponse.setCode(HttpStatus.CREATED.value());
			genericResponse.setMessage("File added to product successfully");
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@DeleteMapping("/product/characteristic/{id}/upload/{idFile}")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> deleteProductFile(
			@PathVariable 
			@NotBlank(message = "Characteristic id is required")
			@Pattern(regexp = "\\d+", message = "The id field must contain only numbers") String id,
			@PathVariable 
			@NotNull(message = "File id is required")
			@NotBlank(message = "File id is required")
			@Pattern(regexp = "\\d+", message = "The id field must contain only numbers") String idFile) throws Exception
	{
		GenericResponseDTO<Object> genericResponse = new GenericResponseDTO<>();
		try
		{			
			ProductFileRequestDTO fileRequestDTO = ProductFileRequestDTO.builder()
					.fileId(idFile)
					.valueId(id)					
					.build();
			
			crudHandler = factory.getBean(ApiBffConstants.PRODUCT_FILE_CATALOG_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.delete(fileRequestDTO );
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}			
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage("File deleted from product successfully");
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@SuppressWarnings("unused")
	@GetMapping(value="/product/characteristic/{id}/file/{fileId}")		
	public ResponseEntity<?> getProductFile(
			@PathVariable 
			@NotBlank(message = "Product id is required")
			@Pattern(regexp = "\\d+", message = "The id field must contain only numbers") String id,
			@PathVariable 
			@Pattern(regexp = "\\d+", message = "The id file must contain only numbers")
			String fileId,
			@RequestParam Map<String, String> allParams) throws Exception
	{
		GenericResponseDTO<FileDTO> genericResponse = new GenericResponseDTO<>();				
		
		byte[] byteArray = new byte[0]; 
		ByteArrayResource resource = new ByteArrayResource(byteArray);
		String fileName = null;
	
		try
		{						
			for (String param : allParams.keySet()) 
			{
				throw new Exception("Parameter "+param+" is not allowed");	            
	        }
			
			ProductFileRequestDTO fileRequestDTO = ProductFileRequestDTO.builder()					
					.fileId(fileId)
					.valueId(id)				
					.build();
			
			crudHandler = factory.getBean(ApiBffConstants.PRODUCT_FILE_CATALOG_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.retrieve(fileRequestDTO );
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}			
			
			FileDTO fileDTO = Format.objectMapping(genericResponse.getPayload(), FileDTO.class);
			
			if(fileDTO.getFile()==null)
			{	
				genericResponse.setCode(HttpStatus.NOT_FOUND.value());
				genericResponse.setStatus("ERROR");
				genericResponse.setMessage("File does not exists");
				return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
			}
					
			byteArray = Base64.getDecoder().decode(fileDTO.getFile());
			resource = new ByteArrayResource(byteArray);
			fileName = fileDTO.getName();
			
			genericResponse.setCode(HttpStatus.OK.value());
			
			//Map<String, Object> response = new HashMap<>();
	        //response.put("fileName", fileDTO.getName());
	        //response.put("base64", fileDTO.getFile());
	        
	        genericResponse.setPayload(fileDTO);
	        
	        return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);		
			
			return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
		}
		
		
	}
	
	//product price
	
	@PostMapping("/product/{id}/price")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> createProductPrice(
			@PathVariable 
			@NotBlank(message = "Product id is required")
			@Pattern(regexp = "\\d+", message = "The product field must contain only numbers") String id,
			@Valid @RequestBody List<ProductPriceDiscountRequestDTO> request) throws Exception
	{
		GenericResponseDTO<Object> genericResponse = new GenericResponseDTO<>();
		try
		{						
			ProductCharacteristicDTO characteristicDTO = ProductCharacteristicDTO.builder()
					.productId(Long.valueOf(id))
					.pricesDiscounts(request)
					.type(EnumProductValueType.PRICE)
					.build();
			crudHandler = factory.getBean(ApiBffConstants.PRODUCT_PRICE_DISC_CATALOG_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.create(characteristicDTO);
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}			
			genericResponse.setCode(HttpStatus.CREATED.value());
			genericResponse.setMessage("Product Price added to product successfully");
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@GetMapping("/product/{id}/price")		
	public ResponseEntity<?> retrieveProductPrice(
			@PathVariable 
			@NotBlank(message = "Product id is required")
			@Pattern(regexp = "\\d+", message = "The product field must contain only numbers") String id,
			@RequestParam Map<String, String> allParams) throws Exception
	{
		GenericResponseDTO<Object> genericResponse = new GenericResponseDTO<>();
		try
		{						
			for (String param : allParams.keySet()) 
			{
				throw new Exception("Parameter "+param+" is not allowed");	            
	        }
			ProductCharacteristicDTO characteristicDTO = ProductCharacteristicDTO.builder()
					.productId(Long.valueOf(id))	
					.type(EnumProductValueType.PRICE)
					.build();
			crudHandler = factory.getBean(ApiBffConstants.PRODUCT_PRICE_DISC_CATALOG_BEAN,ICrudHandler.class);			
			genericResponse = crudHandler.retrieve(characteristicDTO);	
			genericResponse.setCode(HttpStatus.OK.value());	
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@DeleteMapping("/product/{id}/price")
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> deleteProductPrice(
			@PathVariable 
			@NotBlank(message = "Product id is required")
			@Pattern(regexp = "\\d+", message = "The id field must contain only numbers") String id,
			@RequestBody List<ProductPriceDiscountRequestDTO> request) throws Exception
	{
		GenericResponseDTO<String> genericResponse = new GenericResponseDTO<>();
		try
		{							
			crudHandler = factory.getBean(ApiBffConstants.PRODUCT_PRICE_DISC_CATALOG_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.delete( ProductCharacteristicDTO.builder()
					                                                     .productId(Long.valueOf(id))
					                                                     .pricesDiscounts(request)
					                                                     .type(EnumProductValueType.PRICE)
					                                                     .build()
					                                                     );
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage("Prices were successfully deleted from product");
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}

	//product discount
	
	@PostMapping("/product/{id}/discount")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> createProductDiscount(
			@PathVariable 
			@NotBlank(message = "Product id is required")
			@Pattern(regexp = "\\d+", message = "The product field must contain only numbers") String id,
			@Valid @RequestBody List<ProductPriceDiscountRequestDTO> request) throws Exception
	{
		GenericResponseDTO<Object> genericResponse = new GenericResponseDTO<>();
		try
		{						
			ProductCharacteristicDTO characteristicDTO = ProductCharacteristicDTO.builder()
					.productId(Long.valueOf(id))
					.pricesDiscounts(request)
					.type(EnumProductValueType.DISCOUNT)
					.build();
			crudHandler = factory.getBean(ApiBffConstants.PRODUCT_PRICE_DISC_CATALOG_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.create(characteristicDTO);
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}			
			genericResponse.setCode(HttpStatus.CREATED.value());
			genericResponse.setMessage("Product Discount added to product successfully");
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@GetMapping("/product/{id}/discount")	
	public ResponseEntity<?> retrieveProductDiscount(
			@PathVariable 
			@NotBlank(message = "Product id is required")
			@Pattern(regexp = "\\d+", message = "The product field must contain only numbers") String id,
			@RequestParam Map<String, String> allParams) throws Exception
	{
		GenericResponseDTO<Object> genericResponse = new GenericResponseDTO<>();
		try
		{						
			for (String param : allParams.keySet()) 
			{
				throw new Exception("Parameter "+param+" is not allowed");	            
	        }
			ProductCharacteristicDTO characteristicDTO = ProductCharacteristicDTO.builder()
					.productId(Long.valueOf(id))	
					.type(EnumProductValueType.DISCOUNT)
					.build();
			crudHandler = factory.getBean(ApiBffConstants.PRODUCT_PRICE_DISC_CATALOG_BEAN,ICrudHandler.class);			
			genericResponse = crudHandler.retrieve(characteristicDTO);	
			genericResponse.setCode(HttpStatus.OK.value());	
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@DeleteMapping("/product/{id}/discount")
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> deleteProductDiscount(
			@PathVariable 
			@NotBlank(message = "Product id is required")
			@Pattern(regexp = "\\d+", message = "The id field must contain only numbers") String id,
			@RequestBody List<ProductPriceDiscountRequestDTO> request) throws Exception
	{
		GenericResponseDTO<String> genericResponse = new GenericResponseDTO<>();
		try
		{							
			crudHandler = factory.getBean(ApiBffConstants.PRODUCT_PRICE_DISC_CATALOG_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.delete( ProductCharacteristicDTO.builder()
					                                                     .productId(Long.valueOf(id))
					                                                     .pricesDiscounts(request)
					                                                     .type(EnumProductValueType.DISCOUNT)
					                                                     .build()
					                                                     );
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage("Discounts were successfully deleted from product");
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	//Inventory
	
	@PutMapping("/product/{id}/inventory")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> stockInOutProductInventory(
			@PathVariable 
			@NotBlank(message = "Product id is required")
			@Pattern(regexp = "\\d+", message = "The product field must contain only numbers") String id,
			@Valid @RequestBody ProductInventoryRequestDTO request) throws Exception
	{
		GenericResponseDTO<Object> genericResponse = new GenericResponseDTO<>();
		try
		{						
			request.setProductId(id);
			inventoryCatalog = factory.getBean(ApiBffConstants.PRODUCT_INVENTORY_BEAN,IInventoryCatalog.class);
			genericResponse = inventoryCatalog.stockInOut(request);
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}			
			genericResponse.setCode(HttpStatus.CREATED.value());
			genericResponse.setMessage("Product Inventory was updated successfully");
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
	
	@GetMapping("/product/{id}/inventory")		
	public ResponseEntity<?> retrieveProductInventory(
			@PathVariable 
			@NotBlank(message = "Product id is required")
			@Pattern(regexp = "\\d+", message = "The product field must contain only numbers") String id,
			@RequestParam(required = false) 
			String warehouseId,
			@RequestParam Map<String, String> allParams) throws Exception
	{
		GenericResponseDTO<Object> genericResponse = new GenericResponseDTO<>();
		try
		{						
			Set<String> allowedParams = Set.of("warehouseId");
			for (String param : allParams.keySet()) 
			{
	            if (!allowedParams.contains(param)) 
	            {
	                throw new Exception("Parameter "+param+" is not allowed");
	            }
	        }
			ProductInventoryRequestDTO request = ProductInventoryRequestDTO.builder()
					.productId(id)
					.warehouseId(warehouseId)
					.build();
			inventoryCatalog = factory.getBean(ApiBffConstants.PRODUCT_INVENTORY_BEAN,IInventoryCatalog.class);
			genericResponse = inventoryCatalog.retrieve(request);
			genericResponse.setCode(HttpStatus.OK.value());		
		}
		catch(Exception e)
		{
			genericResponse.setCode(HttpStatus.CONFLICT.value());
			genericResponse.setStatus("ERROR");
			genericResponse.setMessage(e.getMessage());
			genericResponse.setPayload(null);			
		}
		
		return new ResponseEntity<GenericResponseDTO<?>>(genericResponse,HttpStatus.resolve(genericResponse.getCode()));
	}
}
