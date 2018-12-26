package com.yowits.mail.configuration;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "spring.mail")
@PropertySource("classpath:mail-slave.properties")
public class MailAccountConfiguration {
	
	private Map<String, String> account = new LinkedHashMap<>();

	public void setAccount(Map<String, String> account) {
		this.account = account;
	}

	public Map<String, String> getAccount() {
		return this.account;
	}

}
