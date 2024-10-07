package net.project.ecommerce.msa.api.bff.communication.grpc;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.project.ecommerce.dependency.constants.GeneralConstants;
import net.project.ecommerce.dependency.dto.request.InternalRequestDTO;
import net.project.ecommerce.dependency.dto.response.GenericResponseDTO;
import net.project.ecommerce.dependency.interfaces.IGenericMessageProducer;
import net.project.ecommerce.dependency.interfaces.IGenericProducer;
import net.project.ecommerce.dependency.util.Utils;
import net.project.ecommerce.dependency.vo.GenericProducerVO;
import net.project.ecommerce.msa.api.bff.config.AppConfig;

@Component(GeneralConstants.MESSAGE_PRODUCER)
public class MessageProducer implements IGenericProducer {

    @SuppressWarnings("rawtypes")
	@Autowired
    private IGenericMessageProducer<InternalRequestDTO, GenericResponseDTO> producer;
    @Autowired
    private BeanFactory factory;
    @Autowired
    private AppConfig config;


    @SuppressWarnings("unchecked")
	@Override
    public <P, R> R process(GenericProducerVO<P> request) throws Exception {
        producer = factory.getBean(request.getChannelType().name(), IGenericMessageProducer.class);
        if (producer == null)  
        {
            throw new Exception( "Not found producer component for the request type");
        }
        return (R) producer.sendMessage(InternalRequestDTO.<P>builder()
                .transactionId(request.getTransactionId())
                .payload(request.getPayload())       
                .context(request.getContext())
                .channelType(request.getChannelType())
                .topicName(config.getKafkaTopicMap().get(request.getService()))
                .blockingStub(Utils.getBlockingStub(
                        config.getGrpcServerMap().get(request.getService()),
                        config.getGrpcIdleTimeout(),
                        config.getGrpcDeadlineTimeout(),
                        config.getInboundMessageSize(),
                        config.getOutboundMessageSize()
                ))
                .option(request.getOption())
                .build());
    }

}
