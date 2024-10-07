package net.project.ecommerce.dependency.interfaces;

import net.project.ecommerce.dependency.vo.GenericProducerVO;

public interface IGenericProducer {
    public <P, R> R process(GenericProducerVO<P> request) throws Exception;
}
