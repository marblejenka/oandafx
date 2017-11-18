package com.github.marblejenka.ratebridge.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.marblejenka.ratebridge.RateProducer;
import com.github.marblejenka.ratebridge.RateStraming;

@Component
public class OandaFxRateStreaming implements RateStraming {

	private static final Logger LOGGER = LoggerFactory.getLogger(OandaFxRateStreaming.class);

	@Autowired
	private OandaFxRateStreamingConfig config;

	@Autowired
	private RateProducer producer;

	public void startStream() {
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			HttpUriRequest httpGet = new HttpGet(config.createUrl());
			httpGet.addHeader(new BasicHeader("Authorization", "Bearer " + config.getAccessToken()));
			httpGet.addHeader(new BasicHeader("X-Accept-Datetime-Format", "UNIX"));

			LOGGER.info("Executing request: " + httpGet.getRequestLine());

			HttpResponse resp = httpClient.execute(httpGet);
			HttpEntity entity = resp.getEntity();

			if (resp.getStatusLine().getStatusCode() == 200 && entity != null) {
				InputStream stream = entity.getContent();
				String line;
				BufferedReader br = new BufferedReader(new InputStreamReader(stream));

				while ((line = br.readLine()) != null) {
					Object obj = JSONValue.parse(line);
					JSONObject tick = (JSONObject) obj;
					LOGGER.debug(tick.toJSONString());

					if (tick.containsKey("tick")) {
						tick = (JSONObject) tick.get("tick");
					}

					if (tick.containsKey("instrument")) {
						String instrument = tick.get("instrument").toString();
						String time = tick.get("time").toString();
						String bid = tick.get("bid").toString();
						String ask = tick.get("ask").toString();

						producer.sendRate(instrument, time, bid, ask);
					}
				}
			} else {
				String responseString = EntityUtils.toString(entity, "UTF-8");
				LOGGER.warn(responseString);
			}

		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (producer != null) {
				producer.close();
			}
		}
	}
}
