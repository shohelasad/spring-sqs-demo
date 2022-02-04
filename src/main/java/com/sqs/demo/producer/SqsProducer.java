package com.sqs.demo.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqs.demo.model.UserDTO;

import io.awspring.cloud.messaging.core.QueueMessagingTemplate;

@Service
public class SqsProducer {
	
	private final Logger logger = LoggerFactory.getLogger(SqsProducer.class);
	
	@Value("${queues.userQueue.name}")
	private String queueName;
	
	private QueueMessagingTemplate messageTemplate;
	private ObjectMapper mapper;

	@Autowired
	public SqsProducer(QueueMessagingTemplate messageTemplate) {
		this.messageTemplate = messageTemplate;
		this.mapper = new ObjectMapper();
	}
	
	public void sendMessage(UserDTO user) {
		String payload = "";
		try {
			payload = mapper.writeValueAsString(user);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
			return;
		}
		logger.info("Sending message [{}]", payload);
		Message<String> msg = MessageBuilder.withPayload(payload).build();
		messageTemplate.send(queueName, msg);
		logger.info("Message sent");
	}
	
}
