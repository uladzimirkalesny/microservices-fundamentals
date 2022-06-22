package com.epam.resourceservice.service.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.epam.resourceservice.exception.ResourceServiceException;
import com.epam.resourceservice.model.ResourceInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j

@Service("awsSimpleStorageService")
public class AwsSimpleStorageService implements CloudProviderSimpleStorageService {

    private static final String BUCKET_NAME = "microservices-fundamentals";

    private final AmazonS3 amazonS3Client;

    public ResourceInfo uploadResourceToS3(byte[] resource) {
        var key = UUID.randomUUID().toString();
        log.info("Generate uuid for binary resource: {}, that will be identify resource into cloud storage", key);

        var metadata = new ObjectMetadata();
        metadata.setContentLength(resource.length);

        amazonS3Client.putObject(new PutObjectRequest(BUCKET_NAME, key, new ByteArrayInputStream(resource), metadata));
        return new ResourceInfo(key);
    }

    @Override
    public byte[] downloadResourceFromS3(String key) {
        log.info("Obtain the binary resource by id = {} from cloud storage, the target resource will be as an array of byte", key);
        try {
            return amazonS3Client.getObject(new GetObjectRequest(BUCKET_NAME, key)).getObjectContent().readAllBytes();
        } catch (Exception e) {
            throw new ResourceServiceException("Can not able to download resource from cloud provider's storage");
        }
    }

    @Override
    public byte[] downloadRangedResourceFromS3(String key, long startRange, long endRange) {
        log.info("Obtain the binary resource by id = {} from cloud storage, the target resource will be as an array of byte", key);
        try {
            var request = new GetObjectRequest(BUCKET_NAME, key);
            request.withRange(startRange, endRange);
            return IOUtils.toByteArray(amazonS3Client.getObject(request).getObjectContent());
        } catch (Exception e) {
            throw new ResourceServiceException("Can not able to download resource from cloud provider's storage");
        }
    }

    @Override
    public void deleteResourceFromS3(String key) {
        log.info("Delete the binary resource by id = {} from cloud storage", key);
        amazonS3Client.deleteObject(new DeleteObjectRequest(BUCKET_NAME, key));
    }

}
