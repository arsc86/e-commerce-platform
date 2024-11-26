package net.project.ecommerce.msa.user.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import net.project.ecommerce.dependency.api.constants.user.RoleUserMethodConstants;
import net.project.ecommerce.dependency.api.dto.user.request.RoleRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.response.RoleResponseDTO;
import net.project.ecommerce.dependency.api.enums.EnumStates;
import net.project.ecommerce.dependency.exception.CustomException;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.msa.user.dao.IRoleDAO;
import net.project.ecommerce.msa.user.model.AdmiRol;
import net.project.ecommerce.msa.user.service.IRoleHandler;

@Component(RoleUserMethodConstants.ROLE_SERVICE_BEAN)
public class RoleServiceImpl implements IRoleHandler{
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private IRoleDAO roleDAO;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R create(RoleRequestDTO request) throws Exception {
		try
		{
			log.info("Creating Role...");
			
			Optional<AdmiRol> optional = roleDAO.findOne(Example.of(AdmiRol.builder()
																		   .name(request.getName())
																		   .status(EnumStates.active.name())
																		   .build()));
			
			if(optional.isPresent())
			{
				throw new CustomException("The Role already exists");
			}
			
			AdmiRol role = roleDAO.save(Format.objectMapping(request, AdmiRol.class));
			
			return (R) Format.objectMapping(role, RoleResponseDTO.class);
		}
		catch(Exception e)
		{
			log.error("Error creating role : {}",e.getLocalizedMessage());			
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
	public <R> R update(RoleRequestDTO request) throws Exception {
		try
		{
			log.info("Updating Role...");
			
			Optional<AdmiRol> optional = roleDAO.findOne(Example.of(AdmiRol.builder().id(Long.valueOf(request.getId())).build()));
			
			if(!optional.isPresent())
			{
				throw new CustomException("The Role already doesn't exists");
			}
			
			Optional<AdmiRol> optional1 = roleDAO.findOne(Example.of(AdmiRol.builder().name(request.getName()).build()));
			
			if(optional1.isPresent())
			{
				throw new CustomException("The Role name already exists");
			}
			
			AdmiRol role = optional.get();
			role.setName(request.getName());
			
			role = roleDAO.save(role);
			
			return (R) Format.objectMapping(role, RoleResponseDTO.class);
		}
		catch(Exception e)
		{
			log.error("Error updating role : {}",e.getLocalizedMessage());			
			if(e.getClass().getCanonicalName().equalsIgnoreCase(CustomException.class.getCanonicalName()))
			{
				throw new CustomException(e.getMessage());
			}					
			throw new CustomException("An error had ocurred in the transaction");
		}
	}

	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public void delete(RoleRequestDTO request) throws Exception {
		try
		{
			log.info("Deleting Role...");
			
			Optional<AdmiRol> optional = roleDAO.findOne(Example.of(AdmiRol.builder().id(Long.valueOf(request.getId())).build()));
			
			if(!optional.isPresent())
			{
				throw new CustomException("The Role already doesn't exists");
			}
			
			AdmiRol role = optional.get();
						
			role.setStatus(EnumStates.inactive.name());			
			role = roleDAO.save(role);			
		}
		catch(Exception e)
		{
			log.error("Error deleting role : {}",e.getLocalizedMessage());			
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
	public <R> R retrieve(RoleRequestDTO request) throws Exception {
		try
		{
			log.info("Getting Roles...");
			
			AdmiRol role = Format.objectMapping(request, AdmiRol.class);
			List<AdmiRol> roleList = roleDAO.findAll(Example.of(role));
			
			return (R) Format.listMapping(roleList, RoleResponseDTO.class);
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
