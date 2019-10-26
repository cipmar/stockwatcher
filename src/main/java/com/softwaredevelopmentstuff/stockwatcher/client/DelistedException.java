package com.softwaredevelopmentstuff.stockwatcher.client;

import java.io.Serializable;

public class DelistedException extends Exception implements Serializable {
    private String symbol;

    private DelistedException() {

    }

    public DelistedException(String symbol) {
        this.symbol = symbol;
    }

    String getSymbol() {
        return symbol;
    }
}
