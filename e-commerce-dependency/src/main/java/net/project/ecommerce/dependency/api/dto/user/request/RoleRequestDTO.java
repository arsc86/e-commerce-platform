package net.project.ecommerce.dependency.api.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequestDTO {
	
	private Long id;
	@NotBlank(message = "The name can't be empty")
	private String name;
	private String status;

}
