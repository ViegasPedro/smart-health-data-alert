package com.unisinos.smart_health_data_alert.commons.mqtt;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.unisinos.smart_health_data_alert.commons.SmartHealthDataProperties;
import com.unisinos.smart_health_data_alert.vital_sign.mqtt.VitalSignSubscriber;

@Component
public class MqttClient {
		
	@Autowired
	private SmartHealthDataProperties properties;
	
	@Autowired
	private VitalSignSubscriber subscriber;
	
	private MqttAsyncClient client = null;
		
	public MqttAsyncClient getClient() {
		if (client == null)
			connect();
		return client;
	}
	
	private void connect() {        
    	MqttConnectOptions options = new MqttConnectOptions();
    	options.setUserName(this.properties.getMqtt().getUser());
        options.setPassword(this.properties.getMqtt().getPassword().toCharArray());
        options.setCleanSession(true);
        options.setKeepAliveInterval(30);
        options.setMaxInflight(30);
        
        try {
            MqttAsyncClient client = new MqttAsyncClient(this.properties.getMqtt().getServerUrl(), this.properties.getMqtt().getClientId());
            client.connect(options);
            
            Thread.sleep(1000);
            
            if (client.isConnected()) {
            	client.subscribe(MqttTopicUtils.EDGE_VITAL_SIGN_TOPIC, 1, subscriber);
            }
            this.client = client;
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}
