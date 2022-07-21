package com.epam.resourceservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest({"server.port:0", "eureka.client.enabled:false"})
@TestPropertySource(locations = "classpath:application-test.properties")
class ResourceServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
