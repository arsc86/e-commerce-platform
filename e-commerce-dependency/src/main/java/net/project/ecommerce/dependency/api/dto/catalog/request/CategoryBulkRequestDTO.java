package net.project.ecommerce.dependency.api.dto.catalog.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryBulkRequestDTO {
		
	private List<CategoryRequestDTO> categories;	

}
