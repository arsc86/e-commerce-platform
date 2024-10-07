package net.project.ecommerce.msa.user.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name="info_payment_method")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoPaymentMethod implements Serializable {
	
	private static final long serialVersionUID = 8571531522887198339L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
	@JsonBackReference
	private InfoUser user;
	
	@NotNull
    @Column(name = "type", length = 25)
    private String type;
	
	@NotNull
    @Column(name = "account_number", length = 50, unique = true)
    private String accountNumber;
	
	@NotNull
    @Column(name = "status", length = 10)
    private String status;
		
    @Column(name = "expiration_date")   
    private String expirationDate;
    
    @Column(name = "is_default",length = 1, nullable = true)
	private String isDefault;
    
	@NotNull
    @Column(name = "created_at")   
	@Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Column(name = "updated_at")	
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    @PrePersist
	public void prePersist() {
		this.createdAt =new Date();
		this.status = EnumStates.active.name();
	}

}
