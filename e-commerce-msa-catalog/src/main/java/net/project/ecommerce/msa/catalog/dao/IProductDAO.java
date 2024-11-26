package net.project.ecommerce.msa.catalog.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductRequestDTO.ProductCharacterisitcValueRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductRequestDTO.ProductCharacteristicRequestDTO;
import net.project.ecommerce.msa.catalog.model.AdmiProduct;

@Repository
public interface IProductDAO extends JpaRepository<AdmiProduct,Long>{	
	
	@Modifying
    @Transactional
    @Query(value = "CREATE INDEX IF NOT EXISTS idx_search_vector ON admi_product USING gin(search_vector);", nativeQuery = true)
    void createSearchVectorIndex();
	
	//Class-based Projection
	@Query("SELECT new net.project.ecommerce.dependency.api.dto.catalog.request.ProductRequestDTO$ProductCharacteristicRequestDTO( " +
		       "ac.id, " +
		       "ac.name, " +
		       "apc.type, " +
		       "apc.value, " +
		       "apc.id " +
		       " ) "+
		       "FROM AdmiProduct ap " +		
		       "JOIN AdmiProductCharact apc ON ap.id = apc.product.id " + 
		       "JOIN apc.characteristic ac " +	
		       "WHERE ap.id = :id " +		           
		       "AND apc.status = 'active' " +
		       "AND ap.status = 'active'")
	List<ProductCharacteristicRequestDTO> findProductCharacteristicBy(@Param("id") Long id);
	
	//Class-based Projection
	@Query("SELECT new net.project.ecommerce.dependency.api.dto.catalog.request.ProductRequestDTO$ProductCharacterisitcValueRequestDTO("
			+ "apcv.id,apcv.value,"
			+ "(SELECT COUNT(ipf) FROM InfoProductFile ipf WHERE ipf.productCharactValue.id = apcv.id AND ipf.status = 'active')"		
			+ ") FROM AdmiProductCharact apc " +
		       "JOIN AdmiProductCharactValue apcv ON apc.id = apcv.productCharact.id " +
		       "WHERE apc.id = :id " +
		       "AND apc.status = 'active'")
	List<ProductCharacterisitcValueRequestDTO> findProductCharacteristicValueBy(@Param("id") Long id);
}
