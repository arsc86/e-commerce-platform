package net.project.ecommerce.msa.catalog.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Table(name="admi_category")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmiCategory implements Serializable{
	
	private static final long serialVersionUID = 7707754060211952427L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
	@JsonBackReference
	private AdmiCategory category;	
		
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<AdmiCategory> listCategories;
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<AdmiProduct> listProduct;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_PROVIDER"))
	@JsonBackReference
	private AdmiProvider provider;	
	
	@Column(name = "name", unique = false, length = 25)	
	private String name;	
	
	@Column(name = "status", length = 10)	
	private String status;	
	
	@Column(name = "created_by", length = 20)	
	private String createdBy;    
    
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
