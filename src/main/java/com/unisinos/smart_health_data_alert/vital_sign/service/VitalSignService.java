package com.unisinos.smart_health_data_alert.vital_sign.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unisinos.smart_health_data_alert.alert.service.AlertService;
import com.unisinos.smart_health_data_alert.commons.mqtt.FogServerPublisher;
import com.unisinos.smart_health_data_alert.commons.mqtt.MqttTopicUtils;
import com.unisinos.smart_health_data_alert.news2.NewsScoreService;
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
	private FogServerPublisher publisher;

	@Transactional
	public void processMessage(VitalSign vitalSign) {
		try {
			int newsScore = NewsScoreService.calculateNewsScore(vitalSign);

			vitalSign.setNewsScore(newsScore);

			// deletes the old value of this vital sign stored in the database
			this.repository.deleteBySensorIdAndType(vitalSign.getSensorId(), vitalSign.getType());

			if (newsScore > NewsScoreService.NORMAL_SCORE) {
				vitalSign.setId(UUID.randomUUID().toString());
				this.repository.save(vitalSign);

				if (newsScore == NewsScoreService.SINGLE_SCORE_LIMIT) {
					this.alertService.sendSingleVitalSignAlert(vitalSign);
					return;
				}

				int userTotalNewsScore = calculateUserTotalNewsScore(vitalSign.getUserId());
				if (userTotalNewsScore >= NewsScoreService.MULTI_SCORE_LIMIT) {
					//this.alertService.send();
				}
			}
			//send the vital sign to fog server
			publisher.publish(MqttTopicUtils.VITAL_SIGN_TOPIC, vitalSign);
		} catch (Exception e) {
			log.error("Error sending alert. {}", e.getLocalizedMessage());
		}
	}

	private int calculateUserTotalNewsScore(String userId) {
		List<VitalSign> vitalSigns = this.repository.findByUserId(userId);
		int totalScore = 0;
		for (VitalSign vitalSign : vitalSigns) {
			totalScore += vitalSign.getNewsScore();
		}
		return totalScore;
	}
}
