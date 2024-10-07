package net.project.ecommerce.dependency.configuration;

import static java.util.Map.entry;

import java.util.Map;

import lombok.Getter;
import net.project.ecommerce.dependency.constants.GeneralConstants;
import net.project.ecommerce.dependency.enums.EnumChannelType;

/**
 * Define the channel map to be used using beanFactory in a message producer
 */
@Getter
public class ChannelConfigurationMap {

    public static final Map<EnumChannelType,String> DELIVERY_CHANNEL = Map.ofEntries(
            entry(EnumChannelType.GRPC,  GeneralConstants.GRPC_MESSAGE_PRODUCER),
            entry(EnumChannelType.KAFKA, GeneralConstants.KAKFA_MESSAGE_PRODUCER)
    );

    private ChannelConfigurationMap() {}
}
