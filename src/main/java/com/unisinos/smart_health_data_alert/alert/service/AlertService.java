package com.unisinos.smart_health_data_alert.alert.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unisinos.smart_health_data_alert.alert.model.Alert;
import com.unisinos.smart_health_data_alert.alert.model.AlertRepository;
import com.unisinos.smart_health_data_alert.alert.model.AlertScoreType;
import com.unisinos.smart_health_data_alert.alert.mqtt.AlertMessage;
import com.unisinos.smart_health_data_alert.alert.mqtt.AlertVitalSign;
import com.unisinos.smart_health_data_alert.commons.mqtt.HospitalPublisher;
import com.unisinos.smart_health_data_alert.commons.mqtt.MqttTopicUtils;
import com.unisinos.smart_health_data_alert.vital_sign.model.VitalSign;
import com.unisinos.smart_health_data_alert.vital_sign.model.VitalSignConverter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlertService {

	@Autowired
	private AlertRepository repository;

	@Autowired
	private HospitalPublisher publisher;

	@Autowired
	private VitalSignConverter converter;

	public void sendSingleVitalSignAlert(VitalSign vitalSign) throws MqttPersistenceException, MqttException {
		AlertMessage message = createMessage(vitalSign);
		publisher.publish(MqttTopicUtils.HOSPITAL_ALERT_TOPIC, message);
		this.saveAlert(message);
		log.info("Single vital sign alert sent from userId: {}, vitalSign: {}", vitalSign.getUserId(), vitalSign.getType());
	}

	public void sendMultiVitalSignAlert(List<VitalSign> vitalSigns) throws MqttPersistenceException, MqttException {
		AlertMessage message = createMessage(vitalSigns);
		publisher.publish(MqttTopicUtils.HOSPITAL_ALERT_TOPIC, message);
		this.saveAlert(message);
		log.info("Multi vital sign alert sent from userId: {}", message.getUserId());
	}

	private void saveAlert(AlertMessage message) {
		Alert alert = new Alert();
		alert.setId(UUID.randomUUID().toString());
		alert.setDate(message.getDate());
		alert.setScoreType(message.getVitalSigns().size() == 1 ? AlertScoreType.SINGLE_VITAL_SIGN : AlertScoreType.MULTI_VITAL_SIGN);
		alert.setEdgeId(message.getEdgeId());
		alert.setNewsScore(sumNewsScore(message.getVitalSigns()));
		alert.setSensorId(message.getSensorId());
		alert.setUserId(message.getUserId());
		repository.save(alert);
	}
	
	private AlertMessage createMessage(VitalSign vitalSign) {
		AlertVitalSign alertVitalSign = new AlertVitalSign(vitalSign.getType(), vitalSign.getValue(),
				vitalSign.getNewsScore());

		AlertMessage message = new AlertMessage();
		message.setDate(new Date());
		message.setEdgeId(vitalSign.getEdgeId());
		message.setSensorId(vitalSign.getSensorId());
		message.setUserId(vitalSign.getUserId());
		message.setVitalSigns(Arrays.asList(alertVitalSign));
		return message;
	}

	private AlertMessage createMessage(List<VitalSign> vitalSigns) {
		List<AlertVitalSign> alertVitalSigns = vitalSigns.stream()
				.map(converter::convert)
				.collect(Collectors.toList());

		AlertMessage message = new AlertMessage();
		message.setDate(new Date());
		message.setEdgeId(vitalSigns.get(0).getEdgeId());
		message.setUserId(vitalSigns.get(0).getUserId());
		message.setVitalSigns(alertVitalSigns);
		return message;
	}
	
	private int sumNewsScore(List<AlertVitalSign> alertVitalSignList) {
        return alertVitalSignList.stream()
                .mapToInt(AlertVitalSign::getNewsScore)
                .sum();
    }
}
