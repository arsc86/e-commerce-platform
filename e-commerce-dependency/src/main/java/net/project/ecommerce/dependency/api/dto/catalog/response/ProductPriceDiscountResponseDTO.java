package net.project.ecommerce.dependency.api.dto.catalog.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.api.enums.EnumPriceDiscountType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPriceDiscountResponseDTO{
	
	private Long id;	
	private Long locationId;
	private String location;
	private double value;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
	private Date startDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
	private Date endDate;	
	private EnumPriceDiscountType type;
	private String status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Date createdAt;

}
