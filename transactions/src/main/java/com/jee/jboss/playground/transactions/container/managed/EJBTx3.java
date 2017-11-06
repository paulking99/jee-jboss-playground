package com.jee.jboss.playground.transactions.container.managed;

import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.transaction.TransactionSynchronizationRegistry;
import org.slf4j.Logger;

@Stateless
public class EJBTx3 {

    @Resource
    private TransactionSynchronizationRegistry transactionSynchronizationRegistry;

    @Inject
    Logger logger;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<String> doMethodWithRequired(final List<String> transactionList) {
        transactionList.add(this.getClass().getSimpleName() + " " + getTransactionId());
        return transactionList;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<String> doMethodWithRequiresNew(final List<String> transactionList) {
        transactionList.add(this.getClass().getSimpleName() + " " + getTransactionId());
        return transactionList;
    }

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public List<String> doMethodWithMandatory(final List<String> transactionList) {
        transactionList.add(this.getClass().getSimpleName() + " " + getTransactionId());
        return transactionList;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<String> doMethodWithNotSupported(final List<String> transactionList) {
        transactionList.add(this.getClass().getSimpleName() + " " + getTransactionId());
        return transactionList;
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<String> doMethodWithSupports(final List<String> transactionList) {
        transactionList.add(this.getClass().getSimpleName() + " " + getTransactionId());
        return transactionList;
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    public List<String> doMethodWithNever(final List<String> transactionList) {
        transactionList.add(this.getClass().getSimpleName() + " " + getTransactionId());
        return transactionList;
    }

    public String getTransactionId(){
        return transactionSynchronizationRegistry.getTransactionKey() == null
                   ? "null" : transactionSynchronizationRegistry.getTransactionKey().toString();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void doMethodWithRequiredThrowsException() {
        logger.info("TX3: throwing exception.");
        throw new EJBException("oh no");
    }
}
