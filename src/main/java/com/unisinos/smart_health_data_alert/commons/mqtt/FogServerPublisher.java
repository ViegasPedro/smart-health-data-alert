package com.unisinos.smart_health_data_alert.commons.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class FogServerPublisher {
	
	@Autowired
	private MqttClient client;
	
	public void publish(String topic, Object message) throws JsonProcessingException, MqttPersistenceException, MqttException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(message);
		
    	MqttMessage mqttMessage = new MqttMessage(json.getBytes());
		
		client.getFogServerClient().publish(topic, mqttMessage);	
	}
}
