package targetdetection;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class CentreListener implements Runnable {
    private JSONObject sensorMessage = new JSONObject();
    private final String sensorId;

    CentreListener(String sensorId) {
        this.sensorId = sensorId;
    }
    @Override
    public void run() {
        KafkaConsumer<String, String> consumer = getKafkaConsumer("centre" + sensorId + "-group");
        String topicToListen = "sensor" + sensorId + "-topic";
        consumer.subscribe(List.of(topicToListen));

        while (true) {
            ConsumerRecords<String, String> records =
                    consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                try {
                    this.sensorMessage = parseSensorMessage(record);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                consumer.commitSync();
                return;
            }
        }
    }

    private KafkaConsumer<String, String> getKafkaConsumer(String group_id) {
        Properties props = PropertiesHelper.getPropertiesForConsumer(group_id);
        return new KafkaConsumer<>(props);
    }

    private JSONObject parseSensorMessage(ConsumerRecord<String, String> record) throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(record.value());
    }

    public JSONObject getSensorMessage() {
        return sensorMessage;
    }
}
