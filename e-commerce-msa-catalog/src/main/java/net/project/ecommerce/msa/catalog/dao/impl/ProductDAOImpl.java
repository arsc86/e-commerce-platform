package net.project.ecommerce.msa.catalog.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.response.ProductCatalogResponseDTO;
import net.project.ecommerce.dependency.api.enums.EnumStates;
import net.project.ecommerce.dependency.util.Format;
import net.project.ecommerce.msa.catalog.model.AdmiProduct;

@Component
public class ProductDAOImpl {
	
	@PersistenceContext
    private EntityManager entityManager;
	
	//Criteria Builder
	public Optional<AdmiProduct> findProductByCodeAndName(String code, String name){
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<AdmiProduct> query = cb.createQuery(AdmiProduct.class);
		
		Root<AdmiProduct> root = query.from(AdmiProduct.class);
		
		Predicate namePredicate   = cb.equal(root.get("name"), name);
		Predicate statusPredicate = cb.equal(root.get("status"), EnumStates.active.name());
		Predicate likeCondition   = cb.like(root.get("code"), code + "%");
		
		query.where(cb.and(namePredicate, likeCondition, statusPredicate));
		
		TypedQuery<AdmiProduct> typedQuery = entityManager.createQuery(query);
		
		List<AdmiProduct> results = typedQuery.getResultList();
		
		if (results.isEmpty()) {
		    return Optional.empty(); 
		} else {
		    return Optional.of(results.get(0));
		}
	}
	
	//JPQL class projection based
	@SuppressWarnings({ "unchecked"})
	public <R> R findProductsBy(ProductRequestDTO filter)
	{
		Query query       = null;
		Long totalRecords = 0L;
		boolean hasNext   = false;
	    StringBuilder queryString = new StringBuilder("");

	    queryString.append(" SELECT ");
	    queryString.append(" ap.id, ac.name, apr.name, ap.code, ap.name, ap.description, ap.status, ");
	    queryString.append(" (SELECT SUM(ii.quantity) FROM info_inventory ii WHERE ii.product_id = ap.id AND ii.status = 'active'), ");	   
	    queryString.append(" ap.created_by, ap.created_at, ap.updated_at ");
	    queryString.append(" FROM admi_product ap ");
	    queryString.append(" JOIN admi_category ac ON ap.category_id = ac.id ");
	    queryString.append(" JOIN admi_provider apr ON ac.provider_id = apr.id ");
	    
	    if (filter.getWarehouseId() != null) {
	        queryString.append(" JOIN info_inventory ii ON ap.id = ii.product_id ");
	        queryString.append(" JOIN admi_warehouse aw ON ii.warehouse_id = aw.id ");
	    }
	    
	    queryString.append(" WHERE 1=1 ");
	    
	    if (filter.getId() != null) {
	        queryString.append(" AND ap.id = :id ");
	    }
	    if (filter.getName() != null) {
	        queryString.append(" AND ap.search_vector @@ to_tsquery(:query) ");
	    }
	    if (filter.getCode() != null) {
	        queryString.append(" AND ap.code = :code ");
	    }
	    if (filter.getStatus() != null) {
	        queryString.append(" AND ap.status = :status ");
	    }
	    if (filter.getWarehouseId() != null) {
	        queryString.append(" AND aw.id = :warehouseId ");
	    }
	    if (filter.getCategoryId() != null) {
	        queryString.append(" AND ac.id = :categoryId ");
	    }
	    if (filter.getProviderName() != null) {
	        queryString.append(" AND apr.name = :provider ");
	    }
	    
	    int pageNumber = filter.getPageNumber();
	    int pageSize   = filter.getPageSize();
	    int offset     = pageNumber * pageSize;
	    
	    if(filter.isPagination())
	    {
	    	//Count total rows query
		    StringBuilder countQueryString = new StringBuilder("SELECT COUNT(1) FROM (");
		    countQueryString.append(queryString);
		    countQueryString.append(") as total");
		    
		    query = this.getFilterQuery(countQueryString, filter);
		    
		    totalRecords = ((Number) query.getSingleResult()).longValue();		    		    
	    }
	    
	    query = this.getFilterQuery(queryString, filter);		    
	    query.setFirstResult(offset);
	    query.setMaxResults(filter.isPagination()?pageSize:pageSize + 1);
	    
	    List<Object[]> results = query.getResultList();
	    
	    List<ProductCatalogResponseDTO> productCatalog = new ArrayList<>();
	    
	    if(filter.isPagination())
	    {
	    	for (Object[] row : results) {	    			       
		        productCatalog.add(getCatalogDTO(row));
	    	}
	    }
	    else
	    {
	    	for (int i = 0; i < Math.min(results.size(), pageSize); i++) {	    	  
    	    	Object[] row = results.get(i);    	        
    	        productCatalog.add(getCatalogDTO(row));
    	    }
	    	
	    	hasNext = results.size() > pageSize;
	    }
	  
	    results = null;
	    
	    if(filter.isPagination())
	    {
	    	return (R) new PageImpl<>(productCatalog, PageRequest.of(filter.getPageNumber(), filter.getPageSize()), totalRecords);
	    }
	    else
	    {
	    	return (R) new SliceImpl<>(productCatalog, PageRequest.of(pageNumber, pageSize), hasNext);
	    }	    
	}
	
	private ProductCatalogResponseDTO getCatalogDTO(Object[] row)
	{
		return new ProductCatalogResponseDTO(
	            (Long) row[0],  // ap.id
	            (String) row[1], // ac.name
	            (String) row[2], // apr.name
	            (String) row[3], // ap.code
	            (String) row[4], // ap.name
	            (String) row[5], // ap.description
	            (String) row[6], // ap.status
	            (Long) row[7], // SUM(ii.quantity)	          
	            (String) row[8], // ap.created_by
	            (Date) row[9], // ap.created_at
	            (Date) row[10]  // ap.updated_at
	     );
	}
	
	private Query getFilterQuery(StringBuilder queryString,ProductRequestDTO filter) {
		
		Query query = entityManager.createNativeQuery(queryString.toString());
		 
		if (filter.getId() != null) {
	        query.setParameter("id", filter.getId());
	    }
	    if (filter.getName() != null) {
	        query.setParameter("query", Format.formatSearchQuery(filter.getName()));
	    }
	    if (filter.getCode() != null) {
	        query.setParameter("code", filter.getCode());
	    }
	    if (filter.getStatus() != null) {
	        query.setParameter("status", filter.getStatus());
	    }
	    if (filter.getWarehouseId() != null) {
	        query.setParameter("warehouseId", filter.getWarehouseId());
	    }
	    if (filter.getProviderName() != null) {
	        query.setParameter("provider", filter.getProviderName());
	    }
	    if (filter.getCategoryId() != null) {
	        query.setParameter("categoryId", filter.getCategoryId());
	    }
	    
	    return query;
		
	}
}
