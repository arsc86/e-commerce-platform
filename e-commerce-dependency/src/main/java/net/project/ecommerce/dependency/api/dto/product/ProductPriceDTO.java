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
public class ProductPriceDTO {
	
	@NotBlank(message = "Price type is required")
	private String type;
	@NotBlank(message = "Price is required")
	@Pattern(regexp = "^[0-9]+(\\.[0-9]{1,2})?$", message = "The price field must contain only numbers")
	private Float price;
	private Date startDate;
	private Date endDate;
	private boolean isActive;
	private String status;

}
