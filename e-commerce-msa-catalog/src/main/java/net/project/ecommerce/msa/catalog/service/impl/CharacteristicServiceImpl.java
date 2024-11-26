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
import net.project.ecommerce.dependency.api.constants.catalog.CatalogCharacteristicMethodConstants;
import net.project.ecommerce.dependency.api.dto.catalog.request.CharacteristicRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.response.CharacteristicResponseDTO;
import net.project.ecommerce.dependency.api.enums.EnumStates;
import net.project.ecommerce.dependency.exception.CustomException;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.msa.catalog.dao.ICharacteristicDAO;
import net.project.ecommerce.msa.catalog.model.AdmiCharacteristic;
import net.project.ecommerce.msa.catalog.service.ICharcteristicCatalogHandler;

@Service(CatalogCharacteristicMethodConstants.CHARACTERISTIC_SERVICE_BEAN)
public class CharacteristicServiceImpl implements ICharcteristicCatalogHandler{
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private ICharacteristicDAO characteristicDAO;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R create(CharacteristicRequestDTO request) throws Exception {
		try
		{
			log.info("Creating new Characteristic...");
			AdmiCharacteristic characteristic = AdmiCharacteristic.builder()
					                                              .name(request.getName())
					                                              .status(EnumStates.active.name())
					                                              .build();
			Example<AdmiCharacteristic> filter = Example.of(characteristic);
			Optional<AdmiCharacteristic> optional = characteristicDAO.findOne(filter);
			if(optional.isPresent())
			{
				throw new CustomException("The characteristic already exists");
			}
			
			characteristic = characteristicDAO.save(characteristic);
		
			return (R) Format.objectMapping(characteristic, CharacteristicResponseDTO.class);
		}
		catch(Exception e)
		{
			log.error("Error creating characteristic {} : {}",request.getId(),e.getLocalizedMessage());			
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
	public <R> R update(CharacteristicRequestDTO request) throws Exception {
		try
		{
			log.info("Updating Characteristic...");
			AdmiCharacteristic characteristic = AdmiCharacteristic.builder()
																  .id(Long.valueOf(request.getId()))					                                             
					                                              .status(EnumStates.active.name())
					                                              .build();
			Example<AdmiCharacteristic> filter = Example.of(characteristic);
			Optional<AdmiCharacteristic> optional = characteristicDAO.findOne(filter);
			if(!optional.isPresent())
			{
				throw new CustomException("The characteristic reference doesn't exists or is inactive");
			}
			
			characteristic = optional.get();
			
			if(!characteristic.getName().equalsIgnoreCase(request.getName()))
			{
				AdmiCharacteristic characteristicTmp = AdmiCharacteristic.builder()
						                           .name(request.getName())				                                             
						                           .status(EnumStates.active.name())
						                           .build();
				filter = Example.of(characteristicTmp);
				optional = characteristicDAO.findOne(filter);
				if(optional.isPresent())
				{
					throw new CustomException("The characteristic already exists, it can't be repeated");
				}
				characteristic.setName(request.getName());
				characteristic.setUpdatedAt(new Date());
				characteristic = characteristicDAO.save(characteristic);
			}
			
			return (R) Format.objectMapping(characteristic, CharacteristicResponseDTO.class);
		}
		catch(Exception e)
		{
			log.error("Error updating characteristic {} : {}",request.getId(),e.getLocalizedMessage());			
			if(e.getClass().getCanonicalName().equalsIgnoreCase(CustomException.class.getCanonicalName()))
			{
				throw new CustomException(e.getMessage());
			}					
			throw new CustomException("An error had ocurred in the transaction");
		}
	}

	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public void delete(CharacteristicRequestDTO request) throws Exception {
		try
		{
			log.info("Deleting Characteristic...");
			AdmiCharacteristic characteristic = AdmiCharacteristic.builder()
					                                              .id(Long.valueOf(request.getId()))
					                                              .status(EnumStates.active.name())
					                                              .build();
			Example<AdmiCharacteristic> filter = Example.of(characteristic);
			Optional<AdmiCharacteristic> optional = characteristicDAO.findOne(filter);
			if(!optional.isPresent())
			{
				throw new CustomException("The characteristic reference doesn't exists or is inactive");
			}
			characteristic = optional.get();
			characteristic.setUpdatedAt(new Date());
			characteristic.setStatus(EnumStates.inactive.name());
			characteristic = characteristicDAO.save(characteristic);		
		}
		catch(Exception e)
		{
			log.error("Error deleting characteristic {} : {}",request.getId(),e.getLocalizedMessage());			
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
	public <R> R retrieve(CharacteristicRequestDTO request) throws Exception {
		try
		{
			log.info("Getting Characteristic...");
			AdmiCharacteristic characteristic = Format.objectMapping(request, AdmiCharacteristic.class);
			Example<AdmiCharacteristic> filter = Example.of(characteristic);
			List<AdmiCharacteristic> optional = characteristicDAO.findAll(filter);			
			return (R) Format.listMapping(optional, CharacteristicResponseDTO.class);
		}
		catch(Exception e)
		{
			log.error("Error updating characteristic {} : {}",request.getId(),e.getLocalizedMessage());			
			if(e.getClass().getCanonicalName().equalsIgnoreCase(CustomException.class.getCanonicalName()))
			{
				throw new CustomException(e.getMessage());
			}					
			throw new CustomException("An error had ocurred in the transaction");
		}
	}

}
