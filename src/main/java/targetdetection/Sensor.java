package targetdetection;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class Sensor {

    static long sensor_id;
    static long sensor_x;
    static long sensor_y;

    public static void main(String[] args) throws ParseException {
        sensor_id = Integer.parseInt(args[0]);
        sensor_x = Integer.parseInt(args[1]);
        sensor_y = Integer.parseInt(args[2]);

        KafkaConsumer<String, String> consumer = Sensor.getKafkaConsumer("sensor-group" + sensor_id);
        String topicToListen = "target-topic";
        consumer.subscribe(Arrays.asList(topicToListen));

        while (true) {
            ConsumerRecords<String, String> records =
                    consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("Sensor " + sensor_id + " received " + record.value() + " from Target");

                JSONObject targetPosition = Sensor.parseTargetPosition(record);
                double angleToTarget = Sensor.calculateAngleToTarget(targetPosition);

                Sensor.sendAngleToCentre(angleToTarget);
                consumer.commitSync();
                return;
            }
        }
    }

    private static KafkaConsumer<String, String> getKafkaConsumer(String group_id) {
        Properties props = PropertiesHelper.getPropertiesForConsumer(group_id);
        return new KafkaConsumer<>(props);
    }

    private static JSONObject parseTargetPosition(ConsumerRecord<String, String> record) throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(record.value());
    }

    private static double calculateAngleToTarget(JSONObject targetPosition) {
        long target_x = (Long) targetPosition.get("x");
        long target_y = (Long) targetPosition.get("y");
        return Math.atan2(target_y - sensor_y, target_x - sensor_x);
    }

    private static void sendAngleToCentre(double angleToTarget) {
        String message = generateSensorMessage(angleToTarget);
        System.out.println("Sensor " + sensor_id + " sending " + message + " to the Centre");

        String topicToSend = "sensor" + sensor_id + "-topic";
        ProducerRecord<String, String> producerRecord =
                new ProducerRecord<>(topicToSend, message);
        KafkaProducer<String, String> producer = Sensor.getKafkaProducer();
        producer.send(producerRecord);
        producer.flush();
        producer.close();
    }

    private static KafkaProducer<String, String> getKafkaProducer() {
        Properties props = PropertiesHelper.getPropertiesForProducer();
        return new KafkaProducer<>(props);
    }

    private static String generateSensorMessage(double angleToTarget) {
        JSONObject obj = new JSONObject();
        obj.put("sensor_id", sensor_id);
        obj.put("sensor_x", sensor_x);
        obj.put("sensor_y", sensor_y);
        obj.put("angle", angleToTarget);
        return obj.toJSONString();
    }
}
