package net.project.ecommerce.dependency.api.dto.catalog.request;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.api.enums.EnumPriceDiscountType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPriceDiscountRequestDTO {
	
	private Long id;
	@NotBlank(message = "Price is required")
	@Pattern(regexp = "^[0-9]+(\\.[0-9]{1,2})?$", message = "The price field must contain only numbers")
	private double value;
	private Date startDate;
	private Date endDate;
	@NotBlank(message = "Price type is required")
	private EnumPriceDiscountType type;
	private String status;
	private RegionRequestDTO region;

}
