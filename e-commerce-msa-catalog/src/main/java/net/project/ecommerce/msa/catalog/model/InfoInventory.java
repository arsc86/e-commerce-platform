package net.project.ecommerce.msa.catalog.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.api.enums.EnumStates;

@Entity
@Data
@Table(name="info_inventory")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class InfoInventory implements Serializable {
	
	private static final long serialVersionUID = -4977106364517957108L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_PRODUCT"))
	//@JsonBackReference
	private AdmiProduct product;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_WAREHOUSE"))
	//@JsonBackReference
	private AdmiWarehouse warehouse;
	
	@NotNull
	@Column(name = "quantity", nullable = false)	
	private int quantity;
	
	@Column(name = "status", length = 15)	
	private String status;
    
    @Column(name = "created_at")       
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;   
    
    @Column(name = "updated_at")   
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt; 
    
    @PrePersist
	public void prePersist() {
    	this.createdAt = new Date();
		this.status = EnumStates.active.name();
	}

}
