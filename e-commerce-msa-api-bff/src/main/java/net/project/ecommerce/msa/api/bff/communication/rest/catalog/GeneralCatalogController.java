package net.project.ecommerce.msa.api.bff.communication.rest.catalog;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import net.project.ecommerce.dependency.api.dto.catalog.request.CategoryBulkRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.request.CategoryRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProviderRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.request.RegionRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.request.WarehouseRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.response.CategoryResponseDTO;
import net.project.ecommerce.dependency.api.dto.catalog.response.ProductCatalogResponseDTO;
import net.project.ecommerce.dependency.api.dto.catalog.response.ProviderResponseDTO;
import net.project.ecommerce.dependency.api.dto.catalog.response.RegionResponseDTO;
import net.project.ecommerce.dependency.api.dto.catalog.response.WarehouseResponseDTO;
import net.project.ecommerce.dependency.api.interfaces.ICrudComplentaryHandler;
import net.project.ecommerce.dependency.api.interfaces.ICrudHandler;
import net.project.ecommerce.dependency.dto.response.GenericResponseDTO;
import net.project.ecommerce.dependency.exception.CustomException;
import net.project.ecommerce.dependency.util.Utils;
import net.project.ecommerce.msa.api.bff.cons.ApiBffConstants;

@RestController
@RequestMapping("/catalog")
public class GeneralCatalogController {
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private ICrudHandler crudHandler;
	
	@Autowired
	private ICrudComplentaryHandler complentaryHandler;
	
	@Autowired
	private BeanFactory factory;
	
	@Autowired
	private Utils utils;
	
	@GetMapping("/category")	
	public ResponseEntity<?> getCategoryBy(
			@RequestParam(required = false) 
			@Pattern(regexp = "\\d+", message = "The id field must contain only numbers")
			String id,
			@RequestParam(required = false) String providerCode,
			@Pattern(regexp = "\\d+", message = "The category id field must contain only numbers")
			@RequestParam(required = false) String categoryId,
			@RequestParam(required = false, defaultValue = "active")
			@Pattern(regexp = "^[a-zA-Z]+$", message = "The status field must have only letters")String status,
			@RequestParam(required = false) String name,
			@RequestParam Map<String, String> allParams) throws Exception
	{
		GenericResponseDTO<List<CategoryResponseDTO>> genericResponse = new GenericResponseDTO<>();
		try
		{			
			Set<String> allowedParams = Set.of("providerCode", "name", "status","categoryId","id");
			for (String param : allParams.keySet()) 
			{
	            if (!allowedParams.contains(param)) 
	            {
	                throw new Exception("Parameter "+param+" is not allowed");
	            }
	        }
			crudHandler = factory.getBean(ApiBffConstants.CATEGORY_CATALOG_BEAN,ICrudHandler.class);			
			CategoryRequestDTO request = CategoryRequestDTO.builder()
					.id(id!=null?Long.valueOf(id):null)
					.name(name)
					.categoryId(categoryId!=null?Long.valueOf(categoryId):null)
					.providerCode( providerCode )
					.status(status)
					.build();
			genericResponse = crudHandler.retrieve(request);			
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
	
	@PostMapping("/category")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequestDTO request,HttpServletRequest httpRequest) throws Exception
	{
		GenericResponseDTO<CategoryResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{				
			request.setCreatedBy(utils.getSessionUserByHttpRequest(httpRequest));
			crudHandler = factory.getBean(ApiBffConstants.CATEGORY_CATALOG_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.create(request);
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}			
			genericResponse.setCode(HttpStatus.CREATED.value());
			genericResponse.setMessage("Category created successfully");
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
	
	@PostMapping("/category/{id}")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> createCategoryBy(@RequestBody CategoryBulkRequestDTO request,
			@NotBlank(message = "Category id is required")
			@Pattern(regexp = "\\d+", message = "The category field must contain only numbers")
			@PathVariable String id,
			HttpServletRequest httpRequest) throws Exception
	{
		GenericResponseDTO<CategoryResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{		
			CategoryRequestDTO category = CategoryRequestDTO.builder()
					.id(Long.valueOf(id))
					.categories(request.getCategories())
					.createdBy(utils.getSessionUserByHttpRequest(httpRequest))
					.build();			
			complentaryHandler = factory.getBean(ApiBffConstants.CATEGORY_CATALOG_BEAN,ICrudComplentaryHandler.class);
			genericResponse = complentaryHandler.createBy(category);
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}			
			genericResponse.setCode(HttpStatus.CREATED.value());
			genericResponse.setMessage("Categories created and added successfully");
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
	
	@PatchMapping("/category/{id}")
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> updateCategory(
			@PathVariable 
			@NotBlank(message = "Category id is required")
			@Pattern(regexp = "\\d+", message = "The category field must contain only numbers") String id,
			@RequestBody CategoryRequestDTO request,
			HttpServletRequest httpRequest) throws Exception
	{
		GenericResponseDTO<CategoryResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{						
			crudHandler = factory.getBean(ApiBffConstants.CATEGORY_CATALOG_BEAN,ICrudHandler.class);
			request.setCreatedBy(utils.getSessionUserByHttpRequest(httpRequest));
			request.setId(Long.valueOf(id));
			genericResponse = crudHandler.update(request);
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new CustomException(genericResponse.getMessage());
			}
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage("Category updated successfully");
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
	
	@DeleteMapping("/category/{id}")
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> deleteCategory(
			@PathVariable 
			@NotBlank(message = "Category id is required")
			@Pattern(regexp = "\\d+", message = "The category field must contain only numbers") String id) throws Exception
	{
		GenericResponseDTO<String> genericResponse = new GenericResponseDTO<>();
		try
		{							
			crudHandler = factory.getBean(ApiBffConstants.CATEGORY_CATALOG_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.delete( CategoryRequestDTO.builder().id(Long.valueOf(id)).build());
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage("Category successfully deleted");
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
	
	//Providers
	
	@PostMapping("/provider")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> createProvider(@Valid @RequestBody ProviderRequestDTO request) throws Exception
	{		
		GenericResponseDTO<ProviderResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{
			crudHandler = factory.getBean(ApiBffConstants.PROVIDER_CATALOG_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.create(request);
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}	
			genericResponse.setCode(HttpStatus.CREATED.value());
			genericResponse.setMessage("Provider user successfully created");
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
	
	@GetMapping("/provider")
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> getProviderBy(
			@RequestParam(required = false) 
			@Pattern(regexp = "\\d+", message = "The id field must contain only numbers")
			String id,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String status,
			@RequestParam(required = false) String code,
			@RequestParam Map<String, String> allParams) throws Exception
	{		
		GenericResponseDTO<List<ProviderResponseDTO>> genericResponse = new GenericResponseDTO<>();
		try
		{
			Set<String> allowedParams = Set.of("code", "name", "status","id");
			for (String param : allParams.keySet()) 
			{
	            if (!allowedParams.contains(param)) 
	            {
	                throw new Exception("Parameter "+param+" is not allowed");
	            }
	        }
			crudHandler = factory.getBean(ApiBffConstants.PROVIDER_CATALOG_BEAN,ICrudHandler.class);
			ProviderRequestDTO request = ProviderRequestDTO.builder()
					.providerCode(code)
					.id(id!=null?Long.valueOf(id):null)
					.name(name)
					.status(status)
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
	
	@PutMapping("/provider/{id}")
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> updateProvider(
			@PathVariable 
			@NotBlank(message = "Provider id is required")
			@Pattern(regexp = "\\d+", message = "The Provider field must contain only numbers") 
			String id, @RequestBody ProviderRequestDTO request) throws Exception
	{		
		GenericResponseDTO<ProviderResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{
			crudHandler = factory.getBean(ApiBffConstants.PROVIDER_CATALOG_BEAN,ICrudHandler.class);
			request.setId(Long.valueOf(id));
			genericResponse = crudHandler.update(request);
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage("Provider user successfully updated");
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
	
	@DeleteMapping("/provider/{id}")
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> deleteProvider(
			@PathVariable 
			@NotBlank(message = "Provider id is required")
			@Pattern(regexp = "\\d+", message = "The Provider field must contain only numbers") 
			String id) throws Exception
	{		
		GenericResponseDTO<String> genericResponse = new GenericResponseDTO<>();
		try
		{		
			crudHandler = factory.getBean(ApiBffConstants.PROVIDER_CATALOG_BEAN,ICrudHandler.class);
			genericResponse= crudHandler.delete( ProviderRequestDTO.builder().id(Long.valueOf(id)).build());
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}
			genericResponse.setCode(HttpStatus.OK.value());		
			genericResponse.setMessage("Provider and its childs were successfully deleted");
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
	
	//Warehouse
	
	@PostMapping("/warehouse")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> createWarehouse(@Valid @RequestBody WarehouseRequestDTO request) throws Exception
	{		
		GenericResponseDTO<WarehouseResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{
			crudHandler = factory.getBean(ApiBffConstants.WAREHOUSE_CATALOG_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.create(request);
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}	
			genericResponse.setCode(HttpStatus.CREATED.value());
			genericResponse.setMessage("Warehouse successfully created");
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
	
	@GetMapping("/warehouse")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> getWarehouseBy(
			@Pattern(regexp = "\\d+", message = "The id field must contain only numbers")
			@RequestParam(required = false) String id,
			@RequestParam(required = false) String name,
			@Pattern(regexp = "\\d+", message = "The location field must contain only numbers")
			@RequestParam(required = false) String location,
			@RequestParam(required = false) String status,
			@RequestParam Map<String, String> allParams) throws Exception
	{
		GenericResponseDTO<List<WarehouseResponseDTO>> genericResponse = new GenericResponseDTO<>();
		try
		{					
			Set<String> allowedParams = Set.of("location", "name", "status","id");
			for (String param : allParams.keySet()) 
			{
	            if (!allowedParams.contains(param)) 
	            {
	                throw new Exception("Parameter "+param+" is not allowed");
	            }
	        }
			crudHandler = factory.getBean(ApiBffConstants.WAREHOUSE_CATALOG_BEAN,ICrudHandler.class);
			WarehouseRequestDTO request = WarehouseRequestDTO.builder()
					.id(id!=null?Long.valueOf(id):null)
					.location(location)
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
	
	@PutMapping("/warehouse/{id}")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> updateWarehouse(
			@PathVariable 
			@NotBlank(message = "Warehouse id is required")
			@Pattern(regexp = "\\d+", message = "The id field must contain only numbers") String id,
			@RequestBody WarehouseRequestDTO request) throws Exception
	{		
		GenericResponseDTO<WarehouseResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{
			request.setId(Long.valueOf(id));
			crudHandler = factory.getBean(ApiBffConstants.WAREHOUSE_CATALOG_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.update(request);
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}	
			genericResponse.setCode(HttpStatus.CREATED.value());
			genericResponse.setMessage("Warehouse successfully updated");
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
	
	@DeleteMapping("/warehouse/{id}")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> deleteWarehouse(
			@PathVariable 
			@NotBlank(message = "Warehouse id is required")
			@Pattern(regexp = "\\d+", message = "The id field must contain only numbers") String id) throws Exception
	{		
		GenericResponseDTO<WarehouseResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{
			crudHandler = factory.getBean(ApiBffConstants.WAREHOUSE_CATALOG_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.delete( WarehouseRequestDTO.builder().id(Long.valueOf(id)).build());
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}
			genericResponse.setCode(HttpStatus.OK.value());		
			genericResponse.setMessage("Warehouse was successfully deleted");		
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
	
	//Region
	
	@GetMapping("/region")
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER","ROLE_USER"})
	public ResponseEntity<?> getRegionBy(
			@Pattern(regexp = "\\d+", message = "The id field must contain only numbers")
			@RequestParam(required = false) String id,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String status,
			@RequestParam(required = false) String parent,
			@RequestParam Map<String, String> allParams) throws Exception
	{
		GenericResponseDTO<List<ProductCatalogResponseDTO>> genericResponse = new GenericResponseDTO<>();
		try
		{		
			Set<String> allowedParams = Set.of("parent", "name", "status","id");
			for (String param : allParams.keySet()) 
			{
	            if (!allowedParams.contains(param)) 
	            {
	                throw new Exception("Parameter "+param+" is not allowed");
	            }
	        }
			crudHandler = factory.getBean(ApiBffConstants.REGION_CATALOG_BEAN,ICrudHandler.class);
			RegionRequestDTO request = RegionRequestDTO.builder()
					.id(id!=null?Long.valueOf(id):null)					
					.name(name)
					.parent(parent)
					.status(status)
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
	
	@PostMapping("/region")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> createRegion(@Valid @RequestBody RegionRequestDTO request) throws Exception
	{
		GenericResponseDTO<RegionResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{						
			crudHandler = factory.getBean(ApiBffConstants.REGION_CATALOG_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.create(request);
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}	
			genericResponse.setCode(HttpStatus.CREATED.value());
			genericResponse.setMessage("Region successfully created");			
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
	
	@PutMapping("/region/{id}")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> updateRegion(
			@PathVariable 
			@NotBlank(message = "Region id is required")
			@Pattern(regexp = "\\d+", message = "The region field must contain only numbers") String id,
			@RequestBody RegionRequestDTO request) throws Exception
	{
		GenericResponseDTO<RegionResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{				
			crudHandler = factory.getBean(ApiBffConstants.REGION_CATALOG_BEAN,ICrudHandler.class);
			request.setId(id!=null?Long.valueOf(id):null);
			genericResponse = crudHandler.update(request);
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}	
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage("Region successfully updated");	
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
	
	@DeleteMapping("/region/{id}")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> deleteProduct(
			@PathVariable 
			@NotBlank(message = "Product id is required")
			@Pattern(regexp = "\\d+", message = "The product field must contain only numbers") String id) throws Exception
	{
		GenericResponseDTO<String> genericResponse = new GenericResponseDTO<>();
		try
		{				
			crudHandler = factory.getBean(ApiBffConstants.REGION_CATALOG_BEAN,ICrudHandler.class);
			genericResponse = crudHandler.delete( ProductRequestDTO.builder().id(Long.valueOf(id)).build() );
			if(genericResponse.getStatus().equalsIgnoreCase("ERROR"))
			{
				throw new Exception(genericResponse.getMessage());
			}
			genericResponse.setCode(HttpStatus.OK.value());		
			genericResponse.setMessage("Region was successfully deleted");
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
