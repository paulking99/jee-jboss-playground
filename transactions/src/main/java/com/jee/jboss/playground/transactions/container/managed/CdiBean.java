package com.jee.jboss.playground.transactions.container.managed;

import java.util.LinkedList;

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

    public LinkedList<String> toSelfOnly(LinkedList<String> transactions){
        setTransactionId("toSelfOnly", transactions);
        return transactions;
    }

    public LinkedList<String> toEjbWithTransactionAttributeRequired(LinkedList<String> transactions){
        setTransactionId("toEjbWithTransactionAttributeRequired", transactions);
        ejbTx2.doMethodWithTransactionAttributeRequired(transactions);
        return transactions;
    }

    public LinkedList<String> toEjbWithTransactionAttributeRequiresNew(LinkedList<String> transactions){
        setTransactionId("toEjbWithTransactionAttributeRequiresNew", transactions);
        ejbTx2.doMethodWithTransactionAttributeRequiresNew(transactions);
        return transactions;
    }

    public LinkedList<String> toEjbWithTransactionAttributeMandatory(LinkedList<String> transactions){
        setTransactionId("toEjbWithTransactionAttributeMandatory", transactions);
        ejbTx2.doMethodWithTransactionAttributeMandatory(transactions);
        return transactions;
    }

    public LinkedList<String> toEjbWithTransactionAttributeNotSupported(LinkedList<String> transactions){
        setTransactionId("toEjbWithTransactionAttributeNotSupported", transactions);
        ejbTx2.doMethodWithTransactionAttributeNotSupported(transactions);
        return transactions;
    }

    public LinkedList<String> toEjbWithTransactionAttributeSupports(LinkedList<String> transactions){
        setTransactionId("toEjbWithTransactionAttributeSupports", transactions);
        ejbTx2.doMethodWithTransactionAttributeSupports(transactions);
        return transactions;
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
