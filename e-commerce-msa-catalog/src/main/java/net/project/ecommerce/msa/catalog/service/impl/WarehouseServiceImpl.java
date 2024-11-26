package net.project.ecommerce.msa.catalog.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import net.project.ecommerce.dependency.api.constants.catalog.CatalogWarehouseMethodConstants;
import net.project.ecommerce.dependency.api.dto.catalog.request.WarehouseRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.response.WarehouseResponseDTO;
import net.project.ecommerce.dependency.api.enums.EnumStates;
import net.project.ecommerce.dependency.exception.CustomException;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.msa.catalog.dao.IRegionDAO;
import net.project.ecommerce.msa.catalog.dao.IWarehouseDAO;
import net.project.ecommerce.msa.catalog.model.AdmiRegion;
import net.project.ecommerce.msa.catalog.model.AdmiWarehouse;
import net.project.ecommerce.msa.catalog.service.IWarehouseCatalogHandler;

@Service(CatalogWarehouseMethodConstants.WAREHOUSE_SERVICE_BEAN)
public class WarehouseServiceImpl implements IWarehouseCatalogHandler{
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private IWarehouseDAO warehouseDAO;
	
	@Autowired
	private IRegionDAO regionDAO;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R create(WarehouseRequestDTO data) throws Exception {
		try
		{
			log.info("Creating Warehouse catalog...");			
			AdmiWarehouse warehouse = Format.objectMapping(data,AdmiWarehouse.class);
			Example<AdmiWarehouse> example = Example.of(AdmiWarehouse.builder().name(data.getName()).build());
			Optional<AdmiWarehouse> optional = warehouseDAO.findOne(example);
			if(optional.isPresent())
			{
				throw new CustomException("Warehouse must be different");
			}		
			Example<AdmiRegion> region = Example.of(AdmiRegion.builder().id(Long.valueOf(data.getLocation())).build());
			Optional<AdmiRegion> optionalRegion = regionDAO.findOne(region);
			if(!optionalRegion.isPresent())
			{
				throw new CustomException("Reference Location doesn't exists");
			}
			
			warehouse.setRegion(optionalRegion.get());
			warehouse = warehouseDAO.save(warehouse);	
			
			WarehouseResponseDTO response = Format.objectMapping(warehouse, WarehouseResponseDTO.class);
			response.setLocationId(warehouse.getRegion().getId());
			response.setLocation(warehouse.getRegion().getName());
			
			return (R) response;
		}
		catch(Exception e)
		{
			log.error("Error creating a new warehouse : {}",e.getLocalizedMessage());			
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
	public <R> R update(WarehouseRequestDTO request) throws Exception {
		try
		{
			log.info("Updating Warehouse catalog...");	
			AdmiWarehouse warehouse = AdmiWarehouse.builder()
					.id(Long.valueOf(request.getId()))
					.status(EnumStates.active.name())
					.build();
			//AdmiWarehouse warehouse = Format.objectMapping(data,AdmiWarehouse.class);
			Example<AdmiWarehouse> example = Example.of(warehouse);
			Optional<AdmiWarehouse> optional = warehouseDAO.findOne(example);
			if(!optional.isPresent())
			{
				throw new CustomException("Warehouse to be updated doesn't exists or is inactive");
			}		
			
			warehouse = optional.get();
			
			//if location changes
			if(warehouse.getRegion().getId() != Long.valueOf(request.getLocation()))
			{
				Example<AdmiRegion> region = Example.of(AdmiRegion.builder().id(Long.valueOf(request.getLocation())).build());
				Optional<AdmiRegion> optionalRegion = regionDAO.findOne(region);
				if(!optionalRegion.isPresent())
				{
					throw new CustomException("Reference Location doesn't exists");
				}
				
				warehouse.setRegion(optionalRegion.get());
			}
			
			warehouse.setAddress(request.getAddress());
			warehouse.setCoordinates(request.getCoordinates());
			warehouse.setDeliveryTime(request.getDeliveryTime().toString());
			warehouse.setName(request.getName());
			warehouse.setUpdatedAt(new Date());
			
			warehouse = warehouseDAO.save(warehouse);			
			
			WarehouseResponseDTO response = Format.objectMapping(warehouse, WarehouseResponseDTO.class);
			response.setLocationId(warehouse.getRegion().getId());
			response.setLocation(warehouse.getRegion().getName());
			
			return (R) response;
		}
		catch(Exception e)
		{
			log.error("Error updating warehouse : {}",e.getLocalizedMessage());			
			if(e.getClass().getCanonicalName().equalsIgnoreCase(CustomException.class.getCanonicalName()))
			{
				throw new CustomException(e.getMessage());
			}					
			throw new CustomException("An error had ocurred in the transaction");
		}
	}
	
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public void delete(WarehouseRequestDTO request) throws Exception {
		try
		{
			log.info("Deleting Warehouse catalog...");	
			AdmiWarehouse warehouse = AdmiWarehouse.builder()
					.id(Long.valueOf(request.getId()))
					.status(EnumStates.active.name())
					.build();
			
			Example<AdmiWarehouse> example = Example.of(warehouse);
			Optional<AdmiWarehouse> optional = warehouseDAO.findOne(example);
			if(!optional.isPresent())
			{
				throw new CustomException("Warehouse to be deleted doesn't exists or is inactive");
			}		
			
			warehouse = optional.get();						
			
			warehouse.setStatus(EnumStates.inactive.name());
			warehouse.setUpdatedAt(new Date());
			
			warehouseDAO.save(warehouse);									
		}
		catch(Exception e)
		{
			log.error("Error deleting warehouse : {}",e.getLocalizedMessage());			
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
	public <R> R retrieve(WarehouseRequestDTO request) throws Exception {
		try
		{
			log.info("Getting Warehouse catalog...");			
			AdmiWarehouse warehouse = Format.objectMapping(request,AdmiWarehouse.class);	
			if(request.getLocation()!=null && !request.getLocation().isEmpty())
			{				
				warehouse.setRegion(AdmiRegion.builder().id(Long.valueOf(request.getLocation())).build());
			}
			Example<AdmiWarehouse> filter       = Example.of(warehouse);
			List<AdmiWarehouse> optionalregion  = warehouseDAO.findAll(filter);	
			
			List<WarehouseResponseDTO> response = new ArrayList<>();
			
			for(AdmiWarehouse wh : optionalregion)
			{
				WarehouseResponseDTO whdto = Format.objectMapping(wh, WarehouseResponseDTO.class);
				whdto.setLocationId(wh.getRegion().getId());
				whdto.setLocation(wh.getRegion().getName());
				response.add(whdto);
			}
			
			return (R) response;
		}
		catch(Exception e)
		{
			log.error("Error getting warehouse : {}",e.getLocalizedMessage());			
			throw new CustomException(e.getMessage());
		}
	}

}
