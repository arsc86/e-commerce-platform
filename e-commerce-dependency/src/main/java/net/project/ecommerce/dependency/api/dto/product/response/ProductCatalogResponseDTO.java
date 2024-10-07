package net.project.ecommerce.dependency.api.dto.product.response;

import java.util.Date;
import java.util.List;

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
public class ProductCatalogResponseDTO {
	
	private Long id;
	private String category;
	private String provider;
	private String code;
	private String name;
	private String description;
	private Integer deliveryTime;
	private String status;
	private String createdBy;
	private Date createdAt;
	private List<ProductPriceDTO> prices;
	private List<DiscountPriceDTO> discounts;
	private List<InventoryDTO> inventory;

}
