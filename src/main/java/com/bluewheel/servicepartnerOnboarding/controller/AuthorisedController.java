package com.bluewheel.servicepartnerOnboarding.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluewheel.servicepartnerOnboarding.service.AuthorisedServiceCentersService;
import com.bluewheel.servicepartnerOnboarding.vo.AuthorisedServiceCenterVO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/bluewheel/service")
public class AuthorisedController {

	private final AuthorisedServiceCentersService authorisedServiceCenterService;

	  @PostMapping("/service-center/authorised")
	    public ResponseEntity<String> create(@Valid @RequestBody AuthorisedServiceCenterVO authorisedServiceCenterVO) {
	        log.info("Creating authorised service center");
	        authorisedServiceCenterService.createAuthorisedServiceCenters(authorisedServiceCenterVO);
	        return ResponseEntity.ok( "Authorised Service Center created successfully!");
	    }

}
