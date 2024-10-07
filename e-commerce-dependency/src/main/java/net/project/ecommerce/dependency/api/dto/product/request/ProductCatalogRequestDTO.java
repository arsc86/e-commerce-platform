package net.project.ecommerce.dependency.api.dto.product.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.api.dto.product.DiscountPriceDTO;
import net.project.ecommerce.dependency.api.dto.product.InventoryDTO;
import net.project.ecommerce.dependency.api.dto.product.ProductPriceDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCatalogRequestDTO {
	
	private Long id;
	@NotBlank(message = "Category reference is required")
	@Pattern(regexp = "\\d+", message = "The category field must contain only numbers")
	private String categoryId;
	@NotBlank(message = "Product code is required")
	private String code;
	@NotBlank(message = "Product name is required")
	private String name;
	private String description;	
	@NotBlank(message = "Creation user is required")
	private String createdBy;
	private String status;
	@Valid
	private List<ProductPriceDTO> prices;
	private List<DiscountPriceDTO> discounts;	
	private List<InventoryDTO> inventory;
	private ProviderRequestDTO provider;

}
