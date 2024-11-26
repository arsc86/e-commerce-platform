package net.project.ecommerce.msa.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import net.project.ecommerce.msa.user.model.InfoCredentialHist;

public interface ICredentialHistoryDAO extends JpaRepository<InfoCredentialHist,Long>{

}
