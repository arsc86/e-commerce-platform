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
import net.project.ecommerce.dependency.api.constants.catalog.CatalogProviderMethodConstants;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProviderRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.response.ProviderResponseDTO;
import net.project.ecommerce.dependency.api.enums.EnumStates;
import net.project.ecommerce.dependency.exception.CustomException;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.msa.catalog.dao.ICategoryDAO;
import net.project.ecommerce.msa.catalog.dao.IProviderDAO;
import net.project.ecommerce.msa.catalog.dao.impl.ProviderDAOImpl;
import net.project.ecommerce.msa.catalog.model.AdmiCategory;
import net.project.ecommerce.msa.catalog.model.AdmiProvider;
import net.project.ecommerce.msa.catalog.service.IProviderCatalogHandler;

@Service(CatalogProviderMethodConstants.PROVIDER_SERVICE_BEAN)
public class ProviderServiceImpl implements IProviderCatalogHandler {
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private IProviderDAO providerDAO;
	
	@Autowired
	private ICategoryDAO categoryDAO;
	
	@Autowired
	private ProviderDAOImpl providerDAOImpl;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R create(ProviderRequestDTO request) throws Exception {
		try
		{
			log.info("Creating Provider catalog...");			
			AdmiProvider provider = Format.objectMapping(request,AdmiProvider.class);
			
			Optional<AdmiProvider> optional = providerDAOImpl.findProviderByNameOrProviderCode(request.getName(), request.getProviderCode());
			if(optional.isPresent())
			{
				throw new CustomException("Provider must be different");
			}
			provider = providerDAO.save(provider);					
			return (R) Format.objectMapping(provider, ProviderResponseDTO.class);
		}
		catch(Exception e)
		{
			log.error("Error creating a new provider : {}",e.getLocalizedMessage());			
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
	public <R> R update(ProviderRequestDTO request) throws Exception {
		try
		{
			log.info("Updating Provider catalog...");			
			AdmiProvider provider = AdmiProvider.builder()
					.id(request.getId())
					.status(EnumStates.active.name())
					.build();			
			Example<AdmiProvider> filter = Example.of(provider);
			Optional<AdmiProvider> optionalProvider = providerDAO.findOne(filter);
			if(!optionalProvider.isPresent())
			{
				throw new CustomException("Provider to be updated doesn't exists or is inactive");
			}
			provider = optionalProvider.get();
			
			//Validate if changes
			if(!provider.getName().equalsIgnoreCase(request.getName()) ||
			   !provider.getProviderCode().equalsIgnoreCase(request.getProviderCode()))
			{
				//Validate if changes exist in another register
				Optional<AdmiProvider> optional = providerDAOImpl.findProviderByNameOrProviderCode(request.getName(),
						                                                                           request.getProviderCode());
				if(optional.isPresent() && optional.get().getId()!=request.getId())
				{
					throw new CustomException("Provider data for update, must be different to another ones ");
				}
				
				provider.setName(request.getName());
				provider.setProviderCode(request.getProviderCode());
				provider.setUpdatedAt(new Date());
				provider = providerDAO.save(provider);	
			}
													
			return (R) Format.objectMapping(provider, ProviderResponseDTO.class);
		}
		catch(Exception e)
		{
			log.error("Error updating a new provider : {}",e.getLocalizedMessage());			
			if(e.getClass().getCanonicalName().equalsIgnoreCase(CustomException.class.getCanonicalName()))
			{
				throw new CustomException(e.getMessage());
			}					
			throw new CustomException("An error had ocurred in the transaction");
		}
	}

	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public void delete(ProviderRequestDTO request) throws Exception {
		try
		{
			log.info("Deleting Provider catalog...");			
			AdmiProvider provider = Format.objectMapping(request,AdmiProvider.class);
			provider.setStatus(EnumStates.active.name());
			Example<AdmiProvider> filter = Example.of(provider);
			Optional<AdmiProvider> optionalProvider = providerDAO.findOne(filter);
			if(!optionalProvider.isPresent())
			{
				throw new CustomException("Provider to be deleted doesn't exists or is inactive");
			}
			provider = optionalProvider.get();
			
			provider.setStatus(EnumStates.inactive.name());
			provider.setUpdatedAt(new Date());
			providerDAO.save(provider);
			
			AdmiCategory category = AdmiCategory.builder()
											    .provider(provider)
											    .build();
			Example<AdmiCategory> categoryFilter = Example.of(category);
			List<AdmiCategory> list = categoryDAO.findAll(categoryFilter);
			
			for(AdmiCategory cat : list)
			{
				cat.setStatus(EnumStates.inactive.name());
				cat.setUpdatedAt(new Date());
				categoryDAO.save(cat);
			}					
		}
		catch(Exception e)
		{
			log.error("Error updating a new provider : {}",e.getLocalizedMessage());			
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
	public <R> R retrieve(ProviderRequestDTO request) throws Exception {
		try
		{
			log.info("Getting Provider catalog...");			
			AdmiProvider provider = Format.objectMapping(request,AdmiProvider.class);				
			Example<AdmiProvider> filterprovider = Example.of(provider);
			List<AdmiProvider> optionalprovider  = providerDAO.findAll(filterprovider);						
			return (R) Format.listMapping(optionalprovider, ProviderResponseDTO.class);
		}
		catch(Exception e)
		{
			log.error("Error getting provider : {}",e.getLocalizedMessage());			
			throw new CustomException(e.getMessage());
		}
	}

}
