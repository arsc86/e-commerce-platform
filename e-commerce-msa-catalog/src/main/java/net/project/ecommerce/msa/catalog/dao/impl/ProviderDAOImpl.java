package net.project.ecommerce.msa.catalog.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import net.project.ecommerce.msa.catalog.model.AdmiProvider;

@Component
public class ProviderDAOImpl{
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Transactional
    public Optional<AdmiProvider> findProviderByNameOrProviderCode(String name, String providerCode) {
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AdmiProvider> query = cb.createQuery(AdmiProvider.class);
       
        Root<AdmiProvider> root = query.from(AdmiProvider.class);
      
        Predicate namePredicate = cb.equal(root.get("name"), name);
        Predicate codePredicate = cb.equal(root.get("providerCode"), providerCode);
    
        query.where(cb.or(namePredicate, codePredicate));
       
        TypedQuery<AdmiProvider> typedQuery = entityManager.createQuery(query);
       
        List<AdmiProvider> results = typedQuery.getResultList();
       
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

}
