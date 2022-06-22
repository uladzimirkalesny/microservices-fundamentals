package com.epam.resourceservice.controller;

import com.epam.resourceservice.dto.CreateResourceResponse;
import com.epam.resourceservice.dto.DeleteResourceResponse;
import com.epam.resourceservice.facade.ResourceFacade;
import com.epam.resourceservice.utils.RequestParamMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor

@RequestMapping("/v1/resources")
@RestController
public class ResourceController {

    private final ResourceFacade resourceFacade;
    private final RequestParamMapper requestParamMapper;

    @PostMapping(headers = "Content-Type=audio/mpeg")
    public ResponseEntity<CreateResourceResponse> uploadResource(@RequestBody byte[] resource) {
        log.info("Start to handle binary resource");
        var savedResource = resourceFacade.saveResource(resource);

        log.info("The binary resource has been saved into database with id = {}", savedResource.getId());
        var createResourceResponse = new CreateResourceResponse(savedResource.getId());

        return ResponseEntity.ok(createResourceResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getResource(@PathVariable(value = "id") Integer id,
                                              @RequestHeader(value = "Range", required = false) String rangeHeader) {
        log.info("Start to obtain a binary resource by id = {}", id);

        var resource = Optional.ofNullable(rangeHeader)
                .map(header -> rangeHeader.substring("bytes=".length()).split("-"))
                .map(ranges -> resourceFacade.findRangedResourceById(id, Long.parseLong(ranges[0]), Long.parseLong(ranges[1])))
                .orElseGet(() -> resourceFacade.findResourceById(id));

        log.info("Successfully obtained the binary resource = {}", resource.length);

        return Optional.ofNullable(rangeHeader)
                .map(header -> ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                        .header("Content-Type", "audio/mpeg")
                        .header("Accept-Ranges", "bytes")
                        .body(resource))
                .orElseGet(() -> ResponseEntity.ok()
                        .contentType(MediaType.asMediaType(MimeType.valueOf("audio/mpeg")))
                        .body(resource));
    }

    @DeleteMapping
    public ResponseEntity<DeleteResourceResponse> deleteResource(@RequestParam(value = "id")
                                                                 @Size(max = 200) String id) {
        var ids = requestParamMapper.mapStringIdsToListOfIntegers(id);
        log.info("Start to remove all binary resources with ids = {}", ids);
        resourceFacade.deleteAllResourcesByIds(ids);

        log.info("The binary resource with ids {} has been deleted from cloud storage and database", ids);
        return ResponseEntity.ok(new DeleteResourceResponse(ids));
    }

}
