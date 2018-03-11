package com.jee.jboss.playground.transactions.util;

import java.util.LinkedList;

public class TransactionUtil {

    public static String getTransactionId(final int index, final LinkedList<String> transactions){
        // Format: className : methodName : transactionId
        try {
            String transaction = transactions.get(index);
            String transactionId = transaction.substring(transaction.lastIndexOf(':') + 2, transaction.length());
            return transactionId;
        }catch (Exception e){
            return "null";
        }
    }
}
