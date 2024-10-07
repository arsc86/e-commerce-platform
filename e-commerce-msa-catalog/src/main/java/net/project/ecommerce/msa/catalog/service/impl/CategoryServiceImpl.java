package net.project.ecommerce.msa.catalog.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import net.project.ecommerce.dependency.api.constants.catalog.CatalogCategoryMethodConstants;
import net.project.ecommerce.dependency.api.dto.catalog.request.CategoryRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.response.CategoryResponseDTO;
import net.project.ecommerce.dependency.api.dto.catalog.response.ProviderResponseDTO;
import net.project.ecommerce.dependency.api.enums.EnumStates;
import net.project.ecommerce.dependency.exception.CustomException;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.msa.catalog.dao.ICategoryDAO;
import net.project.ecommerce.msa.catalog.dao.IProviderDAO;
import net.project.ecommerce.msa.catalog.model.AdmiCategory;
import net.project.ecommerce.msa.catalog.model.AdmiProvider;
import net.project.ecommerce.msa.catalog.service.ICategoryCatalogHandler;

@Service(CatalogCategoryMethodConstants.CATEGORY_SERVICE_BEAN)
@Primary
public class CategoryServiceImpl implements ICategoryCatalogHandler{
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private ICategoryDAO categoryDAO;
	
	@Autowired
	private IProviderDAO providerDAO;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R create(CategoryRequestDTO request) throws Exception {
		try
		{
			log.info("Creating new Category...");	
			Example<AdmiCategory> filterCategory;
			AdmiCategory category = Format.objectMapping(request, AdmiCategory.class);	
			
			if(request.getCategoryId()!=null)
			{
				//Validating if parent reference exists
				filterCategory    = Example.of(AdmiCategory.builder()
															.id(request.getCategoryId())
															.status(EnumStates.active.name())
															.build()
											  );
				Optional<AdmiCategory> optional = categoryDAO.findOne(filterCategory);
				if(!optional.isPresent())
				{
					throw new CustomException("Category parent doesn't exists or is inactive");
				}
				category.setCategory(optional.get());
			}
			if(request.getProviderCode()!=null && !request.getProviderCode().isEmpty())
			{
				//Validating is provider code reference exists
				Example<AdmiProvider> filterProvider = Example.of(AdmiProvider.builder()
																			  .providerCode(request.getProviderCode())
																			  .status(EnumStates.active.name())
																			  .build()
															      );
				Optional<AdmiProvider> optional = providerDAO.findOne(filterProvider);
				if(!optional.isPresent())
				{
					throw new CustomException("Provider reference doesn't exists or is inactive");
				}
				category.setProvider(optional.get());
			}
			//Validate if the new category doesn't exists
			filterCategory = Example.of(category);
			category.setStatus(EnumStates.active.name());
			Optional<AdmiCategory> optional = categoryDAO.findOne(filterCategory);
			if(optional.isPresent())
			{
				throw new CustomException("Category does exists and must be different");
			}
						
			category = categoryDAO.save(category);
			CategoryResponseDTO resp = Format.objectMapping(category, CategoryResponseDTO.class);
			AdmiCategory parent = category.getCategory();
			if(parent!=null)
			{
				resp.setParentId(parent.getId());
				resp.setParentName(parent.getName());
			}				
			resp.setProviderCode(request.getProviderCode());
			return (R) resp;
		}
		catch(Exception e)
		{
			log.error("Error getting category : {}",e.getLocalizedMessage());			
			throw new CustomException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R update(CategoryRequestDTO request) throws Exception {
		try
		{
			log.info("Updating Categories...");		
			
			if(request.getProviderCode()==null || request.getProviderCode().isEmpty())
			{
				throw new CustomException("The Provider code should be exists");
			}
			
			if(request.getName()==null || request.getName().isEmpty())
			{
				throw new CustomException("The category name should be exists");
			}
			
			AdmiCategory category = AdmiCategory.builder()
					.id(request.getId())
					.status(EnumStates.active.name())
					.build();
			Example<AdmiCategory> filterCategory = Example.of(category);			
			Optional<AdmiCategory> optional = categoryDAO.findOne(filterCategory);
			
			if(!optional.isPresent())
			{
				throw new CustomException("The Category to be updated doesn't exists or is inactive");
			}
			
			category = optional.get();
			
			if(request.getCategoryId()!=null)
			{
				//Validating new parent if needed
				AdmiCategory parent = AdmiCategory.builder()
												  .id(request.getCategoryId())						
												  .build();
							
				optional = categoryDAO.findOne(Example.of(parent));
				
				if(!optional.isPresent())
				{
					throw new CustomException("The Parent Category doesn't exists or is inactive");
				}
				
				category.setCategory(parent);
			}
			else
			{
				category.setCategory(null);
			}
			
			AdmiProvider provider = AdmiProvider.builder()
					.providerCode(request.getProviderCode())
					.status(EnumStates.active.name())
					.build();
			Example<AdmiProvider> filterProvider = Example.of(provider);	
			Optional<AdmiProvider> optionalProvider = providerDAO.findOne(filterProvider);
			
			if(!optionalProvider.isPresent())
			{
				throw new CustomException("The Provider reference doesn't exists or is inactive");
			}
			
			category.setProvider(optionalProvider.get());
			category.setName(request.getName());
			category.setUpdatedAt(new Date());							
			category = categoryDAO.save(category);
			
			CategoryResponseDTO resp = Format.objectMapping(category, CategoryResponseDTO.class);
			AdmiCategory parent = category.getCategory();
			if(parent!=null)
			{
				resp.setParentId(parent.getId());
				resp.setParentName(parent.getName());
			}				
			resp.setProviderCode(request.getProviderCode());
			return (R) resp;			
		}
		catch(Exception e)
		{
			log.error("Error updating category {} : {}",request.getId(),e.getLocalizedMessage());			
			if(e.getClass().getCanonicalName().equalsIgnoreCase(CustomException.class.getCanonicalName()))
			{
				throw new CustomException(e.getMessage());
			}					
			throw new CustomException("An error had ocurred in the transaction");
		}
	}

	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public void delete(CategoryRequestDTO request) throws Exception {
		try
		{
			log.info("Deleting Categories...");				
			AdmiCategory category = AdmiCategory.builder()
					.id(request.getId())
					.status(EnumStates.active.name())
					.build();
			Example<AdmiCategory> filterCategory = Example.of(category);			
			Optional<AdmiCategory> optional = categoryDAO.findOne(filterCategory);
			
			if(!optional.isPresent())
			{
				throw new CustomException("Category doesn't exists or is inactive");
			}
			
			category = optional.get();
			
			List<AdmiCategory> categoryList = category.getListCategories();
			
			for(AdmiCategory cat : categoryList)
			{
				cat.setStatus(EnumStates.inactive.name());
				cat.setUpdatedAt(new Date());
				categoryDAO.save(cat);
			}
				  
			category.setStatus(EnumStates.inactive.name());
			category.setUpdatedAt(new Date());
			categoryDAO.save(category);							
		}
		catch(Exception e)
		{
			log.error("Error deleting category to parent {} : {}",request.getId(),e.getLocalizedMessage());	
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
	public <R> R retrieve(CategoryRequestDTO request) throws Exception {
		try
		{
			log.info("Getting Category catalog...");			
			AdmiCategory category = Format.objectMapping(request, AdmiCategory.class);	
			if(request.getCategoryId()!=null)
			{
				category.setCategory(AdmiCategory.builder().id(request.getCategoryId()).build());
			}
			if(request.getProviderCode()!=null && !request.getProviderCode().isEmpty())
			{
				category.setProvider(AdmiProvider.builder().providerCode(request.getProviderCode()).build());
			}
			Example<AdmiCategory> filterCategory    = Example.of(category);
			List<AdmiCategory> optionalCategory = categoryDAO.findAll(filterCategory);	
			List<CategoryResponseDTO> response = new ArrayList<>();
			for(AdmiCategory cat : optionalCategory)
			{
				CategoryResponseDTO resp = Format.objectMapping(cat, CategoryResponseDTO.class);
				AdmiCategory parent = cat.getCategory();
				if(parent!=null)
				{
					resp.setParentId(parent.getId());
					resp.setParentName(parent.getName());
				}				
				resp.setProviderCode(Format.objectMapping(cat.getProvider(), ProviderResponseDTO.class).getProviderCode());
				response.add(resp);
			}			
			return (R) response;
		}
		catch(Exception e)
		{
			log.error("Error getting category : {}",e.getLocalizedMessage());			
			throw new CustomException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R createBy(CategoryRequestDTO request) throws Exception {
		try
		{
			log.info("Creating and adding Categories...");				
			AdmiCategory category = AdmiCategory.builder()
					.id(request.getId())
					.status(EnumStates.active.name())
					.build();
			Example<AdmiCategory> filterCategory = Example.of(category);			
			Optional<AdmiCategory> optional = categoryDAO.findOne(filterCategory);
			
			if(!optional.isPresent())
			{
				throw new CustomException("The parent Category doesn't exists or is inactive");
			}
			
			category = optional.get();
			AdmiProvider provider = category.getProvider();
			
			List<CategoryRequestDTO> categories = request.getCategories();
			List<AdmiCategory> categoryList     = new ArrayList<>();
			
			for(CategoryRequestDTO cat : categories)
			{
				AdmiCategory categoryChild = Format.objectMapping(cat, AdmiCategory.class);
				categoryChild.setProvider(provider);				
				categoryChild.setCategory(category);
				
				//Validate if child doesn't exists
				filterCategory = Example.of(categoryChild);
				optional = categoryDAO.findOne(filterCategory);
				
				if(optional.isPresent())
				{
					throw new CustomException("One of the child Category does exists and must be different : "+cat.getName());
				}
				 
				categoryChild.setCreatedBy(request.getCreatedBy()); 
				categoryList.add(categoryChild);
			}
				  
			category.setListCategories(categoryList);			
			category = categoryDAO.save(category);
			
			//Creating the response
			List<CategoryResponseDTO> response = new ArrayList<>();
			for(AdmiCategory cat : category.getListCategories())
			{
				CategoryResponseDTO resp = Format.objectMapping(cat, CategoryResponseDTO.class);
				AdmiCategory parent = cat.getCategory();//getting parent category
				if(parent!=null)
				{
					resp.setParentId(parent.getId());
					resp.setParentName(parent.getName());
				}				
				resp.setProviderCode(Format.objectMapping(cat.getProvider(), ProviderResponseDTO.class).getProviderCode());
				response.add(resp);
			}			
			return (R) response;
		}
		catch(Exception e)
		{
			log.error("Error adding category to parent {} : {}",request.getId(),e.getLocalizedMessage());	
			if(e.getClass().getCanonicalName().equalsIgnoreCase(CustomException.class.getCanonicalName()))
			{
				throw new CustomException(e.getMessage());
			}
			throw new CustomException("An error had ocurred in the transaction");
		}
	}		
}
