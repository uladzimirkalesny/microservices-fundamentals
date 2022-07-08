package com.epam.resourceservice.repository;

import com.epam.resourceservice.entity.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ResourceRepositoryTest {
    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("Check the context with in-memory database")
    void check_contextStarts() {
        assertAll(
                () -> assertNotNull(resourceRepository),
                () -> assertNotNull(testEntityManager)
        );
    }

    @Test
    @DisplayName("Check find all resources method with in-memory database")
    void findAll_resources() {
        var resource = getResource();

        var persistedResource = testEntityManager.persist(resource);
        var actual = resourceRepository.findById(persistedResource.getId()).get();

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(persistedResource.getId(), actual.getId())
        );
    }

    @Test
    @DisplayName("Check save resource method with in-memory database")
    void save_resource() {
        var resource = getResource();

        var persistedResource = testEntityManager.persist(resource);
        var savedResource = resourceRepository.save(resource);

        assertAll(
                () -> assertNotNull(savedResource),
                () -> assertEquals(persistedResource.getId(), savedResource.getId())
        );
    }

    @Test
    @DisplayName("Check delete resource method with in-memory database")
    void delete_resource() {
        var resource = getResource();

        var persistedResource = testEntityManager.persist(resource);
        resourceRepository.delete(resource);

        var actual =
                StreamSupport.stream(resourceRepository.findAll().spliterator(), false)
                        .collect(Collectors.toList());

        assertTrue(actual.isEmpty());
    }

    private Resource getResource() {
        return new Resource("key1", "location1");
    }
}
