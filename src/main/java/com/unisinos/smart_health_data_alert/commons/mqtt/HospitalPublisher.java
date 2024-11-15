package com.unisinos.smart_health_data_alert.commons.mqtt;

import org.springframework.stereotype.Component;

import com.unisinos.smart_health_data_alert.alert.mqtt.AlertMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HospitalPublisher {

	//mocks a hospital publisher
	public void publish(String topic, AlertMessage message) {
		log.info("Message published to hospital server for userId: {}, vital signs: {}", message.getVitalSigns());
	}
}
