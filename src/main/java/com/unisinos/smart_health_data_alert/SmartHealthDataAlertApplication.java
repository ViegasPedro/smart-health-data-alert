package com.unisinos.smart_health_data_alert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;

import com.unisinos.smart_health_data_alert.commons.SmartHealthDataProperties;
import com.unisinos.smart_health_data_alert.commons.mqtt.MqttClient;

@SpringBootApplication
@EnableConfigurationProperties(value = { SmartHealthDataProperties.class })
public class SmartHealthDataAlertApplication {

	@Autowired
	private MqttClient mqttClient;
	
	public static void main(String[] args) {
		SpringApplication.run(SmartHealthDataAlertApplication.class, args);
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void startMqttClient() {
	    this.mqttClient.getClient();
	    this.mqttClient.getFogServerClient();
	}

}
