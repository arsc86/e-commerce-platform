package net.project.ecommerce.dependency.api.dto.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
	@NotNull(message = "Provider is required to create de category")
	private String providerId;
	private String categoryId;
	private String status;

}
