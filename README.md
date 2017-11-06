# jee-jboss-playground
This project has a number or sub-modules, each of which contains a Java EE technology example.
It runs on a docker jboss/wildfly container.

See the README.txt in each sub-module for further details.

Run each sub-module individually. The parent pom.xml is a configuration pom and allows the entire repository to be imported to the IDE as a single project for convenience.

## How to execute the tests
1. Start the jboss/wildfly docker container : <br>

``` 
cd jee-jboss-playground/docker
docker-compose up
```


2. Execute the test: <br>

```
cd jee-design-patterns/<sub-module-name>
mvn verify
```

## How to debug the tessts
All default ports on wildfly have been prefixed with 1. See:
```
jee-jboss-playground/docker/docker-compose.yml
```

To debug code, create a remote debug configuration in the IDE to port **18787**

Run the test in debug mode.
