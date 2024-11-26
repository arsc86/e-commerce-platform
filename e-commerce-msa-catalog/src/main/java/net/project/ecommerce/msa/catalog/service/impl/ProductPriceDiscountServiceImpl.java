package net.project.ecommerce.msa.catalog.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import net.project.ecommerce.dependency.api.constants.catalog.CatalogProductPriceDiscMethodConstants;
import net.project.ecommerce.dependency.api.dto.catalog.ProductCharacteristicDTO;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductPriceDiscountRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.response.ProductPriceDiscountResponseDTO;
import net.project.ecommerce.dependency.api.enums.EnumProductValueType;
import net.project.ecommerce.dependency.api.enums.EnumStates;
import net.project.ecommerce.dependency.exception.CustomException;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.msa.catalog.dao.IProductDAO;
import net.project.ecommerce.msa.catalog.dao.IProductDiscountDAO;
import net.project.ecommerce.msa.catalog.dao.IProductPriceDAO;
import net.project.ecommerce.msa.catalog.dao.IRegionDAO;
import net.project.ecommerce.msa.catalog.model.AdmiProduct;
import net.project.ecommerce.msa.catalog.model.AdmiRegion;
import net.project.ecommerce.msa.catalog.model.InfoProductDiscount;
import net.project.ecommerce.msa.catalog.model.InfoProductPrice;
import net.project.ecommerce.msa.catalog.service.IProductPriceDiscCatalogHandler;

@Component(CatalogProductPriceDiscMethodConstants.PRODUCT_PRICE_DISC_SERVICE_BEAN)
public class ProductPriceDiscountServiceImpl implements IProductPriceDiscCatalogHandler {
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private IProductDAO productDAO;
	
	@Autowired
	private IProductPriceDAO productPriceDAO;
	
	@Autowired
	private IProductDiscountDAO productDiscountDAO;
	
	@Autowired
	private IRegionDAO regionDAO;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R create(ProductCharacteristicDTO request) throws Exception {
		try
		{
			log.info("Adding price or discount to Product...");
			
			if(request.getPricesDiscounts().isEmpty())
			{
				throw new CustomException("The prices or discounts don't exist");
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
			
			if(request.getPricesDiscounts().size()>1)
			{
				for(ProductPriceDiscountRequestDTO priceDTO : request.getPricesDiscounts())
				{
					for(ProductPriceDiscountRequestDTO priceDTO1 : request.getPricesDiscounts())
					{
						if(priceDTO.getCategory().equals(priceDTO1.getCategory()))
						{
							throw new CustomException("The new category type should be different");
						}
					}												
				}
			}
								
			//Validate the product type
			if(request.getType().equals(EnumProductValueType.PRICE))
			{
				for(InfoProductPrice productPrice : product.getListProductPrices())
				{
					for(ProductPriceDiscountRequestDTO priceDTO : request.getPricesDiscounts())
					{
						//Validate if price type doesn't exists
						if(priceDTO.getCategory().name().equals(productPrice.getType()) && 
						   productPrice.getStatus().equalsIgnoreCase(EnumStates.active.name()))
						{
							throw new CustomException("The category price type already exists");
						}												
					}
				}
			}
			else				
			{
				for(InfoProductDiscount productDisc : product.getListProductDiscounts())
				{
					for(ProductPriceDiscountRequestDTO priceDTO : request.getPricesDiscounts())
					{
						//Validate if discount type doesn't exists
						if(priceDTO.getCategory().name().equals(productDisc.getType()) && 
						   productDisc.getStatus().equalsIgnoreCase(EnumStates.active.name()))
						{
							throw new CustomException("The category discount type already exists");
						}												
					}
				}
			}
				
			if(!request.getPricesDiscounts().isEmpty())
			{
				List<InfoProductPrice> listProductPrice   = new ArrayList<>();
				List<InfoProductDiscount> listProductDisc = new ArrayList<>();
				
				for(ProductPriceDiscountRequestDTO priceDTO : request.getPricesDiscounts())
				{						
					//Getting Region
					Optional<AdmiRegion> optionalRegion = regionDAO.findOne(Example.of(AdmiRegion.builder()
																								.id(Long.valueOf(priceDTO.getLocationId()))
																								.status(EnumStates.active.name())
																								.build())
																								);						
					if(!optionalRegion.isPresent())
					{
						throw new CustomException("The Location reference doesn't exists or is inactive");
					}
					
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date startDate = dateFormat.parse(priceDTO.getStartDate());
					Date endDate   = dateFormat.parse(priceDTO.getEndDate());
					
					if(startDate.after(endDate))
					{
						throw new CustomException("The start date should be minor than end date");
					}
					
					if(request.getType().equals(EnumProductValueType.PRICE))
					{
						InfoProductPrice price = InfoProductPrice.builder()
								 .value(Double.valueOf(priceDTO.getValue()))
								 .region(optionalRegion.get())
								 .startDate(dateFormat.parse(priceDTO.getStartDate()))
								 .endDate(dateFormat.parse(priceDTO.getEndDate()))
								 .type(priceDTO.getCategory().name())
								 .product(product)
								 .build();
						listProductPrice.add(price);
						product.setListProductPrices(listProductPrice);
					}
					else
					{
						InfoProductDiscount price = InfoProductDiscount.builder()
								 .value(Double.valueOf(priceDTO.getValue()))
								 .region(optionalRegion.get())
								 .startDate(dateFormat.parse(priceDTO.getStartDate()))
								 .endDate(dateFormat.parse(priceDTO.getEndDate()))
								 .type(priceDTO.getCategory().name())
								 .product(product)
								 .build();
						listProductDisc.add(price);
						product.setListProductDiscounts(listProductDisc);
					}
				}								
			}

			productDAO.save(product);
			
			return (R) "OK";
		}
		catch(Exception e)
		{
			log.error("Error adding price to product : {}",e.getLocalizedMessage());			
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
			log.info("Deleting price or discount from Product...");
			
			if(request.getPricesDiscounts().isEmpty())
			{
				throw new CustomException("The product price or discount list don't exist or is inactive");
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
			
			boolean valueExist = true;						
			
			if(request.getType().equals(EnumProductValueType.PRICE))
			{
				//Validating if product price already exists related to product
				for(InfoProductPrice price : product.getListProductPrices() )
				{
					for(ProductPriceDiscountRequestDTO dto : request.getPricesDiscounts())
					{
						if(price.getId() != Long.valueOf(dto.getValueId()))
						{
							valueExist = false;
						}					
					}
				}
				
				if(!valueExist)
				{
					throw new CustomException("One of the product price doesn't exists, please check it out and try again");
				}
				
				for(InfoProductPrice price : product.getListProductPrices())
				{
					for(ProductPriceDiscountRequestDTO priceDTO : request.getPricesDiscounts())
					{
						if(price.getId()==Long.valueOf(priceDTO.getValueId()))
						{
							price.setStatus(EnumStates.inactive.name());
							price.setUpdatedAt(new Date());
							productPriceDAO.save(price);
						}
					}										
				}
			}
			else//DISCOUNT
			{
				//Validating if product discount already exists related to product
				for(InfoProductDiscount price : product.getListProductDiscounts() )
				{
					for(ProductPriceDiscountRequestDTO dto : request.getPricesDiscounts())
					{
						if(price.getId() != Long.valueOf(dto.getValueId()))
						{
							valueExist = false;
						}					
					}
				}
				
				if(!valueExist)
				{
					throw new CustomException("One of the product discount doesn't exists, please check it out and try again");
				}
				
				for(InfoProductDiscount discount : product.getListProductDiscounts())
				{
					for(ProductPriceDiscountRequestDTO priceDTO : request.getPricesDiscounts())
					{
						if(discount.getId()==Long.valueOf(priceDTO.getValueId()))
						{
							discount.setStatus(EnumStates.inactive.name());
							discount.setUpdatedAt(new Date());
							productDiscountDAO.save(discount);
						}
					}										
				}
			}			
		}
		catch(Exception e)
		{
			log.error("Error getting price/discount from product : {}",e.getLocalizedMessage());			
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
			log.info("Getting price or discount from Product...");
						
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
			
			List<ProductPriceDiscountResponseDTO> response = new ArrayList<>();
			
			if(request.getType().equals(EnumProductValueType.PRICE))
			{
				for(InfoProductPrice price : product.getListProductPrices())
				{
					if(price.getStatus().equalsIgnoreCase(EnumStates.active.name()))
					{
						ProductPriceDiscountResponseDTO priceDTO = Format.objectMapping(price, ProductPriceDiscountResponseDTO.class);
						AdmiRegion region = price.getRegion();
						priceDTO.setLocationId(region.getId());
						priceDTO.setLocation(region.getName());
						response.add(priceDTO);
					}					
				}
			}
			else
			{
				for(InfoProductDiscount price : product.getListProductDiscounts())
				{
					if(price.getStatus().equalsIgnoreCase(EnumStates.active.name()))
					{
						ProductPriceDiscountResponseDTO priceDTO = Format.objectMapping(price, ProductPriceDiscountResponseDTO.class);
						AdmiRegion region = price.getRegion();
						priceDTO.setLocationId(region.getId());
						priceDTO.setLocation(region.getName());
						response.add(priceDTO);
					}					
				}
			}
			
			return (R) response;
		}
		catch(Exception e)
		{
			log.error("Error getting price/discount from product : {}",e.getLocalizedMessage());			
			if(e.getClass().getCanonicalName().equalsIgnoreCase(CustomException.class.getCanonicalName()))
			{
				throw new CustomException(e.getMessage());
			}					
			throw new CustomException("An error had ocurred in the transaction");
		}
	}

}
