package com.bluewheel.servicepartnerOnboarding.service;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bluewheel.servicepartnerOnboarding.exception.BluewheelRuntimeException;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class DigitalOceanSpaceService {
	
	@Value(value="${spring.application.name}")
	private String name;
	@Value(value="${servicecenter.sales.rep.ids}")
	private String onBoardSalesReps;

    @Value(value="${do.space.access-key}")
    private String accessKey;

    @Value("${do.space.secret-key}")
    private String secretKey;

    @Value("${do.space.endpoint}")
    private String endpoint;

    @Value("${do.space.bucket-name}")
    private String bucketName;

    private  S3Client s3Client;
    
    private S3Presigner s3Presigner;
    
    @PostConstruct
    public void init() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of("blr1"))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        
        this.s3Presigner = S3Presigner.builder().region(Region.of("blr1"))
				.credentialsProvider(StaticCredentialsProvider.create(awsCreds))
				.build();
    }

    // Upload file
    public void uploadFile(String key,MultipartFile file)  {
//        String fileName = filePath.getFileName().toString();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try {
			s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
		} catch (AwsServiceException | SdkClientException | IOException e) {
			throw new BluewheelRuntimeException("Error occured while uploading document",
					HttpStatus.INTERNAL_SERVER_ERROR, "S3_UPLOAD_ERROR");
		}
     
    }

    // List files in Space
    public ListObjectsV2Response listFiles() {
        ListObjectsV2Request listObjects = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        return s3Client.listObjectsV2(listObjects);
    }
    
    public InputStreamResource getDocument(String key) {
		try {
			log.debug("downloading document from s3 bucket: {}",key);

			GetObjectRequest gor = GetObjectRequest.builder().bucket(bucketName).key(key).build();
			ResponseInputStream<GetObjectResponse> doc = s3Client.getObject(gor);

			return new InputStreamResource(doc);
		} catch (NoSuchKeyException e) {
			throw new BluewheelRuntimeException("S3_FILE_NOT_FOUND_ERROR_MESSAGE",
					HttpStatus.NOT_FOUND, "NOT.FOUND");
		} catch (S3Exception | SdkClientException e) {
			throw new BluewheelRuntimeException("FILE_READ_ERROR_MESSAGE",
					HttpStatus.INTERNAL_SERVER_ERROR,"FILE.READ.ERROR.CODE");
		}

	}


   

    // Delete file
    public void deleteFile(String fileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    public Map<Integer, String> getPresignUrls(Map<Integer, String> s3Keys) {
		log.debug("generating the presigned urls using s3");
		Map<Integer,String> presignUrls = new HashMap<>();
		s3Keys.forEach((key,val)->{
			GetObjectRequest gor = GetObjectRequest.builder().bucket(bucketName).key(val).build();
			GetObjectPresignRequest popr = GetObjectPresignRequest.builder().getObjectRequest(gor).signatureDuration(Duration.ofDays(2)).build();
			PresignedGetObjectRequest pgor = s3Presigner.presignGetObject(popr);
			presignUrls.put(key, pgor.url().toString());
		});
		return presignUrls;
				
	}

    
}