package net.project.ecommerce.dependency.vo;

import lombok.Builder;
import lombok.Data;
import net.project.ecommerce.dependency.enums.EnumChannelType;

@Builder
@Data
public class GenericProducerVO<T> {
   
    private String transactionId;
    private String service;
    private String option;
    private String context;
    private EnumChannelType channelType;
    private T payload;   
}
