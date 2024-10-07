package net.project.ecommerce.dependency.interfaces;

public interface IGenericMessageProducer<P,R> {
	public R sendMessage(P data) throws Exception;
}
