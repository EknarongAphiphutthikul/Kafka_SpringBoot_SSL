package com.example.demo.manual.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
	
	@PostConstruct
	public void test() throws Exception {
		KafkaTemplate<String, String> kafkatemplate = cretaeKafkaTemplate();
		kafkatemplate.send("topic2", "test2");
	}

	private ProducerFactory<String, String> producerFactory() throws Exception {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "broker1.dev.kafka:9092");
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.ACKS_CONFIG, "all");
		configProps.put(ProducerConfig.CLIENT_ID_CONFIG, "demo-producer");
		configProps.put("ssl.truststore.location", "D:\\My_Work\\Git\\Kafka_SpringBoot\\cert\\kafkaclient.truststore.jks");
		configProps.put("ssl.truststore.password", "changeit");
		configProps.put("security.protocol", "SASL_SSL");
		configProps.put("sasl.mechanism", "PLAIN");
		String saslJaasConfig = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"kafkausr\" password=\"P@ssw0rd\";";
		configProps.put("sasl.jaas.config", saslJaasConfig);
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	private KafkaTemplate<String, String> cretaeKafkaTemplate() throws Exception {
		return new KafkaTemplate<>(producerFactory());
	}
}
