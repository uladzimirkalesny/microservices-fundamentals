package com.epam.resourceservice.service.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CopyObjectRequest;
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

    private final AmazonS3 amazonS3Client;

    public ResourceInfo uploadResourceToS3(byte[] resource, String bucketName) {
        var key = UUID.randomUUID().toString();
        log.info("Generate uuid for binary resource: {}, that will be identify resource into cloud storage", key);

        var metadata = new ObjectMetadata();
        metadata.setContentLength(resource.length);
        metadata.setContentType("audio/mpeg");

        amazonS3Client.putObject(new PutObjectRequest(bucketName, key, new ByteArrayInputStream(resource), metadata));
        return new ResourceInfo(key, amazonS3Client.getUrl(bucketName, key));
    }

    @Override
    public byte[] downloadResourceFromS3(String key, String bucketName) {
        log.info("Obtain the binary resource by id = {} from cloud storage, the target resource will be as an array of byte", key);
        try {
            return amazonS3Client.getObject(new GetObjectRequest(bucketName, key)).getObjectContent().readAllBytes();
        } catch (Exception e) {
            throw new ResourceServiceException("Can not able to download resource from cloud provider's storage");
        }
    }

    @Override
    public byte[] downloadRangedResourceFromS3(String key, String bucketName, long startRange, long endRange) {
        log.info("Obtain the binary resource by id = {} from cloud storage, the target resource will be as an array of byte", key);
        try {
            var request = new GetObjectRequest(bucketName, key);
            request.withRange(startRange, endRange);
            return IOUtils.toByteArray(amazonS3Client.getObject(request).getObjectContent());
        } catch (Exception e) {
            throw new ResourceServiceException("Can not able to download resource from cloud provider's storage");
        }
    }

    @Override
    public void deleteResourceFromS3(String key, String bucketName) {
        log.info("Delete the binary resource by id = {} from cloud storage", key);
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, key));
    }

    @Override
    public ResourceInfo transferResourceToPermanentStorage(String key, String fromBucket, String toBucket) {
        amazonS3Client.copyObject(new CopyObjectRequest(fromBucket, key, toBucket, key));
        amazonS3Client.deleteObject(new DeleteObjectRequest(fromBucket, key));
        return new ResourceInfo(key, amazonS3Client.getUrl(toBucket, key));
    }

}
