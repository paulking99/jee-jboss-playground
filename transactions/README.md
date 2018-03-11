# JEE transactions

A series of tests to show jee transactions at work.
 * See JEE Transaction rules at: [The Java EE 6 Tutorial ](http://docs.oracle.com/javaee/6/tutorial/doc/bncij.html)
 * Each Arquillian integration test tests a JEE transaction rule.
 * The transaction IDs are collected are in a LinkedList&lt;String&gt; with format:<br>
   CLASS_NAME : METHOD_NAME : TRANSACTION_ID
 * Transactions are logged in the Wildfly terminal, example:
 ``` 
 ejbToEjbWithTransactionAttributeRequiresNew transaction list:
     EJBTx1 : toOtherEjbWithTransactionAttributeRequiresNew : 0:ffffac110002:-7b4165b5:5aa5290f:29
     EJBTx2 : doMethodWithTransactionAttributeRequiresNew : 0:ffffac110002:-7b4165b5:5aa5290f:2b
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

* View container id:
```docker ps```

* View docker logs:
```docker logs -f <CONTAINER ID>```

Note: pom.xml uses docker-maven-plugin configuration that calls a Dockerfile

## Debug the tests

To debug code, create a remote debug configuration in the IDE to port **8787**

Run the test in debug mode.