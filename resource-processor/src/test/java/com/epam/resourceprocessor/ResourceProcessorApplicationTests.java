package com.epam.resourceprocessor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest({"server.port:0", "eureka.client.enabled:false"})
class ResourceProcessorApplicationTests {

	@Test
	void contextLoads() {
	}

}
