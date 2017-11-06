package com.jee.jboss.playground.transactions.container.managed;

public class Status {

    private static final String[] values = {
               "STATUS_ACTIVE",
               "STATUS_MARKED_ROLLBACK",
               "STATUS_PREPARED",
               "STATUS_COMMITTED",
               "STATUS_ROLLEDBACK",
               "STATUS_UNKNOWN",
               "STATUS_NO_TRANSACTION",
               "STATUS_PREPARING",
               "STATUS_COMMITTING",
               "STATUS_ROLLING_BACK"};

    public static String toString(final int value){
        return values[value];
    }
}