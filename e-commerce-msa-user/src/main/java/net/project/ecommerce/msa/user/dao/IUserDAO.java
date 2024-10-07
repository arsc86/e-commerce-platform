package net.project.ecommerce.msa.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.project.ecommerce.msa.user.model.InfoUser;

@Repository
public interface IUserDAO extends JpaRepository<InfoUser,Long>{	

}
