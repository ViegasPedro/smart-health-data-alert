package com.unisinos.smart_health_data_alert.commons.mqtt;

import java.text.SimpleDateFormat;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Component
public class MqttPublisher {
	
	private MqttClient client;
	
	@Autowired
	public void setMqttClient(@Lazy MqttClient client) {
		this.client = client;
	}
	
	public void publish(String topic, Object message) throws JsonProcessingException, MqttPersistenceException, MqttException {
		ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		String json = objectMapper.writeValueAsString(message);
				
    	MqttMessage mqttMessage = new MqttMessage(json.getBytes());
		
		client.getClient().publish(topic, mqttMessage);	
	}
}
