package net.project.ecommerce.msa.user.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name="admi_role")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmiRol implements Serializable{
	
	private static final long serialVersionUID = 4018870247961454297L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long id;
	
	@Column(unique = true, length = 20)
	private String name;
	
	 @Column(name = "status", length = 10)
	private String status;
    
    @Column(name = "created_at")   
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;        
    
    @PrePersist
	public void prePersist() {
    	this.createdAt = new Date();
		this.status = EnumStates.active.name();
	}

}
