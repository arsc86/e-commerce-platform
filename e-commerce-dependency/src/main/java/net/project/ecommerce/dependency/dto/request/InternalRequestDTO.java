package net.project.ecommerce.dependency.dto.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.project.ecommerce.dependency.grpc.configuration.GrpcEcommerceGenericServiceGrpc.GrpcEcommerceGenericServiceBlockingStub;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class InternalRequestDTO<P> extends GenericRequestDTO<P> implements Serializable {
    
	private static final long serialVersionUID = -2365264046292631052L;
	public String topicName;
    public GrpcEcommerceGenericServiceBlockingStub blockingStub;
}
