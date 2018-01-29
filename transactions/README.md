# JEE transactions

A series of tests to show jee transactions at work.
 * See JEE Transaction rules at: [The Java EE 6 Tutorial ](http://docs.oracle.com/javaee/6/tutorial/doc/bncij.html)
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
## Run the tests

This project uses the [docker-maven-plugin](https://dmp.fabric8.io/)

* Build docker image:
```mvn docker:build```
* Start docker container:
```mvn docker:start ```
* Run the tests:
```mvn verify```
* Or build and start docker container and run tests in single command:
```mvn docker:build docker:start verify```
* Stop and destroy docker container:
```mvn docker:stop```
* Remove image (not parent image): 
```mvn -Ddocker.removeAll docker:remove```

Note: pom.xml uses docker-maven-plugin configuration that calls a Dockerfile

## Debug the tests

To debug code, create a remote debug configuration in the IDE to port **8787**

Run the test in debug mode.