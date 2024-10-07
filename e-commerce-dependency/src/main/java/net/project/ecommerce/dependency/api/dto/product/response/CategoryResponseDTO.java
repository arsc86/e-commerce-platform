package net.project.ecommerce.dependency.api.dto.product.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDTO {
	
	private Long id;
	private CategoryResponseDTO category;
	private ProviderResponseDTO provider;
	private String name;
	private String status;
	private String createdBy;
	private Date creationAt;
	private Date updatedAt;

}
