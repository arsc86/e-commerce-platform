package net.project.ecommerce.msa.api.bff.communication.rest;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import net.project.ecommerce.dependency.api.dto.product.request.CategoryRequestDTO;
import net.project.ecommerce.dependency.api.dto.product.request.ProductCatalogRequestDTO;
import net.project.ecommerce.dependency.api.dto.product.request.ProviderRequestDTO;
import net.project.ecommerce.dependency.api.dto.product.request.WarehouseRequestDTO;
import net.project.ecommerce.dependency.api.dto.product.response.CategoryResponseDTO;
import net.project.ecommerce.dependency.api.dto.product.response.ProductCatalogResponseDTO;
import net.project.ecommerce.dependency.api.dto.product.response.ProviderResponseDTO;
import net.project.ecommerce.dependency.api.dto.product.response.WarehouseResponseDTO;
import net.project.ecommerce.dependency.dto.response.GenericResponseDTO;
import net.project.ecommerce.msa.api.bff.cons.ApiBffConstants;
import net.project.ecommerce.msa.api.bff.handler.ICatalogHandler;

@RestController
@RequestMapping("/catalog")
public class CatalogController {
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private ICatalogHandler catalogHandler;	
	
	@Autowired
	private BeanFactory factory;
	
	@GetMapping("/category")
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER","ROLE_USER"})
	public ResponseEntity<?> getCategoryBy(
			@RequestParam(required = false) 
			@Pattern(regexp = "\\d+", message = "The category id field must contain only numbers")
			String id,
			@RequestParam(required = false) String provider,
			@RequestParam(required = true, defaultValue = "active")
						  @NotBlank(message = "Status is required") String status,
			@RequestParam(required = false) String name) throws Exception
	{
		GenericResponseDTO<List<CategoryResponseDTO>> genericResponse = new GenericResponseDTO<>();
		try
		{						
			catalogHandler = factory.getBean(ApiBffConstants.CATEGORY_CATALOG_BEAN,ICatalogHandler.class);
			CategoryRequestDTO request = CategoryRequestDTO.builder()
					.id(id!=null?Long.valueOf(id):null)
					.name(name)
					.providerId(provider)
					.status(status)
					.build();
			genericResponse.setPayload(catalogHandler.getBy(request));
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
	
	@PostMapping("/category")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequestDTO request) throws Exception
	{
		GenericResponseDTO<CategoryResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{					
			catalogHandler = factory.getBean(ApiBffConstants.CATEGORY_CATALOG_BEAN,ICatalogHandler.class);
			genericResponse.setPayload(catalogHandler.create(request));
			genericResponse.setCode(HttpStatus.CREATED.value());
			genericResponse.setMessage("Category created successfully");
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
	
	@PutMapping("/category/{id}")
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> updateCategory(
			@PathVariable 
			@NotBlank(message = "Category id is required")
			@Pattern(regexp = "\\d+", message = "The category field must contain only numbers") String id,
			@RequestBody CategoryRequestDTO request) throws Exception
	{
		GenericResponseDTO<CategoryResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{						
			catalogHandler = factory.getBean(ApiBffConstants.CATEGORY_CATALOG_BEAN,ICatalogHandler.class);
			request.setId(Long.valueOf(id));
			genericResponse.setPayload(catalogHandler.update(request));
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage("Category updated successfully");
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
			catalogHandler = factory.getBean(ApiBffConstants.CATEGORY_CATALOG_BEAN,ICatalogHandler.class);
			genericResponse.setPayload(catalogHandler.delete( CategoryRequestDTO.builder().id(Long.valueOf(id)).build()) );
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
	
	//Providers
	
	@PostMapping("/provider")	
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<?> createProvider(@Valid @RequestBody ProviderRequestDTO request) throws Exception
	{		
		GenericResponseDTO<ProviderResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{
			catalogHandler = factory.getBean(ApiBffConstants.PROVIDER_CATALOG_BEAN,ICatalogHandler.class);
			genericResponse.setPayload(catalogHandler.create(request));
			genericResponse.setCode(HttpStatus.CREATED.value());
			genericResponse.setMessage("Provider user successfully created");
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
	
	@GetMapping("/provider")
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> getProviderBy(
			@RequestParam(required = false) 
			@Pattern(regexp = "\\d+", message = "The id field must contain only numbers")
			String id,
			@RequestParam(required = false) String name,
			@RequestParam(required = true, defaultValue = "active")
						  @NotBlank(message = "Status is required") String status,
			@RequestParam(required = false) String code) throws Exception
	{		
		GenericResponseDTO<List<ProviderResponseDTO>> genericResponse = new GenericResponseDTO<>();
		try
		{
			catalogHandler = factory.getBean(ApiBffConstants.PROVIDER_CATALOG_BEAN,ICatalogHandler.class);
			ProviderRequestDTO request = ProviderRequestDTO.builder()
					.code(code)
					.id(id!=null?Long.valueOf(id):null)
					.name(name)
					.status(status)
					.build();
			genericResponse.setPayload(catalogHandler.getBy(request));
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
	
	@PutMapping("/provider/{id}")
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<?> updateProvider(
			@PathVariable 
			@NotBlank(message = "Provider id is required")
			@Pattern(regexp = "\\d+", message = "The Provider field must contain only numbers") 
			String id, @RequestBody ProviderRequestDTO request) throws Exception
	{		
		GenericResponseDTO<ProviderResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{
			catalogHandler = factory.getBean(ApiBffConstants.PROVIDER_CATALOG_BEAN,ICatalogHandler.class);
			request.setId(Long.valueOf(id));
			genericResponse.setPayload(catalogHandler.update(id));
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage("Provider user successfully created");
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
	
	@DeleteMapping("/provider/{id}")
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<?> deleteProvider(
			@PathVariable 
			@NotBlank(message = "Provider id is required")
			@Pattern(regexp = "\\d+", message = "The Provider field must contain only numbers") 
			String id) throws Exception
	{		
		GenericResponseDTO<String> genericResponse = new GenericResponseDTO<>();
		try
		{		
			catalogHandler = factory.getBean(ApiBffConstants.PROVIDER_CATALOG_BEAN,ICatalogHandler.class);
			genericResponse.setMessage(catalogHandler.delete( ProviderRequestDTO.builder().id(Long.valueOf(id)).build()) );
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
	
	//Warehouse
	
	@PostMapping("/warehouse")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> createWarehouse(@Valid @RequestBody WarehouseRequestDTO request) throws Exception
	{		
		GenericResponseDTO<WarehouseResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{
			catalogHandler = factory.getBean(ApiBffConstants.WAREHOUSE_CATALOG_BEAN,ICatalogHandler.class);
			genericResponse.setPayload(catalogHandler.create(request));
			genericResponse.setCode(HttpStatus.CREATED.value());
			genericResponse.setMessage("Provider user successfully created");
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
	
	@GetMapping("/warehouse")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> getWarehouseBy(
			@RequestParam(required = false) String id,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String location,
			@RequestParam(required = true, defaultValue = "active")
						  @NotBlank(message = "Status is required") String status,
			@RequestParam(required = false) String provider) throws Exception
	{
		GenericResponseDTO<List<WarehouseResponseDTO>> genericResponse = new GenericResponseDTO<>();
		try
		{						
			catalogHandler = factory.getBean(ApiBffConstants.WAREHOUSE_CATALOG_BEAN,ICatalogHandler.class);
			WarehouseRequestDTO request = WarehouseRequestDTO.builder()
					.id(id!=null?Long.valueOf(id):null)
					.location(location)
					.name(name)
					.provider(provider)
					.status(status)
					.build();
			genericResponse.setPayload(catalogHandler.getBy(request));
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
			catalogHandler = factory.getBean(ApiBffConstants.WAREHOUSE_CATALOG_BEAN,ICatalogHandler.class);
			genericResponse.setPayload(catalogHandler.create(request));
			genericResponse.setCode(HttpStatus.OK.value());
			genericResponse.setMessage("Provider user successfully updated");
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
			catalogHandler = factory.getBean(ApiBffConstants.WAREHOUSE_CATALOG_BEAN,ICatalogHandler.class);
			genericResponse.setMessage(catalogHandler.create( WarehouseRequestDTO.builder().id(Long.valueOf(id)).build()) );
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
	
	//Products
	
	@GetMapping("/product")
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER","ROLE_USER"})
	public ResponseEntity<?> getCatalogBy(
			@RequestParam(required = false) String id,
			@RequestParam(required = false) String category,
			@RequestParam(required = true, defaultValue = "active")
						  @NotBlank(message = "Status is required") String status,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String provider,
			@RequestParam(required = false) String code) throws Exception
	{
		GenericResponseDTO<List<ProductCatalogResponseDTO>> genericResponse = new GenericResponseDTO<>();
		try
		{						
			catalogHandler = factory.getBean(ApiBffConstants.PRODUCT_CATALOG_BEAN,ICatalogHandler.class);
			ProductCatalogRequestDTO request = ProductCatalogRequestDTO.builder()
					.id(id!=null?Long.valueOf(id):null)
					.categoryId(category)
					.name(name)
					.provider(ProviderRequestDTO.builder().code(provider).build())
					.code(code)
					.status(status)
					.build();								
			genericResponse.setPayload(catalogHandler.getBy(request));
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
	
	@PostMapping("/product")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> createProduct(@Valid @RequestBody ProductCatalogRequestDTO request) throws Exception
	{
		GenericResponseDTO<ProductCatalogResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{						
			catalogHandler = factory.getBean(ApiBffConstants.PRODUCT_CATALOG_BEAN,ICatalogHandler.class);
			genericResponse.setPayload(catalogHandler.create(request));
			genericResponse.setCode(HttpStatus.CREATED.value());
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
	
	@PutMapping("/product/{id}")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> updateProduct(
			@PathVariable 
			@NotBlank(message = "Product id is required")
			@Pattern(regexp = "\\d+", message = "The product field must contain only numbers") String id,
			@RequestBody ProductCatalogRequestDTO request) throws Exception
	{
		GenericResponseDTO<ProductCatalogResponseDTO> genericResponse = new GenericResponseDTO<>();
		try
		{				
			catalogHandler = factory.getBean(ApiBffConstants.PRODUCT_CATALOG_BEAN,ICatalogHandler.class);
			request.setId(id!=null?Long.valueOf(id):null);
			genericResponse.setPayload(catalogHandler.update(request));
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
	
	@DeleteMapping("/product/{id}")	
	@Secured({"ROLE_ADMIN","ROLE_PROVIDER"})
	public ResponseEntity<?> deleteProduct(
			@PathVariable 
			@NotBlank(message = "Product id is required")
			@Pattern(regexp = "\\d+", message = "The product field must contain only numbers") String id) throws Exception
	{
		GenericResponseDTO<String> genericResponse = new GenericResponseDTO<>();
		try
		{				
			catalogHandler = factory.getBean(ApiBffConstants.PRODUCT_CATALOG_BEAN,ICatalogHandler.class);
			genericResponse.setMessage(catalogHandler.delete( ProductCatalogRequestDTO.builder().id(Long.valueOf(id)).build()) );
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

}
