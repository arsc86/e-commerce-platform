package net.project.ecommerce.msa.user.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
import lombok.Data;

@Entity
@Data
@Table(name="info_credential")
public class InfoCredential implements Serializable{
	
	private static final long serialVersionUID = -8773965922194914235L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long id;
	
	@NotNull
	@Column(name = "password", length = 100)
	private String password;
	
	@Column(name = "expired_at")
	@NotNull
    @Temporal(TemporalType.DATE)
	private Date expiredAt;
	
	@Column(name = "created_at")  
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
		
	@JsonManagedReference
	@OneToMany(mappedBy = "credential", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InfoCredentialHist> listCredentialHist;
	
	@PrePersist
	public void prePersist() {
		this.createdAt = new Date();
	}

}
