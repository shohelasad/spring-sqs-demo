package com.sqs.demo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sqs.demo.model.UserDTO;

import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;

@Service
public class SqsMessageListener {
	private final Logger logger = LoggerFactory.getLogger(SqsMessageListener.class);
	
	@SqsListener(value = "${queues.testQueue.name}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	public void receiveMessage(final String message) {
		logger.info("Received message [{}]", message);
	}
	
	@SqsListener(value = "${queues.userQueue.name}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	public void receiveUserMessage(final UserDTO user) {
		logger.info("Received message [{}]", user);
	}

}
