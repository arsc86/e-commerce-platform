package net.project.ecommerce.msa.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import net.project.ecommerce.msa.user.model.InfoCredential;

public interface ICredentialDAO extends JpaRepository<InfoCredential,Long>{

}
