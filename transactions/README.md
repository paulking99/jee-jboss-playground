#transactions

A series of tests to show jee transactions at work.
 * See JEE Transaction rules at: http://docs.oracle.com/javaee/6/tutorial/doc/bncij.html
 * Each Arquillian integration test tests a JEE transaction rule.
 * The transaction IDs are collected are in a Map&lt;String,String&gt; with format: CLASS_NAME, TRANSACTION_ID
 * Transactions are logged in the Wildfly terminal, example:
 ``` 
 wildfly_1  | 
 wildfly_1  | ejbToEjbWithTransactionAttributeRequiresNew transaction list:
 wildfly_1  |    Transaction EJBTx1 : 0:ffffac120002:-3f9d4994:5a00332c:7f
 wildfly_1  |    Transaction EJBTx2 : 0:ffffac120002:-3f9d4994:5a00332c:80
 wildfly_1  | 
```