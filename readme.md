# Simple Kafka Producer

## Kafka Topic



## Creating a Kafka Topic

```cd``` into the Kafka directory, and run the following command to create a new topic:

```shell
./bin/kafka-topics.sh --create --topic thetechcheck --replication-factor 1 --partitions 1 --zookeeper localhost:2181
```

## Running the SpringBoot application

```cd``` into the project directory and run the following command to create a ```.jar``` file of the project:

```shell
mvn clean install
```

This will create a ```.jar``` file in the ```target``` directory, inside the project directory. Now to run the project, run the following command from the same project directory:

```shell
java -jar target/<name_of_jar_file>.jar
```

You should now be seeing the output in the terminal.

Update:
Just run the application after doing maven clean install and the the constructor calls the test producer which then gets consumed later. You have to have created the producer and consumer first. I named them ray.producer and ray.consumer in the properties file. Which i created through postman instead of doing the command above.