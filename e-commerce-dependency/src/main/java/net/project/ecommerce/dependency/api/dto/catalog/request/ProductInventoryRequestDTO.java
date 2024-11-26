package net.project.ecommerce.dependency.api.dto.catalog.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.api.enums.EnumInventoryActionType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInventoryRequestDTO {

	private String productId;
	@NotNull(message = "Warehouse is required")
	private String warehouseId;
	@NotNull(message = "Quantity is required")
	@Pattern(regexp = "\\d+", message = "The quantity must contain only numbers")
	private String quantity;
	@NotNull(message = "Inventory action is required")
	private EnumInventoryActionType action;

}
