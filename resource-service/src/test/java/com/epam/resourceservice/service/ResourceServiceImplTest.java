package com.epam.resourceservice.service;

import com.epam.resourceservice.entity.Resource;
import com.epam.resourceservice.repository.ResourceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {ResourceServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ResourceServiceImplTest {

    @Autowired
    private ResourceServiceImpl resourceServiceImpl;

    @MockBean
    private ResourceRepository resourceRepository;

    @Test
    @DisplayName("Test save resource method should return an instance after creating")
    void testSaveResource_success() {
        var resource = new Resource();
        resource.setId(1);
        resource.setResourceKey("Resource Key");
        resource.setResourceLocation("Resource Location");

        when(resourceRepository.save(any(Resource.class))).thenReturn(resource);

        assertNotNull(resourceServiceImpl.saveResource(resource));
        verify(resourceRepository).save(any(Resource.class));
    }

    @Test
    @DisplayName("Test find resource by id method should return a resource after searching")
    void testFindResourceById() {
        var resource = new Resource();
        resource.setId(1);
        resource.setResourceKey("Resource Key");
        resource.setResourceLocation("Resource Location");

        var result = Optional.of(resource);

        when(resourceRepository.findById(any(Integer.class))).thenReturn(result);
        assertSame(resource, resourceServiceImpl.findResourceById(1));
        verify(resourceRepository).findById(any(Integer.class));
    }

    @Test
    @DisplayName("Test delete resource by id method should return resource key after deleting")
    void testDeleteResourceById_success() {
        var resource = new Resource();
        resource.setId(1);
        resource.setResourceKey("Resource Key");
        resource.setResourceLocation("Resource Location");

        var result = Optional.of(resource);
        doNothing().when(resourceRepository).deleteById(any(Integer.class));
        when(resourceRepository.findById(any(Integer.class))).thenReturn(result);

        assertEquals("Resource Key", resourceServiceImpl.deleteResourceById(1));
        verify(resourceRepository).findById(any(Integer.class));
        verify(resourceRepository).deleteById(any(Integer.class));
    }

    @Test
    @DisplayName("Test delete resource by id method should throws an Exception after the resource not found")
    void testDeleteResourceById_throwAnException_whenNotFound() {
        var resource = new Resource();
        resource.setId(1);
        resource.setResourceKey("Resource Key");
        resource.setResourceLocation("Resource Location");

        var result = Optional.of(resource);

        doThrow(new EntityNotFoundException("An error occurred")).when(resourceRepository).deleteById(any(Integer.class));

        when(resourceRepository.findById(any(Integer.class))).thenReturn(result);
        assertThrows(EntityNotFoundException.class, () -> resourceServiceImpl.deleteResourceById(1));

        verify(resourceRepository).findById(any(Integer.class));
        verify(resourceRepository).deleteById(any(Integer.class));
    }
}

