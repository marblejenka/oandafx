package com.github.marblejenka.ratebridge;

public interface RateProducer extends Cloneable{
    public void sendRate(String currencyPair, String time, String bid, String ask);
    public void sendError(String cause);
    public void close();
}
