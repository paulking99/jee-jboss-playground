package com.jee.jboss.playground.transactions.container.managed;

import java.util.LinkedList;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.transaction.TransactionSynchronizationRegistry;

@Stateless
public class EJBTx2 {

    @Resource
    private TransactionSynchronizationRegistry transactionSynchronizationRegistry;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public LinkedList<String> doMethodWithTransactionAttributeRequired(final LinkedList<String> transactions) {
        return setTransactionId("doMethodWithTransactionAttributeRequired", transactions);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public LinkedList<String> doMethodWithTransactionAttributeRequiresNew(final LinkedList<String> transactions) {
        return setTransactionId("doMethodWithTransactionAttributeRequiresNew", transactions);
    }

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public LinkedList<String> doMethodWithTransactionAttributeMandatory(final LinkedList<String> transactions) {
        return setTransactionId("doMethodWithTransactionAttributeMandatory", transactions);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public LinkedList<String> doMethodWithTransactionAttributeNotSupported(final LinkedList<String> transactions) {
        return setTransactionId("doMethodWithTransactionAttributeNotSupported", transactions);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public LinkedList<String> doMethodWithTransactionAttributeSupports(final LinkedList<String> transactions) {
        return setTransactionId("doMethodWithTransactionAttributeSupports", transactions);
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    public LinkedList<String> doMethodWithTransactionAttributeNever(final LinkedList<String> transactions) {
        return setTransactionId("doMethodWithTransactionAttributeNever", transactions);
    }

    /*
     * PRIVATE METHODS
     */

    private String getTransactionId(){
        return transactionSynchronizationRegistry.getTransactionKey() == null
                   ? "null" : transactionSynchronizationRegistry.getTransactionKey().toString();
    }

    private LinkedList<String> setTransactionId(final String methodName, final LinkedList<String> transactions){
        transactions.add(String.format("%s : %s : %s", this.getClass().getSimpleName(), methodName, getTransactionId()));
        return transactions;
    }

    private String getTransactionStatus(){
        return Status.toString(transactionSynchronizationRegistry.getTransactionStatus());
    }

}
