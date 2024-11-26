package net.project.ecommerce.msa.catalog.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name="admi_product_charact_value")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AdmiProductCharactValue implements Serializable{
	
	private static final long serialVersionUID = 1012895597206127799L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_charact_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_PRODUCT_CHARACT"))	
	private AdmiProductCharact productCharact;
	
	@OneToMany(mappedBy = "productCharactValue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<InfoProductFile> listProductFiles;	
	
	@Column(name = "value", length = 50, nullable = false)	
	private String value;
	
	@Column(name = "status", length = 10, nullable = false)	
	private String status;
    
    @Column(name = "created_at", nullable = false)       
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
