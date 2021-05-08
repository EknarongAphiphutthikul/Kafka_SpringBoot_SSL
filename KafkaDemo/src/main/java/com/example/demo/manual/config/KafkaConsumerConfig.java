package com.example.demo.manual.config;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public abstract class KafkaConsumerConfig {
	
	protected KafkaConsumer<String, String> consumerFactory() throws Exception {
		Properties config = new Properties();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "broker1.dev.kafka:9092");
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // earliest or latest
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "demo");
		config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 20);
		config.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);
		config.put(ConsumerConfig.CLIENT_ID_CONFIG, "demo-consumer");
		config.put("ssl.truststore.location", "D:\\My_Work\\Git\\Kafka_SpringBoot\\cert\\kafkaclient.truststore.jks");
		config.put("ssl.truststore.password", "changeit");
		config.put("security.protocol", "SASL_SSL");
		config.put("sasl.mechanism", "PLAIN");
		String saslJaasConfig = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"kafkausr\" password=\"P@ssw0rd\";";
		config.put("sasl.jaas.config", saslJaasConfig);
		return new KafkaConsumer<>(config);
	}
}
