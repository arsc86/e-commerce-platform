package net.project.ecommerce.msa.catalog.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.project.ecommerce.dependency.api.dto.catalog.response.ProductInventoryResponseDTO;
import net.project.ecommerce.msa.catalog.model.InfoInventory;

@Repository
public interface IProductInventoryDAO extends JpaRepository<InfoInventory,Long>{
	
	@Modifying
	@Query(value = """
	    INSERT INTO info_inventory (product_id, warehouse_id, quantity, status, created_at)
			VALUES (:productId, :warehouseId, :quantity, 'active', NOW())
			ON CONFLICT (product_id, warehouse_id)
			DO UPDATE 
			SET quantity = info_inventory.quantity - EXCLUDED.quantity,
			    updated_at = now()
			WHERE info_inventory.quantity >= EXCLUDED.quantity;
	""", nativeQuery = true)
	int reserveStock(@Param("productId") Long productId, 
	                 @Param("warehouseId") Long warehouseId, 
	                 @Param("quantity") int quantity);
	
	@Modifying
	@Query("UPDATE InfoInventory p SET p.quantity = p.quantity + :quantity "
			+ "WHERE p.product.id = :productId AND p.warehouse.id = :warehouseId")
	int releaseStock(@Param("productId") Long productId, @Param("warehouseId") Long warehouseId,@Param("quantity") int quantity);
	
	
	@Query("select new net.project.ecommerce.dependency.api.dto.catalog.response.ProductInventoryResponseDTO( \n"
			+ "aw.id,   "
			+ "aw.name, "
			+ "ai.quantity ) "
			+ "FROM InfoInventory ai JOIN AdmiProduct ap ON ai.product.id = ap.id "
			+ "JOIN AdmiWarehouse aw ON ai.warehouse.id = aw.id "  
			+ "WHERE "
			+ "ai.status = 'active' "
			+ "AND ap.id = :productId "
			+ "AND :warehouseId IS NULL or aw.id = :warehouseId ")
	List<ProductInventoryResponseDTO> findProductInventoryBy(@Param("productId") String productId,@Param("warehouseId") String warehouseId);

	
	

}
