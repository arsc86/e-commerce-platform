package net.project.ecommerce.dependency.api.dto.catalog.request;

import java.util.List;

import org.springframework.hateoas.EntityModel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.api.enums.EnumCharacteristicType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDTO {
	
	private Long id;
	@NotBlank(message = "The category reference is required")
	@Pattern(regexp = "\\d+", message = "The category field reference must contain only numbers")
	private Long categoryId;
	@NotBlank(message = "Product code is required")
	@Pattern(regexp = "^PROD-COD-PROV-\\d-CAT-\\d-[A-Z]$", message = "The producto code format is incorrect")
	private String code;
	@NotBlank(message = "Product name is required")
	private String name;
	private String description;		
	private String createdBy;
	private String status;		
	private Long warehouseId;
	private String providerName;
	private String categoryName;
	private int pageNumber;
	private int pageSize;
	private boolean pagination;
	private List<ProductPriceDiscountRequestDTO> prices;
	private List<ProductPriceDiscountRequestDTO> discounts;	
	private List<ProductCharacteristicRequestDTO> characteristics;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ProductCharacteristicRequestDTO{	
		private Long id;
		@NotBlank(message = "The characteristic reference is required")
		@Pattern(regexp = "\\d+", message = "The characteristic field reference must contain only numbers")
		private String characteristicId;
		private String characteristicName;
		@NotNull(message = "The characteristic type is required")
		private EnumCharacteristicType type;
		private String value;
		private List<ProductCharacterisitcValueRequestDTO> characteristicsValues;
		
		//JPQL constructor
		public ProductCharacteristicRequestDTO(Long characteristicId, String characteristicName, String type, String value, Long characteristicValueId) {
	        this.characteristicId = characteristicId.toString();
	        this.characteristicName = characteristicName;
	        this.type = EnumCharacteristicType.valueOf(type);
	        this.value = value;
	        this.id = characteristicValueId;
	    }
	}
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	@NoArgsConstructor
	@Builder
	public static class ProductCharacterisitcValueRequestDTO extends EntityModel<ProductCharacterisitcValueRequestDTO>{
		private Long id;
		@NotBlank(message = "The characteristic value is required")
		private String value;	
		private Long files;
		
		//JPQL constructor
		public ProductCharacterisitcValueRequestDTO(Long id, String value, Long files) {
			this.id = id;
			this.value = value;
			this.files = files ;
		}
	}
}
