package com.bluewheel.servicepartnerOnboarding.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "verification", schema = "servicepartner_onboarding_sales")
@NamedQuery(name = "Verification.findAll", query = "SELECT v FROM Verification v")
public class Verification implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "verifier_name")
	private String verifierName;
	
	@Column(name = "verifier_rep_id")
	private String verifierRepId;
	
	@Column(name = "verification_status")
	private String verificationStatus;
	
	@Column(name = "flex_installation_date")
	private LocalDate flexInstallationDate;
	
	@Column(name = "flex_dimensions")
	private String flexDimensions;
	
	@Column(name = "comments")
	private String comments;
	
	@Column(name = "created_at")
	private Timestamp createdAt;
	
	@Column(name = "updated_at")
	private Timestamp updatedAt;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "service_center_id", referencedColumnName = "id")
	private ServiceCenter serviceCenter;
	
	@Column(name="reason")
	private String reason;
	
	@Column(name="followup_date")
	private LocalDate followupDate;

}
