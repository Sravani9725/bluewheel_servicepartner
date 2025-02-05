package com.bluewheel.servicepartnerOnboarding.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
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
@Table(name = "documents", schema = "servicepartner_onboarding_sales")
@NamedQuery(name = "Documents.findAll", query = "SELECT v FROM Documents v")
public class Documents implements Serializable{
		
		private static final long serialVersionUID = 1L;

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "id")
		private Integer id;
		
		private String filename;
		
		private String key;
		
		private String docCategory;
		
		@ManyToOne
	    @JoinColumn(name= "service_center_id")
		private ServiceCenter serviceCenter;
		
		@ManyToOne
	    @JoinColumn(name= "verification")
		private Verification verification;
		
		@ManyToOne
	    @JoinColumn(name= "flex_id")
		private Flex flex;
		@ManyToOne
	    @JoinColumn(name= "onboard_id")
		private Onboard onboard;
}
