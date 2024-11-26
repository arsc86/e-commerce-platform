package net.project.ecommerce.msa.catalog.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import net.project.ecommerce.dependency.api.constants.catalog.CatalogProductInventoryMethodConstants;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductInventoryRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.response.ProductInventoryResponseDTO;
import net.project.ecommerce.dependency.api.enums.EnumInventoryActionType;
import net.project.ecommerce.dependency.api.enums.EnumStates;
import net.project.ecommerce.dependency.exception.CustomException;
import net.project.ecommerce.msa.catalog.dao.IProductDAO;
import net.project.ecommerce.msa.catalog.dao.IProductInventoryDAO;
import net.project.ecommerce.msa.catalog.model.AdmiProduct;
import net.project.ecommerce.msa.catalog.service.IProductInventoryHandler;

@Component(CatalogProductInventoryMethodConstants.PRODUCT_INVENTORY_SERVICE_BEAN)
public class ProductInventoryServiceImpl implements IProductInventoryHandler{
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private IProductInventoryDAO inventoryDAO;
	
	@Autowired
	private IProductDAO productDAO;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R stockInOut(ProductInventoryRequestDTO request) throws Exception {
		try
		{
			log.info("Updating Product Inventory...");

			//Validating if product name exists
			Optional<AdmiProduct> optional = productDAO.findOne(Example.of(AdmiProduct.builder()
					                                                                  .id(Long.valueOf(request.getProductId()))
					                                                                  .status(EnumStates.active.name())
					                                                                  .build()));
			
			if(!optional.isPresent())
			{
				throw new CustomException("The product reference doesn't exists or is inactive");
			}
			
			if(request.getAction().equals(EnumInventoryActionType.STOCK_OUT))
			{
				int updated = inventoryDAO.releaseStock(Long.valueOf(request.getProductId()),
						                                Long.valueOf(request.getWarehouseId()),
						                                Integer.valueOf(request.getQuantity())
						                               );
				
				if(updated == 0)
				{
					throw new CustomException("The product stock couldn't be updated, the reference maybe doesn't exists");
				}
			}
			else
			{
				int updated = inventoryDAO.reserveStock(Long.valueOf(request.getProductId()),
								                        Long.valueOf(request.getWarehouseId()),
								                        Integer.valueOf(request.getQuantity())
								                       );
				if(updated == 0)
				{
					throw new CustomException("The product stock couldn't be reserved, doesn't has enough stock");
				}
			}
			
			return (R) "OK";
		}
		catch(Exception e)
		{
			log.error("Error updating inventory : {}",e.getLocalizedMessage());			
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
	public <R> R retrieve(ProductInventoryRequestDTO request) throws Exception {
		try
		{
			log.info("Getting Product Inventory...");

			//Validating if product name exists
			Optional<AdmiProduct> optional = productDAO.findOne(Example.of(AdmiProduct.builder()
					                                                                  .id(Long.valueOf(request.getProductId()))
					                                                                  .status(EnumStates.active.name())
					                                                                  .build()));
			
			if(!optional.isPresent())
			{
				throw new CustomException("The product reference doesn't exists or is inactive");
			}						
			
			List<ProductInventoryResponseDTO> list = inventoryDAO.findProductInventoryBy(request.getProductId(), request.getWarehouseId());
			
			return (R) list;
		}
		catch(Exception e)
		{
			log.error("Getting inventory : {}",e.getLocalizedMessage());			
			if(e.getClass().getCanonicalName().equalsIgnoreCase(CustomException.class.getCanonicalName()))
			{
				throw new CustomException(e.getMessage());
			}					
			throw new CustomException("An error had ocurred in the transaction");
		}
	}

}
