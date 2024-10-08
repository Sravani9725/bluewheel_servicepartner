package com.bluewheel.servicepartnerOnboarding.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bluewheel.servicepartnerOnboarding.entity.Flex;
import com.bluewheel.servicepartnerOnboarding.entity.Onboard;
import com.bluewheel.servicepartnerOnboarding.entity.Photography;
import com.bluewheel.servicepartnerOnboarding.entity.ServiceCenter;
import com.bluewheel.servicepartnerOnboarding.entity.Training;
import com.bluewheel.servicepartnerOnboarding.entity.Verification;
import com.bluewheel.servicepartnerOnboarding.enums.FlexStatusEnum;
import com.bluewheel.servicepartnerOnboarding.enums.StatusEnum;
import com.bluewheel.servicepartnerOnboarding.enums.RegistrationStatusEnum;
import com.bluewheel.servicepartnerOnboarding.enums.VerificationStatusEnum;
import com.bluewheel.servicepartnerOnboarding.exception.BluewheelBusinessException;
import com.bluewheel.servicepartnerOnboarding.repository.FlexRepository;
import com.bluewheel.servicepartnerOnboarding.repository.OnboardRepositoty;
import com.bluewheel.servicepartnerOnboarding.repository.PhotographyRepository;
import com.bluewheel.servicepartnerOnboarding.repository.ServiceCenterRepository;
import com.bluewheel.servicepartnerOnboarding.repository.TrainingRepository;
import com.bluewheel.servicepartnerOnboarding.repository.VerificationRepository;
import com.bluewheel.servicepartnerOnboarding.service.ServiceCenterService;
import com.bluewheel.servicepartnerOnboarding.vo.CommonVO;
import com.bluewheel.servicepartnerOnboarding.vo.FollowUpRepVO;
import com.bluewheel.servicepartnerOnboarding.vo.FollowUpVO;
import com.bluewheel.servicepartnerOnboarding.vo.GetServiceCenterVO;
import com.bluewheel.servicepartnerOnboarding.vo.PhotographyVO;
import com.bluewheel.servicepartnerOnboarding.vo.ServiceCenterVO;
import com.bluewheel.servicepartnerOnboarding.vo.VerificationVO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceCenterServiceImpl implements ServiceCenterService {

	private static final List<String> SubscriptionType = Arrays.asList("paid", "unpaid");

	@Value("#{'${servicecenter.sales.rep.ids}'.split(',')}")
	private List<String> onBoardSalesReps;

	@Value("#{'${servicecenter.verification.sales.rep.ids}'.split(',')}")
	private List<String> verifierSalesRep;

	@Value("#{'${servicecenter.flex.sales.rep.ids}'.split(',')}")
	private List<String> flexSalesRep;

	@Value("#{'${servicecenter.photography.sales.rep.ids}'.split(',')}")
	private List<String> phSalesRep;

	@Value("#{'${servicecenter.trainingAndOnboard.sales.rep.ids}'.split(',')}")
	private List<String> tainingAndOnboardSalesRep;

	private final ServiceCenterRepository servicecenterRepo;
	private final VerificationRepository verificationRepo;
	private final FlexRepository flexRepo;
	private final PhotographyRepository phRepo;
	private final TrainingRepository trainingRepo;
	private final OnboardRepositoty onBoardRepo;

	public String onBoardServiceCenter(@Valid ServiceCenterVO serviceCenter) {
		// Fetch existing service center by phone number
		ServiceCenter center = servicecenterRepo.findByServiceCenterPhoneNumber(serviceCenter.getPhoneNumber());

		// Validate Sales Rep Id
		if (!onBoardSalesReps.contains(serviceCenter.getSalesRepId())) {
			throw new BluewheelBusinessException(
					"Sales Rep Id does not match with existing sales rep ids for onboarding a service center",
					HttpStatus.EXPECTATION_FAILED, "INVALID.DATA");
		}

		// Validate Subscription Type if RegistrationStatus is Registered
		if (RegistrationStatusEnum.Registered.name().equals(serviceCenter.getRegistrationStatus())) {
			if (serviceCenter.getSubscriptionType() == null) {
				throw new BluewheelBusinessException("Please provide subscription type", HttpStatus.EXPECTATION_FAILED,
						"INVALID.DATA");
			} else if (!SubscriptionType.contains(serviceCenter.getSubscriptionType().toLowerCase())) {
				throw new BluewheelBusinessException("Please provide a valid subscription type",
						HttpStatus.EXPECTATION_FAILED, "INVALID.DATA");
			}
		}

		// Validate Follow-up details if RegistrationStatus is Followup
		if (RegistrationStatusEnum.Followup.name().equals(serviceCenter.getRegistrationStatus())) {
			if (serviceCenter.getFollowup() == null) {
				throw new BluewheelBusinessException("Please provide follow-up details", HttpStatus.EXPECTATION_FAILED,
						"INVALID.DATA");
			} else if (serviceCenter.getFollowup().getFollowUpDate() == null
					|| serviceCenter.getFollowup().getReason() == null) {
				throw new BluewheelBusinessException("Please provide follow-up details with reason and date",
						HttpStatus.EXPECTATION_FAILED, "INVALID.DATA");
			}
		}

		// Set registration date if status is Registered and date is missing
		if (RegistrationStatusEnum.Registered.name().equals(serviceCenter.getRegistrationStatus())
				&& serviceCenter.getRegisteredDate() == null) {
			serviceCenter.setRegisteredDate(LocalDate.now());
		}

		// Check if the existing service center and Sales Rep Id mismatch
		if (center != null && !center.getSalesRepId().equals(serviceCenter.getSalesRepId())) {
			throw new BluewheelBusinessException("Sales Rep Id mismatch error message", HttpStatus.BAD_REQUEST,
					"INVALID.DATA");
		}

		// Create a new ServiceCenter if center is null (onboarding a new service
		// center)
		if (center == null) {
			center = new ServiceCenter();
			center.setCreatedAt(Timestamp.from(Instant.now()));
		}

		// Update center details with ServiceCenterVO values
		center.setServiceCenterName(getOrDefault(serviceCenter.getServiceCenterName(),
				center != null ? center.getServiceCenterName() : null));
		center.setServiceCenterPhoneNumber(getOrDefault(serviceCenter.getPhoneNumber(),
				center != null ? center.getServiceCenterPhoneNumber() : null));
		center.setSalesRepId(
				getOrDefault(serviceCenter.getSalesRepId(), center != null ? center.getSalesRepId() : null));
		center.setServiceCenterOwnerName(getOrDefault(serviceCenter.getServiceCenterOwnerName(),
				center != null ? center.getServiceCenterOwnerName() : null));
		center.setServiceCenterAddress(getOrDefault(serviceCenter.getServiceCenterAddress(),
				center != null ? center.getServiceCenterAddress() : null));
		center.setRegisteredDate(
				getOrDefault(serviceCenter.getRegisteredDate(), center != null ? center.getRegisteredDate() : null));
		center.setRegistrationStatus(getOrDefault(serviceCenter.getRegistrationStatus(),
				center != null ? center.getRegistrationStatus() : null));
		center.setComments(getOrDefault(serviceCenter.getComments(), center != null ? center.getComments() : null));
		center.setSubscriptionType(getOrDefault(serviceCenter.getSubscriptionType(),
				center != null ? center.getSubscriptionType() : null));
		center.setLatitude(getOrDefault(serviceCenter.getLatitude(), center != null ? center.getLatitude() : null));
		center.setLongitude(getOrDefault(serviceCenter.getLongitude(), center != null ? center.getLongitude() : null));

		if (serviceCenter.getFollowup() != null) {
			center.setReason(
					getOrDefault(serviceCenter.getFollowup().getReason(), center != null ? center.getReason() : null));
			center.setFollowupDate(getOrDefault(serviceCenter.getFollowup().getFollowUpDate(),
					center != null ? center.getFollowupDate() : null));
		}

		center.setUpdatedAt(Timestamp.from(Instant.now()));

		// Save the service center
		servicecenterRepo.save(center);

		return "Service center details uploaded successfully";
	}

	@Override
	public String addVerificationDetails(@Valid VerificationVO verificationvo) {
		ServiceCenter center = servicecenterRepo.findByServiceCenterPhoneNumber(verificationvo.getPhoneNumber());

		if (center == null) {
			throw new BluewheelBusinessException("Service Center not found with provided phone number",
					HttpStatus.NOT_FOUND, "INVALID.DATA");
		}

		if (!center.getRegistrationStatus().equals(RegistrationStatusEnum.Registered.name())) {
			throw new BluewheelBusinessException(
					"Please complete the registration process before proceeding to verification process",
					HttpStatus.NOT_FOUND, "INVALID.DATA");
		}

		Verification verification = verificationRepo.findByServiceCenter(center);

		if (verification != null && !verification.getVerifierRepId().equals(verificationvo.getVerifierRepId())) {
			throw new BluewheelBusinessException("Verifier Rep Id mismatch", HttpStatus.BAD_REQUEST, "INVALID.DATA");
		}

		if (verification == null && !verifierSalesRep.contains(verificationvo.getVerifierRepId())) {
			throw new BluewheelBusinessException(
					"Verifier Rep Id does not match with existing verifier rep ids for verifying a service center",
					HttpStatus.BAD_REQUEST, "INVALID.DATA");
		}

		// Additional validation checks
		validateVerificationDetails(verificationvo);

		// Initialize or update Verification entity
		if (verification == null) {
			verification = new Verification();
			verification.setCreatedAt(Timestamp.from(Instant.now()));
		}

		// Set fields from VerificationVO
		verification.setComments(
				getOrDefault(verificationvo.getComments(), verification != null ? verification.getComments() : null));
		verification.setFlexDimensions(getOrDefault(verificationvo.getFlexDimensions(),
				verification != null ? verification.getFlexDimensions() : null));
		verification.setFlexInstallationDate(getOrDefault(verificationvo.getFlexInstallationDate(),
				verification != null ? verification.getFlexInstallationDate() : null));
		verification.setVerificationStatus(getOrDefault(verificationvo.getVerificationStatus(),
				verification != null ? verification.getVerificationStatus() : null));
		verification.setVerifierName(getOrDefault(verificationvo.getVerifierName(),
				verification != null ? verification.getVerifierName() : null));
		verification.setVerifierRepId(verificationvo.getVerifierRepId());
		verification.setUpdatedAt(Timestamp.from(Instant.now()));
		verification.setServiceCenter(center);

		// Set follow-up details
		if (verificationvo.getFollowup() != null) {
			verification.setReason(getOrDefault(verificationvo.getFollowup().getReason(),
					verification != null ? verification.getReason() : null));
			verification.setFollowupDate(getOrDefault(verificationvo.getFollowup().getFollowUpDate(),
					verification != null ? verification.getFollowupDate() : null));
		}

		verificationRepo.save(verification);

		return "Verification details added/updated for the service center";
	}

	private void validateVerificationDetails(VerificationVO verificationvo) {
		if (VerificationStatusEnum.Verified.name().equals(verificationvo.getVerificationStatus())) {
			if (verificationvo.getFlexDimensions() == null || verificationvo.getFlexInstallationDate() == null) {
				throw new BluewheelBusinessException("Please provide Flex dimensions and Installation date",
						HttpStatus.BAD_REQUEST, "INVALID.DATA");
			}
		}

		if (VerificationStatusEnum.VerificationPending.name().equals(verificationvo.getVerificationStatus())
				&& verificationvo.getFollowup() == null) {
			throw new BluewheelBusinessException("Please provide follow-up details", HttpStatus.EXPECTATION_FAILED,
					"INVALID.DATA");
		}

		if (VerificationStatusEnum.VerificationPending.name().equals(verificationvo.getVerificationStatus())
				&& (verificationvo.getFollowup().getFollowUpDate() == null
						|| verificationvo.getFollowup().getReason() == null)) {
			throw new BluewheelBusinessException("Please provide follow-up details with reason and date",
					HttpStatus.EXPECTATION_FAILED, "INVALID.DATA");
		}
	}

	private <T> T getOrDefault(T value, T defaultValue) {
		return value != null ? value : defaultValue;
	}

	@Override
	public String addFlexInstallationDetails(@Valid CommonVO flexvo) {
		ServiceCenter center = servicecenterRepo.findByServiceCenterPhoneNumber(flexvo.getPhoneNumber());

		if (center == null) {
			throw new BluewheelBusinessException("Service Center not found with provided phone number",
					HttpStatus.NOT_FOUND, "INVALID.DATA");
		}

		if (center.getVerification() != null
				&& !center.getVerification().getVerificationStatus().equals(VerificationStatusEnum.Verified.name())) {
			throw new BluewheelBusinessException(
					"Please complete the Verification process before proceeding to Flex Installation process",
					HttpStatus.NOT_FOUND, "INVALID.DATA");
		}
		Flex flex = flexRepo.getByServiceCenter(center);
		if (flex != null && !flex.getFlexRepId().equals(flexvo.getRepId())) {
			throw new BluewheelBusinessException("Flex Rep Id mismatch", HttpStatus.BAD_REQUEST, "INVALID.DATA");
		}

		if (flex == null && !flexSalesRep.contains(flexvo.getRepId())) {
			throw new BluewheelBusinessException("Flex Rep Id does not match with existing flex rep ids",
					HttpStatus.BAD_REQUEST, "INVALID.DATA");
		}
		additionalFlexValidations(flexvo);
		if (flex == null) {
			flex = new Flex();
			flex.setCreatedAt(Timestamp.from(Instant.now()));
		}

		flex.setComments(getOrDefault(flexvo.getComments(), flex != null ? flex.getComments() : null));
		flex.setFlexInstallationStatus(
				getOrDefault(flexvo.getStatus(), flex != null ? flex.getFlexInstallationStatus() : null));
		flex.setFlexRepId(getOrDefault(flexvo.getRepId(), flex != null ? flex.getFlexRepId() : null));
		flex.setPhAppointmentDate(getOrDefault(flexvo.getPhDate(), flex != null ? flex.getPhAppointmentDate() : null));
		flex.setServiceCenter(center);
		flex.setUpdatedAt(Timestamp.from(Instant.now()));
		if (flexvo.getFollowup() != null) {
			flex.setReason(getOrDefault(flexvo.getFollowup().getReason(), flex != null ? flex.getReason() : null));
			flex.setFollowupDate(
					getOrDefault(flexvo.getFollowup().getFollowUpDate(), flex != null ? flex.getFollowupDate() : null));
		}

		flexRepo.save(flex);

		return "Flex details added/updated for a service center";

	}

	private void additionalFlexValidations(@Valid CommonVO flexvo) {
		if (flexvo.getStatus() != null && flexvo.getStatus().equals(FlexStatusEnum.flexInstallationPending.name())
				&& flexvo.getFollowup() == null) {
			throw new BluewheelBusinessException("Please provide follow-up details", HttpStatus.EXPECTATION_FAILED,
					"INVALID.DATA");
		}

		if (FlexStatusEnum.flexInstallationPending.name().equals(flexvo.getStatus())
				&& (flexvo.getFollowup().getFollowUpDate() == null || flexvo.getFollowup().getReason() == null)) {
			throw new BluewheelBusinessException("Please provide follow-up details with reason and date",
					HttpStatus.EXPECTATION_FAILED, "INVALID.DATA");
		}

		if (flexvo.getStatus() != null && flexvo.getStatus().equals(FlexStatusEnum.FlexInstallationcomplete.name())
				&& flexvo.getPhDate() == null) {
			throw new BluewheelBusinessException("Please provide photography appointment date",
					HttpStatus.EXPECTATION_FAILED, "INVALID.DATA");
		}

	}

	@Override
	public String addPhotographyDetails(@Valid CommonVO phVO) {
		ServiceCenter center = servicecenterRepo.findByServiceCenterPhoneNumber(phVO.getPhoneNumber());

		if (center == null) {
			throw new BluewheelBusinessException("Service Center not found with provided phone number",
					HttpStatus.NOT_FOUND, "INVALID.DATA");
		}

		if (center.getFlex() != null && !center.getFlex().getFlexInstallationStatus()
				.equals(FlexStatusEnum.FlexInstallationcomplete.name())) {
			throw new BluewheelBusinessException(
					"Please complete the Flex Installation process before proceeding with photography",
					HttpStatus.NOT_FOUND, "INVALID.DATA");
		}
		additionalValidations(phVO);
		Photography photo = phRepo.getByServiceCenter(center);
		if (photo != null && !photo.getPhRepId().equals(phVO.getRepId())) {
			throw new BluewheelBusinessException("Photography Rep Id mismatch", HttpStatus.BAD_REQUEST, "INVALID.DATA");
		}

		if (photo == null && !phSalesRep.contains(phVO.getRepId())) {
			throw new BluewheelBusinessException("Photography Rep Id does not match with existing photography rep ids",
					HttpStatus.BAD_REQUEST, "INVALID.DATA");
		}

		if (photo == null) {
			photo = new Photography();
			photo.setCreatedAt(Timestamp.from(Instant.now()));
		}
		photo.setComments(getOrDefault(phVO.getComments(), photo != null ? photo.getComments() : null));
		photo.setPhStatus(getOrDefault(phVO.getStatus(), photo != null ? photo.getPhStatus() : null));
		photo.setPhRepId(getOrDefault(phVO.getRepId(), photo != null ? photo.getPhRepId() : null));
		photo.setPhDate(getOrDefault(phVO.getPhDate(), photo != null ? photo.getPhDate() : null));
		photo.setServiceCenter(center);
		photo.setUpdatedAt(Timestamp.from(Instant.now()));
		if (phVO.getFollowup() != null) {
			photo.setReason(getOrDefault(phVO.getFollowup().getReason(), photo != null ? photo.getReason() : null));
			photo.setFollowupDate(
					getOrDefault(phVO.getFollowup().getFollowUpDate(), photo != null ? photo.getFollowupDate() : null));
		}
		return "Photography details added/updated for a service center";
	}

	private void additionalValidations(@Valid CommonVO phVO) {
		if (phVO.getStatus() != null && phVO.getStatus().equals(StatusEnum.pending.name())
				&& phVO.getFollowup() == null) {
			throw new BluewheelBusinessException("Please provide follow-up details", HttpStatus.EXPECTATION_FAILED,
					"INVALID.DATA");
		}

		if (StatusEnum.pending.name().equals(phVO.getStatus())
				&& (phVO.getFollowup().getFollowUpDate() == null || phVO.getFollowup().getReason() == null)) {
			throw new BluewheelBusinessException("Please provide follow-up details with reason and date",
					HttpStatus.EXPECTATION_FAILED, "INVALID.DATA");
		}

	}

	@Override
	public String addTrainingDetails(@Valid CommonVO phVO) {
		ServiceCenter center = servicecenterRepo.findByServiceCenterPhoneNumber(phVO.getPhoneNumber());

		if (center == null) {
			throw new BluewheelBusinessException("Service Center not found with provided phone number",
					HttpStatus.NOT_FOUND, "INVALID.DATA");
		}

		if (center.getPhotography() != null
				&& !center.getPhotography().getPhStatus().equals(FlexStatusEnum.FlexInstallationcomplete.name())) {
			throw new BluewheelBusinessException(
					"Please complete the Photography process before proceeding with Training", HttpStatus.NOT_FOUND,
					"INVALID.DATA");
		}
		additionalValidations(phVO);
		Training photo = trainingRepo.getByServiceCenter(center);
		if (photo != null && !photo.getTrRepId().equals(phVO.getRepId())) {
			throw new BluewheelBusinessException("Trainer Rep Id mismatch", HttpStatus.BAD_REQUEST, "INVALID.DATA");
		}

		if (photo == null && !tainingAndOnboardSalesRep.contains(phVO.getRepId())) {
			throw new BluewheelBusinessException("Training Rep Id does not match with existing training rep ids",
					HttpStatus.BAD_REQUEST, "INVALID.DATA");
		}

		if (photo == null) {
			photo = new Training();
			photo.setCreatedAt(Timestamp.from(Instant.now()));
		}
		photo.setComments(getOrDefault(phVO.getComments(), photo != null ? photo.getComments() : null));
		photo.setTrainingStatus(getOrDefault(phVO.getStatus(), photo != null ? photo.getTrainingStatus() : null));
		photo.setTrRepId(getOrDefault(phVO.getRepId(), photo != null ? photo.getTrRepId() : null));
		photo.setServiceCenter(center);
		photo.setUpdatedAt(Timestamp.from(Instant.now()));
		if (phVO.getFollowup() != null) {
			photo.setReason(getOrDefault(phVO.getFollowup().getReason(), photo != null ? photo.getReason() : null));
			photo.setFollowupDate(
					getOrDefault(phVO.getFollowup().getFollowUpDate(), photo != null ? photo.getFollowupDate() : null));
		}
		return "Training details added/updated for a service center";
	}

	@Override
	public String addOnboardDetails(@Valid CommonVO phVO) {
		ServiceCenter center = servicecenterRepo.findByServiceCenterPhoneNumber(phVO.getPhoneNumber());

		if (center == null) {
			throw new BluewheelBusinessException("Service Center not found with provided phone number",
					HttpStatus.NOT_FOUND, "INVALID.DATA");
		}

		if (center.getTraining() != null
				&& !center.getTraining().getTrainingStatus().equals(StatusEnum.complete.name())) {
			throw new BluewheelBusinessException("Please complete the Training and then proceed to on boarding.",
					HttpStatus.NOT_FOUND, "INVALID.DATA");
		}
		if (center.getTraining() != null && !center.getTraining().getTrRepId().equals(phVO.getRepId())) {
			throw new BluewheelBusinessException("rep id should match with rep id of training", HttpStatus.NOT_FOUND,
					"INVALID.DATA");
		}
		additionalValidations(phVO);
		Onboard photo = onBoardRepo.getByServiceCenter(center);
		if (photo != null && !photo.getTrRepId().equals(phVO.getRepId())) {
			throw new BluewheelBusinessException("Rep Id mismatch", HttpStatus.BAD_REQUEST, "INVALID.DATA");
		}

		if (photo == null && !tainingAndOnboardSalesRep.contains(phVO.getRepId())) {
			throw new BluewheelBusinessException("Onboard Rep Id does not match with existing Onboard rep ids",
					HttpStatus.BAD_REQUEST, "INVALID.DATA");
		}

		if (photo == null) {
			photo = new Onboard();
			photo.setCreatedAt(Timestamp.from(Instant.now()));
		}
		photo.setComments(getOrDefault(phVO.getComments(), photo != null ? photo.getComments() : null));
		photo.setOnboardStatus(getOrDefault(phVO.getStatus(), photo != null ? photo.getOnboardStatus() : null));
		photo.setTrRepId(getOrDefault(phVO.getRepId(), photo != null ? photo.getTrRepId() : null));
		photo.setServiceCenter(center);
		photo.setUpdatedAt(Timestamp.from(Instant.now()));
		if (phVO.getFollowup() != null) {
			photo.setReason(getOrDefault(phVO.getFollowup().getReason(), photo != null ? photo.getReason() : null));
			photo.setFollowupDate(
					getOrDefault(phVO.getFollowup().getFollowUpDate(), photo != null ? photo.getFollowupDate() : null));
		}
		return "Onboard details added/updated for a service center";
	}

	@Override
	public GetServiceCenterVO getServiceCenter(@NotBlank(message = "phonenumber cannot be null") String phoneNumber) {
		ServiceCenter center = servicecenterRepo.findByServiceCenterPhoneNumber(phoneNumber);
		if (center == null) {
			throw new BluewheelBusinessException("Service Center not found with provided phone number",
					HttpStatus.NOT_FOUND, "INVALID.DATA");
		}
		return GetServiceCenterVO.builder().salesRepId(center.getSalesRepId())
				.registeredDate(center.getRegisteredDate()).registrationComments(center.getComments())
				.serviceCenterAddress(center.getServiceCenterAddress()).serviceCenterName(center.getServiceCenterName())
				.phoneNumber(center.getServiceCenterPhoneNumber()).registrationStatus(center.getRegistrationStatus())
				.serviceCenterOwnerName(center.getServiceCenterOwnerName())
				.subscriptionType(center.getSubscriptionType())
				.registrationFollowup(buildFollowup(center.getReason(), center.getFollowupDate()))
				.verificationDetails(getVerifierVO(center.getVerification())).flexDetails(getFlexVO(center.getFlex()))
				.photographyDetails(getPhotographyVO(center.getPhotography()))
				.trainingDetails(getTrainingVO(center.getTraining()))
				.onBoardingDetails(getOnboardVO(center.getOnboard())).build();

	}

	private CommonVO getOnboardVO(Onboard onboard) {
		if (onboard == null)
			return null;
		return CommonVO.builder().comments(onboard.getComments()).repId(onboard.getTrRepId())
				.status(onboard.getOnboardStatus())
				.followup(buildFollowup(onboard.getReason(), onboard.getFollowupDate())).build();

	}

	private CommonVO getTrainingVO(Training training) {
		if (training == null)
			return null;
		return CommonVO.builder().comments(training.getComments()).repId(training.getTrRepId())
				.status(training.getTrainingStatus())
				.followup(buildFollowup(training.getReason(), training.getFollowupDate())).build();

	}

	private CommonVO getPhotographyVO(Photography photography) {
		if (photography == null)
			return null;
		return CommonVO.builder().comments(photography.getComments()).repId(photography.getPhRepId())
				.status(photography.getPhStatus())
				.followup(buildFollowup(photography.getReason(), photography.getFollowupDate())).build();
	}

	private CommonVO getFlexVO(Flex flex) {
		if (flex == null)
			return null;
		return CommonVO.builder().comments(flex.getComments()).repId(flex.getFlexRepId())
				.status(flex.getFlexInstallationStatus())
				.followup(buildFollowup(flex.getReason(), flex.getFollowupDate())).build();
	}

	private VerificationVO getVerifierVO(Verification verification) {
		if (verification == null)
			return null;
		return VerificationVO.builder().comments(verification.getComments())
				.flexDimensions(verification.getFlexDimensions())
				.flexInstallationDate(verification.getFlexInstallationDate())
				.verificationStatus(verification.getVerificationStatus()).verifierName(verification.getVerifierName())
				.verifierRepId(verification.getVerifierRepId())
				.followup(buildFollowup(verification.getReason(), verification.getFollowupDate())).build();
	}

	private FollowUpVO buildFollowup(String reason, LocalDate followupDate) {
		if (reason == null && followupDate == null)
			return null;
		return FollowUpVO.builder().reason(reason != null ? reason : null)
				.followUpDate(followupDate != null ? followupDate : null).build();

	}

	@Override
	public List<FollowUpRepVO> getFollowupsByRepId(@NotBlank(message = "rep id cannot be null") String repId) {
		List<FollowUpRepVO> returnList = new ArrayList<FollowUpRepVO>();
		boolean flag = false;
		if (onBoardSalesReps.contains(repId)) {
			List<ServiceCenter> centers = servicecenterRepo.findBySalesRepId(repId);
			for (ServiceCenter c : centers) {
				if (c.getRegistrationStatus().equals(RegistrationStatusEnum.Followup.name())
						|| c.getRegistrationStatus().equals(RegistrationStatusEnum.Assigned.name()))
					returnList.add(followUpRepVOMapper(c, c.getReason(), c.getFollowupDate()));

			}
			flag = true;
		}
		if (verifierSalesRep.contains(repId)) {
			List<Verification> verifications = verificationRepo.getByVerifierRepId(repId);
			for (Verification v : verifications) {
				if (v.getVerificationStatus().equals(VerificationStatusEnum.VerificationPending.name())
						|| (v.getVerificationStatus().equals(VerificationStatusEnum.Assigned.name())))
					returnList.add(followUpRepVOMapper(v.getServiceCenter(), v.getReason(), v.getFollowupDate()));
			}
			flag = true;
		}
		if (flexSalesRep.contains(repId)) {
			List<Flex> flexs = flexRepo.getByFlexRepId(repId);
			for (Flex f : flexs) {
				if (f.getFlexInstallationStatus().equals(FlexStatusEnum.flexInstallationPending.name())
						|| f.getFlexInstallationStatus().equals(FlexStatusEnum.Assigned.name()))
					returnList.add(followUpRepVOMapper(f.getServiceCenter(), f.getReason(), f.getFollowupDate()));
			}
			flag = true;
		}
		if (phSalesRep.contains(repId)) {
			List<Photography> photos = phRepo.getByPhRepId(repId);
			for (Photography p : photos) {
				if (p.getPhStatus().equals(StatusEnum.pending.name())
						|| p.getPhStatus().equals(StatusEnum.Assigned.name()))
					returnList.add(followUpRepVOMapper(p.getServiceCenter(), p.getReason(), p.getFollowupDate()));
			}
			flag = true;
		}
		if (tainingAndOnboardSalesRep.contains(repId)) {
			List<Training> trainings = trainingRepo.getByTrRepId(repId);
			for (Training p : trainings) {
				if (p.getTrainingStatus().equals(StatusEnum.pending.name())
						|| p.getTrainingStatus().equals(StatusEnum.Assigned.name()))
					returnList.add(followUpRepVOMapper(p.getServiceCenter(), p.getReason(), p.getFollowupDate()));
			}
			List<Onboard> onboards = onBoardRepo.getByTrRepId(repId);
			for (Onboard p : onboards) {
				if (p.getOnboardStatus().equals(StatusEnum.pending.name())
						|| p.getOnboardStatus().equals(StatusEnum.Assigned.name()))
					returnList.add(followUpRepVOMapper(p.getServiceCenter(), p.getReason(), p.getFollowupDate()));
			}
			flag = true;
		}
		if (!flag)
			throw new BluewheelBusinessException("No such rep id found", HttpStatus.BAD_REQUEST, "INVALID.DATA");
		return returnList;
	}

	public FollowUpRepVO followUpRepVOMapper(ServiceCenter center, String reason, LocalDate date) {
		return FollowUpRepVO.builder().serviceCenterPhonenumber(center.getServiceCenterPhoneNumber())
				.serviceCenterName(center.getServiceCenterName()).serviceCenterAddress(center.getServiceCenterAddress())
				.serviceCenterOwnerName(center.getServiceCenterOwnerName()).followUpDetails(buildFollowup(reason, date))
				.build();
	}

}
