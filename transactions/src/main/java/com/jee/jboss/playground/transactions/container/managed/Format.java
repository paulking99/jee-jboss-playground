package com.jee.jboss.playground.transactions.container.managed;

public class Format {

    public static String format(final String className, final String transactionId, final int status){
        return String.format("Class: %s. Transaction ID: %s, State: %s", className, transactionId, Status.toString(status));
    }
}
