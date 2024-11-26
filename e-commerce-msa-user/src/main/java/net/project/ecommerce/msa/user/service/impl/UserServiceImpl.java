package net.project.ecommerce.msa.user.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import net.project.ecommerce.dependency.api.constants.user.UserMethodConstants;
import net.project.ecommerce.dependency.api.dto.user.request.UserRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.response.UserResponseDTO;
import net.project.ecommerce.dependency.api.enums.EnumStates;
import net.project.ecommerce.dependency.api.enums.EnumUserActivityType;
import net.project.ecommerce.dependency.exception.CustomException;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.msa.user.dao.ICredentialHistoryDAO;
import net.project.ecommerce.msa.user.dao.IProfileDAO;
import net.project.ecommerce.msa.user.dao.IRoleDAO;
import net.project.ecommerce.msa.user.dao.IUserActivityDAO;
import net.project.ecommerce.msa.user.dao.IUserDAO;
import net.project.ecommerce.msa.user.model.AdmiRol;
import net.project.ecommerce.msa.user.model.InfoCredential;
import net.project.ecommerce.msa.user.model.InfoProfile;
import net.project.ecommerce.msa.user.model.InfoUser;
import net.project.ecommerce.msa.user.service.IUserCrudHandler;
import net.project.ecommerce.msa.user.util.UserUtils;

@Service(UserMethodConstants.USER_SERVICE_BEAN)
@Primary
public class UserServiceImpl implements IUserCrudHandler{
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private IUserDAO userDAO;
	
	@Autowired
	private ICredentialHistoryDAO historyDAO;
	
	@Autowired
	private IRoleDAO roleDAO;
	
	@Autowired
	private IProfileDAO profileDAO;
	
	@Autowired
	private IUserActivityDAO userActivityDAO;
	
	@Autowired
    private EntitiesMapper entitiesMapper;
	
	@Value("${user.credential.expiration.time:}")
	private Integer passwordExpirationTime;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R create(UserRequestDTO request) throws Exception{	
		try
		{
			log.info("Creating user");
			Example<InfoUser> filterUser    = Example.of(Format.objectMapping(request, InfoUser.class));
			Optional<InfoUser> optionalUser = userDAO.findOne(filterUser);
			if(optionalUser.isPresent())
			{
				throw new CustomException("Can not save a new user, the given username already exists");
			}
			Optional<InfoProfile> optionalProfile = profileDAO.findByEmail(request.getProfile().getEmail());
			if(optionalProfile.isPresent())
			{
				throw new CustomException("Can not save a new user, the given email must be unique");
			}			
			InfoUser user          = Format.objectMapping(request, InfoUser.class);					
			//get role reference
			Example<AdmiRol> example = Example.of(AdmiRol.builder().name(request.getRole().name()).build());			
			user.setRoles(roleDAO.findAll(example));
			//Create profile
			InfoProfile profile = Format.objectMapping(request.getProfile(), InfoProfile.class);
			//Create Credential
			InfoCredential credential = new InfoCredential();
			credential.setExpiredAt(UserUtils.getExpirationTime(passwordExpirationTime));
			credential.setPassword(request.getPassword());
			user.setCredential(credential);				
			user.setProfile(profile);
			user = userDAO.save(user);
			
			//Set credential history
			historyDAO.save(UserUtils.setCredentialHistory(credential));
			//Set user activity
			userActivityDAO.save(UserUtils.getUserActivity(EnumUserActivityType.CREATION_USER, user, "Creation user" ));
			UserResponseDTO response = Format.objectMapping(user, UserResponseDTO.class);
			return (R) response;
		}
		catch(Exception e)
		{
			log.error("Error creating user : {} ,{}",e.getCause(), e.getLocalizedMessage());
			if(e.getCause() instanceof ConstraintViolationException)
			{
				throw new CustomException("Error trying creating User");
			}
			throw new CustomException(e.getMessage());
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R update(UserRequestDTO request) throws Exception {
		try 
		{
			log.info("Updating user");
			InfoUser user = new InfoUser();
			user.setId(request.getId());
			Example<InfoUser> filterUser    = Example.of(user);
			Optional<InfoUser> optionalUser = userDAO.findOne(filterUser);			
			if(!optionalUser.isPresent())
			{
				throw new CustomException("The user to be update does not exist");
			}
			user = optionalUser.get();
						
			//updating user
			user.setUsername(request.getUsername().isEmpty()?user.getUsername():request.getUsername());
			user.setFirstName(request.getFirstName().isEmpty()?user.getFirstName():request.getFirstName());
			user.setLastName(request.getLastName().isEmpty()?user.getLastName():request.getLastName());		
			
			InfoProfile profile = user.getProfile();
			user.setUpdatedAt(new Date());
			
			//Updating profile
			entitiesMapper.updateProfileFromDto(request.getProfile(), profile);

			profile.setUpdatedAt(new Date());				
			user.setProfile(profile);
			
			user = userDAO.save(user);
			
			//Set user activity
			userActivityDAO.save(UserUtils.getUserActivity(EnumUserActivityType.UPDATE_PROFILE, user, "User has been updated" ));		
			
			UserResponseDTO response = Format.objectMapping(user, UserResponseDTO.class);
			return (R) response;
		}
		catch(Exception e)
		{
			log.error("Error updating user : {} ,{}",e.getCause(), e.getLocalizedMessage());
			if(e.getCause() instanceof ConstraintViolationException)
			{
				throw new CustomException("Error trying to update User");
			}
			throw new CustomException(e.getMessage());
		}
	}

	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public void delete(UserRequestDTO request) throws Exception {
		try 
		{
			log.info("Deleting user");
			InfoUser user = new InfoUser();
			user.setId(request.getId());
			Example<InfoUser> filterUser    = Example.of(user);
			Optional<InfoUser> optionalUser = userDAO.findOne(filterUser);
			if(!optionalUser.isPresent())
			{
				throw new CustomException("The user to be deleted does not exist");
			}
			user = optionalUser.get();
			InfoProfile profile = user.getProfile();
			
			//updating user
			user.setStatus(EnumStates.inactive.name());					
			user.setUpdatedAt(new Date());
			
			profile.setStatus(EnumStates.inactive.name());
			profile.setUpdatedAt(new Date());
			
			user.setProfile(profile);
			user = userDAO.save(user);
			
			//Set user activity
			userActivityDAO.save(UserUtils.getUserActivity(EnumUserActivityType.DELETE_USER, user, "User has been deleted" ));			
		}
		catch(Exception e)
		{
			log.error("Error updating user : {} ,{}",e.getCause(), e.getLocalizedMessage());
			if(e.getCause() instanceof ConstraintViolationException)
			{
				throw new CustomException("Error trying to update User");
			}
			throw new CustomException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R retrieve(UserRequestDTO request) throws Exception {
		try
		{
			log.info("Getting user...");
			Example<InfoUser> filterUser    = Example.of(Format.objectMapping(request, InfoUser.class));
			List<InfoUser> optionalUser = userDAO.findAll(filterUser);			
			List<UserResponseDTO> response = Format.listMapping(optionalUser, UserResponseDTO.class);
			return (R) response;
		}
		catch(Exception e)
		{
			log.error("Error getting user : {}",e.getLocalizedMessage());			
			throw new CustomException(e.getMessage());
		}
	}	
}
