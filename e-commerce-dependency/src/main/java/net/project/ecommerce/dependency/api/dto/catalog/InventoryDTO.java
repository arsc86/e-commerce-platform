package net.project.ecommerce.dependency.api.dto.catalog;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.api.dto.catalog.request.WarehouseRequestDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryDTO {
	
	private Long id;
	@NotBlank(message = "Product Quantity is required")
	@Pattern(regexp = "\\d+", message = "The quantity field must contain only numbers")
	private Integer quantity;
	@Valid
	private WarehouseRequestDTO warehouseRequestDTO;

}
