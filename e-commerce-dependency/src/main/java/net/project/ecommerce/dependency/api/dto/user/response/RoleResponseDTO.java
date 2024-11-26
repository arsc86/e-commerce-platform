package net.project.ecommerce.dependency.api.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDTO {
	
	private String id;
	private String name;
	private String createdAt;
	private String status;

}
