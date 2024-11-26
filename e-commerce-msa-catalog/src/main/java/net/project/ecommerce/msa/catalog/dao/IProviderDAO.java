package net.project.ecommerce.msa.catalog.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.project.ecommerce.msa.catalog.model.AdmiProvider;

@Repository
public interface IProviderDAO extends JpaRepository<AdmiProvider,Long>{	
	
	public Optional<AdmiProvider> findProviderByNameOrProviderCode(String name, String providerCode);

}
