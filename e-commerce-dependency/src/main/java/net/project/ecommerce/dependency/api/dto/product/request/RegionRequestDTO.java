package net.project.ecommerce.dependency.api.dto.product.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegionRequestDTO {
	
	private Long id;
	@NotBlank(message = "Category name is required")
	private String name;
	@NotBlank(message = "Category parent name is required")
	private String parentName;
	private String status;

}
