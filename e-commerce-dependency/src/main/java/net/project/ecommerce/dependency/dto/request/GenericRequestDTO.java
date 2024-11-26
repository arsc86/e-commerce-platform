package net.project.ecommerce.dependency.dto.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.project.ecommerce.dependency.enums.EnumChannelType;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class  GenericRequestDTO<P> implements Serializable {
    
	private static final long serialVersionUID = 2334315079886883433L;
	private String transactionId;
    private String option;
    private String context;
    private EnumChannelType channelType;
    private P payload;
}
