package net.project.ecommerce.msa.catalog.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.project.ecommerce.msa.catalog.model.AdmiRegion;

@Repository
public interface IRegionDAO extends JpaRepository<AdmiRegion,Long>{

}
