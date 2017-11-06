package com.jee.jboss.playground.transactions.util;

import java.util.Map;

public class TransactionUtil {

    public static String getTransactionId(final int index, final Map<String,String> transactions){
        try {
            return (String)transactions.values().toArray()[index];
        } catch(Exception e){
            //Do nothing
        }
        return "null";
    }

}
