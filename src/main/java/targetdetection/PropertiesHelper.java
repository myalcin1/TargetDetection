package targetdetection;

import java.util.Properties;

public class PropertiesHelper {
    public static Properties getPropertiesForProducer() {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty(
                "key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer"
        );
        props.setProperty(
                "value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer"
        );
        return props;
    }

    public static Properties getPropertiesForConsumer(String group_id) {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("group.id", group_id);
        props.setProperty(
                "key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer"
        );
        props.setProperty(
                "value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer"
        );
        props.setProperty(
                "enable.auto.commit", "true"
        );
        //props.setProperty("auto.offset.reset", "earliest");
        return props;
    }
}
