package com.jee.jboss.playground.transactions.container.managed;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.TransactionSynchronizationRegistry;

/**
 * Bean has container-managed transactions by default.
 * <p>
 * Enterprise beans that use container-managed transaction demarcation also must not use the {@link javax.transaction.UserTransaction} interface.
 */
@Stateless
public class EJBTx1 {

    @Resource
    private TransactionSynchronizationRegistry transactionSynchronizationRegistry;

    @Inject
    EJBTx2 ejbTx2;

    public Map<String, String> toSelfOnly() {
        Map<String, String> transactions = new LinkedHashMap<>();
        transactions.put(this.getClass().getSimpleName(), getTransactionId());
        return transactions;
    }

    public Map<String, String> toOtherEjbWithTransactionAttributeRequired() {
        Map<String, String> transactions = new LinkedHashMap<>();
        transactions.put(this.getClass().getSimpleName(), getTransactionId());
        return ejbTx2.doMethodWithTransactionAttributeRequired(transactions);
    }

    public Map<String, String> toOtherEjbWithTransactionAttributeRequiresNew() {
        Map<String, String> transactions = new LinkedHashMap<>();
        transactions.put(this.getClass().getSimpleName(), getTransactionId());
        return ejbTx2.doMethodWithTransactionAttributeRequiresNew(transactions);
    }

    public Map<String, String> toOtherEjbWithTransactionAttributeMandatory() {
        Map<String, String> transactions = new LinkedHashMap<>();
        transactions.put(this.getClass().getSimpleName(), getTransactionId());
        return ejbTx2.doMethodWithTransactionAttributeMandatory(transactions);
    }

    public Map<String, String> toOtherEjbWithTransactionAttributeNotSupported() {
        Map<String, String> transactions = new LinkedHashMap<>();
        transactions.put(this.getClass().getSimpleName(), getTransactionId());
        return ejbTx2.doMethodWithTransactionAttributeNotSupported(transactions);
    }

    private String getTransactionId() {
        return transactionSynchronizationRegistry.getTransactionKey() == null
                   ? "null" : transactionSynchronizationRegistry.getTransactionKey().toString();
    }

    private String getTransactionStatus() {
        return Status.toString(transactionSynchronizationRegistry.getTransactionStatus());
    }

}
