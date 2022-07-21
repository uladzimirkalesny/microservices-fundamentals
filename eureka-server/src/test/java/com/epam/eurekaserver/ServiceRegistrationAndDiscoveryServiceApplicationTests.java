package com.epam.eurekaserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceRegistrationAndDiscoveryServiceApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate template;

    @Test
    void shouldStartEurekaServer() {
        var urlCandidate = "http://localhost:%d/eureka/apps";
        var entity = template.getForEntity(String.format(urlCandidate, port), String.class);

        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
