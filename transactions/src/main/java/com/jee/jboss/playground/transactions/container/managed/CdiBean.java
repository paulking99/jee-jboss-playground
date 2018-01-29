package com.jee.jboss.playground.transactions.container.managed;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.transaction.TransactionSynchronizationRegistry;

/**
 * This class is a CDI bean, it is not an EJB.
 * http://docs.jboss.org/cdi/spec/1.2/cdi-spec.html#bean_defining_annotations
 */
@Dependent
public class CdiBean {

    //Note: in the Cdi bean we have to specify the resource name, in the Ejb we do not.
    @Resource(name = "java:comp/TransactionSynchronizationRegistry")
    private TransactionSynchronizationRegistry transactionSynchronizationRegistry;

    @Inject
    EJBTx2 ejbTx2;

    public Map<String, String> toSelfOnly(){
        Map<String, String> transactions = new LinkedHashMap<>();
        transactions.put(this.getClass().getSimpleName(), getTransactionId());
        return transactions;
    }

    public Map<String, String> toEjbWithTransactionAttributeRequired(){
            Map<String, String> transactions = new LinkedHashMap<>();
            transactions.put(this.getClass().getSimpleName(), getTransactionId());
            ejbTx2.doMethodWithTransactionAttributeRequired(transactions);
            return transactions;
    }

    public Map<String, String> toEjbWithTransactionAttributeMandatory(){
        Map<String, String> transactions = new LinkedHashMap<>();
        transactions.put(this.getClass().getSimpleName(), getTransactionId());
        ejbTx2.doMethodWithTransactionAttributeMandatory(transactions);
        return transactions;
    }

    private String getTransactionId(){
        return transactionSynchronizationRegistry.getTransactionKey() == null
                   ? "null" : transactionSynchronizationRegistry.getTransactionKey().toString();
    }
}
