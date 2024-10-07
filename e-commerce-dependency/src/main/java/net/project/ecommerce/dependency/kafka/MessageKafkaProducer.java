package net.project.ecommerce.dependency.kafka;


import java.util.Arrays;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import net.project.ecommerce.dependency.constants.GeneralConstants;
import net.project.ecommerce.dependency.dto.request.GenericRequestDTO;
import net.project.ecommerce.dependency.dto.request.InternalRequestDTO;
import net.project.ecommerce.dependency.dto.response.GenericResponseDTO;
import net.project.ecommerce.dependency.interfaces.IGenericMessageProducer;
import net.project.ecommerce.dependency.kafka.configuration.KafkaProperties;
import net.project.ecommerce.dependency.util.Validator;

/**
 * Kafka Producer implementation
 *
 * @author Allan Suarez <mailto:arsc86@gmail.com>>
 */
@SuppressWarnings({"rawtypes" })
@Service(GeneralConstants.KAKFA_MESSAGE_PRODUCER)
public class MessageKafkaProducer implements IGenericMessageProducer<InternalRequestDTO, GenericResponseDTO> 
{
    Logger log = LogManager.getLogger(this.getClass());
        
    @Autowired
    KafkaProperties kafkaProperties;

    @Autowired(required = false)
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public GenericResponseDTO sendMessage(InternalRequestDTO data) throws Exception 
    {
        try 
        {
            Validator.validateFieldObjectFromTemplate(data, Arrays.asList("topicName", "option", " payload", "transactionId"));
                       
            ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(
            		data.getTopicName(), 
            		GenericRequestDTO.builder()                   
				                     .option(data.getOption())
				                     .transactionId(data.getTransactionId())
				                     .payload(data.getPayload())
				                     .build()
            		);
                                 
            log.info("Sending Kakfa message request to topic: {}, to Remote Option: {}", data.getTopicName(),data.getOption());
            
            if (data.getTransactionId() != null) 
            {
                producerRecord.headers().add("idTransaccion", data.getTransactionId().getBytes());
            }
            
            kafkaTemplate.send(producerRecord);
            
            log.info("Message sent to broker with Id: {}",data.getTransactionId());
            
            return GenericResponseDTO.<String>builder()
                    				 .message(new StringBuilder("Message sent to Broker").toString())
                                     .build();
        } 
        catch (Exception e) 
        {
        	log.error("Error ocurred while execute kafka request ".concat(data.toString()));
            throw new Exception("Error ocurred while execute kafka request ");
        }
    }
}
