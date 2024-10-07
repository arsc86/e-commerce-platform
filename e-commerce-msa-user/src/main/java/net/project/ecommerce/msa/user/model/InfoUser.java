package net.project.ecommerce.msa.user.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import net.project.ecommerce.dependency.api.enums.EnumStates;

@Entity
@Data
@Table(name="info_user")
@ToString
public class InfoUser implements Serializable {
	
	private static final long serialVersionUID = -848133023954974911L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)	
    private Long id;

	@NotNull
	@Column(unique = true, length = 10)
    private String username;
    
	@NotNull
    @Column(name = "first_name", length = 20)
    private String firstName;
    
	@NotNull
    @Column(name = "last_name", length = 20)
    private String lastName;
    
	@NotNull
    @Column(name = "status", length = 10)
    private String status;
    
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")    
    private Date createdAt;
    
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    @NotNull    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credential_id", referencedColumnName = "id",
    		    foreignKey = @ForeignKey(name = "FK_CREDENTIAL_USER"))
    private InfoCredential credential;
    
    @NotNull    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", referencedColumnName = "id",
    		    foreignKey = @ForeignKey(name = "FK_PROFILE_USER"))
    private InfoProfile profile;
    
    @NotNull   
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="info_role_user", 
	           joinColumns = @JoinColumn(name="user_id"),
	           inverseJoinColumns = @JoinColumn(name="role_id"),
	           uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id","role_id"})})
	private List<AdmiRol> roles;
    
    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InfoUserActivity> listUserActivity;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<InfoAddress> listAddress;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<InfoPaymentMethod> listPaymentMethod;
       
    @Column(name = "refresh_token",columnDefinition = "TEXT") 
    private String refreshToken;
    
    @PrePersist
	public void prePersist() {
		this.createdAt = new Date();
		this.status = EnumStates.active.name();
	}
}
