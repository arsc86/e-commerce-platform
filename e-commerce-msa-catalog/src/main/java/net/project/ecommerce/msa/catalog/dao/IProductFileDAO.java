package net.project.ecommerce.msa.catalog.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.project.ecommerce.dependency.api.dto.catalog.response.ProductFileResponseDTO.FileDTO;
import net.project.ecommerce.msa.catalog.model.InfoProductFile;

@Repository
public interface IProductFileDAO extends JpaRepository<InfoProductFile,Long>{
	
	@Query("SELECT new net.project.ecommerce.dependency.api.dto.catalog.response.ProductFileResponseDTO$FileDTO( " +
		       "ipf.id, " +
		       "ipf.fileName, " +
		       "ipf.file " +
		       " ) "+
		       "FROM AdmiProductCharactValue ap " +		
		       "JOIN InfoProductFile ipf ON ap.id = ipf.productCharactValue.id " + 		   
		       "WHERE ap.id = :id " +		 
			   "AND ( :fieldId is NULL or ipf.id = :fieldId ) " +
			   "AND ipf.status  = 'active' ")
	public List<FileDTO> getFileByCharactValue(@Param("id") Long id, @Param("fieldId") Long fieldId);

}
