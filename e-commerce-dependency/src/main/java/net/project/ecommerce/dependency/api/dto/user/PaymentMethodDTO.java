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
public class PaymentMethodDTO {
	
	private Long id;
	@NotBlank(message = "Payment type is required")
	private String type;
	@NotBlank(message = "Account Number is required")
	@Pattern(regexp = "\\d+", message = "The account number must be a numeric value")
	private String accountNumber;
    @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{4}$", message = "The expiration date has an invalid format")
	private String expirationDate;
	@NotBlank(message = "You must check if is a default payment method or not")
	private String isDefault;
	private String status;

}
