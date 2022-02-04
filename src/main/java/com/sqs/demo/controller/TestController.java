package com.sqs.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sqs.demo.model.UserDTO;
import com.sqs.demo.producer.SqsProducer;
import com.sqs.demo.producer.SqsProducerWithoutTemplate;

@RestController
@SuppressWarnings("rawtypes")
public class TestController {
	
	private SqsProducer sqsProducer;
	private SqsProducerWithoutTemplate sqsProducerWithoutTemplate;
	
	@Autowired
	public TestController(SqsProducer sqsProducer, SqsProducerWithoutTemplate sqsProducerWithoutTemplate) {
		this.sqsProducer = sqsProducer;
		this.sqsProducerWithoutTemplate = sqsProducerWithoutTemplate;
	}

	@PostMapping("/sendMessage")
	public ResponseEntity sendMessage(@RequestBody UserDTO user) {
		sqsProducerWithoutTemplate.sendMessage(user);
		return ResponseEntity.ok("Message sent successfully");
	}

	@PostMapping("/sendMessageWithTemplate")
	public ResponseEntity sendMessageWithTemplate(@RequestBody UserDTO user) {
		sqsProducer.sendMessage(user);
		return ResponseEntity.ok("Message sent successfully");
	}
}
