package com.bluewheel.servicepartnerOnboarding.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bluewheel.servicepartnerOnboarding.vo.DocumentsResponseVO;
import com.bluewheel.servicepartnerOnboarding.vo.DownloadResponseVO;

import jakarta.validation.Valid;

public interface DocumentService {

	Integer uploadDocument(@Valid MultipartFile documentvo, String docCategory, Integer id);

	DownloadResponseVO getDocument(Integer documentId);

	void deleteDocument(Integer documentId);

	List<DocumentsResponseVO> getAllDocuments(String docCategory, Integer id);

}
