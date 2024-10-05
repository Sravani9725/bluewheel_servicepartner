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
@Table(name = "service_centers", schema = "servicepartner_onboarding_sales")
@NamedQuery(name = "ServiceCenter.findAll", query = "SELECT s FROM ServiceCenter s")
public class ServiceCenter implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="service_center_phone_number")
	private String serviceCenterPhoneNumber;
	
	@Column(name="service_center_owner_name")
	private String serviceCenterOwnerName;
	
	@Column(name="service_center_address")
	private String serviceCenterAddress;
	
	@Column(name="service_center_name")
	private String serviceCenterName;
	
	@Column(name="sales_rep_id")
	private String salesRepId;
	
	@Column(name="registered_date")
	private LocalDate registeredDate;
	
	@Column(name="created_at")
	private Timestamp createdAt;
	
	@Column(name="updatedAt")
	private Timestamp updatedAt;
	
	@Column(name="comments")
	private String comments;
	
	@Column(name="registration_status")
	private String registrationStatus;
	
	@Column(name="subscription_type")
	private String subscriptionType;
	
	@Column(name="reason")
	private String reason;
	
	@Column(name="followup_date")
	private LocalDate FollowupDate;
	
	@OneToOne(mappedBy = "serviceCenter", cascade = CascadeType.ALL)
	private Verification verification;
	
	@OneToOne(mappedBy = "serviceCenter", cascade = CascadeType.ALL)
	private Flex flex;
	
	@OneToOne(mappedBy = "serviceCenter", cascade = CascadeType.ALL)
	private Photography photography;
	
	@OneToOne(mappedBy = "serviceCenter", cascade = CascadeType.ALL)
	private Training training;
	@OneToOne(mappedBy = "serviceCenter", cascade = CascadeType.ALL)
	private Onboard onboard;
	
	
	
}
