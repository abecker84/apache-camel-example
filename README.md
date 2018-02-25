# Apache Camel Example
This project contains a demo of a very simplistic Apache Camel setup. It allows to route incoming JSON files from a configurable inbox directory to a configurable endpoint of the example customer REST service.

## Prerequisites

* Have maven und Java 8 installed on your machine
* Have the example REST service [1] cloned and run locally
* Have created a local exchange directory for camel, such as /home/<foobar>/camel

## HowTo run

mvn verify -DcamelExchangePath=</path/to/exchange/dir> -DcustomerRestEndpoint=http://localhost:8080/rest-example

Optionally, feel free to import the maven project into the IDE of your choice :-)

## References

[1] Example REST service: https://github.com/abecker84/fiddling/tree/master/spring-boot-restservice-example
