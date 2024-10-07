package net.project.ecommerce.dependency.api.dto.catalog.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.api.enums.EnumPriceDiscountType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPriceDiscountResponseDTO {
	
	private Long id;	
	private double value;
	private Date startDate;
	private Date endDate;	
	private EnumPriceDiscountType type;
	private String status;
	private Date createdAt;
	private RegionResponseDTO region;

}
