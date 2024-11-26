package net.project.ecommerce.msa.user.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import net.project.ecommerce.dependency.api.constants.user.UserMethodConstants;
import net.project.ecommerce.dependency.api.dto.user.CredentialDTO;
import net.project.ecommerce.dependency.api.dto.user.RoleDTO;
import net.project.ecommerce.dependency.api.dto.user.request.RefreshTokenRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.request.UserRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.request.ValidateEmailRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.response.AuthenticationResponseDTO;
import net.project.ecommerce.dependency.api.dto.user.response.UserResponseDTO;
import net.project.ecommerce.dependency.api.enums.EnumStates;
import net.project.ecommerce.dependency.api.enums.EnumUserActivityType;
import net.project.ecommerce.dependency.exception.CustomException;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.msa.user.dao.ICredentialHistoryDAO;
import net.project.ecommerce.msa.user.dao.IUserActivityDAO;
import net.project.ecommerce.msa.user.dao.IUserDAO;
import net.project.ecommerce.msa.user.dao.impl.UserDAOImpl;
import net.project.ecommerce.msa.user.model.InfoCredential;
import net.project.ecommerce.msa.user.model.InfoCredentialHist;
import net.project.ecommerce.msa.user.model.InfoProfile;
import net.project.ecommerce.msa.user.model.InfoUser;
import net.project.ecommerce.msa.user.service.IAuthenticationHandler;
import net.project.ecommerce.msa.user.util.UserUtils;

@Service(UserMethodConstants.AUTH_SERVICE_BEAN)
public class AuthenticationServiceImpl implements IAuthenticationHandler{
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private IUserDAO userDAO;
	
	@Autowired
	private UserDAOImpl userDAOImpl;
	
	@Autowired
	private ICredentialHistoryDAO historyDAO;
	
	@Autowired
	private IUserActivityDAO userActivityDAO;
	
	@Value("${user.credential.expiration.time:}")
	private Integer passwordExpirationTime;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R authenticate(UserRequestDTO request) throws Exception {
		try
		{			
			List<String> statuses = Arrays.asList(EnumStates.active.name(),EnumStates.pending.name());
			Optional<InfoUser> optionalUser = userDAOImpl.authentication(request.getUsername(), statuses);
			if(!optionalUser.isPresent())
			{
				throw new CustomException("Username doesn't exists");
			}						
			InfoUser user = optionalUser.get();	
						
			AuthenticationResponseDTO response = AuthenticationResponseDTO.builder()
					.username(user.getUsername())
					.password(user.getCredential().getPassword())
					.role(Format.listMapping(user.getRoles(), RoleDTO.class))
					.isPasswordExpired(new Date().after(user.getCredential().getExpiredAt()))
					.build();
			return (R) response;
		}
		catch(Exception e)
		{
			log.error("Error getting user : {}",e.getLocalizedMessage());			
			throw new CustomException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R getLastPassword(UserRequestDTO request) throws Exception {
		try
		{
			InfoUser user = getUser(request);		
			List<InfoCredentialHist> credentialList = user.getCredential().getListCredentialHist();
			
			return (R) credentialList.stream().map(credential -> CredentialDTO.builder()
					.password(credential.getLastPassword()).build())
			.collect(Collectors.toList());			
		}
		catch(Exception e)
		{
			log.error("Error getting user : {}",e.getLocalizedMessage());			
			throw new CustomException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R updatePassword(UserRequestDTO request) throws Exception {
		try
		{								
			InfoUser user = getUser(request);	
			InfoCredential credential = user.getCredential();			
			credential.setExpiredAt(UserUtils.getExpirationTime(passwordExpirationTime));
			credential.setPassword(request.getPassword());
			user.setCredential(credential);
			userDAO.save(user);
			
			List<InfoCredentialHist> credentialHist = historyDAO.findAll();
			
			//Inactive the all last passwords
			for(InfoCredentialHist hist : credentialHist)
			{
				hist.setStatus(EnumStates.inactive.name());				
				historyDAO.save(hist);
			}
			
			//Set credential history
			historyDAO.save(UserUtils.setCredentialHistory(credential));
			
			//Set user activity
			userActivityDAO.save(UserUtils.getUserActivity(EnumUserActivityType.CHANGE_PASSWORD, user, "User Password has been changed"));
			
			return (R) Format.objectMapping(user, UserResponseDTO.class);
		}
		catch(Exception e)
		{
			log.error("Error getting user : {}",e.getLocalizedMessage());			
			throw new CustomException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R updateRefreshToken(UserRequestDTO request) throws Exception {
		try
		{
			String token = request.getRefreshToken();
			request.setStatus(EnumStates.active.name());
			request.setRefreshToken(null);
			List<String> statuses = Arrays.asList(EnumStates.active.name(),EnumStates.pending.name());
			Optional<InfoUser> optionalUser = userDAOImpl.authentication(request.getUsername(), statuses);
			if(!optionalUser.isPresent())
			{
				throw new CustomException("Username doesn't exists");
			}						
			InfoUser user = optionalUser.get();	
			//InfoUser user = getUser(request);		
			user.setRefreshToken(token);
			user = userDAO.save(user);
			return (R) Format.objectMapping(user, UserResponseDTO.class);
		}
		catch(Exception e)
		{
			log.error("Error getting user : {}",e.getLocalizedMessage());			
			throw new CustomException(e.getMessage());
		}
	}
	
	private InfoUser getUser(UserRequestDTO request) throws Exception
	{
		Example<InfoUser> filterUser    = Example.of(Format.objectMapping(request, InfoUser.class));
		Optional<InfoUser> optionalUser = userDAO.findOne(filterUser);
		if(!optionalUser.isPresent())
		{
			throw new CustomException("Username doesn't exists");
		}
		return optionalUser.get();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R getRefreshToken(UserRequestDTO request) throws Exception {
		try
		{			
			List<String> statuses = Arrays.asList(EnumStates.active.name());
			Optional<InfoUser> optionalUser = userDAOImpl.authentication(request.getUsername(), statuses);
			if(!optionalUser.isPresent())
			{
				throw new CustomException("Username doesn't exists");
			}						
			InfoUser user = optionalUser.get();					
			return (R) Format.objectMapping(user, RefreshTokenRequestDTO.class);
		}
		catch(Exception e)
		{
			log.error("Error getting user : {}",e.getLocalizedMessage());			
			throw new CustomException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R validateMailUser(ValidateEmailRequestDTO request) throws Exception {
		try
		{			
			List<String> statuses = Arrays.asList(EnumStates.pending.name());
			Optional<InfoUser> optionalUser = userDAOImpl.authentication(request.getUsername(), statuses);
			if(!optionalUser.isPresent())
			{
				throw new CustomException("Username doesn't exists or account has been validated");
			}						
			InfoUser user = optionalUser.get();	
			InfoProfile profile = user.getProfile();
			if(profile==null)
			{
				throw new CustomException("Username doesn't have a complete profile information or doesn't exists");
			}
			
			if(!request.getEmail().equalsIgnoreCase(profile.getEmail()))
			{
				throw new CustomException("User email is invalid or doesn't exists");
			}
			return (R) request;
		}
		catch(Exception e)
		{
			log.error("Error validating user mail : {} ,{}",e.getCause(), e.getLocalizedMessage());
			if(e.getCause() instanceof ConstraintViolationException)
			{
				throw new CustomException("Error validating user mail");
			}
			throw new CustomException(e.getMessage());
		}
	}

	


}
