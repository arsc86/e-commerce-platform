package net.project.ecommerce.msa.user.util;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import net.project.ecommerce.dependency.api.enums.EnumUserActivityType;
import net.project.ecommerce.msa.user.model.InfoCredential;
import net.project.ecommerce.msa.user.model.InfoCredentialHist;
import net.project.ecommerce.msa.user.model.InfoUser;
import net.project.ecommerce.msa.user.model.InfoUserActivity;

@Service
public class UserUtils {
	
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
}
