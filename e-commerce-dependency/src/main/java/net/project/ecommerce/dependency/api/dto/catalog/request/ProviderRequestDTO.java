package net.project.ecommerce.dependency.api.dto.catalog.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderRequestDTO {
	
	private Long id;
	@NotBlank(message = "Provider name is required")
	private String name;	
	@NotBlank(message = "Provider code is required")
	private String providerCode;	
	private String status;
}
