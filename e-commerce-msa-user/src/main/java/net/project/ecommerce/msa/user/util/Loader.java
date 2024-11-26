package net.project.ecommerce.msa.user.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import net.project.ecommerce.dependency.api.enums.EnumStates;
import net.project.ecommerce.dependency.api.enums.EnumUserActivityType;
import net.project.ecommerce.msa.user.dao.ICredentialHistoryDAO;
import net.project.ecommerce.msa.user.dao.IUserActivityDAO;
import net.project.ecommerce.msa.user.dao.IUserDAO;
import net.project.ecommerce.msa.user.model.AdmiRol;
import net.project.ecommerce.msa.user.model.InfoCredential;
import net.project.ecommerce.msa.user.model.InfoCredentialHist;
import net.project.ecommerce.msa.user.model.InfoProfile;
import net.project.ecommerce.msa.user.model.InfoUser;
import net.project.ecommerce.msa.user.model.InfoUserActivity;

@Component
public class Loader implements CommandLineRunner {
	
	@Autowired
	private IUserDAO userDAO;
	
	@Autowired
	private ICredentialHistoryDAO historyDAO;
	
	@Autowired
	private IUserActivityDAO userActivityDAO;

	@Override
	public void run(String... args) throws Exception 
	{		
		List<AdmiRol> listRoles = new ArrayList<>();
		//Roles
		AdmiRol role = new AdmiRol();
		role.setName("ADMIN");
		listRoles.add(role);
//		role = new AdmiRol();
//		role.setName("USER");
//		listRoles.add(role);	
		
		//Admin user
		InfoUser user = new InfoUser();
		user.setUsername("admin");
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setStatus(EnumStates.active.name());
		user.setRoles(listRoles);
		
		//Admin credential
		InfoCredential credential = new InfoCredential();
		credential.setPassword("$2a$12$FcXctiH0ozxCkDV/cVVpt.zSFkd3TGpcHSNNehve7lZI4eHrhrX8e");//admin				      
        credential.setExpiredAt(UserUtils.getExpirationTime(1));    
                 
        //Profile
		InfoProfile profile = new InfoProfile();
		profile.setEmail("arsc86@gmail.com");
		
		user.setProfile(profile);
		user.setCredential(credential);
		
		userDAO.save(user);
		
		//History password
        InfoCredentialHist credentialHist = new InfoCredentialHist();
        credentialHist.setCredential(credential);
        credentialHist.setLastPassword(credential.getPassword());
        historyDAO.save(credentialHist);
		
		//User Activity
		InfoUserActivity activity = new InfoUserActivity();
		activity.setUser(user);
		activity.setDescription("Initial user Admin creation");
		activity.setType(EnumUserActivityType.CREATION_USER.name());
		userActivityDAO.save(activity);
		
		
	}

}
