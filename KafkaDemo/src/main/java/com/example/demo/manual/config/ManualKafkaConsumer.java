package com.example.demo.manual.config;

import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;

@Component
public class ManualKafkaConsumer extends KafkaConsumerConfig {
	
	private Long consumerPoll;
	private int consumerThread;
	private String topic;
	private ExecutorService executor;
	private final CountDownLatch latch;
	private ForkJoinPool forkjoinPool;
	private KafkaConsumer<String, String> consumer;
	
	public ManualKafkaConsumer() {
		latch = new CountDownLatch(1);
	}

	@PostConstruct
	public void start() throws Exception {
		prepareConfig();
		startListening();
	}
	
	private void prepareConfig() throws Exception {
		consumerPoll = 1800L;
		consumerThread = 20;
		topic = "topic2";
		executor = Executors.newSingleThreadExecutor();
		forkjoinPool = new ForkJoinPool(consumerThread);
		consumer = consumerFactory();
	}
	
	private void startListening() {
		executor.submit(getListenTask());
	}
	
	private Runnable getListenTask() {
		return () -> {
			consumer.subscribe(Collections.singletonList(topic));
			try {
				while (true) {
					pollRecords();
				}
			} catch (WakeupException ex) {
				ex.printStackTrace();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			} catch (ExecutionException ex) {
				ex.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				consumer.close();
				latch.countDown();
			}
		};
	}
	
	private void pollRecords() throws InterruptedException, ExecutionException {
		ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(consumerPoll));
		if (records.count() != 0) {
			consumer.commitSync();
			Iterator<ConsumerRecord<String, String>> iterator = records.iterator();
			List<ConsumerRecord<String, String>>  listJob2 = new LinkedList<>();
			while (iterator.hasNext()) {
				ConsumerRecord<String, String> record = iterator.next();
				listJob2.add(record);
			}
			
			forkjoinPool.submit(() -> listJob2.parallelStream()
			        .forEach((record) ->
			        {
			        	process(record.value());
			        })).get();
		}		
	}
	
	private void process(String message) {
		System.out.println(message);
	}
	
	@PreDestroy
	private void addShutDownHook() {
		stopConsumer();
		closeExecutor();
	}

	public void stopConsumer() {
		consumer.wakeup();
		try {
			latch.await();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	public void closeExecutor() {
		executor.shutdown();
		try {
			if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
				executor.shutdownNow();
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			executor.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}
}
