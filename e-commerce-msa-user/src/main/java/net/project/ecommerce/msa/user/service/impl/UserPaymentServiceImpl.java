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
import net.project.ecommerce.dependency.api.dto.user.PaymentMethodDTO;
import net.project.ecommerce.dependency.api.dto.user.request.UserRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.response.UserPaymentMethodsResponseDTO;
import net.project.ecommerce.dependency.api.enums.EnumStates;
import net.project.ecommerce.dependency.api.enums.EnumUserActivityType;
import net.project.ecommerce.dependency.exception.CustomException;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.msa.user.dao.IPaymentMethodDAO;
import net.project.ecommerce.msa.user.dao.IUserActivityDAO;
import net.project.ecommerce.msa.user.dao.IUserDAO;
import net.project.ecommerce.msa.user.model.InfoPaymentMethod;
import net.project.ecommerce.msa.user.model.InfoUser;
import net.project.ecommerce.msa.user.service.IUserCrudHandler;
import net.project.ecommerce.msa.user.util.UserUtils;

@Component(UserMethodConstants.USER_PAYMENT_SERVICE_BEAN)
public class UserPaymentServiceImpl implements IUserCrudHandler{
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private IUserDAO userDAO;
	
	@Autowired
	private IPaymentMethodDAO paymentMethodDAO;
	
	@Autowired
	private IUserActivityDAO userActivityDAO;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public <R> R create(UserRequestDTO request) throws Exception {
		try 
		{			
			InfoUser user = getUserById(request);
			
			//getCurrent Addresses
			List<InfoPaymentMethod> currentPayments = user.getListPaymentMethod();
			
			//If do not exist previous payment method
			if(user.getListPaymentMethod().isEmpty())
			{
				//Validate consistency in default context data
				UserUtils.validateDefaultValue(request,"payment method");
			}
			
			List<InfoPaymentMethod> paymentMethods = new ArrayList<>();
			
			String currentAccountNumber = "";
			
			for(PaymentMethodDTO payment : request.getPaymentMethods())
			{
				if(currentAccountNumber.equals(payment.getAccountNumber())) 
				{
					throw new CustomException("The payment Methods account number to be saved must be different");
				}
				
				currentAccountNumber = payment.getAccountNumber();
			}
			
			for(PaymentMethodDTO payment : request.getPaymentMethods())
			{
				//Validate if i already have a payment method, do not repeat the default one
				for(InfoPaymentMethod payMethod: currentPayments)
				{
					if(payMethod.getIsDefault().equals(payment.getIsDefault()))
					{
						throw new CustomException("You only should have one default payment method");
					}
					
					//validating if the account number doesn't exists
					if(payMethod.getAccountNumber().equals(payment.getAccountNumber()))
					{
						throw new CustomException("The account numbers should be different");
					}
				}
				
				InfoPaymentMethod userPayment = Format.objectMapping(payment, InfoPaymentMethod.class);
				userPayment.setUser(user);				
				paymentMethods.add(userPayment);					
			}
		
			user.setListPaymentMethod(paymentMethods);
			
			user = userDAO.save(user);
			
			//Set user activity
			userActivityDAO.save(UserUtils.getUserActivity(EnumUserActivityType.ADD_PAYMENT_METHOD, user, "New Payment Methods have been added" ));		
			
			List<UserPaymentMethodsResponseDTO> list = Format.listMapping(user.getListPaymentMethod(), UserPaymentMethodsResponseDTO.class);
			
			return (R) list;
		}
		catch(Exception e)
		{
			log.error("Error add new payment method : {} ,{}",e.getCause(), e.getLocalizedMessage());
			if(e.getCause() instanceof ConstraintViolationException)
			{
				throw new CustomException("Error trying to add payment method");
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
			log.info("Updating User Payment method by Id");
			InfoUser user = getUserById(request);
			
			//getCurrent Addresses
			List<InfoPaymentMethod> currentPayment = user.getListPaymentMethod();		
			
			//Get address update information from request
			PaymentMethodDTO paymentMethodRequest = request.getPaymentMethods().get(0);
			
			//Getting the existing Payment Method		
			InfoPaymentMethod infoPaymentMethod = new InfoPaymentMethod();
			infoPaymentMethod.setId(paymentMethodRequest.getId());
			infoPaymentMethod.setStatus(EnumStates.active.name());
			Example<InfoPaymentMethod> filterPayment    = Example.of(infoPaymentMethod);
			Optional<InfoPaymentMethod> optionalPayment = paymentMethodDAO.findOne(filterPayment);			
			if(!optionalPayment.isPresent())
			{
				throw new CustomException("The Payment Method to be updated does not exists or is inactive");
			}
			
			InfoPaymentMethod paymentMethod = optionalPayment.get();
			
			//Validate if the payment method belong to the present user
			boolean paymentExists = currentPayment.stream()
	                .anyMatch(existingAddress -> existingAddress.equals(paymentMethod));

	        if (!paymentExists) {
	            throw new CustomException("The Payment Method doesn't exists for this user.");
	        }
			
			//Validate if the payment method to be updated doesn't have default repeated	        
	        for(PaymentMethodDTO payment : request.getPaymentMethods())
			{
				//Validate if i already have a payment method, do not repeat the default one
				for(InfoPaymentMethod payMethod: currentPayment)
				{
					if(payMethod.getIsDefault().equals(payment.getIsDefault()) &&
					   !payMethod.getAccountNumber().equals(payment.getAccountNumber()))
					{
						throw new CustomException("You only should have one default payment method");
					}					
				}							
			}
	        
	        paymentMethod.setAccountNumber(paymentMethodRequest.getAccountNumber());
	        paymentMethod.setExpirationDate(paymentMethodRequest.getExpirationDate());
	        paymentMethod.setIsDefault(paymentMethodRequest.getIsDefault());
	        paymentMethod.setType(paymentMethodRequest.getType());
	        paymentMethod.setUpdatedAt(new Date());
	        
			paymentMethodDAO.save(paymentMethod);						
			
			//Set user activity
			userActivityDAO.save(UserUtils.getUserActivity(EnumUserActivityType.UPDATE_PAYMENT_METHOD, user, "The Payment Method has been updated" ));		
			
			UserPaymentMethodsResponseDTO response = Format.objectMapping(paymentMethod, UserPaymentMethodsResponseDTO.class);
			return (R) response;
		}
		catch(Exception e)
		{
			log.error("Error updating user payment method : {} ,{}",e.getCause(), e.getLocalizedMessage());
			if(e.getCause() instanceof ConstraintViolationException)
			{
				throw new CustomException("Error trying to update payment method");
			}
			throw new CustomException(e.getMessage());
		}
	}

	@Override
	@Transactional(rollbackOn  = { CustomException.class })
	public void delete(UserRequestDTO request) throws Exception {
		try 
		{
			log.info("Deleting User Payment Method by Id");
			InfoUser user = getUserById(request);
			//getCurrent Addresses
			List<InfoPaymentMethod> currentPayment = user.getListPaymentMethod();		
			
			//Get address update information
			PaymentMethodDTO paymentMethodRequest = request.getPaymentMethods().get(0);
			
			//Getting the existing Address						
			InfoPaymentMethod infoPayment = new InfoPaymentMethod();
			infoPayment.setId(paymentMethodRequest.getId());
			infoPayment.setStatus(EnumStates.active.name());
			Example<InfoPaymentMethod> filterPayment    = Example.of(infoPayment);
			Optional<InfoPaymentMethod> optionalPayment = paymentMethodDAO.findOne(filterPayment);			
			if(!optionalPayment.isPresent())
			{
				throw new CustomException("The User Payment Method to be deleted does not exist or is inactive");
			}
			
			InfoPaymentMethod paymentMethod = optionalPayment.get();
			
			//Validate if the payment method belong to the present user
			boolean paymentExists = currentPayment.stream()
	                .anyMatch(existingAddress -> existingAddress.equals(paymentMethod));

	        if (!paymentExists) {
	            throw new CustomException("The Payment Method doesn't exists for this user.");
	        }											
					
	        paymentMethod.setStatus(EnumStates.inactive.name());
	        paymentMethod.setUpdatedAt(new Date());

			paymentMethodDAO.save(paymentMethod);						
			
			//Set user activity
			userActivityDAO.save(UserUtils.getUserActivity(EnumUserActivityType.DELETE_PAYMENT_METHOD, user, "The Payment Method has been deleted" ));					
		}
		catch(Exception e)
		{
			log.error("Error deleting user payment Method : {} ,{}",e.getCause(), e.getLocalizedMessage());
			if(e.getCause() instanceof ConstraintViolationException)
			{
				throw new CustomException("Error trying to delete payment Method");
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
			log.info("Getting user payment methods by id...");
			InfoUser user = getUserById(request);	
			
			PaymentMethodDTO paymentDTO = request.getPaymentMethods().get(0);
			
			InfoPaymentMethod infoPayment = new InfoPaymentMethod();
			infoPayment.setUser(user);
			infoPayment.setStatus(paymentDTO.getStatus());
			infoPayment.setIsDefault(paymentDTO.getIsDefault());
			infoPayment.setType(paymentDTO.getType());
			
			Example<InfoPaymentMethod> filterPayment    = Example.of(infoPayment);
			List<InfoPaymentMethod> optionalPayment = paymentMethodDAO.findAll(filterPayment);
			
			List<UserPaymentMethodsResponseDTO> response = Format.listMapping(optionalPayment, UserPaymentMethodsResponseDTO.class);
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
