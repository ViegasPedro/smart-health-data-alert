package com.unisinos.smart_health_data_alert.alert.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Alert {
	
	@Id
	private String id;
	private Date date;
	private AlertScoreType scoreType;
	private int newsScore;
	private String edgeId;
	private String sensorId;
	private String userId;	
	
}
