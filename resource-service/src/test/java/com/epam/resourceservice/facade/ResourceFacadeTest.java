package com.epam.resourceservice.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.resourceservice.entity.Resource;
import com.epam.resourceservice.model.ResourceInfo;
import com.epam.resourceservice.service.ResourceService;
import com.epam.resourceservice.service.storage.CloudProviderSimpleStorageService;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ResourceFacade.class})
@ExtendWith(SpringExtension.class)
@PropertySource("classpath:application-test.properties")
@EnableConfigurationProperties
class ResourceFacadeTest {
    @MockBean
    private CloudProviderSimpleStorageService cloudProviderSimpleStorageService;

    @MockBean
    private FanoutExchange fanoutExchange;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ResourceFacade resourceFacade;

    @MockBean
    private ResourceService resourceService;

    @Test
    void testSaveResource() throws UnsupportedEncodingException, MalformedURLException, AmqpException {
        when(cloudProviderSimpleStorageService.uploadResourceToS3((byte[]) any())).thenReturn(
                new ResourceInfo("Key", Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL()));

        Resource resource = new Resource();
        resource.setId(1);
        resource.setResourceKey("Resource Key");
        resource.setResourceLocation("Resource Location");
        when(resourceService.saveResource((Resource) any())).thenReturn(resource);
        doNothing().when(rabbitTemplate).convertAndSend((String) any(), (Object) any());
        when(fanoutExchange.getName()).thenReturn("Name");
        assertSame(resource, resourceFacade.saveResource("AAAAAAAA".getBytes("UTF-8")));
        verify(cloudProviderSimpleStorageService).uploadResourceToS3((byte[]) any());
        verify(resourceService).saveResource((Resource) any());
        verify(rabbitTemplate).convertAndSend((String) any(), (Object) any());
        verify(fanoutExchange).getName();
    }

    @Test
    void testFindResourceById() throws UnsupportedEncodingException {
        when(cloudProviderSimpleStorageService.downloadResourceFromS3((String) any()))
                .thenReturn("AAAAAAAA".getBytes("UTF-8"));

        Resource resource = new Resource();
        resource.setId(1);
        resource.setResourceKey("Resource Key");
        resource.setResourceLocation("Resource Location");
        when(resourceService.findResourceById((Integer) any())).thenReturn(resource);
        byte[] actualFindResourceByIdResult = resourceFacade.findResourceById(1);
        assertEquals(8, actualFindResourceByIdResult.length);
        assertEquals('A', actualFindResourceByIdResult[0]);
        assertEquals('A', actualFindResourceByIdResult[1]);
        assertEquals('A', actualFindResourceByIdResult[2]);
        assertEquals('A', actualFindResourceByIdResult[3]);
        assertEquals('A', actualFindResourceByIdResult[4]);
        assertEquals('A', actualFindResourceByIdResult[5]);
        assertEquals('A', actualFindResourceByIdResult[6]);
        assertEquals('A', actualFindResourceByIdResult[7]);
        verify(cloudProviderSimpleStorageService).downloadResourceFromS3((String) any());
        verify(resourceService).findResourceById((Integer) any());
    }

    @Test
    void testFindRangedResourceById() throws UnsupportedEncodingException {
        when(cloudProviderSimpleStorageService.downloadRangedResourceFromS3((String) any(), anyLong(), anyLong()))
                .thenReturn("AAAAAAAA".getBytes("UTF-8"));

        Resource resource = new Resource();
        resource.setId(1);
        resource.setResourceKey("Resource Key");
        resource.setResourceLocation("Resource Location");
        when(resourceService.findResourceById((Integer) any())).thenReturn(resource);
        byte[] actualFindRangedResourceByIdResult = resourceFacade.findRangedResourceById(1, 1L, 1L);
        assertEquals(8, actualFindRangedResourceByIdResult.length);
        assertEquals('A', actualFindRangedResourceByIdResult[0]);
        assertEquals('A', actualFindRangedResourceByIdResult[1]);
        assertEquals('A', actualFindRangedResourceByIdResult[2]);
        assertEquals('A', actualFindRangedResourceByIdResult[3]);
        assertEquals('A', actualFindRangedResourceByIdResult[4]);
        assertEquals('A', actualFindRangedResourceByIdResult[5]);
        assertEquals('A', actualFindRangedResourceByIdResult[6]);
        assertEquals('A', actualFindRangedResourceByIdResult[7]);
        verify(cloudProviderSimpleStorageService).downloadRangedResourceFromS3((String) any(), anyLong(), anyLong());
        verify(resourceService).findResourceById((Integer) any());
    }

    @Test
    void testDeleteAllResourcesByIds2() {
        doNothing().when(cloudProviderSimpleStorageService).deleteResourceFromS3((String) any());
        when(resourceService.deleteResourceById((Integer) any())).thenReturn("42");

        ArrayList<Integer> integerList = new ArrayList<>();
        integerList.add(2);
        resourceFacade.deleteAllResourcesByIds(integerList);
        verify(cloudProviderSimpleStorageService).deleteResourceFromS3((String) any());
        verify(resourceService).deleteResourceById((Integer) any());
    }

    @Test
    void testDeleteAllResourcesByIds3() {
        doNothing().when(cloudProviderSimpleStorageService).deleteResourceFromS3((String) any());
        when(resourceService.deleteResourceById((Integer) any())).thenReturn("42");

        ArrayList<Integer> integerList = new ArrayList<>();
        integerList.add(2);
        integerList.add(2);
        resourceFacade.deleteAllResourcesByIds(integerList);
        verify(cloudProviderSimpleStorageService, atLeast(1)).deleteResourceFromS3((String) any());
        verify(resourceService, atLeast(1)).deleteResourceById((Integer) any());
    }
}

