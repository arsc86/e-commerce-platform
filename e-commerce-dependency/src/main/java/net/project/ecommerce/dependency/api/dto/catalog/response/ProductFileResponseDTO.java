package net.project.ecommerce.dependency.api.dto.catalog.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductFileResponseDTO {
	
	private Long id;
	private Long valueId;
	private String type;
	private String fileName;	
	private String status;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Date createdAt;
	
	@Builder
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class FileDTO{
		private Long id;
		private String name;
		private String file;
	}

}
