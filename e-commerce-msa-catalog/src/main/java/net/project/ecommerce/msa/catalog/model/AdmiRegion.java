package net.project.ecommerce.msa.catalog.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name="admi_region")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AdmiRegion implements Serializable{

	private static final long serialVersionUID = -5109889004598866663L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long id;
	
	@NotNull
	@Column(name = "parent_region", length = 30, nullable = false)
	private String parent;
	
	@NotNull
	@Column(name = "name", length = 30, nullable = false)
	private String name;
	
	@NotNull
    @Column(name = "status", length = 10)
    private String status;
    
	@NotNull
    @Column(name = "created_at")   
	@Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Column(name = "updated_at")	
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)	
	private List<InfoProductPrice> listProductFiles;
    
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)	
	private List<InfoProductDiscount> listInfoProductDiscounts;
    
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)	
	private List<AdmiWarehouse> listAdmiWarehouses;
    
    @PrePersist
	public void prePersist() {
		this.createdAt =new Date();
		this.status = EnumStates.active.name();
	}
}
