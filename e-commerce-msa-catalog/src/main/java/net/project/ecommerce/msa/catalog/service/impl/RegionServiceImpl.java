package net.project.ecommerce.msa.catalog.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import net.project.ecommerce.dependency.api.constants.catalog.CatalogRegionMethodConstants;
import net.project.ecommerce.dependency.api.dto.catalog.request.RegionRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.response.RegionResponseDTO;
import net.project.ecommerce.dependency.api.enums.EnumStates;
import net.project.ecommerce.dependency.exception.CustomException;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.msa.catalog.dao.IRegionDAO;
import net.project.ecommerce.msa.catalog.model.AdmiRegion;
import net.project.ecommerce.msa.catalog.service.IRegionCatalogHandler;

@Service(CatalogRegionMethodConstants.REGION_SERVICE_BEAN)
public class RegionServiceImpl implements IRegionCatalogHandler {
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private IRegionDAO regionDAO;		
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R create(RegionRequestDTO request) throws Exception {
		try
		{
			log.info("Creating Region catalog...");			
			AdmiRegion region = Format.objectMapping(request,AdmiRegion.class);
			Example<AdmiRegion> example = Example.of(AdmiRegion.builder().name(request.getName()).build());
			Optional<AdmiRegion> optional = regionDAO.findOne(example);
			if(optional.isPresent())
			{
				throw new CustomException("Region must be different");
			}			
			region = regionDAO.save(region);					
			return (R) Format.objectMapping(region, RegionResponseDTO.class);
		}
		catch(Exception e)
		{
			log.error("Error creating a new region : {}",e.getLocalizedMessage());			
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
	public <R> R update(RegionRequestDTO request) throws Exception {
		try
		{
			log.info("Updating Region catalog...");			
			AdmiRegion region = AdmiRegion.builder()
					.id(request.getId())
					.status(EnumStates.active.name())
					.build();			
			Example<AdmiRegion> filter = Example.of(region);
			Optional<AdmiRegion> optionalProvider = regionDAO.findOne(filter);
			if(!optionalProvider.isPresent())
			{
				throw new CustomException("Region to be updated doesn't exists or is inactive");
			}
			region = optionalProvider.get();
			
			//Validate if changes
			if(!region.getName().equalsIgnoreCase(request.getName()))
			{				
				region.setName(request.getName());
				region.setParent( request.getParent());
				region.setUpdatedAt(new Date());
				region = regionDAO.save(region);	
			}
													
			return (R) Format.objectMapping(region, RegionResponseDTO.class);
		}
		catch(Exception e)
		{
			log.error("Error updating region : {}",e.getLocalizedMessage());			
			if(e.getClass().getCanonicalName().equalsIgnoreCase(CustomException.class.getCanonicalName()))
			{
				throw new CustomException(e.getMessage());
			}					
			throw new CustomException("An error had ocurred in the transaction");
		}
	}

	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public void delete(RegionRequestDTO request) throws Exception {
		try
		{
			log.info("Deleting Region catalog...");			
			AdmiRegion region = Format.objectMapping(request,AdmiRegion.class);
			region.setStatus(EnumStates.active.name());
			Example<AdmiRegion> filter = Example.of(region);
			Optional<AdmiRegion> optionalProvider = regionDAO.findOne(filter);
			if(!optionalProvider.isPresent())
			{
				throw new CustomException("Region to be deleted doesn't exists or is inactive");
			}
			region = optionalProvider.get();
			
			region.setStatus(EnumStates.inactive.name());
			region.setUpdatedAt(new Date());
			regionDAO.save(region);					
		}
		catch(Exception e)
		{
			log.error("Error deleting region : {}",e.getLocalizedMessage());			
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
	public <R> R retrieve(RegionRequestDTO request) throws Exception {
		try
		{
			log.info("Getting Region catalog...");			
			AdmiRegion region = Format.objectMapping(request,AdmiRegion.class);				
			Example<AdmiRegion> filter       = Example.of(region);
			List<AdmiRegion> optionalregion  = regionDAO.findAll(filter);						
			return (R) Format.listMapping(optionalregion, RegionResponseDTO.class);
		}
		catch(Exception e)
		{
			log.error("Error getting region : {}",e.getLocalizedMessage());			
			throw new CustomException(e.getMessage());
		}
	}

}
