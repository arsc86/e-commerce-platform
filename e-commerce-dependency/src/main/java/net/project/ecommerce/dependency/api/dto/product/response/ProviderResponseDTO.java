package net.project.ecommerce.dependency.api.dto.product.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderResponseDTO {
	
	private String id;
	private String code;
	private String name;
	private String status;
	private String createdAt;
	private String createdBy;

}
