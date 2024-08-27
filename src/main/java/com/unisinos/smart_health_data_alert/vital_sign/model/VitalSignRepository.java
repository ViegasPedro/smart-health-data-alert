package com.unisinos.smart_health_data_alert.vital_sign.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VitalSignRepository extends JpaRepository<VitalSign, String> {

	void deleteBySensorIdAndType(String sensorId, VitalSignType Type);
	
	List<VitalSign> findByUserId(String userId);
}
