package net.project.ecommerce.dependency.api.dto.catalog.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.api.dto.catalog.InventoryDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCatalogRequestDTO {
	
	private Long id;
	@Valid
	private CategoryRequestDTO category;
	@NotBlank(message = "Product code is required")
	private String code;
	@NotBlank(message = "Product name is required")
	private String name;
	private String description;	
	@NotBlank(message = "Creation user is required")
	private String createdBy;
	private String status;
	@Valid
	private List<ProductPriceDiscountRequestDTO> prices;
	private List<ProductPriceDiscountRequestDTO> discounts;	
	private List<InventoryDTO> inventory;
	@Valid
	private ProviderRequestDTO provider;

}
