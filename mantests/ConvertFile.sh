#!/bin/sh

#Step back to root directory
cd ..

#Clean mvn project
mvn clean

#Build mvn project
mvn package -DskipTests

#Do a conversion
java -jar target/csv2rdf-1.0.-jar-with-dependencies.jar examples/cars/template.ttl examples/cars/cars.csv mantests/cars.ttl