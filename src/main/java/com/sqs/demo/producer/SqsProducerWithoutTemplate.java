package com.sqs.demo.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqs.demo.model.UserDTO;

import io.awspring.cloud.messaging.core.QueueMessageChannel;

@Service
public class SqsProducerWithoutTemplate {
	private final Logger logger = LoggerFactory.getLogger(SqsProducerWithoutTemplate.class);
	
	@Value("${queues.userQueue.arn}")
	private String queueArn;
	
	private AmazonSQSAsync amazonSQSAsync;
	private ObjectMapper mapper;
	
	@Autowired
	public SqsProducerWithoutTemplate(AmazonSQSAsync amazonSQSAsync) {
		this.amazonSQSAsync = amazonSQSAsync;
		this.mapper = new ObjectMapper();
	}
	
	public boolean sendMessage(UserDTO user) {
		String payload = "";
		try {
			payload = mapper.writeValueAsString(user);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		
		MessageChannel messageChannel = new QueueMessageChannel(amazonSQSAsync, queueArn);
		Message<String> msg = MessageBuilder.withPayload(payload).build();
		return messageChannel.send(msg);
	}
}
