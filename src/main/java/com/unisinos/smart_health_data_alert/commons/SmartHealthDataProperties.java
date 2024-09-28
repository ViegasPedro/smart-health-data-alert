package com.unisinos.smart_health_data_alert.commons;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@ConfigurationProperties(prefix = "unisinos.smart-health-data")
public class SmartHealthDataProperties {

	private Mqtt mqtt;
	private FogServer fogServer;
	
	@Getter
	@Setter
	public static class Mqtt {
		private String serverUrl;
		private String clientId;
		private String user;
		private String password;
	}
	
	@Getter
	@Setter
	public static class FogServer {
		private Mqtt mqtt;
	}
}
