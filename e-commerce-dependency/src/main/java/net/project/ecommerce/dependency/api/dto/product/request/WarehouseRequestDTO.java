package net.project.ecommerce.dependency.api.dto.product.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseRequestDTO {
	
	private Long id;
	@NotBlank(message = "Warehouse name is required")
	private String name;
	@NotBlank(message = "Warehouse Product delivery time is required")
	private Integer deliveryTime;
	@NotBlank(message = "Warehouse location is required")
	private String location;
	private String coordinates;
	private String status;
	@NotBlank(message = "Warehouse Provider is required")
	private String provider;

}
