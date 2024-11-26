package net.project.ecommerce.msa.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import net.project.ecommerce.dependency.api.constants.user.UserMethodConstants;
import net.project.ecommerce.dependency.api.dto.user.AddressDTO;
import net.project.ecommerce.dependency.api.dto.user.request.UserRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.response.UserAddressResponseDTO;
import net.project.ecommerce.dependency.api.enums.EnumStates;
import net.project.ecommerce.dependency.api.enums.EnumUserActivityType;
import net.project.ecommerce.dependency.exception.CustomException;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.msa.user.dao.IUserActivityDAO;
import net.project.ecommerce.msa.user.dao.IUserAddressDAO;
import net.project.ecommerce.msa.user.dao.IUserDAO;
import net.project.ecommerce.msa.user.model.InfoAddress;
import net.project.ecommerce.msa.user.model.InfoUser;
import net.project.ecommerce.msa.user.service.IUserCrudHandler;
import net.project.ecommerce.msa.user.util.UserUtils;

@Component(UserMethodConstants.USER_ADDRESS_SERVICE_BEAN)
public class UserAddressServiceImpl implements IUserCrudHandler{
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private IUserDAO userDAO;
	
	@Autowired
	private IUserAddressDAO userAddressDAO;
	
	@Autowired
	private IUserActivityDAO userActivityDAO;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R create(UserRequestDTO request) throws Exception {
		try 
		{
			log.info("Getting User Address by Id");
			InfoUser user = getUserById(request);
			
			//getCurrent Addresses
			List<InfoAddress> currentAddresses = user.getListAddress();
			
			//If do not exist previous addresses
			if(user.getListAddress().isEmpty())
			{
				//Validate consistency in default context data
				UserUtils.validateDefaultValue(request,"address");
			}
			
			List<InfoAddress> addresses = new ArrayList<>();
			
			for(AddressDTO address : request.getAddress())
			{
				//Validate if i already have an address, do not repeat the default one
				for(InfoAddress add: currentAddresses)
				{
					if(add.getIsDefault().equals(address.getIsDefault()) 
							&& add.getIsDefault().equalsIgnoreCase("Y") && 
							add.getStatus().equalsIgnoreCase(EnumStates.active.name()))
					{
						throw new CustomException("You only should have one default address");
					}
				}
				
				InfoAddress userAddress = Format.objectMapping(address, InfoAddress.class);
				userAddress.setUser(user);				
				addresses.add(userAddress);					
			}
		
			user.setListAddress(addresses);
			
			user = userDAO.save(user);
			
			//Set user activity
			userActivityDAO.save(UserUtils.getUserActivity(EnumUserActivityType.ADD_ADDRESS, user, "New Addresses have been added" ));		
			
			List<UserAddressResponseDTO> list = Format.listMapping(user.getListAddress(), UserAddressResponseDTO.class);
			
			return (R) list;
		}
		catch(Exception e)
		{
			log.error("Error add new user address : {} ,{}",e.getCause(), e.getLocalizedMessage());
			if(e.getCause() instanceof ConstraintViolationException)
			{
				throw new CustomException("Error trying to add address");
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
			log.info("Updating User Address by Id");
			InfoUser user = getUserById(request);
			
			//getCurrent Addresses
			List<InfoAddress> currentAddresses = user.getListAddress();		
			
			//Get address update information
			AddressDTO addressRequest = request.getAddress().get(0);
			
			//Getting the existing Address		
			InfoAddress infoAddress = new InfoAddress();
			infoAddress.setId(addressRequest.getId());
			infoAddress.setStatus(EnumStates.active.name());
			Example<InfoAddress> filterAddress    = Example.of(infoAddress);
			Optional<InfoAddress> optionalAddress = userAddressDAO.findOne(filterAddress);			
			if(!optionalAddress.isPresent())
			{
				throw new CustomException("The address to be updated does not exists or is inactive");
			}
			
			InfoAddress address = optionalAddress.get();
			
			//Validate if the address belong to the present user
			boolean addressExists = currentAddresses.stream()
	                .anyMatch(existingAddress -> existingAddress.equals(address));

	        if (!addressExists) {
	            throw new CustomException("The address doesn't exists for this user.");
	        }
			
			//Validate if the address to be updated doesn't have default repeated	        
	        if(request.getAddress().get(0).getIsDefault().equalsIgnoreCase("Y"))
	        {
	        	for(InfoAddress addr : currentAddresses)
				{
					if(addr.getIsDefault().equals("Y") && !addr.equals(address)
							&& addr.getStatus().equals(EnumStates.active.name()))
					{
						throw new CustomException("You only should have one default address");
					}
				}
	        }
			
	        address.setRegion(addressRequest.getRegion());
	        address.setAddress(addressRequest.getAddress());
	        address.setCountry(addressRequest.getCountry());
	        address.setCity(addressRequest.getCity());
	        address.setCoordinates(addressRequest.getCoordinates());
	        address.setIsDefault(addressRequest.getIsDefault());
	        address.setZipcode(addressRequest.getZipcode());
	        address.setUpdatedAt(new Date());

			userAddressDAO.save(address);						
			
			//Set user activity
			userActivityDAO.save(UserUtils.getUserActivity(EnumUserActivityType.UPDATE_ADDRESS, user, "The Address has been updated" ));		
			
			UserAddressResponseDTO response = Format.objectMapping(address, UserAddressResponseDTO.class);
			return (R) response;
		}
		catch(Exception e)
		{
			log.error("Error updating user address : {} ,{}",e.getCause(), e.getLocalizedMessage());
			if(e.getCause() instanceof ConstraintViolationException)
			{
				throw new CustomException("Error trying to add address");
			}
			throw new CustomException(e.getMessage());
		}
	}

	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public void delete(UserRequestDTO request) throws Exception {
		try 
		{
			log.info("Deleting User Address by Id");
			InfoUser user = getUserById(request);
			//getCurrent Addresses
			List<InfoAddress> currentAddresses = user.getListAddress();		
			
			//Get address update information
			AddressDTO addressRequest = request.getAddress().get(0);
			
			//Getting the existing Address						
			InfoAddress infoAddress = new InfoAddress();
			infoAddress.setId(addressRequest.getId());
			infoAddress.setStatus(EnumStates.active.name());
			Example<InfoAddress> filterAddress    = Example.of(infoAddress);
			Optional<InfoAddress> optionalAddress = userAddressDAO.findOne(filterAddress);			
			if(!optionalAddress.isPresent())
			{
				throw new CustomException("The User Address to delete does not exist or is inactive");
			}
			
			InfoAddress address = optionalAddress.get();
			
			//Validate if the address belong to the present user
			boolean addressExists = currentAddresses.stream()
	                .anyMatch(existingAddress -> existingAddress.equals(address));

	        if (!addressExists) {
	            throw new CustomException("The address doesn't exists for this user.");
	        }											
					
	        address.setStatus(EnumStates.inactive.name());
	        address.setUpdatedAt(new Date());

			userAddressDAO.save(address);						
			
			//Set user activity
			userActivityDAO.save(UserUtils.getUserActivity(EnumUserActivityType.DELETE_ADDRESS, user, "The Address has been deleted" ));					
		}
		catch(Exception e)
		{
			log.error("Error deleting user address : {} ,{}",e.getCause(), e.getLocalizedMessage());
			if(e.getCause() instanceof ConstraintViolationException)
			{
				throw new CustomException("Error trying to add address");
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
			log.info("Getting user addresses...");
			InfoUser user = getUserById(request);	
			
			AddressDTO address = request.getAddress().get(0);
			
			InfoAddress infoAddress = new InfoAddress();
			infoAddress.setUser(user);
			infoAddress.setStatus(address.getStatus());
			infoAddress.setIsDefault(address.getIsDefault());
			
			Example<InfoAddress> filterAddress    = Example.of(infoAddress);
			List<InfoAddress> optionalAddress = userAddressDAO.findAll(filterAddress);
			
			List<UserAddressResponseDTO> response = Format.listMapping(optionalAddress, UserAddressResponseDTO.class);
			return (R) response;
		}
		catch(Exception e)
		{
			log.error("Error getting user addresses: {}",e.getLocalizedMessage());			
			throw new CustomException(e.getMessage());
		}
	}

	private InfoUser getUserById(UserRequestDTO request) throws Exception {
		InfoUser user = new InfoUser();
		user.setId(request.getId());
		Example<InfoUser> filterUser    = Example.of(user);
		Optional<InfoUser> optionalUser = userDAO.findOne(filterUser);			
		if(!optionalUser.isPresent())
		{
			throw new CustomException("The user to get his address does not exist");
		}
		return optionalUser.get();
	}
}
