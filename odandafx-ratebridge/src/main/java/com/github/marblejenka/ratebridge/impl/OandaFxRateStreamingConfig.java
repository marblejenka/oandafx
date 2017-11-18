package com.github.marblejenka.ratebridge.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "oandafx")
public class OandaFxRateStreamingConfig {
    private String domain;
    private String accessToken;
    private String accountId;
    private String instruments;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getInstruments() {
        return instruments;
    }

    public void setInstruments(String instruments) {
        this.instruments = instruments;
    }

    @Override
    public String toString() {
        return "OandaFxRateStreamingConfig [domain=" + domain + ", accessToken=" + accessToken + ", accountId=" + accountId + ", instruments=" + instruments + "]";
    }

    public String createUrl() {
        return domain + "/v1/prices?accountId=" + accountId + "&instruments=" + instruments;
    }

}
