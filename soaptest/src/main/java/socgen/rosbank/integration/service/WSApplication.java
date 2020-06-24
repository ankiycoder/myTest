package socgen.rosbank.integration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import socgen.rosbank.integration.service.test.WsClientService;

@SpringBootApplication
@ComponentScan("socgen.rosbank.integration")
@EnableScheduling
public class WSApplication {

	private final Logger log = LoggerFactory.getLogger(WSApplication.class);

	@Autowired
	private WsClientService prod;

	public static void main(String[] args) {

		SpringApplication.run(WSApplication.class, args);

	}

	@Scheduled(fixedDelay = 5000)
	public void scheduleFixedDelayTask() {
		log.info(" **** NEW ITERATION TEST 123 ****");
		prod.go();
	}
}
