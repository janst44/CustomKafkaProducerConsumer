package org.familysearch.search.deadLetterReprocess;

import org.familysearch.search.deadLetterReprocess.kafkaConsumers.KafkaProducerConsumer;
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Properties;

@SpringBootApplication
public class KafkaProducerConsumerRunner implements CommandLineRunner {

    @Value("${kafka.topic.produce.to}")
    private String produceToTopic;

    @Value("${kafka.topic.consume.from}")
    private String consumeFromTopic;

    @Value("${kafka.bootstrap.servers}")
    private String kafkaBootstrapServers;

    @Value("${kafka.schema.registry}")
    private String kafkaSchemaRegistry;

    @Value("${zookeeper.groupId}")
    private String zookeeperGroupId;

    @Value("${zookeeper.host}")
    String zookeeperHost;

    //private static final Logger logger = Logger.getLogger(SimpleKafkaProducerApplication.class);

    public static void main( String[] args ) {
        SpringApplication.run(KafkaProducerConsumerRunner.class, args);
    }

    @Override
    public void run(String... args) {

        /*
         * Defining producer properties.
         */
        Properties producerProperties = new Properties();
        producerProperties.put("bootstrap.servers", kafkaBootstrapServers);//We could pass a list but only need a single port
        producerProperties.put("schema.registry.url", kafkaSchemaRegistry);//We could pass a list but only need a single port
        producerProperties.put("acks", "all");
        producerProperties.put("retries", 0);
        producerProperties.put("batch.size", 16384);
        producerProperties.put("linger.ms", 1);
        producerProperties.put("buffer.memory", 33554432);
        producerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProperties.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");


        /*
         * Defining Kafka consumer properties.
         */
        Properties consumerProperties = new Properties();
        consumerProperties.put("bootstrap.servers", kafkaBootstrapServers);
        consumerProperties.put("schema.registry.url", kafkaSchemaRegistry);
        consumerProperties.put("group.id", zookeeperGroupId);
        consumerProperties.put("zookeeper.session.timeout.ms", "6000");
        consumerProperties.put("zookeeper.sync.time.ms","2000");
        consumerProperties.put("auto.commit.enable", "false");
        consumerProperties.put("auto.commit.interval.ms", "1000");
        consumerProperties.put("consumer.timeout.ms", "-1");
        consumerProperties.put("max.poll.records", "1");
        consumerProperties.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.put("specific.avro.reader", "true");

        /*
         * Creating a thread to listen to the kafka topic
         */
        Thread kafkaConsumerThread = new Thread(() -> {
            //logger.info("Starting Kafka consumer thread.");
            System.out.println("starting kafka consumer thread");

            KafkaProducerConsumer simpleKafkaConsumer = new KafkaProducerConsumer(
                    produceToTopic,
                    consumeFromTopic,
                    producerProperties,
                    consumerProperties
            );

            simpleKafkaConsumer.runSingleWorker();
        });

        /*
         * Starting the first thread.
         */
        kafkaConsumerThread.start();
    }

}
