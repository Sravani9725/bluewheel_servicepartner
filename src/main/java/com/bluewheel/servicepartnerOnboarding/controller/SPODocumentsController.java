package com.bluewheel.servicepartnerOnboarding.controller;

import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bluewheel.servicepartnerOnboarding.service.DocumentService;
import com.bluewheel.servicepartnerOnboarding.vo.DocumentsResponseVO;
import com.bluewheel.servicepartnerOnboarding.vo.DownloadResponseVO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/bluewheel/service")
public class SPODocumentsController {
	
	private final DocumentService dmService;
	
	@PostMapping(path="/upload/{docCategory}/{id}",consumes="multipart/form-data")
	public ResponseEntity<Integer> uploadDocument(@Valid MultipartFile documentvo, @PathVariable String docCategory,@PathVariable Integer id ){
		log.info("request received to upload a document");
		Integer res = dmService.uploadDocument(documentvo,docCategory,id);
		log.info("document uploaded successfully {}",res);
		return ResponseEntity.ok().body(res);
		
	}
	
	@GetMapping(path="/document/{documentId}")
	public ResponseEntity<InputStreamResource> download(@PathVariable Integer documentId ){
		log.info("request received to download a document: {}",documentId);
		DownloadResponseVO isr = dmService.getDocument(documentId);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+isr.getFileName());
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
		log.info("returning document stream");
		return ResponseEntity.ok().headers(headers).body(isr.getInputStreamResource());
		
	}
	@DeleteMapping(path="/document/{documentId}")
	public ResponseEntity<Void> deleteDocument(@PathVariable Integer documentId){
		log.info("request received to delete a document: {}",documentId);
		dmService.deleteDocument(documentId);
		log.info("document deleted successfully :{}",documentId);
		return ResponseEntity.noContent().build();
		
		
	}
	
	@GetMapping(path="/documents/{docCategory}/{id}")
	public ResponseEntity<List<DocumentsResponseVO>> getAllClientDocuments(@PathVariable String docCategory,@PathVariable Integer id){
		log.info("request received to get all documents of a {}: {}",docCategory,id);
		List<DocumentsResponseVO> res = dmService.getAllDocuments(docCategory,id);
		log.info("returning all the documents of  {}: {}",docCategory,id);
		return ResponseEntity.ok().body(res);

		
	}

}
