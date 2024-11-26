package net.project.ecommerce.msa.user.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import net.project.ecommerce.dependency.api.dto.user.request.UserRequestDTO;
import net.project.ecommerce.dependency.api.dto.user.response.UserResponseDTO;
import net.project.ecommerce.dependency.exception.CustomException;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.msa.user.model.AdmiRol;
import net.project.ecommerce.msa.user.model.InfoUser;

@Component
public class UserDAOImpl {
	
	@PersistenceContext
    private EntityManager entityManager;
	
	//Criteria Builder
	public Optional<InfoUser> authentication(String username, List<String> statuses){
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<InfoUser> query = cb.createQuery(InfoUser.class);
		
		Root<InfoUser> root = query.from(InfoUser.class);
		
		Predicate namePredicate   = cb.equal(root.get("username"), username);
		Predicate statusPredicate = root.get("status").in(statuses);
		
		query.where(cb.and(namePredicate, statusPredicate));
		
		TypedQuery<InfoUser> typedQuery = entityManager.createQuery(query);
		
		List<InfoUser> results = typedQuery.getResultList();
		
		if (results.isEmpty()) {
		    return Optional.empty(); 
		} else {
		    return Optional.of(results.get(0));
		}
	}
	
	//JPQL class projection based
	@SuppressWarnings({ "unchecked"})
	public <R> R findUsersBy(UserRequestDTO request) throws CustomException
	{
		try {	        
	        
	        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	        CriteriaQuery<InfoUser> query = cb.createQuery(InfoUser.class);
	        Root<InfoUser> userRoot = query.from(InfoUser.class);

	        List<Predicate> predicates = new ArrayList<>();
	       
	        if (request.getId() != null) {
	            predicates.add(cb.equal(userRoot.get("id"), request.getId()));
	        }
	        if (request.getUsername() != null) {
	            predicates.add(cb.equal(userRoot.get("username"), request.getUsername()));
	        }
	        if (request.getFirstName() != null) {
	            predicates.add(cb.equal(userRoot.get("firstName"), request.getFirstName()));
	        }
	        if (request.getLastName() != null) {
	            predicates.add(cb.equal(userRoot.get("lastName"), request.getLastName()));
	        }
	        if (request.getStatus() != null) {
	            predicates.add(cb.equal(userRoot.get("status"), request.getStatus()));
	        }
	        
	        if (request.getRole() != null && !request.getRole().name().isEmpty()) 
	        {
	            Join<InfoUser, AdmiRol> joinRoles = userRoot.join("roles");
	            predicates.add(joinRoles.get("name").in(request.getRole().name()));
	            query.distinct(true);
	        }
	       
	        query.where(cb.and(predicates.toArray(new Predicate[0])));
	     
	        List<InfoUser> users = entityManager.createQuery(query).getResultList();
	       
	        List<UserResponseDTO> response = Format.listMapping(users, UserResponseDTO.class);

	        return (R) response;

	    } 
		catch (Exception e) 
		{	       
	        throw new CustomException(e.getMessage());
	    }	    
	}

}
