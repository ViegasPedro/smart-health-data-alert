package com.unisinos.smart_health_data_alert.vital_sign.service;

import java.util.List;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.unisinos.smart_health_data_alert.alert.service.AlertService;
import com.unisinos.smart_health_data_alert.commons.mqtt.MqttPublisher;
import com.unisinos.smart_health_data_alert.commons.mqtt.MqttTopicUtils;
import com.unisinos.smart_health_data_alert.news2.NewsScoreService;
import com.unisinos.smart_health_data_alert.vital_sign.model.InvalidVitalSignValueException;
import com.unisinos.smart_health_data_alert.vital_sign.model.VitalSign;
import com.unisinos.smart_health_data_alert.vital_sign.model.VitalSignRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VitalSignService {

	@Autowired
	private VitalSignRepository repository;

	@Autowired
	private AlertService alertService;
	
	@Autowired
	private MqttPublisher publisher;

	@Transactional
	public void processMessage(VitalSign vitalSign) {
		try {
			if (!StringUtils.hasLength(vitalSign.getValue()))
				throw new InvalidVitalSignValueException();
			
			int newsScore = NewsScoreService.calculateNewsScore(vitalSign);
			vitalSign.setNewsScore(newsScore);

			//deletes the old value of this vital sign stored in the database
			this.repository.deleteBySensorIdAndType(vitalSign.getSensorId(), vitalSign.getType());

			//process vital sign and trigger alerts if necessary
			if (newsScore > NewsScoreService.NORMAL_SCORE) {
				this.handleVitalSignWithAbnormalScore(vitalSign);
			}
			//send the vital sign to fog server
			this.publisher.publish(MqttTopicUtils.FOG_VITAL_SIGN_TOPIC, vitalSign);
		
		} catch (InvalidVitalSignValueException e) {
			log.error("Ignoring invalid vital sign value");
		} catch (Exception e) {
			log.error("Error sending alert. {}", e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
    private void handleVitalSignWithAbnormalScore(VitalSign vitalSign) throws JsonProcessingException, MqttPersistenceException, MqttException {
        log.info("Saving vital sign with abnormal NEWS score. Type: {}, Value: {}, Score: {}", 
                 vitalSign.getType(), vitalSign.getValue(), vitalSign.getNewsScore());

        vitalSign.setId(UUID.randomUUID().toString());
        repository.save(vitalSign);

        if (vitalSign.getNewsScore() == NewsScoreService.SINGLE_SCORE_LIMIT) {
            alertService.sendSingleVitalSignAlert(vitalSign);
        }

        List<VitalSign> vitalSigns = repository.findByUserId(vitalSign.getUserId());
        int userTotalNewsScore = calculateUserTotalNewsScore(vitalSigns);
        if (userTotalNewsScore >= NewsScoreService.MULTI_SCORE_LIMIT) {
            alertService.sendMultiVitalSignAlert(vitalSigns);
        }
    }
    
    private int calculateUserTotalNewsScore(List<VitalSign> vitalSigns) {
		int totalScore = 0;
		for (VitalSign vitalSign : vitalSigns) {
			totalScore += vitalSign.getNewsScore();
		}
		return totalScore;
	}
}
