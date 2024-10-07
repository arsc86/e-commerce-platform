package net.project.ecommerce.msa.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import net.project.ecommerce.msa.user.model.InfoUserActivity;

public interface IUserActivityDAO extends JpaRepository<InfoUserActivity,Long>{

}
