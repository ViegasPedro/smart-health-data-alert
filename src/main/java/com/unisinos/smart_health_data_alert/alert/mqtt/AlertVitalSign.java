package com.unisinos.smart_health_data_alert.alert.mqtt;

import com.unisinos.smart_health_data_alert.vital_sign.model.VitalSignType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlertVitalSign {
 
	private VitalSignType type;
	private String value;
	private int newsScore;
}
