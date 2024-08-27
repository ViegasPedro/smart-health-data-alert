package com.unisinos.smart_health_data_alert.vital_sign.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VitalSignType {
	
	TEMPERATURE("TEMPERATURE"), HEARTRATE("HEARTRATE"), BLOODPRESSURE("BLOODPRESSURE");
	
	private final String type;

	@Override
	public String toString() {
		return type;
	}
	
}
