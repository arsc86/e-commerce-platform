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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.api.enums.EnumStates;

@Entity
@Data
@Table(name="info_product_file")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class InfoProductFile implements Serializable{
	
	private static final long serialVersionUID = 1432751045225027139L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_PRODUCT"))
	//@JsonBackReference
	private AdmiProduct product;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_charact_value_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_PRODUCT_CHARACT_VALUE"))
	//@JsonBackReference
	private AdmiProductCharactValue productCharactValue;
	
	@Column(name = "type", length = 10)	
	private String type;
	
	@Column(name = "url", length = 100)	
	private String url;
	
	@Column(name = "status", length = 10)	
	private String status;
	
	@Column(name = "created_by", length = 20)	
	private String createdBy;
    
    @Column(name = "created_at")       
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;   
    
    @Column(name = "updated_at", nullable = true)   
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt; 
    
    @PrePersist
   	public void prePersist() {
       	this.createdAt = new Date();
   		this.status = EnumStates.active.name();
   	}

}
