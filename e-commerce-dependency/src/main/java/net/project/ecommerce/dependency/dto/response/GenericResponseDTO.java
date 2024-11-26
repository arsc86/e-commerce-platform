package net.project.ecommerce.dependency.dto.response;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.constants.GeneralConstants;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponseDTO<T> implements Serializable {

	private static final long serialVersionUID = 8212235211102628827L;

	@Builder.Default
	private Integer code = GeneralConstants.CODE_OK;
	@Builder.Default
	private String status = GeneralConstants.SUCCESS_STATUS_DEFAULT;
	@Builder.Default
	private  String message = GeneralConstants.SUCCESS_MESSAGE_DEFAULT;
	private T payload;

}
