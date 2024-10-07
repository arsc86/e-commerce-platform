package net.project.ecommerce.msa.user.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import lombok.Data;
import net.project.ecommerce.dependency.api.enums.EnumStates;

@Entity
@Data
@Table(name="info_credential_hist")
public class InfoCredentialHist implements Serializable {
	
	private static final long serialVersionUID = 4933423893860601481L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)	
    private Long id;		
	
	@NotNull	
	@JsonBackReference
	@ManyToOne
    @JoinColumn(name = "credential_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_CREDENTIAL_HISTORY"))
	private InfoCredential credential;
	
	@NotNull
	@Column(name = "last_password", length = 100)
	private String lastPassword;
	
	@NotNull
    @Column(name = "status", length = 10)
    private String status;
    
	@NotNull
    @Column(name = "created_at")    
	@Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
	
	@PrePersist
	public void prePersist() {
		this.createdAt = new Date();
		this.status = EnumStates.active.name();
	}

}
