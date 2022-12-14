package com.epam.resourceservice.service.storage;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.apache.http.client.methods.HttpDelete;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {AwsSimpleStorageService.class})
@ExtendWith(SpringExtension.class)
class AwsSimpleStorageServiceTest {
    @MockBean
    private AmazonS3 amazonS3;

    @Autowired
    @Qualifier("awsSimpleStorageService")
    private CloudProviderSimpleStorageService service;

    @Test
    void testUploadResourceToS3() throws Exception {
        var putObjectResult = new PutObjectResult();
        when(amazonS3.putObject(any(PutObjectRequest.class))).thenReturn(putObjectResult);
        when(amazonS3.getUrl(anyString(), anyString()))
                .thenReturn(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL());

        var resourceInfo = service.uploadResourceToS3("A".getBytes(StandardCharsets.UTF_8), "Bucket");

        assertNotNull(resourceInfo);

        verify(amazonS3).putObject(any(PutObjectRequest.class));
        verify(amazonS3).getUrl(anyString(), anyString());
    }

    @Test
    void testDownloadResourceFromS3() {
        var s3Object = new S3Object();
        var in = new ByteArrayInputStream("A".getBytes(StandardCharsets.UTF_8));

        s3Object.setObjectContent(new S3ObjectInputStream(in, new HttpDelete()));
        when(amazonS3.getObject(any(GetObjectRequest.class))).thenReturn(s3Object);

        byte[] actual = service.downloadResourceFromS3("Key", "Bucket");

        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(1, actual.length);
            assertEquals('A', actual[0]);
        });

        verify(amazonS3).getObject(any(GetObjectRequest.class));
    }

    @Test
    void testDownloadRangedResourceFromS33() {
        var s3Object = mock(S3Object.class);
        var in = new ByteArrayInputStream("A".getBytes(StandardCharsets.UTF_8));

        when(s3Object.getObjectContent()).thenReturn(new S3ObjectInputStream(in, new HttpDelete()));
        when(amazonS3.getObject(any(GetObjectRequest.class))).thenReturn(s3Object);

        var actual = service.downloadRangedResourceFromS3("Key", "Bucket", 1L, 1L);

        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(1, actual.length);
        });

        verify(amazonS3).getObject((GetObjectRequest) any());
        verify(s3Object).getObjectContent();
    }

    @Test
    void testDeleteResourceFromS3() throws SdkClientException {
        doNothing().when(amazonS3).deleteObject(any(DeleteObjectRequest.class));
        service.deleteResourceFromS3("Key", "Bucket");
        verify(amazonS3).deleteObject(any(DeleteObjectRequest.class));
    }
}

