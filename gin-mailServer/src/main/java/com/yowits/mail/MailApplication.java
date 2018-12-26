package com.yowits.mail;

import com.yowits.mail.component.jmbc.connection.ConnectionPoolManager;
import com.yowits.mail.component.mail.service.MailProperties;
import com.yowits.mail.component.mail.service.MailerTemplate;
import com.yowits.mail.component.operation.service.OperationRecordService;
import com.yowits.mail.configuration.MailAccountConfiguration;
import com.yowits.mail.constan.dto.Constans;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author jian.liu
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = {JpaRepositoriesAutoConfiguration.class})
@EnableEurekaClient
@ComponentScan(basePackages="com.yowits.mail")
@EnableHystrix
@EnableCircuitBreaker
@Slf4j
public class MailApplication implements CommandLineRunner{

	private @Autowired MailProperties properties;
	private @Autowired MailAccountConfiguration config;
//	private @Autowired KafkaContext context;
//	private @Autowired TopicSubscriberFactoryBean subScriber;
    private @Autowired MailerTemplate template;
    private @Autowired OperationRecordService recordService;	
//    private @Autowired MailListener mailListener;
    
    public static void main(String[] args) {
        SpringApplication.run(MailApplication.class);
		log.info("mailServer v3 start OK");
    }

	@Override
	public void run(String... args) throws Exception {
		Constans.PROPERTIES_MAP.put(Constans.PROPERTIES_KEY, this.properties);
		ConnectionPoolManager.getInstance().init(config.getAccount());
//		mailListener.setRecordService(recordService);
//		mailListener.setTemplate(template);
//		context.setListeners(Arrays.asList(mailListener));
//		subScriber.init();
	}

}
