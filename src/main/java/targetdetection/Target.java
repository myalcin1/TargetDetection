package targetdetection;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import org.json.simple.JSONObject;

public class Target {

    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        JSONObject obj = new JSONObject();
        obj.put("x", x);
        obj.put("y", y);
        String message = obj.toJSONString();
        System.out.println("Target sending: " + message);
        Target.send(message);
    }

    private static void send(String message) {
        String topicName = "target-topic";
        ProducerRecord<String, String> producerRecord =
                new ProducerRecord<>(topicName, message);
        KafkaProducer<String, String> producer = Target.getKafkaProducer();
        producer.send(producerRecord);
        producer.flush();
        producer.close();
    }

    private static KafkaProducer<String, String> getKafkaProducer() {
        Properties props = PropertiesHelper.getPropertiesForProducer();
        return new KafkaProducer<>(props);
    }
}