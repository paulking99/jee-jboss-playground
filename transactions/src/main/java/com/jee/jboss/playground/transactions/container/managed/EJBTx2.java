package com.jee.jboss.playground.transactions.container.managed;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.transaction.TransactionSynchronizationRegistry;
import org.slf4j.Logger;

@Stateless
public class EJBTx2 {

    @Resource
    private TransactionSynchronizationRegistry transactionSynchronizationRegistry;

    @Resource
    private EJBContext context;

    @Inject
    EJBTx3 ejbTx3;

    @Inject
    Logger logger;

    @Resource
    private TimerService timerService;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Map<String, String> doMethodWithTransactionAttributeRequired(final Map<String, String> transactions) {
        return setTransactionId(transactions);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Map<String, String> doMethodWithTransactionAttributeRequiresNew(final Map<String, String> transactions) {
        return setTransactionId(transactions);
    }

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public Map<String, String> doMethodWithTransactionAttributeMandatory(final Map<String, String> transactions) {
        return setTransactionId(transactions);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<String, String> doMethodWithTransactionAttributeNotSupported(final Map<String, String> transactions) {
        return setTransactionId(transactions);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Map<String, String> doMethodWithTransactionAttributeSupports(final Map<String, String> transactions) {
        return setTransactionId(transactions);
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    public Map<String, String> doMethodWithTransactionAttributeNever(final Map<String, String> transactions) {
        return setTransactionId(transactions);
    }

    /*
     * PRIVATE METHODS
     */

    private String getTransactionId(){
        return transactionSynchronizationRegistry.getTransactionKey() == null
                   ? "null" : transactionSynchronizationRegistry.getTransactionKey().toString();
    }

    private String getTransactionStatus(){
        return Status.toString(transactionSynchronizationRegistry.getTransactionStatus());
    }

    private Map<String, String> setTransactionId(final Map<String, String> transactions){
        transactions.put(this.getClass().getSimpleName(), getTransactionId());
        return transactions;
    }
}
