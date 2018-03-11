package com.jee.jboss.playground.transactions.container.managed;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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

    public LinkedList<String> toSelfOnly(LinkedList<String> transactions) {
        return setTransactionId("toSelfOnly", transactions);
    }

    public LinkedList<String> toSelfPublicMethodWithTransactionAttributeRequiresNew(final LinkedList<String> transactions) {
        setTransactionId("toSelfPublicMethodWithTransactionAttributeRequiresNew", transactions);
        return doPublicMethodWithTransactionAttributeRequiresNew(transactions);
    }

    public LinkedList<String> toSelfPrivateMethodWithTransactionAttributeRequiresNew(final LinkedList<String> transactions) {
        setTransactionId("toSelfPrivateMethodWithTransactionAttributeRequiresNew", transactions);
        return this.doPrivateMethodWithTransactionAttributeRequiresNew(transactions);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public LinkedList<String> doPublicMethodWithTransactionAttributeRequiresNew(final LinkedList<String> transactions) {
        return setTransactionId("doPublicMethodWithTransactionAttributeRequiresNew", transactions);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private LinkedList<String> doPrivateMethodWithTransactionAttributeRequiresNew(final LinkedList<String> transactions) {
        return setTransactionId("doPrivateMethodWithTransactionAttributeRequiresNew", transactions);
    }

    public LinkedList<String> toOtherEjbWithTransactionAttributeRequired(final LinkedList<String> transactions) {
        setTransactionId("toOtherEjbWithTransactionAttributeRequired", transactions);
        return ejbTx2.doMethodWithTransactionAttributeRequired(transactions);
    }

    public LinkedList<String> toOtherEjbWithTransactionAttributeRequiresNew(final LinkedList<String> transactions) {
        setTransactionId("toOtherEjbWithTransactionAttributeRequiresNew", transactions);
        return ejbTx2.doMethodWithTransactionAttributeRequiresNew(transactions);
    }

    public LinkedList<String> toOtherEjbWithTransactionAttributeMandatory(final LinkedList<String> transactions) {
        setTransactionId("toOtherEjbWithTransactionAttributeMandatory", transactions);
        return ejbTx2.doMethodWithTransactionAttributeMandatory(transactions);
    }

    public LinkedList<String> toOtherEjbWithTransactionAttributeNotSupported(final LinkedList<String> transactions) {
        setTransactionId("toOtherEjbWithTransactionAttributeNotSupported", transactions);
        return ejbTx2.doMethodWithTransactionAttributeNotSupported(transactions);
    }

    public LinkedList<String> toOtherEjbWithTransactionAttributeSupports(final LinkedList<String> transactions) {
        setTransactionId("toOtherEjbWithTransactionAttributeSupports", transactions);
        return ejbTx2.doMethodWithTransactionAttributeSupports(transactions);
    }

    public LinkedList<String> toOtherEjbWithTransactionAttributeNever(final LinkedList<String> transactions) {
        setTransactionId("toOtherEjbWithTransactionAttributeNever", transactions);
        return ejbTx2.doMethodWithTransactionAttributeNever(transactions);
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
