package net.project.ecommerce.dependency.api.dto.user.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.api.dto.user.PaymentMethodDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentRequestDTO {
	
	@Valid
    @NotEmpty(message = "The user payment method can't be empty")
	private List<PaymentMethodDTO> payments;

}
