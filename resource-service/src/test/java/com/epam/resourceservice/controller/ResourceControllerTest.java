package com.epam.resourceservice.controller;

import com.epam.resourceservice.facade.ResourceFacade;
import com.epam.resourceservice.utils.RequestParamMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {ResourceController.class})
@ExtendWith(SpringExtension.class)
class ResourceControllerTest {
    @MockBean
    private RequestParamMapper requestParamMapper;

    @Autowired
    private ResourceController resourceController;

    @MockBean
    private ResourceFacade resourceFacade;

    @Test
    void testDeleteResource() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/v1/resources").param("id", "foo");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(resourceController)
                .build()
                .perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(405));
    }

    @Test
    void testGetResource() throws Exception {
        when(resourceFacade.findResourceById((Integer) any())).thenReturn("A".getBytes(StandardCharsets.UTF_8));

        var requestBuilder = MockMvcRequestBuilders.get("/v1/resources/{id}", 1);

        MockMvcBuilders.standaloneSetup(resourceController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("audio/mpeg"))
                .andExpect(MockMvcResultMatchers.content().string("A"));
    }

    @Test
    void testGetResourceRanged() throws Exception {
        when(resourceFacade.findRangedResourceById((Integer) any(), anyLong(), anyLong()))
                .thenReturn("A".getBytes(StandardCharsets.UTF_8));

        when(resourceFacade.findResourceById((Integer) any())).thenReturn("A".getBytes(StandardCharsets.UTF_8));

        var requestBuilder = MockMvcRequestBuilders.get("/v1/resources/{id}", 1)
                .header("Range", "bytes=500-999");

        var actual = MockMvcBuilders.standaloneSetup(resourceController)
                .build()
                .perform(requestBuilder);

        actual.andExpect(MockMvcResultMatchers.status().is(206))
                .andExpect(MockMvcResultMatchers.content().contentType("audio/mpeg"))
                .andExpect(MockMvcResultMatchers.content().string("A"));
    }

    @Test
    void testUploadResource() throws Exception {
        var contentTypeResult = MockMvcRequestBuilders.get("/v1/resources")
                .contentType(MediaType.APPLICATION_JSON);

        var requestBuilder = contentTypeResult
                .content((new ObjectMapper()).writeValueAsString(new byte[]{'A'}));

        var actual = MockMvcBuilders.standaloneSetup(resourceController)
                .build()
                .perform(requestBuilder);

        actual.andExpect(MockMvcResultMatchers.status().is(405));
    }

}

