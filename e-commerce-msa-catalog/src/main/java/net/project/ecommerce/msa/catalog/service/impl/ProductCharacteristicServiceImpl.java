package net.project.ecommerce.msa.catalog.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import net.project.ecommerce.dependency.api.constants.catalog.CatalogProductCharacMethodConstants;
import net.project.ecommerce.dependency.api.dto.catalog.ProductCharacteristicDTO;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductRequestDTO.ProductCharacterisitcValueRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductRequestDTO.ProductCharacteristicRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.response.ProductFileResponseDTO.FileDTO;
import net.project.ecommerce.dependency.api.enums.EnumCharacteristicType;
import net.project.ecommerce.dependency.api.enums.EnumStates;
import net.project.ecommerce.dependency.exception.CustomException;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.msa.catalog.dao.ICharacteristicDAO;
import net.project.ecommerce.msa.catalog.dao.IProductCharacteristicDAO;
import net.project.ecommerce.msa.catalog.dao.IProductCharacteristicValueDAO;
import net.project.ecommerce.msa.catalog.dao.IProductDAO;
import net.project.ecommerce.msa.catalog.dao.IProductFileDAO;
import net.project.ecommerce.msa.catalog.model.AdmiCharacteristic;
import net.project.ecommerce.msa.catalog.model.AdmiProduct;
import net.project.ecommerce.msa.catalog.model.AdmiProductCharact;
import net.project.ecommerce.msa.catalog.model.AdmiProductCharactValue;
import net.project.ecommerce.msa.catalog.service.IProductCharcteristicCatalogHandler;
import net.project.ecommerce.msa.catalog.util.CatalogConstants;
import net.project.ecommerce.msa.catalog.util.CatalogUtils;

@Service(CatalogProductCharacMethodConstants.PRODUCT_CHARAC_SERVICE_BEAN)
public class ProductCharacteristicServiceImpl implements IProductCharcteristicCatalogHandler{
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private IProductDAO productDAO;
	
	@Autowired
	private IProductCharacteristicDAO productCharacteristicDAO;
	
	@Autowired
	private IProductCharacteristicValueDAO productCharacteristicValueDAO;
	
	@Autowired
	private ICharacteristicDAO characteristicDAO;
	
	@Autowired
	private IProductFileDAO productFileDAO;
	
	@Autowired
	private CatalogUtils utils;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R create(ProductCharacteristicDTO request) throws Exception {
		try
		{
			log.info("Adding new Characteristic to Product : {}",request.getProductId());
			AdmiProduct product = AdmiProduct.builder()
                                              .id(Long.valueOf(request.getProductId()))
                                              .status(EnumStates.active.name())
                                              .build();
			Example<AdmiProduct> filter = Example.of(product);
			Optional<AdmiProduct> optional = productDAO.findOne(filter);
			if(!optional.isPresent())
			{
				throw new CustomException("The product reference doesn't exists or is inactive");
			}
			
			product = optional.get();
						
			//Validating if characteristic already exists related to product
			for(AdmiProductCharact productCharact : product.getListProductCharacteristics() )
			{
				for(ProductCharacteristicRequestDTO characteristicRequestDTO : request.getCharacteristics())
				{
					if(productCharact.getCharacteristic().getId() == Long.valueOf(characteristicRequestDTO.getCharacteristicId()))
					{
						throw new CustomException("The characteristic "+productCharact.getCharacteristic().getName()+" already exists");
					}
				}
			}
			
			//Characteristics
			if(!request.getCharacteristics().isEmpty())
			{				
				List<AdmiProductCharact> listProductCharact = new ArrayList<>();
				
				for(ProductCharacteristicRequestDTO ProductCharacteristicRequestDTO : request.getCharacteristics())
				{
					//Validating if characteristic type is TEXT or MULTIPLE
					if(ProductCharacteristicRequestDTO.getType()==EnumCharacteristicType.MULTIPLE &&
							(ProductCharacteristicRequestDTO.getCharacteristicsValues()==null ||
							ProductCharacteristicRequestDTO.getCharacteristicsValues().isEmpty()))
					{
						throw new CustomException("You should add the asociated values related with charcateristic");
					}
					else
					{
						if(ProductCharacteristicRequestDTO.getType()==EnumCharacteristicType.TEXT && 
						   ProductCharacteristicRequestDTO.getValue()==null)
						{
							throw new CustomException("You should insert the charcateristic value");
						}
					}
										
					Optional<AdmiCharacteristic> optionalCharact = 
							characteristicDAO.findById(Long.valueOf(ProductCharacteristicRequestDTO.getCharacteristicId()));

					if(!optionalCharact.isPresent())
					{
						throw new CustomException("The characteristic reference doesn't exists");
					}
														
					AdmiProductCharact admiProductCharact = Format.objectMapping(ProductCharacteristicRequestDTO, AdmiProductCharact.class);
					admiProductCharact.setCharacteristic(optionalCharact.get());	
					admiProductCharact.setProduct(product);
																			
					if(ProductCharacteristicRequestDTO.getType()==EnumCharacteristicType.MULTIPLE)
					{					
						admiProductCharact.setValue(null);
						
						//Characteristics Values
						if(!ProductCharacteristicRequestDTO.getCharacteristicsValues().isEmpty())
						{
							List<AdmiProductCharactValue> listProductCharactList = new ArrayList<>();
							
							for(ProductCharacterisitcValueRequestDTO characterisitcValueDTO : ProductCharacteristicRequestDTO.getCharacteristicsValues())
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
			
			productDAO.save(product);
		
			return (R) "OK";
		}
		catch(Exception e)
		{
			log.error("Error adding characteristic to product {} : {}",request.getProductId(),e.getLocalizedMessage());			
			if(e.getClass().getCanonicalName().equalsIgnoreCase(CustomException.class.getCanonicalName()))
			{
				throw new CustomException(e.getMessage());
			}					
			throw new CustomException("An error had ocurred in the transaction");
		}
	}
	
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public void delete(ProductCharacteristicDTO request) throws Exception {
		try
		{
			log.info("Deleting Characteristic from Product : {}",request.getProductId());
			if(request.getCharacteristics()==null)
			{
				throw new CustomException("Don't exist characteristics to be deleted");
			}
			AdmiProduct product = AdmiProduct.builder()
                                              .id(Long.valueOf(request.getProductId()))
                                              .status(EnumStates.active.name())
                                              .build();
			Example<AdmiProduct> filter = Example.of(product);
			Optional<AdmiProduct> optional = productDAO.findOne(filter);
			if(!optional.isPresent())
			{
				throw new CustomException("The product reference doesn't exists or is inactive");
			}
			
			product = optional.get();
			
			boolean charactExists = true;
			
			//Validating if characteristic already exists related to product
			for(AdmiProductCharact productCharact : product.getListProductCharacteristics() )
			{
				for(ProductCharacteristicRequestDTO characteristicRequestDTO : request.getCharacteristics())
				{
					if(productCharact.getCharacteristic().getId() != Long.valueOf(characteristicRequestDTO.getCharacteristicId()))
					{
						charactExists = false;
					}					
				}
			}
			
			if(!charactExists)
			{
				throw new CustomException("One of the characteristic doesn't exists, please check it out and try again");
			}
			
			//Characteristics
			if(request.getCharacteristics()!=null && !request.getCharacteristics().isEmpty())
			{				
				//Getting existing characteristics
				List<AdmiProductCharact> listProductCharact = product.getListProductCharacteristics();
				
				for(AdmiProductCharact productCharact : listProductCharact)
				{					
					for(ProductCharacteristicRequestDTO characteristicDTO : request.getCharacteristics() )
					{
						//If exist a coincidence, the register will be inactive 
						if(productCharact.getCharacteristic().getId()==Long.valueOf(characteristicDTO.getCharacteristicId()))
						{
							productCharact.setStatus(EnumStates.inactive.name());
							productCharact.setUpdatedAt(new Date());
							
							List<AdmiProductCharactValue> listCharactValues = productCharact.getLisProductCharactValues();
							
							for(AdmiProductCharactValue charactValue : listCharactValues )
							{
								charactValue.setStatus(EnumStates.inactive.name());
								charactValue.setUpdatedAt(new Date());
								productCharacteristicValueDAO.save(charactValue);
							}
							
							productCharacteristicDAO.save(productCharact);
						}
					}						
				}						
			}					
		}
		catch(Exception e)
		{
			log.error("Error deleting characteristic from product {} : {}",request.getProductId(),e.getLocalizedMessage());			
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
	public <R> R retrieve(ProductCharacteristicDTO request) throws Exception {
		try
		{
			log.info("Getting Characteristic from Product : {}",request.getProductId());
			AdmiProduct product = AdmiProduct.builder()
                                              .id(Long.valueOf(request.getProductId()))
                                              .status(EnumStates.active.name())
                                              .build();
			Example<AdmiProduct> filter = Example.of(product);
			Optional<AdmiProduct> optional = productDAO.findOne(filter);
			if(!optional.isPresent())
			{
				throw new CustomException("The product reference doesn't exists or is inactive");
			}
			
			product = optional.get();
			
			List<ProductCharacteristicRequestDTO> productCharactList = productDAO.findProductCharacteristicBy(request.getProductId());

			for(ProductCharacteristicRequestDTO productCharactdto : productCharactList)
			{					
				EnumCharacteristicType type = productCharactdto.getType();
				Long productCharactId       = productCharactdto.getId();

				//Getting characteristic values
				
				if(type.equals(EnumCharacteristicType.MULTIPLE))
				{
					List<ProductCharacterisitcValueRequestDTO> productCharacValuetList = 
							productDAO.findProductCharacteristicValueBy(Long.valueOf(productCharactId));
					
					for(ProductCharacterisitcValueRequestDTO value : productCharacValuetList )
					{
						if(value.getFiles()>0)
						{
							List<FileDTO> list = productFileDAO.getFileByCharactValue(value.getId(), null);
							for(FileDTO file : list)
							{
								value.add(Link.of(utils.getHATEOASUri(CatalogConstants.FILE, value.getId(),file.getId()), 
										                              CatalogConstants.FILE));
							}							
						}							
					}
					
					productCharactdto.setCharacteristicsValues(productCharacValuetList);						
				}	
			}
	
			return (R) productCharactList;				
								
		}
		catch(Exception e)
		{
			log.error("Error getting characteristic from product {} : {}",request.getProductId(),e.getLocalizedMessage());			
			if(e.getClass().getCanonicalName().equalsIgnoreCase(CustomException.class.getCanonicalName()))
			{
				throw new CustomException(e.getMessage());
			}					
			throw new CustomException("An error had ocurred in the transaction");
		}
	}

}
