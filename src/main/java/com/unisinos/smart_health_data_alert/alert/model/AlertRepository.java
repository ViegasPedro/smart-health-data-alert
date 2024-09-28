package com.unisinos.smart_health_data_alert.alert.model;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, String> {

	boolean existsByUserIdAndDateGreaterThan(String userId, Date date);
}
