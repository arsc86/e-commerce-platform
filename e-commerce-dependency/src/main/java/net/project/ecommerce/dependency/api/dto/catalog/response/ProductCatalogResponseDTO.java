package net.project.ecommerce.dependency.api.dto.catalog.response;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.api.dto.catalog.InventoryDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCatalogResponseDTO {
	
	private Long id;
	private CategoryResponseDTO category;
	private ProviderResponseDTO provider;
	private String code;
	private String name;
	private String description;
	private Integer deliveryTime;
	private String status;
	private String createdBy;
	private Date createdAt;
	private List<ProductPriceDiscountResponseDTO> prices;
	private List<ProductPriceDiscountResponseDTO> discounts;
	private List<InventoryDTO> inventory;

}
