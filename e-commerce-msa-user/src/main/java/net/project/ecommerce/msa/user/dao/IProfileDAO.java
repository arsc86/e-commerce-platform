package net.project.ecommerce.msa.user.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.project.ecommerce.msa.user.model.InfoProfile;

public interface IProfileDAO extends JpaRepository<InfoProfile,Long>{
	
	public Optional<InfoProfile> findByEmail(String email);

}
