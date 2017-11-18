package com.github.marblejenka.ratebridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RateBridgeStremingRunner {
	private static final Logger logger = LoggerFactory.getLogger(RateBridgeStremingRunner.class);

	private int count;

	@Autowired
	private RateStraming rateStreaming;

	@Scheduled(fixedDelay = 1000)
	public void start() {
		count = 0;
		logger.info("scheduled task started");
		startWithRetry();
	}

	@Retryable(value = RuntimeException.class, backoff = @Backoff(delay = 200))
	public void startWithRetry() {
		++count;
		logger.info("Starting streaming from Oanda {}", count);
		rateStreaming.startStream();
		logger.info("Ending streaming from Oanda");
	}

}
