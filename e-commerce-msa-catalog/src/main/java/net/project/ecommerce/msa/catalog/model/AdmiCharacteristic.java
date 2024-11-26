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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.api.enums.EnumStates;

@Entity
@Data
@Table(name="admi_characteristic")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AdmiCharacteristic implements Serializable{
	
	private static final long serialVersionUID = 3017571460129339852L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long id;
	
	@Column(name = "name", length = 20, nullable = false)	
	private String name;
	
	@Column(name = "status", length = 10, nullable = false)	
	private String status;
    
    @Column(name = "created_at", nullable = false)       
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;   
    
    @Column(name = "updated_at", nullable = true)   
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt; 
    
    @OneToMany(mappedBy = "characteristic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)	
	private List<AdmiProductCharact> listProductCharacteristics;
    
    @PrePersist
	public void prePersist() {
    	this.createdAt = new Date();
		this.status = EnumStates.active.name();
	}

}
