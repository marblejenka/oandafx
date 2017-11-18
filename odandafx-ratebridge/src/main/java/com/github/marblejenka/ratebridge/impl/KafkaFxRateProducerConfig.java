package com.github.marblejenka.ratebridge.impl;

import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaFxRateProducerConfig {

    private String bootstrapServers;
    private int retries;
    private String acks;
    private String compressionType;
    private int batchSize;

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public String getAcks() {
        return acks;
    }

    public void setAcks(String acks) {
        this.acks = acks;
    }

    public String getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(String compressionType) {
        this.compressionType = compressionType;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    public String toString() {
        return "KafkaFxRateProducerConfig [bootstrapServers=" + bootstrapServers + ", retries=" + retries + ", acks=" + acks + ", compressionType=" + compressionType + ", batchSize=" + batchSize
                + "]";
    }

    public Properties toProperties() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, this.getClass().getSimpleName());
        properties.put(ProducerConfig.RETRIES_CONFIG, this.retries);
        properties.put(ProducerConfig.ACKS_CONFIG, this.acks);
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, this.compressionType);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, this.batchSize);
        // TODO プロパティから入れる
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 50);
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 5000);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        return properties;
    }
}
