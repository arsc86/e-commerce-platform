package net.project.ecommerce.dependency.api.dto.catalog;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductPriceDiscountRequestDTO;
import net.project.ecommerce.dependency.api.dto.catalog.request.ProductRequestDTO.ProductCharacteristicRequestDTO;
import net.project.ecommerce.dependency.api.enums.EnumProductValueType;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCharacteristicDTO {
	
	private Long productId;
	private List<ProductCharacteristicRequestDTO> characteristics;
	private List<ProductPriceDiscountRequestDTO> pricesDiscounts;
	private EnumProductValueType type; 

}
