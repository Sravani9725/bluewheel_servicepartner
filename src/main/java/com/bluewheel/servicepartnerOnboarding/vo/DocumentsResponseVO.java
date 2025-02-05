package com.bluewheel.servicepartnerOnboarding.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DocumentsResponseVO {
	private Integer documentId;
	private String documentCategoryType;
	private String fileName;
	private String presignedUrl;
}
