package com.unisinos.smart_health_data_alert.vital_sign.mqtt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unisinos.smart_health_data_alert.vital_sign.model.VitalSign;
import com.unisinos.smart_health_data_alert.vital_sign.service.VitalSignService;

@Component
public class VitalSignSubscriber implements IMqttMessageListener {

	@Autowired
	private VitalSignService vitalSignService;
		
	private ExecutorService pool = Executors.newFixedThreadPool(10);

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		 pool.execute(new MessageHandler(topic, message));
	}

	class MessageHandler implements Runnable {
		MqttMessage message;
		String topic;

		public MessageHandler(String topic, MqttMessage message) {
			this.message = message;
			this.topic = topic;
		}

		public void run() {
			System.out.println("Thread [ " + Thread.currentThread().getName() + "], Topic[ " + topic + "],  Message ["
					+ message + "] ");
			Gson gson = new GsonBuilder()
					.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
					.create();
			
			JSONObject json =new JSONObject(new String(message.getPayload()));
			VitalSign vitalSign = gson.fromJson(json.toString(), VitalSign.class);
						
			vitalSignService.processMessage(vitalSign);
		}
	}

}
