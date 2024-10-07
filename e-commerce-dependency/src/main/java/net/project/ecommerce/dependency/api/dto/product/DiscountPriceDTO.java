package net.project.ecommerce.dependency.api.dto.product;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountPriceDTO {
		
	@NotBlank(message = "Discount is required")
	@Pattern(regexp = "^[0-9]+(\\.[0-9]{1,2})?$", message = "The discount field must contain only numbers")
	private Float discount;
	private Date startDate;
	private Date endDate;
	private boolean isActive;
	private String status;
	

}
