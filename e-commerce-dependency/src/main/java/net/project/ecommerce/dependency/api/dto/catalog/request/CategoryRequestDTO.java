package net.project.ecommerce.dependency.api.dto.catalog.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequestDTO {
	
	private Long id;
	@NotBlank(message = "Category name is required")
	private String name;	
	@NotBlank(message = "Provide Code can't be empty")
	private String providerCode;	
	private Long categoryId;	
	private String status;
	private String createdBy;
	private List<CategoryRequestDTO> categories;

}
