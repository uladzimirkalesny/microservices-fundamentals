package com.epam.resourceprocessor;

import com.epam.resourceprocessor.service.ResourceMetadataParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.nio.file.Files;

@Slf4j

@SpringBootApplication
public class ResourceProcessorApplication {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(ResourceProcessorApplication.class, args);

        log.info("Start to obtain path to resource file : {}", args[0]);
        File file = new File(args[0]);

        log.info("Prepare to retrieve bytes from resource : {}", file.getName());
        byte[] resource = Files.readAllBytes(file.toPath());

        var service = context.getBean(ResourceMetadataParserService.class);
        var resourceMetadata = service.extractResourceMetadata(resource);

        log.info("ResourceMetadata : {}", resourceMetadata);
    }
}
