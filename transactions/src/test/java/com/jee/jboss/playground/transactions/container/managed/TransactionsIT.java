package com.jee.jboss.playground.transactions.container.managed;

import com.jee.jboss.playground.transactions.log.Slf4jLoggerProducer;
import com.jee.jboss.playground.transactions.util.TransactionUtil;

import java.util.Map;

import javax.inject.Inject;
import javax.ejb.EJBTransactionRequiredException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import static com.jee.jboss.playground.transactions.util.TransactionUtil.getTransactionId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * A series of tests to show jee transactions at work.
 * See full rules at: http://docs.oracle.com/javaee/6/tutorial/doc/bncij.html
 * <p>
 * Each Unit test tests a JEE transaction rule.
 * The transaction IDs are collected are in a Map&lt;String,String&gt; with format: CLASS_NAME, TRANSACTION_ID
 */
@RunWith(Arquillian.class)
public class TransactionsIT {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                   .addClass(EJBTx1.class)
                   .addClass(Slf4jLoggerProducer.class)
                   .addClass(CdiBean.class)
                   .addClass(EJBTx2.class)
                   .addClass(EJBTx3.class)
                   .addClass(Format.class)
                   .addClass(Status.class)
                   .addClass(TransactionUtil.class);
        //.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private EJBTx1 ejbTx1;

    @Inject
    private CdiBean cdiBean;

    @Inject
    private Logger logger;

    /**
     * Test default transaction on an EJB public method with no TransactionAttribute defined.
     * <p>
     * By default, a transaction exists on a stateless session bean public method.
     */
    @Test
    public void transactionExistsForStatelessSessionBean() {
        Map<String, String> transactions = ejbTx1.toSelfOnly();
        assertEquals("Too many transactions.", 1, transactions.size());
        logTransactions(transactions, "transactionExistsForStatelessSessionBean");
        assertNotNull("The transaction ID is null, there is no transaction", getTransactionId(0, transactions));
    }

    /**
     * Test default transaction on a CDI bean with no TransactionAttribute defined.
     * <p>
     * A transaction does not exist on a plain CDI java bean public method.
     */
    @Test
    public void transactionDoesNotExistForCdiBean() {
        assertNotNull("CDI bean is null", cdiBean);
        Map<String, String> transactions = cdiBean.toSelfOnly();
        assertEquals("Too many transactions.", 1, transactions.size());
        logTransactions(transactions, "transactionDoesNotExistForCdiBean");
        assertEquals("The CDI bean transaction should be null.", "null", getTransactionId(0, transactions));
    }

    /**
     * Test TransactionAttribute.REQUIRED when the client is in a transaction.
     * <p>
     * Step 1: transaction started by session bean EJBTx1.<br>
     * Step 2: EJBTx1 makes call to ejbTx2 method with transaction TransactionAttributeType.REQUIRED.<br>
     * <p>
     * Rule: &quot;If the client is running within a transaction and invokes the enterprise bean’s method, the method executes within the client’s
     * transaction.&quot;
     * The transaction started in first EJB is used by method in second EJB.
     */
    @Test
    public void ejbToEjbWithTransactionAttributeRequired() {
        Map<String, String> transactions = ejbTx1.toOtherEjbWithTransactionAttributeRequired();
        logTransactions(transactions, "ejbToEjbWithTransactionAttributeRequired");
        assertEquals("Transaction IDs do not match", getTransactionId(0, transactions), getTransactionId(1, transactions));
    }

    /**
     * Test TransactionAttribute.REQUIRED when the client is not in a transaction.
     * <p>
     * Step 1: CDI bean is called. CDI bean is the client of the EJB. No transaction exists.<br>
     * Step 2: CDI bean calls EJB with TransactionAttributeType.REQUIRED. The container starts a new transaction in the EJB.<br>
     * <p>
     * Rule: &quot;If the client is not associated with a transaction, the container starts a new transaction before running the method.&quot;
     */
    @Test
    public void cdiBeanToEjbWithTransactionAttributeRequired() {
        assertNotNull("CDI bean is null", cdiBean);
        Map<String, String> transactions = cdiBean.toEjbWithTransactionAttributeRequired();
        logTransactions(transactions, "cdiBeanToEjbWithTransactionAttributeRequired");
        assertEquals("Incorrect number of transactions.", 2, transactions.size());
        assertEquals("The CDI bean transaction should be null.", "null", getTransactionId(0, transactions));
        assertNotEquals("The EJB bean transaction should be non null.", "null", getTransactionId(1, transactions));
    }

    /**
     * Test TransactionAttribute.REQUIRED_NEW when the client is in a transaction.
     * <p>
     * Step 1: transaction started by EJBTx1.<br>
     * Step 2: EJBTx1 calls method on EBJTx2 TransactionAttributeType.REQUIRES_NEW.<br>
     * <p>
     * The transaction started in EJBTx1 is suspended. ejbTx2 starts a new transaction.
     * <p>
     * Rules:
     * <p>
     * If the client is running within a transaction and invokes the enterprise bean’s method, the container takes the following steps:
     * <ol>
     * <li>Suspends the client’s transaction</li>
     * <li>Starts a new transaction</li>
     * <li>Delegates the call to the method</li>
     * <li>Resumes the client’s transaction after the method completes</li>
     * </ol>
     * <p>
     * If the client is not associated with a transaction, the container starts a new transaction before running the method.
     * <p>
     * You should use the RequiresNew attribute when you want to ensure that the method always runs within a new transaction.
     */
    @Test
    public void ejbToEjbWithTransactionAttributeRequiresNew() {
        final Map<String, String> transactions = ejbTx1.toOtherEjbWithTransactionAttributeRequiresNew();
        logTransactions(transactions, "ejbToEjbWithTransactionAttributeRequiresNew");
        assertEquals("Incorrect number of transactions.", 2, transactions.size());
        assertNotEquals("EJBTx1 bean transaction should not be null.", "null", getTransactionId(0, transactions));
        assertNotEquals("EJBTx2 bean transaction should be non null.", "null", getTransactionId(1, transactions));
        assertNotEquals("Transactions should not be the same.", getTransactionId(0, transactions), getTransactionId(1, transactions));
    }

    /**
     * Test TransactionAttribute.MANDATORY when the client is in a transaction.
     * <p>
     * Step 1: transaction started by EJBTx1.<br>
     * Step 2: EJBTx1 calls method on EBJTx2 with TransactionAttributeType.MANDATORY.<br>
     * <p>
     * The transaction started in EJBTx1 is used in the EJBTx2.
     * <p>
     * Rules:
     * <p>
     * If the client is running within a transaction and invokes the enterprise bean’s method, the method executes within the client’s transaction.
     * If the client is not associated with a transaction, the container throws a TransactionRequiredException.
     * Use the Mandatory attribute if the enterprise bean’s method must use the transaction of the client.
     */
    @Test
    public void ejbToEjbWithTransactionAttributeMandatory() {
        final Map<String, String> transactions = ejbTx1.toOtherEjbWithTransactionAttributeMandatory();
        logTransactions(transactions, "ejbToEjbWithTransactionAttributeMandatory");
        assertEquals("Incorrect number of transactions.", 2, transactions.size());
        assertNotEquals("EJBTx1 bean transaction should not be null.", "null", getTransactionId(0, transactions));
        assertNotEquals("EJBTx2 bean transaction should be non null.", "null", getTransactionId(1, transactions));
        assertEquals("Transactions should be the same.", getTransactionId(0, transactions), getTransactionId(1, transactions));
    }

    /**
     * Test TransactionAttribute.MANDATORY when the client is not in a transaction.
     * <p>
     * Step 1: transaction started by CdiBean. No transaction exists.<br>
     * Step 2: CdiBean calls method on EBJTx2 with TransactionAttributeType.MANDATORY.<br>
     * Step 3: As there is no transaction in CdiBean then the EJBTx2 bean throws TransactionRequiredException.<br>
     * <p>
     * Rules:
     * <p>
     * If the client is running within a transaction and invokes the enterprise bean’s method, the method executes within the client’s transaction.
     * If the client is not associated with a transaction, the container throws a TransactionRequiredException.
     * Use the Mandatory attribute if the enterprise bean’s method must use the transaction of the client.
     */
    @Test(expected = EJBTransactionRequiredException.class)
    public void cdiBeanToEjbWithTransactionAttributeMandatory() {
        final Map<String, String> transactions = cdiBean.toEjbWithTransactionAttributeMandatory();
    }

    /**
     * Test TransactionAttribute.NOT_SUPPORTED when the client is in a transaction.
     * <p>
     * Step 1: transaction started by EJBTx1.<br>
     * Step 2: EJBTx1 calls method on EBJTx2 with TransactionAttributeType.NOT_SUPPORTED.<br>
     * <p>
     * The transaction started in EJBTx1 is suspended and the method on EJBTx2 is run without a transaction.<br>
     *     After the method on EJBTx2 is complete the transaction on EJBTx1 is resumed.
     * <p>
     * Rules:
     * <p>
     * If the client is running within a transaction and invokes the enterprise bean’s method, the container suspends the client’s transaction before invoking the method.
     * After the method has completed, the container resumes the client’s transaction.
     * If the client is not associated with a transaction, the container does not start a new transaction before running the method.
     * Use the NotSupported attribute for methods that don’t need transactions. Because transactions involve overhead, this attribute may improve performance.
     */
    @Test
    public void ejbToEjbWithTransactionAttributeNotSupported() {
        final Map<String, String> transactions = ejbTx1.toOtherEjbWithTransactionAttributeNotSupported();
        logTransactions(transactions, "ejbToEjbWithTransactionAttributeMandatory");
        assertEquals("Incorrect number of transactions.", 2, transactions.size());
        assertNotEquals("EJBTx1 bean transaction should not be null.", "null", getTransactionId(0, transactions));
        assertEquals("EJBTx2 bean transaction should be null.", "null", getTransactionId(1, transactions));
        assertNotEquals("Transactions should not be the same.", getTransactionId(0, transactions), getTransactionId(1, transactions));
    }

    /**
     * Test TransactionAttribute.NOT_SUPPORTED when the client is not in a transaction.
     * <p>
     * Step 1: transaction started by CdiBean. No transaction exists.<br>
     * Step 2: CdiBean calls method on EBJTx2 with TransactionAttributeType.MANDATORY.<br>
     * Step 3: As there is no transaction in CdiBean then the EJBTx2 bean throws TransactionRequiredException.<br>
     * <p>
     * Rules:
     * <p>
     * If the client is running within a transaction and invokes the enterprise bean’s method, the container suspends the client’s transaction before invoking the method.
     * After the method has completed, the container resumes the client’s transaction.
     * If the client is not associated with a transaction, the container does not start a new transaction before running the method.
     * Use the NotSupported attribute for methods that don’t need transactions. Because transactions involve overhead, this attribute may improve performance.
     */
    @Test(expected = EJBTransactionRequiredException.class)
    public void cdiBeanToEjbWithTransactionAttributeNotSupported() {
        final Map<String, String> transactions = cdiBean.toEjbWithTransactionAttributeMandatory();
    }

    /*
     * PRIVATE METHODS
     */

    private void logTransactions(Map<String, String> transactions, final String testName) {
        StringBuffer output = new StringBuffer();
        output.append("\n\n" + testName + " transaction list:");
        for (Map.Entry<String, String> entry : transactions.entrySet()) {
            output.append("\n\tTransaction " + entry.getKey() + " : " + entry.getValue());
        }
        output.append("\n\n");
        logger.info(output.toString());
    }
}
