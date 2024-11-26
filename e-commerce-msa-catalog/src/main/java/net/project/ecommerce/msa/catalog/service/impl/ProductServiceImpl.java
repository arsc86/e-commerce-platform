package net.project.ecommerce.msa.catalog.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Slice;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import net.project.ecommerce.dependency.api.constants.catalog.CatalogProductMethodConstants;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductRequestDTO.ProductCharacterisitcValueRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductRequestDTO.ProductCharacteristicRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.response.ProductCatalogResponseDTO;
import net.project.ecommerce.dependency.api.enums.EnumCharacteristicType;
import net.project.ecommerce.dependency.api.enums.EnumStates;
import net.project.ecommerce.dependency.exception.CustomException;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.msa.catalog.dao.ICategoryDAO;
import net.project.ecommerce.msa.catalog.dao.ICharacteristicDAO;
import net.project.ecommerce.msa.catalog.dao.IProductCharacteristicDAO;
import net.project.ecommerce.msa.catalog.dao.IProductCharacteristicValueDAO;
import net.project.ecommerce.msa.catalog.dao.IProductDAO;
import net.project.ecommerce.msa.catalog.dao.impl.ProductDAOImpl;
import net.project.ecommerce.msa.catalog.model.AdmiCategory;
import net.project.ecommerce.msa.catalog.model.AdmiCharacteristic;
import net.project.ecommerce.msa.catalog.model.AdmiProduct;
import net.project.ecommerce.msa.catalog.model.AdmiProductCharact;
import net.project.ecommerce.msa.catalog.model.AdmiProductCharactValue;
import net.project.ecommerce.msa.catalog.service.IProductCatalogHandler;
import net.project.ecommerce.msa.catalog.util.CatalogConstants;
import net.project.ecommerce.msa.catalog.util.CatalogUtils;

@Component(CatalogProductMethodConstants.PRODUCT_SERVICE_BEAN)
public class ProductServiceImpl implements IProductCatalogHandler {
	
	Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private IProductDAO productDAO;
	
	@Autowired
	private ProductDAOImpl productDAOImpl;
	
	@Autowired			
	private ICategoryDAO categoryDAO;
	
	@Autowired
	private ICharacteristicDAO characteristicDAO;
	
	@Autowired
	private IProductCharacteristicDAO productCharacteristicDAO;
	
	@Autowired
	private IProductCharacteristicValueDAO productCharacteristicValueDAO;
	
	@Autowired
	private CatalogUtils utils;
	
	@Value("${catalog.product.random.code:true}")
	private boolean isGenerateRandomCode;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R create(ProductRequestDTO request) throws Exception {
		try
		{
			log.info("Creating new Product...");

			//Validating if product name exists related to a provider by code format
			Optional<AdmiProduct> optional = productDAOImpl.findProductByCodeAndName(request.getCode(), request.getName());
			
			if(optional.isPresent())
			{
				throw new CustomException("The product already exists with code : "+request.getCode());
			}
			
			//Validating category
			Optional<AdmiCategory> optionalCategory = categoryDAO.findById(Long.valueOf(request.getCategoryId()));
			if(!optionalCategory.isPresent())
			{
				throw new CustomException("The Category doesn't exists");
			}
			AdmiProduct product = Format.objectMapping(request, AdmiProduct.class);
			product.setCategory(optionalCategory.get());
			if(isGenerateRandomCode)
			{
				String code = product.getCode()+"-"+CatalogUtils.generateRandomCode();
				product.setCode(code);
			}	
						
			//Characteristics
			if(!request.getCharacteristics().isEmpty())
			{				
				List<AdmiProductCharact> listProductCharact = new ArrayList<>();
				
				for(ProductCharacteristicRequestDTO productCharacteristicDTO : request.getCharacteristics() )
				{
					//Validating if characteristic type is TEXT or MULTIPLE
					if(productCharacteristicDTO.getType()==EnumCharacteristicType.MULTIPLE &&
					   productCharacteristicDTO.getCharacteristicsValues().isEmpty())
					{
						throw new CustomException("You should add the asociated values related with charcateristic");
					}
					else
					{
						if(productCharacteristicDTO.getType()==EnumCharacteristicType.TEXT && 
						   productCharacteristicDTO.getValue()==null)
						{
							throw new CustomException("You should insert the charcateristic value");
						}
					}
										
					Optional<AdmiCharacteristic> optionalCharact = characteristicDAO.findById(Long.valueOf(productCharacteristicDTO.getCharacteristicId()));

					if(!optionalCharact.isPresent())
					{
						throw new CustomException("The characteristic reference doesn't exists");
					}
					
					AdmiProductCharact admiProductCharact = Format.objectMapping(productCharacteristicDTO, AdmiProductCharact.class);
					admiProductCharact.setCharacteristic(optionalCharact.get());	
					admiProductCharact.setProduct(product);
					
					if(productCharacteristicDTO.getType()==EnumCharacteristicType.MULTIPLE)
					{					
						admiProductCharact.setValue(null);
						
						//Characteristics Values
						if(!productCharacteristicDTO.getCharacteristicsValues().isEmpty())
						{
							List<AdmiProductCharactValue> listProductCharactList = new ArrayList<>();
							
							for(ProductCharacterisitcValueRequestDTO characterisitcValueDTO : productCharacteristicDTO.getCharacteristicsValues())
							{
								AdmiProductCharactValue charactValue = Format.objectMapping(characterisitcValueDTO, AdmiProductCharactValue.class);
								charactValue.setProductCharact(admiProductCharact);
								listProductCharactList.add(charactValue);
							}
							
							admiProductCharact.setLisProductCharactValues(listProductCharactList);						
						}					
					}
					
					listProductCharact.add(admiProductCharact);
					
				}
				
				product.setListProductCharacteristics(listProductCharact);				
			}
						
			product = productDAO.save(product);	
			
			String category = product.getCategory().getName();
			
			ProductCatalogResponseDTO response = Format.objectMapping(product, ProductCatalogResponseDTO.class);			
			response.setCategoryName(category);
			response.setProvider(product.getCategory().getProvider().getName());
			
			return (R) response;
		}
		catch(Exception e)
		{
			log.error("Error getting product catalog : {}",e.getLocalizedMessage());			
			if(e.getClass().getCanonicalName().equalsIgnoreCase(CustomException.class.getCanonicalName()))
			{
				throw new CustomException(e.getMessage());
			}					
			throw new CustomException("An error had ocurred in the transaction");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R update(ProductRequestDTO request) throws Exception {
		try
		{
			log.info("Updating Product...");
									
			AdmiProduct product = AdmiProduct.builder()
					.id(Long.valueOf(request.getId()))
					.status(EnumStates.active.name())
					.build();
			Example<AdmiProduct> filter = Example.of(product);
			Optional<AdmiProduct> optional = productDAO.findOne(filter);
			if(!optional.isPresent())
			{
				throw new CustomException("The product doesn't exists or is inactive");
			}
			
			//Getting actual product
			product = optional.get();						
			
			//Validate if category is been changed
			if(product.getCategory().getId()!=Long.valueOf(request.getCategoryId()))
			{
				//Validating category
				Optional<AdmiCategory> optionalCategory = categoryDAO.findById(Long.valueOf(request.getCategoryId()));
				if(!optionalCategory.isPresent())
				{
					throw new CustomException("The Category doesn't exists");
				}
				
				product.setCategory(optionalCategory.get());
			}						
			
			product.setDescription(request.getDescription());
			product.setName(request.getName());
			product.setUpdatedAt(new Date());
						
			product = productDAO.save(product);
						
			ProductCatalogResponseDTO response = Format.objectMapping(product, ProductCatalogResponseDTO.class);
			response.setCategoryName(product.getCategory().getName());
			
			return (R) response;
		}
		catch(Exception e)
		{
			log.error("Error updating product : {}",e.getLocalizedMessage());			
			if(e.getClass().getCanonicalName().equalsIgnoreCase(CustomException.class.getCanonicalName()))
			{
				throw new CustomException(e.getMessage());
			}					
			throw new CustomException("An error had ocurred in the transaction");
		}
	}

	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public void delete(ProductRequestDTO request) throws Exception {
		try
		{
			log.info("Deleting Product...");
									
			AdmiProduct product = AdmiProduct.builder()
					.id(Long.valueOf(request.getId()))
					.status(EnumStates.active.name())
					.build();
			Example<AdmiProduct> filter = Example.of(product);
			Optional<AdmiProduct> optional = productDAO.findOne(filter);
			if(!optional.isPresent())
			{
				throw new CustomException("The product doesn't exists or is inactive");
			}
			
			//Getting actual product
			product = optional.get();												
			product.setStatus(EnumStates.inactive.name());
			product.setUpdatedAt(new Date());
			product = productDAO.save(product);		
			
			//if product have characteristics, they are deleted too.
			List<AdmiProductCharact> listProductCaract = product.getListProductCharacteristics();
			
			if(!listProductCaract.isEmpty())
			{
				for(AdmiProductCharact productCharact : listProductCaract)
				{
					List<AdmiProductCharactValue> charactValues = productCharact.getLisProductCharactValues();
					
					for(AdmiProductCharactValue productCharactValue : charactValues)
					{
						productCharactValue.setStatus(EnumStates.inactive.name());
						productCharactValue.setUpdatedAt(new Date());
						productCharacteristicValueDAO.save(productCharactValue);
					}
					
					productCharact.setStatus(EnumStates.inactive.name());
					productCharact.setUpdatedAt(new Date());
					productCharacteristicDAO.save(productCharact);
				}
			}
		}
		catch(Exception e)
		{
			log.error("Error deleting product : {}",e.getLocalizedMessage());			
			if(e.getClass().getCanonicalName().equalsIgnoreCase(CustomException.class.getCanonicalName()))
			{
				throw new CustomException(e.getMessage());
			}					
			throw new CustomException("An error had ocurred in the transaction");
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R retrieve(ProductRequestDTO request) throws Exception {
		try
		{
			log.info("Getting Product...");
			
			Slice<ProductCatalogResponseDTO> responseList = productDAOImpl.findProductsBy(request);

			for(ProductCatalogResponseDTO dto : responseList)
			{				
				dto.add(Link.of(utils.getHATEOASUri(CatalogConstants.CHARACT, dto.getId()), CatalogConstants.CHARACT));	
				dto.add(Link.of(utils.getHATEOASUri(CatalogConstants.INVENTORY, dto.getId()), CatalogConstants.INVENTORY));
				dto.add(Link.of(utils.getHATEOASUri(CatalogConstants.PRICE, dto.getId()), CatalogConstants.PRICE));
				dto.add(Link.of(utils.getHATEOASUri(CatalogConstants.DISCOUNT, dto.getId()), CatalogConstants.DISCOUNT));		
			}
			
			return (R) responseList;
		}
		catch(Exception e)
		{
			log.error("Error getting product catalog : {}",e.getLocalizedMessage());			
			if(e.getClass().getCanonicalName().equalsIgnoreCase(CustomException.class.getCanonicalName()))
			{
				throw new CustomException(e.getMessage());
			}					
			throw new CustomException("An error had ocurred in the transaction");
		}
	}

}
