package com.unisinos.smart_health_data_alert.alert.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.unisinos.smart_health_data_alert.alert.model.Alert;
import com.unisinos.smart_health_data_alert.alert.model.AlertRepository;
import com.unisinos.smart_health_data_alert.alert.model.AlertScoreType;
import com.unisinos.smart_health_data_alert.alert.mqtt.AlertMessage;
import com.unisinos.smart_health_data_alert.alert.mqtt.AlertVitalSign;
import com.unisinos.smart_health_data_alert.commons.mqtt.FogServerPublisher;
import com.unisinos.smart_health_data_alert.commons.mqtt.MqttTopicUtils;
import com.unisinos.smart_health_data_alert.vital_sign.model.VitalSign;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlertService {
	
	private static final int MINUTES_BETWEEN_ALERTS = 2;

	@Autowired
	private AlertRepository repository;
	
	@Autowired
	private FogServerPublisher publisher;
	
	public void sendSingleVitalSignAlert(VitalSign vitalSign) throws JsonProcessingException, MqttPersistenceException, MqttException {
		Date alertLimitDate = new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(MINUTES_BETWEEN_ALERTS));
		
		if (!repository.existsByUserIdAndDateGreaterThan(vitalSign.getUserId(), alertLimitDate)) {
			AlertMessage message = createMessage(vitalSign);
			
			publisher.publish(MqttTopicUtils.ALERT_TOPIC, message);
			log.info("Alert sent from userId: {}, vitalSign: {}", vitalSign.getUserId(), vitalSign.getType());
			
			this.saveAlert(vitalSign);
		} else {
			log.info("Alert already sent from this userId: {}", vitalSign.getUserId());
		}
		
	}
	
	public void sendMultiVitalSignAlert(List<VitalSign> vitalSigns) {
		
	}
	
	private void saveAlert(VitalSign vitalSign) {
		Alert alert = new Alert();
		alert.setId(UUID.randomUUID().toString());
		alert.setDate(new Date());
		alert.setScoreType(AlertScoreType.SINGLE_VITAL_SIGN);
		alert.setEdgeId(vitalSign.getEdgeId());
		alert.setNewsScore(vitalSign.getNewsScore());
		alert.setSensorId(vitalSign.getSensorId());
		alert.setUserId(vitalSign.getUserId());
		repository.save(alert);
	}
	
	private AlertMessage createMessage(VitalSign vitalSign) {
		AlertVitalSign alertVitalSign = new AlertVitalSign(vitalSign.getType(), vitalSign.getValue(), vitalSign.getNewsScore());
		
		AlertMessage message = new AlertMessage();
		message.setDate(new Date());
		message.setEdgeId(vitalSign.getEdgeId());
		message.setSensorId(vitalSign.getSensorId());
		message.setUserId(vitalSign.getUserId());
		message.setVitalSigns(Arrays.asList(alertVitalSign));
		return message;
	}
}
