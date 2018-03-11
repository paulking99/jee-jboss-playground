# JEE Concurrency
TODO: create examples for thread manipulation in JEE, e.g. @Asynchronous

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

Note: pom.xml uses docker-maven-plugin configuration that calls a docker-compose.yml. This plugin is limited as it does
not support volumes and networks in the docker-compose.yml file. See https://dmp.fabric8.io/#limitations

## Debug the tests

To debug code, create a remote debug configuration in the IDE to port **8787**

Run the test in debug mode.