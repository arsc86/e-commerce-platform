package net.project.ecommerce.dependency.api.dto.catalog.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacteristicRequestDTO {
	
	private Long id;
	@NotBlank(message = "Characteristic name is required")
	private String name;
	private String status;

}
