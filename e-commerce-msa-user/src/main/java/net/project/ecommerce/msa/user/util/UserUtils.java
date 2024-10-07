package net.project.ecommerce.msa.user.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import net.project.ecommerce.dependency.api.dto.user.request.UserRequestDTO;
import net.project.ecommerce.dependency.api.enums.EnumUserActivityType;
import net.project.ecommerce.dependency.exception.CustomException;
import net.project.ecommerce.msa.user.model.InfoCredential;
import net.project.ecommerce.msa.user.model.InfoCredentialHist;
import net.project.ecommerce.msa.user.model.InfoUser;
import net.project.ecommerce.msa.user.model.InfoUserActivity;

@Service
public class UserUtils {
	
	static Logger log = LogManager.getLogger(UserUtils.class);		
	
	public static InfoCredentialHist setCredentialHistory(InfoCredential credential) {
		InfoCredentialHist hist = new InfoCredentialHist();
		hist.setCredential(credential);
		hist.setLastPassword(credential.getPassword());
		return hist;
	}
	
	public static InfoUserActivity getUserActivity(EnumUserActivityType type,InfoUser user,String description) {
		InfoUserActivity activity = new InfoUserActivity();
		activity.setDescription(description);
		activity.setType(type.name());
		activity.setUser(user);
		return activity;
	}
	
	public static Date getExpirationTime(Integer time) {
		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.MONTH, time); 
        return calendar.getTime();
	}
	
	public static void validateDefaultValue(UserRequestDTO request,String type) throws Exception{
		int cont=0;
		if(request.getAddress()!=null)
		{
			cont = (int) request.getAddress().stream()
					.filter(address -> "Y".equals(address.getIsDefault()))
					.count();
		}
		else
		{
			cont = (int) request.getPaymentMethods().stream()
					.filter(payment -> "Y".equals(payment.getIsDefault()))
					.count();
		}
		if(cont==0)throw new CustomException("You should select at least one "+type+" as default");
		if(cont>1)throw new CustomException("You only should have one default "+type+"");		
	}
}
