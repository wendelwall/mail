package com.yowits.mail.constan.dto;

import java.util.HashMap;
import java.util.Map;

import com.yowits.mail.component.mail.service.MailProperties;


public class Constans {
	
	public static final String PROPERTIES_KEY = "email-properties-key";
	
	public static final Map<String, MailProperties> PROPERTIES_MAP = new HashMap<>();
	
	public static ThreadLocal<String> mailSendFlag = new ThreadLocal<>();
	
	public static final String KFK_CONTEXT_PRODUCER_KEY = "kafka-context-producer-key";
	
	public static final String KFK_CONTEXT_CONSUMER_CONNERTOR = "kafka-context-consumer-connector";
}
