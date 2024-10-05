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
@Table(name = "onboard", schema = "servicepartner_onboarding_sales")
@NamedQuery(name = "Onboard.findAll", query = "SELECT v FROM Onboard v")
public class Onboard implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "tr_rep_id")
	private String trRepId;
	
	@Column(name = "onboard_status")
	private String onboardStatus;
	
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
