package com.unisinos.smart_health_data_alert.vital_sign.model;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.unisinos.smart_health_data_alert.alert.mqtt.AlertVitalSign;

@Component
public class VitalSignConverter implements Converter<VitalSign, AlertVitalSign>{

	@Override
	public AlertVitalSign convert(VitalSign source) {
		return new AlertVitalSign(source.getType(), source.getValue(), source.getNewsScore());
	}

}
