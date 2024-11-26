package net.project.ecommerce.msa.catalog.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import net.project.ecommerce.dependency.api.constants.catalog.CatalogProductFileMethodConstants;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductFileRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.response.ProductFileResponseDTO;
import net.project.ecommerce.dependency.api.dto.catalog.response.ProductFileResponseDTO.FileDTO;
import net.project.ecommerce.dependency.api.enums.EnumStates;
import net.project.ecommerce.dependency.exception.CustomException;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.msa.catalog.dao.IProductCharacteristicValueDAO;
import net.project.ecommerce.msa.catalog.dao.IProductFileDAO;
import net.project.ecommerce.msa.catalog.model.AdmiProductCharactValue;
import net.project.ecommerce.msa.catalog.model.InfoProductFile;
import net.project.ecommerce.msa.catalog.service.IProductFileHandler;

@Component(CatalogProductFileMethodConstants.PRODUCT_FILE_SERVICE_BEAN)
public class ProductFileServiceImpl implements IProductFileHandler {
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private IProductFileDAO productFileDAO;
	
	@Autowired
	private IProductCharacteristicValueDAO productCharacteristicValueDAO;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R create(ProductFileRequestDTO request) throws Exception {
		try
		{
			log.info("Adding file to Product");
			Optional<AdmiProductCharactValue> optional = 
					productCharacteristicValueDAO.findOne(Example.of(AdmiProductCharactValue.builder()
																							.id(Long.valueOf(request.getValueId()))
																							.status(EnumStates.active.name())
																							.build())
																							);
			if(!optional.isPresent())
			{
				throw new CustomException("The characteristic reference doesn't exists or is inactive");
			}
			
			AdmiProductCharactValue value = optional.get();
			
			InfoProductFile productFile = Format.objectMapping(request, InfoProductFile.class);
			productFile.setProductCharactValue(value);
			productFile.setCreatedBy(request.getCreatedBy());
			productFile.setFile(request.getBase64File());
	 		productFile = productFileDAO.save(productFile);
			
			ProductFileResponseDTO response = Format.objectMapping(productFile, ProductFileResponseDTO.class);
			response.setValueId(value.getId());
									
			return (R) response;
		}
		catch(Exception e)
		{
			log.error("Error adding file : {}",e.getLocalizedMessage());			
			if(e.getClass().getCanonicalName().equalsIgnoreCase(CustomException.class.getCanonicalName()))
			{
				throw new CustomException(e.getMessage());
			}					
			throw new CustomException("An error had ocurred in the transaction");
		}
	}

	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public void delete(ProductFileRequestDTO request) throws Exception {
		try
		{
			log.info("Deleting file...");
			
			Optional<AdmiProductCharactValue> optionalValue = productCharacteristicValueDAO
					.findOne(Example.of(AdmiProductCharactValue.builder().id(Long.valueOf(request.getValueId()))
							.status(EnumStates.active.name())
							.build())
							);
			
			if(!optionalValue.isPresent())
			{
				throw new CustomException("The characteristic value doesn't exists or is inactive");
			}
			
			Optional<InfoProductFile> optional = 
					productFileDAO.findOne(Example.of(InfoProductFile.builder()
																	.id(Long.valueOf(request.getFileId()))
																	.productCharactValue(optionalValue.get())
																	.status(EnumStates.active.name())
																	.build())
																	);
			if(!optional.isPresent())
			{
				throw new CustomException("The file doesn't exists asociated to product characteristic or is inactive");
			}
									
			InfoProductFile file = optional.get();
			
			file.setStatus(EnumStates.inactive.name());
			file.setUpdatedAt(new Date());
			productFileDAO.save(file);
		}
		catch(Exception e)
		{
			log.error("Error getting file by characteristic value : {}",e.getLocalizedMessage());			
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
	public <R> R retrieve(ProductFileRequestDTO request) throws Exception {
		try
		{
			log.info("Getting file...");

			List<FileDTO> files = productFileDAO.getFileByCharactValue(Long.valueOf(request.getValueId()),
					Long.valueOf(request.getFileId()));
			
			return (R) files.get(0);
		}
		catch(Exception e)
		{
			log.error("Error getting file by characteristic value : {}",e.getLocalizedMessage());			
			if(e.getClass().getCanonicalName().equalsIgnoreCase(CustomException.class.getCanonicalName()))
			{
				throw new CustomException(e.getMessage());
			}					
			throw new CustomException("An error had ocurred in the transaction");
		}
	}

}
