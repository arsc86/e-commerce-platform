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
@Table(name="info_product_discount")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class InfoProductDiscount implements Serializable{
	
	private static final long serialVersionUID = 7219316067789337590L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_PRODUCT"))
	//@JsonBackReference
	private AdmiProduct product;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_REGION"))
	//@JsonBackReference
	private AdmiRegion region;
		
	@NotNull
	@Column(name = "type",length = 20, nullable = false)	
	private String type;
		
	@NotNull
	@Column(name = "discount", nullable = false)	
	private double discount;
	
	@Column(name = "start_date")	
	@Temporal(TemporalType.DATE)
	private Date start_date;
	
	@Column(name = "end_date")	
	@Temporal(TemporalType.DATE)
	private Date end_date;		
	
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
