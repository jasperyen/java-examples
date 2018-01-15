
package MQTT;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

/**
 *
 * @author Jasper
 */
public class MQTTHandler implements MqttCallback {

    private static final Logger logger = Logger.getLogger(MQTTHandler.class.getName());
    
    private static final String HOST = "tcp://aws.jasper-tw.com:1883";
    private static final String CLIENT_ID = "Java_Servlet";

    private static final String SUB_TOPIC = "ttu/demo/info";
    private static final String PUB_TOPIC = "ttu/demo/state";
    
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "laba5606";
    
    private static MemoryPersistence memory;
    private static MqttClient client;
    
    static {
    
        try {
            memory = new MemoryPersistence();
            client = new MqttClient(HOST, CLIENT_ID, memory);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(USERNAME);
            options.setPassword(PASSWORD.toCharArray());

            options.setConnectionTimeout(3);
            options.setKeepAliveInterval(10);

            client.setCallback(new MQTTHandler());
            client.connect(options);
            
        } catch (MqttException ex) {
            logger.log(Level.WARNING, "MQTTHandler init failed : {0}", ex.toString());
        }
        
    }
    
    public static void addSubscribe (String device) {
        try {
            String topic = SUB_TOPIC + "/" + device;
            
            logger.log(Level.INFO, "addSubscribe : {0}", topic);
            
            client.subscribe(topic);  
        } catch (MqttException ex) {
            logger.log(Level.WARNING, "addSubscribe failed : {0}", ex.toString());
        }
    }
    
    private static void publish(MqttTopic topic, MqttMessage message) throws MqttException {
        
        logger.log(Level.INFO, "publishMessage : topic - {0}, Qos - {1}, message - {2}",
                    new String[]{topic.getName(), message.getQos() + "", new String(message.getPayload())} );
        
        MqttDeliveryToken token = topic.publish(message);
        token.waitForCompletion();
    }
    
    public static void publish(String device, String txt) throws MqttException {
        MqttTopic topic = client.getTopic(PUB_TOPIC + "/" + device);
        
        MqttMessage message = new MqttMessage();
        message.setQos(2);
        message.setPayload(txt.getBytes(StandardCharsets.UTF_8));
        
        publish(topic, message);
    }
    
    @Override
    public void connectionLost(Throwable cause) {
        logger.log(Level.WARNING, "connectionLost : {0}", cause.getMessage());
    }  

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        logger.log(Level.INFO, "deliveryComplete : {0}", token.isComplete());
    }
    
    @Override
    public void messageArrived(String topic, MqttMessage message) throws MqttException {
        
        String txt = new String(message.getPayload());
        
        logger.log(Level.INFO, "messageArrived : topic - {0}, Qos - {1}, message - {2}",
                    new String[]{topic, message.getQos() + "", txt} );
        
        if (!topic.startsWith(SUB_TOPIC))
            return;
        
        String device = topic.substring( topic.indexOf(SUB_TOPIC) + SUB_TOPIC.length() + 1 );
        
        deviceHandler(device, txt);
    }
    
    private static void deviceHandler (String device, String txt) {
       
        try {
            IOTDevice d = IOTDevice.getDeviceByName(device);
            
            if (d == null)
                return;
            
            String key = d.getAESKey();
            String jsonString = decryptText(txt, key);
            
            logger.log(Level.INFO, "decryptText : {0}", jsonString);
            
            JSONObject json = new JSONObject(jsonString);

            boolean state = json.getInt("s") != 0;
            double temperature = json.getDouble("t");

            d.addStamp(state, temperature);
            
            if (temperature > d.maxTemperature)
                state = false;
            else if (temperature < d.minTemperature)
                state = true;
            
            JSONObject pubJson = new JSONObject();
            pubJson.put("s", state ? 1 : 0);
            
            logger.log(Level.INFO, "encryptText : {0}", pubJson.toString());
            
            //publish(device, pubJson.toString());
            publish(device, encryptText(pubJson.toString(), key));
            
        } catch (Exception ex) {
            logger.log(Level.WARNING, "deviceHandler failed : {0}", ex.toString());
        }
    }
    
    private static String decryptText (String txt, String key) throws Exception {
        
        SecretKeySpec spec = new SecretKeySpec(toByteArray(key), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, spec);
        
        byte[] original = cipher.doFinal(toByteArray(txt));
        
        return new String(original);
    }
    
    private static String encryptText (String txt, String key) throws Exception {
        
        SecretKeySpec spec = new SecretKeySpec(toByteArray(key), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, spec);
        
        byte[] encryptData = cipher.doFinal(txt.getBytes());
        
        return toHexString(encryptData);
    }
    
    public static String toHexString(byte[] array) {
        return DatatypeConverter.printHexBinary(array).toUpperCase();
    }

    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s.toUpperCase());
    }
}
