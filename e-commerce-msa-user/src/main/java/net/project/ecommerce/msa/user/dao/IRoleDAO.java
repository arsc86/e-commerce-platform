package net.project.ecommerce.msa.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import net.project.ecommerce.msa.user.model.AdmiRol;

public interface IRoleDAO extends JpaRepository<AdmiRol,Long> {

}
