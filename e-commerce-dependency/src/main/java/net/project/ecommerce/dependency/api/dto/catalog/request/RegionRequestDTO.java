package net.project.ecommerce.dependency.api.dto.catalog.request;

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
	@NotBlank(message = "Region name is required")
	private String name;
	@NotBlank(message = "Region parent name is required")
	private String parent;
	private String status;

}
