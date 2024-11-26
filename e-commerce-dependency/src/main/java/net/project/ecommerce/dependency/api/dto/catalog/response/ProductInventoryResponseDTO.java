package net.project.ecommerce.dependency.api.dto.catalog.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInventoryResponseDTO {
	
	private Long warehouseId;
	private String warehouse;
	private int quantity;
	
	public ProductInventoryResponseDTO(String warehouseId, String warehouse, String quantity)
	{
		this.warehouseId = Long.valueOf(warehouseId);
		this.warehouse   = warehouse;
		this.quantity    = Integer.valueOf(quantity);
	}

}
