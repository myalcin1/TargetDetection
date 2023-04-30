package targetdetection;

import org.json.simple.JSONObject;

public class Centre {

    public static void main(String[] args) throws InterruptedException {
        CentreListener listener0 = new CentreListener("0");
        CentreListener listener1 = new CentreListener("1");
        Thread thread0 = new Thread(listener0);
        Thread thread1 = new Thread(listener1);

        thread0.start();
        thread1.start();

        thread0.join();
        thread1.join();

        calculateTargetPosition(listener0.getSensorMessage(), listener1.getSensorMessage());
    }

    private static void calculateTargetPosition(JSONObject sensor0Message, JSONObject sensor1Message) {
        System.out.println("calculation started");
        long x0 = (Long) sensor0Message.get("sensor_x");
        long y0 = (Long) sensor0Message.get("sensor_y");
        double m0 = Math.tan((double) sensor0Message.get("angle"));

        long x1 = (Long) sensor1Message.get("sensor_x");
        long y1 = (Long) sensor1Message.get("sensor_y");
        double m1 = Math.tan((double) sensor1Message.get("angle"));

        if (m1 == m0) {
            System.out.println("Position of the target can not be determined because it is inline with two sensors.");
        }
        else {
            double x = (m0 * x0 - y0 - m1 * x1 + y1) / (m0 - m1);
            double y = m0 * x - m0 * x0 + y0;

            System.out.println("Target position is (" + Math.round(x) + ", " + Math.round(y) + ")");
        }
    }
}
