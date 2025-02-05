package com.bluewheel.servicepartnerOnboarding.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bluewheel.servicepartnerOnboarding.entity.Documents;
import com.bluewheel.servicepartnerOnboarding.entity.Flex;
import com.bluewheel.servicepartnerOnboarding.entity.Onboard;
import com.bluewheel.servicepartnerOnboarding.entity.ServiceCenter;
import com.bluewheel.servicepartnerOnboarding.entity.Verification;
import com.bluewheel.servicepartnerOnboarding.enums.DocumentCategoryEnum;
import com.bluewheel.servicepartnerOnboarding.exception.BluewheelBusinessException;
import com.bluewheel.servicepartnerOnboarding.repository.DocumentsRepository;
import com.bluewheel.servicepartnerOnboarding.repository.FlexRepository;
import com.bluewheel.servicepartnerOnboarding.repository.OnboardRepositoty;
import com.bluewheel.servicepartnerOnboarding.repository.ServiceCenterRepository;
import com.bluewheel.servicepartnerOnboarding.repository.VerificationRepository;
import com.bluewheel.servicepartnerOnboarding.service.DigitalOceanSpaceService;
import com.bluewheel.servicepartnerOnboarding.service.DocumentService;
import com.bluewheel.servicepartnerOnboarding.vo.DocumentsResponseVO;
import com.bluewheel.servicepartnerOnboarding.vo.DownloadResponseVO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

	private final ServiceCenterRepository serviceCenterRepository;
	private final FlexRepository flexRepo;
	private final VerificationRepository verficationRepo;
	private final OnboardRepositoty onboardRepo;
	private final DigitalOceanSpaceService spaceServive;
	private final DocumentsRepository docRepo;

	@Override
	public Integer uploadDocument(@Valid MultipartFile documentvo, String docCategory, Integer id) {
		Documents doc = new Documents();
		switch (DocumentCategoryEnum.valueOf(docCategory)) {
		case REGISTRATION:
			Optional<ServiceCenter> sc = serviceCenterRepository.findById(id);
			if (sc.isEmpty())
				throw new BluewheelBusinessException("Serice center not found with give id", HttpStatus.NOT_FOUND,
						"INVALID.DATA");
			doc.setServiceCenter(sc.get());
			break;
		case FLEX_INSTALLATION:
			Optional<Flex> flex = flexRepo.findById(id);
			if (flex.isEmpty())
				throw new BluewheelBusinessException("Flex installation not found with give id", HttpStatus.NOT_FOUND,
						"INVALID.DATA");
			doc.setFlex(flex.get());
			break;
		case ONBOARDING:
			Optional<Onboard> onboard = onboardRepo.findById(id);
			if (onboard.isEmpty())
				throw new BluewheelBusinessException("Onboarding details not found with give id", HttpStatus.NOT_FOUND,
						"INVALID.DATA");
			doc.setOnboard(onboard.get());
			break;
		case VERIFICATION:
			Optional<Verification> verification = verficationRepo.findById(id);
			if (verification.isEmpty())
				throw new BluewheelBusinessException("Onboarding details not found with give id", HttpStatus.NOT_FOUND,
						"INVALID.DATA");
			doc.setVerification(verification.get());
			break;

		}
		doc.setDocCategory(docCategory);
		doc.setFilename(documentvo.getName());
		doc.setKey(buildKey(id, docCategory, documentvo.getName()));
		spaceServive.uploadFile(doc.getKey(), documentvo);
		doc = docRepo.save(doc);
		return doc.getId();

	}

	private String buildKey(Integer id, String docCategory, String filename) {
		return id + "/" + docCategory + "/" + filename;
	}

	@Override
	public DownloadResponseVO getDocument(Integer documentId) {
		log.info("fetching the document from db:{}", documentId);
		Documents doc = getDoc(documentId);
		log.debug("built s3key for document {} is {}", documentId, doc.getKey());
		log.info("calling s3 service to download a document: {}", documentId);
		InputStreamResource docData = spaceServive.getDocument(doc.getKey());
		log.info("returning the downloaded InputStreamResource from s3 service");
		return DownloadResponseVO.builder().fileName(doc.getFilename()).inputStreamResource(docData).build();
	}

	private Documents getDoc(Integer documentId) {
		return docRepo.findById(documentId).orElseThrow(()-> new BluewheelBusinessException("Document not found with given Id",HttpStatus.NOT_FOUND,"INVALID.DATA"));
	}

	@Override
	public void deleteDocument(Integer documentId) {
		log.info("fetching the document from db:{}", documentId);
		Documents doc = getDoc(documentId);
		log.debug("built s3key for document {} is {}", documentId, doc.getKey());
		log.info("calling s3 service to delete a document: {}", documentId);
		spaceServive.deleteFile(doc.getKey());
		docRepo.delete(doc);
		
	}

	@Override
	public List<DocumentsResponseVO> getAllDocuments(String docCategory, Integer id) {
		List<Documents> docList = new ArrayList<Documents>();
		switch (DocumentCategoryEnum.valueOf(docCategory)) {
		case REGISTRATION:
			Optional<ServiceCenter> sc = serviceCenterRepository.findById(id);
			if (sc.isEmpty())
				throw new BluewheelBusinessException("Serice center not found with give id", HttpStatus.NOT_FOUND,
						"INVALID.DATA");
			docList = docRepo.findByServiceCenter(sc.get());
			break;
		case FLEX_INSTALLATION:
			Optional<Flex> flex = flexRepo.findById(id);
			if (flex.isEmpty())
				throw new BluewheelBusinessException("Flex installation not found with give id", HttpStatus.NOT_FOUND,
						"INVALID.DATA");
			docList = docRepo.findByFlex(flex.get());
			break;
		case ONBOARDING:
			Optional<Onboard> onboard = onboardRepo.findById(id);
			if (onboard.isEmpty())
				throw new BluewheelBusinessException("Onboarding details not found with give id", HttpStatus.NOT_FOUND,
						"INVALID.DATA");
			docList = docRepo.findByOnboard(onboard.get());
			break;
		case VERIFICATION:
			Optional<Verification> verification = verficationRepo.findById(id);
			if (verification.isEmpty())
				throw new BluewheelBusinessException("Onboarding details not found with give id", HttpStatus.NOT_FOUND,
						"INVALID.DATA");
			docList = docRepo.findByVerification(verification.get());
			break;

		}
		if(docList.isEmpty())
			throw new BluewheelBusinessException("NO images found",HttpStatus.NO_CONTENT,"INVALID.DATA");
		
		log.debug("building the s3keys for list of documents for a clinet: {}", id);
		Map<Integer, String> s3Keys = docList.stream()
				.collect(Collectors.toMap(Documents::getId, Documents::getKey));
		log.info("calling s3 servive to generate the presigned urls for all the documents of {}: ", id);
		Map<Integer, String> presignUrl = spaceServive.getPresignUrls(s3Keys);
		List<DocumentsResponseVO> responsevo = new ArrayList<>();
		docList.forEach(doc -> responsevo.add(
				DocumentsResponseVO.builder().documentId(doc.getId()).documentCategoryType(doc.getDocCategory())
						.fileName(doc.getFilename()).presignedUrl(presignUrl.get(doc.getId()))
						.build()));
		return responsevo;
	}

}
