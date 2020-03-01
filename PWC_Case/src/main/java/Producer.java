import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Scanner;

public class Producer
{
    private static Logger logger;
    private static KafkaProducer<Integer, String> producer;

    public static void main(String[] args)
    {
        InitProducer();
        ListenForInput();
    }

    private static void InitProducer()
    {
        logger = LoggerFactory.getLogger(Producer.class);
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        producer = new KafkaProducer<>(properties);

        System.out.println("Successfully created the Kafka Producer!");
    }

    private static void ListenForInput()
    {
        Scanner sc = new Scanner(System.in);
        String topic = "new-client";
        int clientId;
        String clientName;
        while (true)
        {
            try
            {
                System.out.println("Please Specify your client id:");
                clientId = Integer.parseInt(sc.nextLine());
                System.out.println("Please specify your client name:");
                clientName = sc.nextLine();
                final ProducerRecord<Integer, String> record = new ProducerRecord<>(topic, clientId, clientName);
                sendRecord(record);
            }
            catch(NumberFormatException e)
            {
                System.out.println("Wrong id input, please specify an <Integer>");
            }
        }
    }

    private static void  sendRecord(ProducerRecord<Integer, String> record)
    {
        producer.send(record, (RecordMetadata recordMetadata, Exception e) -> {
            if(e == null)
            {
                logger.info("-- Metadata -- \n" +
                            "Topic: " + recordMetadata.topic() + "\n" +
                            "Partition: " + recordMetadata.partition() + "\n" +
                            "Offset: " + recordMetadata.offset() + "\n" +
                            "Timestamp: " + recordMetadata.timestamp()
                            );
            }
            else
            {
                logger.error("Error: ", e);
            }
        });
        producer.flush();
    }
}
