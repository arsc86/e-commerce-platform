package net.project.ecommerce.dependency.api.dto.product.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegionResponseDTO {
	
	private Long id;	
	private String name;	
	private String parentName;
	private String status;
	private Date createdAt;
	private Date updatedAt;

}
