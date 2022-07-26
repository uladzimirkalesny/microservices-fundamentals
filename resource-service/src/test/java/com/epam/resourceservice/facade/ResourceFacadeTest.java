package com.epam.resourceservice.facade;

import com.epam.resourceservice.entity.Resource;
import com.epam.resourceservice.model.Storage;
import com.epam.resourceservice.service.ResourceService;
import com.epam.resourceservice.service.storage.CloudProviderSimpleStorageService;
import com.epam.resourceservice.service.storage.StorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @MockBean
    private StorageService storageService;

    @Test
    void testDeleteAllResourcesByIds2() {
        doNothing().when(cloudProviderSimpleStorageService).deleteResourceFromS3((String) any(), (String) any());

        Resource resource = new Resource();
        resource.setId(1);
        resource.setResourceKey("Resource Key");
        resource.setResourceLocation("Resource Location");
        resource.setStorageId(123);
        when(resourceService.deleteResourceById((Integer) any())).thenReturn("42");
        when(resourceService.findResourceById((Integer) any())).thenReturn(resource);
        when(storageService.obtainStorageById((Integer) any())).thenReturn(new Storage());

        ArrayList<Integer> integerList = new ArrayList<>();
        integerList.add(2);
        resourceFacade.deleteAllResourcesByIds(integerList);
        verify(cloudProviderSimpleStorageService).deleteResourceFromS3((String) any(), (String) any());
        verify(resourceService).findResourceById((Integer) any());
        verify(resourceService).deleteResourceById((Integer) any());
        verify(storageService).obtainStorageById((Integer) any());
    }

    @Test
    void testDeleteAllResourcesByIds4() {
        doNothing().when(cloudProviderSimpleStorageService).deleteResourceFromS3((String) any(), (String) any());

        Resource resource = new Resource();
        resource.setId(1);
        resource.setResourceKey("Resource Key");
        resource.setResourceLocation("Resource Location");
        resource.setStorageId(123);
        when(resourceService.deleteResourceById((Integer) any())).thenReturn("42");
        when(resourceService.findResourceById((Integer) any())).thenReturn(resource);
        when(storageService.obtainStorageById((Integer) any())).thenReturn(new Storage());

        ArrayList<Integer> integerList = new ArrayList<>();
        integerList.add(2);
        integerList.add(2);
        resourceFacade.deleteAllResourcesByIds(integerList);
        verify(cloudProviderSimpleStorageService, atLeast(1)).deleteResourceFromS3((String) any(), (String) any());
        verify(resourceService, atLeast(1)).findResourceById((Integer) any());
        verify(resourceService, atLeast(1)).deleteResourceById((Integer) any());
        verify(storageService, atLeast(1)).obtainStorageById((Integer) any());
    }
}

