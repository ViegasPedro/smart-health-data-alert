package com.unisinos.smart_health_data_alert.vital_sign.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VitalSignType {

	
	TEMPERATURE, 
	HEARTRATE, 
	BLOODPRESSURE, 
	RESPIRATIONRATE, 
	SPO2, 
	OXYGEN, 
	CONSCIOUSNESS;

//	TEMPERATURE("TEMPERATURE"), 
//	HEARTRATE("HEARTRATE"), 
//	BLOODPRESSURE("BLOODPRESSURE"), 
//	RESPIRATIONRATE("RESPIRATIONRATE"), 
//	SPO2("SPO2"), 
//	OXYGEN("OXYGEN"), 
//	CONSCIOUSNESS("CONSCIOUSNESS");
//	
//	private final String type;
//
//	@Override
//	public String toString() {
//		return type;
//	}
	
}
