package com.sqs.demo.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.awspring.cloud.messaging.config.QueueMessageHandlerFactory;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;

@Configuration
public class AwsSqsConfig {
		
	@Value(value="${aws.sqs.accessKey}")
	private String accessKey;
	
	@Value(value="${aws.sqs.secretKey}")
	private String secretKey;
	
	@Value(value="${cloud.aws.region.static}")
	private String region;
	
	@Bean
	@Primary
	public AmazonSQSAsync amazonSQSAsync() {
		return AmazonSQSAsyncClientBuilder
				.standard()
				.withRegion(region)
				.build();
	}
	
	public AWSCredentialsProvider credentialsProvider() {
		return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
	}
	
	@Bean
	@DependsOn("amazonSQSAsync")
	public QueueMessagingTemplate messageTemplate(AmazonSQSAsync amazonSQSAsync) {
		return new QueueMessagingTemplate(amazonSQSAsync);
	}
	
	@Bean
	@DependsOn("amazonSQSAsync")
	public QueueMessageHandlerFactory queueMessageHandlerFactory(final ObjectMapper mapper,
			final AmazonSQSAsync amazonSQSAsync) {
		final QueueMessageHandlerFactory queueHandlerFactory = new QueueMessageHandlerFactory();
		queueHandlerFactory.setAmazonSqs(amazonSQSAsync);

		queueHandlerFactory.setArgumentResolvers(
				Collections.singletonList(new PayloadMethodArgumentResolver(jackson2MessageConverter(mapper))));
		return queueHandlerFactory;
	}

	private MessageConverter jackson2MessageConverter(final ObjectMapper mapper) {

		final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();

		// set strict content type match to false to enable the listener to handle AWS events
		converter.setStrictContentTypeMatch(false);
		converter.setObjectMapper(mapper);
		return converter;
	}

}
