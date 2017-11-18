package com.github.marblejenka.ratebridge.impl;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.marblejenka.ratebridge.RateProducer;

@Component
public class KafkaFxRateProducer implements RateProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaFxRateProducer.class);

	@Autowired
	private KafkaFxRateProducerConfig config;

	private Producer<String, String> producer;

	public KafkaFxRateProducer() {
	}

	@PostConstruct
	public void wiring() {
		LOGGER.info(config.toProperties().toString());
		producer = new KafkaProducer<String, String>(config.toProperties());
	}

	public void sendRate(String currencyPair, String time, String bid, String ask) {
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(currencyPair.replace("_", ""),
				time + "|" + bid + "|" + ask + "|");
		LOGGER.debug(record.toString());

		producer.send(record, new Callback() {
			public void onCompletion(RecordMetadata metadata, Exception e) {
				if (e != null) {
					e.printStackTrace();
				} else {
					LOGGER.debug("The offset of the record we just sent is: " + metadata.offset());
				}
			}
		});
	}

	public void sendError(String cause) {
		ProducerRecord<String, String> record = new ProducerRecord<String, String>("errors", cause);
		LOGGER.debug(record.toString());
		producer.send(record);
	}

	@Override
	public void close() {
		if (producer != null) {
			producer.close();
		}
	}
}
