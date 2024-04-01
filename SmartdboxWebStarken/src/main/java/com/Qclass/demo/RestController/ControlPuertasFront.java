package com.Qclass.demo.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.Qclass.demo.Services.StarkenServices;
import com.Qclass.demo.Services.Telegram;
import com.Qclass.demo.classes.CheckMeasureBulto;
import com.Qclass.demo.classes.DatosQB;
import com.Qclass.demo.classes.DatosResponse;
import com.Qclass.demo.classes.GpioModulo;
import com.Qclass.demo.classes.Measurement;
import com.Qclass.demo.classes.MyBean;
import com.Qclass.demo.classes.ServiceResponse;

import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PreAuthorize("hasAnyRole('ADMIN','FRONT')")
@RestController
@RequestMapping("/API/Puertas")
public class ControlPuertasFront {

	


	
	final static Logger logger = LoggerFactory.getLogger(ControlPuertasFront.class);
	
	@Autowired
	public DatosResponse datos;
	
	
	@Autowired
	MyBean myBean;
	
	@Autowired
	public Telegram alerta;
	
	@GetMapping("/Check/Puerta/Delantera")
	ServiceResponse validarPuertaDelantera() {

		double alto = 0;
		double largo = 0;
		double ancho = 0;
		double peso = 0;
		int toleranciaMedidasLimite =myBean.getToleranciaMedidasLimite();
		double toleranciaPesoMaximo = myBean.getPesoToleranciaMaxima();
//		String[] medidasLimite = datos.getPayload()[0].getMedidas_limite().split("x");
		int limiteMedidaAlto = myBean.getLimiteMedidaAlto() + + toleranciaMedidasLimite;
		int limiteMedidaLargo = myBean.getLimiteMedidaLargo() + toleranciaMedidasLimite;
		int limiteMedidaAncho = myBean.getLimiteMedidaAncho() + toleranciaMedidasLimite;
		double limitePeso = myBean.getLimitePeso() + toleranciaPesoMaximo;
		Measurement measurement = GpioModulo.validarMedidasCamara();
		peso = measurement.getPeso();
		alto = measurement.getAlto();
		largo = measurement.getLargo();
		ancho = measurement.getAncho();
		boolean MedidasCheck = false;
		
		String bulto = StarkenServices.getAttribute("BULTOS");
		String of = bulto.substring(14, 23);
		
		if (largo <= 0 || alto <0 || ancho <= 0) {
			
			return new ServiceResponse(506,
					"dimensiones en 0 - Para continuar debes colocar el bulto dentro del módulo");
		}

		if(peso > limitePeso) {
			
			System.out.println("508 - El Bulto of: "+of+ " ingresado supera el peso maximo " + peso);
			
			alerta.enviarAlerta("508 - El Bulto of: "+of+ " ingresado supera el peso maximo " + peso);
			
			return new ServiceResponse(508,"Peso maximo Excedido");
			
			
		}
		logger.info("MEDIDAS LIMITES alto "+ limiteMedidaAlto +" ancho " + limiteMedidaAncho +" largo" + limiteMedidaLargo );
		if (largo > 0 && largo <= limiteMedidaLargo && alto >= 0 && alto <= limiteMedidaAlto
				&& ancho > 0 && ancho <= limiteMedidaAncho) {
			MedidasCheck = true;
		

			if (GpioModulo.status_puertaDelantera() == false) {

				GpioModulo.ONLY_cerrarPuertaDelantera();
				// GpioModulo.Esperarhasta_puertaDelantera_Cerrada();
			} else {
				logger.info("Puerta delantera Cerrada");
			}

			if (GpioModulo.check_puertaDelantera_cerrada()) {
				logger.info("Retornando 200");
				return new ServiceResponse(200, "Puerta Cerrada");
			} else {
				logger.info("Retornando 506");
				return new ServiceResponse(506, "Error en la puerta Delantera");
			}

		} else {
			System.out.println("509 - Se ingreso un bulto: "+ of +" que supero las dimensiones maximas, Alto: " + alto +" Largo: " + largo + " Ancho: " + ancho );
			alerta.enviarAlerta("509 - Se ingreso un bulto: "+ of +" que supero las dimensiones maximas, Alto: " + alto +" Largo: " + largo + " Ancho: " + ancho );
			return new ServiceResponse(509, "Medidas Rechazadas - Tu envío execede las dimensiones máximas ");
		}

	}

}
