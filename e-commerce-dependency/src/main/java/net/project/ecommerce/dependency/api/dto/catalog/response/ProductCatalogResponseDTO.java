package net.project.ecommerce.dependency.api.dto.catalog.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductRequestDTO.ProductCharacteristicRequestDTO;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductCatalogResponseDTO extends RepresentationModel<ProductCatalogResponseDTO>
{
	private Long id;
	private String categoryName;	
	private String provider;
	private String code;
	private String name;
	private String description;	
	private String status;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Date createdAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Date updatedAt;	
	private Long stock;
	@Builder.Default
	@JsonIgnore
	private List<ProductCharacteristicRequestDTO> characteristics  = new ArrayList<>();	
	
	//JPQL constructor
	public ProductCatalogResponseDTO(Long id, String categoryName, String provider,String code, String name, 
            String description, String status, Long stock,String createdBy,
            Date createdAt, Date updatedAt) {
		this.id = id;
		this.categoryName = categoryName;
		this.provider = provider;
		this.code = code;
		this.name = name;
		this.description = description;
		this.stock = stock!=null?stock:0;
		this.status = status;
		this.createdBy = createdBy;      
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
