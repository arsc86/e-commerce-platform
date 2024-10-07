package net.project.ecommerce.msa.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.project.ecommerce.msa.user.model.InfoPaymentMethod;

@Repository
public interface IPaymentMethodDAO extends JpaRepository<InfoPaymentMethod,Long>{

}
