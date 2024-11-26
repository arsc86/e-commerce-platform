package net.project.ecommerce.dependency.api.dto.catalog.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.api.enums.EnumFileType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFileRequestDTO {
		
	private String valueId;
	private String fileId;
	@NotNull(message = "You should choose a specific file type")
	private EnumFileType type;
	private String fileName;
	private String base64File;
	private String status;
	private String createdBy;
}
