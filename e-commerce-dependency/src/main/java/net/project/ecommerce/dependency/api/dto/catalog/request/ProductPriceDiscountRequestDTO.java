package net.project.ecommerce.dependency.api.dto.catalog.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
	
	private Long productId;
	@NotBlank(message = "Price is required")
	@Pattern(regexp = "^[0-9]+(\\.[0-9]{1,2})?$", message = "The price field must contain only numbers")
	private String value;
	@NotNull(message = "Start Date is required")
	@Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|1\\d|2\\d|3[01])$", message = "The date must be in the format YYYY-MM-DD and be a valid calendar date")
	private String startDate;
	@NotNull(message = "End Date is required")
	@Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|1\\d|2\\d|3[01])$", message = "The date must be in the format YYYY-MM-DD and be a valid calendar date")
	private String endDate;
	@NotNull(message = "Type is required")
	private EnumPriceDiscountType category;	
	private String status;
	@NotNull(message = "Location is required")
	@NotBlank(message = "Location is required")
	private String locationId;
	private String valueId;

}