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

@Entity
@Data
@Table(name="info_user_activity")
public class InfoUserActivity implements Serializable{

	private static final long serialVersionUID = 7380947951555605756L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)	
    private Long id;
	
	@NotNull
	@ManyToOne
	@JsonBackReference
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_USER"))
	private InfoUser user;
	
	@NotNull
	@Column(name = "description", length = 100)
	private String description;
	
	@NotNull
	@Column(name = "activity_type", length = 25)
	private String type;
	
	@NotNull
	@Column(name = "created_at")  
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;		
	
	@PrePersist
	public void prePersist() {
		this.createdAt = new Date();
	}
}
