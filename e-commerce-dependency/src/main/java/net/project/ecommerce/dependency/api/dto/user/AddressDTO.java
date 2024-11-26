package net.project.ecommerce.dependency.api.dto.user;

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
public class AddressDTO {
	
	private Long id;
	@NotBlank(message = "Region is required")
	private String region;
	@NotBlank(message = "Country is required")
	private String country;
	@NotBlank(message = "City is required")
	private String city;
	@NotBlank(message = "Address is required")
	private String address;
	private String zipcode;
	private String coordinates;
	@NotBlank(message = "You should select at least one address as default")
	@Pattern(regexp = "^[YN]$", message = "isDefault must be 'Y' or 'N'")
	private String isDefault;
	private String status;

}
