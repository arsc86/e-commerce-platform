package net.project.ecommerce.dependency.api.dto.catalog.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
	@Pattern(regexp = "\\d+", message = "The delivery time field must contain only numbers")
	private String deliveryTime;
	@NotBlank(message = "Warehouse address is required")
	private String address;
	private String coordinates;
	private String status;	
	@NotBlank(message = "Location is required")
	@Pattern(regexp = "\\d+", message = "The location field must contain only numbers")
	private String location;
	private String productId;

}
