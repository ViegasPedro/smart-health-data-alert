package com.unisinos.smart_health_data_alert.alert.model;

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
	
}
