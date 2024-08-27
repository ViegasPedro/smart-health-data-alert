package com.unisinos.smart_health_data_alert.alert.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unisinos.smart_health_data_alert.alert.model.AlertRepository;

@Service
public class AlertService {

	@Autowired
	private AlertRepository repository;
	
	public void send() {
		System.out.println("Alerta enviado");
		//verifica se precisa emitir o alerta ou se já foi enviado
		
		//emite o alerta
		
		//salva o alerta no banco
	}
}
