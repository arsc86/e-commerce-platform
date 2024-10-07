package net.project.ecommerce.dependency.api.dto.product.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseResponseDTO {
	
	private Long id;	
	private String name;	
	private Integer deliveryTime;	
	private String location;
	private String coordinates;
	private String status;
	private Date createdAt;
	private String createdBy;

}
