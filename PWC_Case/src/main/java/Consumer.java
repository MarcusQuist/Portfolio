import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Consumer
{
    private static Logger logger;
    private static KafkaConsumer<Integer, String> consumer;

    public static void main(String[] args)
    {
        InitConsumer(args);
        ListenForRecords();
    }
    private static void InitConsumer(String[] args)
    {
        if (args.length != 1)
        {
            System.err.println("Error: Please specify a groupId as argument");
            System.exit(1);
        }
        String bootstrapServers = "127.0.0.1:9092";
        String groupId = args[0];
        String topic = "new-client";
        logger = LoggerFactory.getLogger(Consumer.class.getName());

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton(topic));
    }

    private static void ListenForRecords()
    {
        while(true)
        {
            ConsumerRecords<Integer, String> records = consumer.poll(Duration.ofMillis(100));
            for(ConsumerRecord<Integer, String> record : records)
            {
                logger.info("Key: " + record.key() + ", Value: " + record.value() + ", Partition: " + record.partition() + ", Offset: " + record.offset());
            }
        }
    }
}
