package com.bluewheel.servicepartnerOnboarding.vo;

import org.springframework.core.io.InputStreamResource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DownloadResponseVO {
	private String fileName;
	private InputStreamResource inputStreamResource;
}
