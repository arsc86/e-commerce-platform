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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.project.ecommerce.dependency.api.enums.EnumStates;

@Entity
@Data
@Table(name="info_profile")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoProfile implements Serializable {
	
	private static final long serialVersionUID = 3372913039572714844L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)	
    private Long id;
		
    @Column(name = "provider_code", length = 10)
	private String providerCode;
	
	@NotNull
	@Email
    @Column(name = "email", length = 30)
	private String email;
		
    @Column(name = "phone", length = 12)
	private String phone;		    
		
    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
	private Date birthday;
		
    @Column(name = "profile_image", length = 100)
	private String profileImage;
	
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
    
    @PrePersist
	public void prePersist() {
		this.createdAt =new Date();
		this.status = EnumStates.active.name();
	}

}
