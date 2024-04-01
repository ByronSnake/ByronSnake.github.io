
package com.Qclass.demo.RestController;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Qclass.demo.Services.StarkenServices;
import com.Qclass.demo.Services.Telegram;
import com.Qclass.demo.classes.BultosRestantes;
import com.Qclass.demo.classes.DatosQB;
import com.Qclass.demo.classes.DatosResponse;
import com.Qclass.demo.classes.GpioModulo;
import com.Qclass.demo.classes.Measurement;
import com.Qclass.demo.classes.MyBean;
import com.Qclass.demo.classes.ServiceResponse;
import com.Qclass.demo.classes.ValidarEncomienda;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import jakarta.servlet.http.HttpSession;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

@PreAuthorize("hasAnyRole('ADMIN','FRONT')")
@RestController
@RequestMapping("/API/CheckMeasurePackage")
public class CheckMeasurePackageController {

	final static Logger logger = LoggerFactory.getLogger(CheckMeasurePackageController.class);

	@Autowired
	public HttpSession httpSession;

	@Autowired
	public MyBean myBean;

	//@Autowired
	//private DatosResponse datos;
	
	@Autowired
	public Telegram alerta;

	@GetMapping("/bultoEnSession")
	ServiceResponse all() {
		logger.info("ConsultaServicio CheckMeasurePackage");

		logger.warn("********** sessionid = " + httpSession.getId());

		ServiceResponse sr = new ServiceResponse(400, "DEFAULT");

		// Desarrollar Cerrado puerta delantera

		logger.info("Puerta Delantera Cerrada");

		int alto = 0;
		int largo = 0;
		int ancho = 0;

		double Peso_maximoBalanza = myBean.getLimitePeso();
		int toleranciaMedidasLimite = myBean.getMedidasToleranciaMaxima();
		double Peso_maximoConsultaCheckMeasure = 0.32;
		String codUsuario = myBean.getCodUsuario();
		String  ubiCodigo = myBean.getUbiCodigo();
		// double PESO_conBulto = GpioModulo.GetPeso(); // ORIGINAL QUE OBTIENE EL PESO
		// POR PUERTO SERIAL
		// double PESO_conBulto = 0.15;
		double PESO_conBulto = 2;
		double PESO_TARA = 0;
		double PESO_final = 0;

		String BULTO = "";

		try { // revisar si el bulto esta en sesion

			BULTO = StarkenServices.getAttribute("BULTO_EN_Proceso");

		} catch (Exception e) {
			logger.error("---> Exception : " + e);
		}
		logger.info("---> BULTO : " + BULTO);
		if (BULTO.equals("")) {

			return new ServiceResponse(599, "Bulto vacio en la session");

		}

		try {

			logger.info("Leyendo pesotara " + PESO_TARA);

			String pesoString = StarkenServices.getAttribute("PESO_TARA");

			System.out.println("STring peso tara " + pesoString);

			PESO_TARA = Double.parseDouble(pesoString);

		} catch (Exception e) {
			logger.error("---> Exception : " + e);
			PESO_TARA = 0.0;
		}

		Measurement measurement = GpioModulo.validarMedidasCamara();

		alto = measurement.getAlto();
		ancho = measurement.getAncho();
		largo = measurement.getLargo();
		PESO_conBulto = measurement.getPeso();
		// PESO_conBulto = 2.0;
		PESO_final = Math.round(Math.abs(PESO_conBulto - PESO_TARA) * 100.0) / 100.0;
		logger.info("PESO CON BULTO " + PESO_conBulto + "PESO FINAL " + PESO_final);
		// PESO_final = 2;

		StarkenServices.setAttribute("pESO_final", PESO_final);
		// evaluar medidas camara

		boolean pesoCheck = false;
		boolean MedidasCheck = false;
		//String[] medidasLimite = datos.getPayload()[0].getMedidas_limite().split("x");
		int altoMaximo = myBean.getLimiteMedidaAlto() + toleranciaMedidasLimite;
		int largoMaximo = myBean.getLimiteMedidaLargo() + toleranciaMedidasLimite;
		int anchoMaximo = myBean.getLimiteMedidaAncho() + toleranciaMedidasLimite;
		int length = largo;
		int height = alto;
		int width = ancho;
		int largo2 = (int) length;
		int alto2 = (int) height;
		int ancho2 = (int) width;

		if (PESO_final >= 0.01 && PESO_final <= Peso_maximoBalanza) {
			pesoCheck = true;
		} else if (PESO_final > Peso_maximoBalanza) {
			String of = BULTO.substring(14, 23);
			alerta.enviarAlerta("OF: " + of +"Excede el peso maximo");
			return new ServiceResponse(508, "Peso maximo Excedido");

		} else if (PESO_final == 0 && length > 0 && length <= largoMaximo || height > 0 && height <= altoMaximo
				|| width > 0 && width <= anchoMaximo) {
			PESO_final = 0.01;
			pesoCheck = true;

		} else {
			
			
			return new ServiceResponse(506,
					"Peso < 0.01 = peso en 0 - Para continuar debes colocar el bulto dentro del módulo");
		}

		if (length <= 0 || height < 0 || width <= 0) {

			if (GpioModulo.status_puertaDelantera()) {
				if (GpioModulo.status_puertaTrasera() == false) {
					logger.info("-------Cerrando puerta trasera-------");
					GpioModulo.ONLY_CerrarPuertaTrasera();
					if (GpioModulo.check_puertaTrasera_cerrada()) {
						logger.info("" + GpioModulo.status_puertaTrasera());
						logger.info("-------Abriendo puerta Delantera-------");
						// GpioModulo.Esperarhasta_puertatrasera_Cerrada();
						GpioModulo.ONLY_AbrirPuertaDelantera();
						GpioModulo.encenderLuz();
					}

				} else {
					logger.info("-------Abriendo puerta delantera-------");
					GpioModulo.ONLY_AbrirPuertaDelantera();
					GpioModulo.encenderLuz();
				}

			} else {
				logger.info("La puerta delantera esta abierta");
			}

			return new ServiceResponse(506,
					"dimensiones en 0 - Para continuar debes colocar el bulto dentro del módulo");
		}

		if (length > 0 && length <= largoMaximo && height >= 0 && height <= altoMaximo && width > 0
				&& width <= anchoMaximo) {
			MedidasCheck = true;

		} else {

			if (GpioModulo.status_puertaDelantera()) {
				if (GpioModulo.status_puertaTrasera() == false) {
					logger.info("-------Cerrando puerta trasera-------");
					GpioModulo.ONLY_CerrarPuertaTrasera();
					if (GpioModulo.check_puertaTrasera_cerrada()) {
						logger.info("" + GpioModulo.status_puertaTrasera());
						logger.info("-------Abriendo puerta Delantera-------");
						// GpioModulo.Esperarhasta_puertatrasera_Cerrada();
						GpioModulo.ONLY_AbrirPuertaDelantera();
						GpioModulo.encenderLuz();
					}

				} else {
					logger.info("-------Abriendo puerta delantera-------");
					GpioModulo.ONLY_AbrirPuertaDelantera();
					GpioModulo.encenderLuz();
				}

			} else {
				logger.info("La puerta delantera esta abierta");
			}
			String of = BULTO.substring(14, 23);
			alerta.enviarAlerta("OF: " +of +" Excede las medidas maximas, Alto: " + alto + " Largo: " + largo + " Ancho: " + ancho);
			return new ServiceResponse(509, "Medidas Rechazadas - Tu envío execede las dimensiones máximas ");
		}

		logger.info("MedidasCheck: " + MedidasCheck + "     pesoCheck:" + pesoCheck);

		// si pasa el filtro de peso y medidas volumetricas esta bien
		if (MedidasCheck && pesoCheck == true) {

			// evaluar si consultar peso al servicio como regla de negocio
			if (PESO_final > Peso_maximoConsultaCheckMeasure) {
				// Consultar Medidas a Servicio Starken

				// transformar los double a string
				logger.info("CONSULTA CHECKMEASURE LARGO " + length + " ANCHO " + width + "ALTO " + height
						+ "PESO	FINAL" + PESO_final + " codUsuario " + codUsuario + " UbiCodigo " + ubiCodigo);
				sr = consultarServicioStarkenCheckMeasure(BULTO, String.valueOf(height), String.valueOf(width),
						String.valueOf(length), String.valueOf(PESO_final),codUsuario,ubiCodigo);

				int status = sr.getStatus();
				String texto = (String) sr.getMessage();

				String[] mensaje = texto.split("-");

				if (mensaje.length >= 2) {

					if (status == 500) {
				        try {
				            int numero = Integer.parseInt(mensaje[0].trim());
				            sr = new ServiceResponse(numero, mensaje[1]);
				            alerta.enviarAlerta("Servicio CheckMeasure: Status: " + numero + " Mensaje: " + mensaje[1]);
				        } catch (NumberFormatException e) {
				            sr = new ServiceResponse(status, texto);
				            alerta.enviarAlerta("Servicio CheckMeasure: Status: " + status + " Mensaje: " + texto);
						       
				        }
				    } else {
				        // Si el status no es 500, retorna status y texto
				        sr = new ServiceResponse(status, texto);
				    }
				} else {

					// retorna status y texto en el caso en el que no hay guion ("-") en el mensaje
					sr = new ServiceResponse(status, texto);
				}

			} else {
				logger.info("LOGEANDO LARGO " + largo2 + " ANCHO " + ancho2 + "ALTO " + alto2 + "PESO	FINAL"
						+ PESO_final);
				ValidarEncomienda data = new ValidarEncomienda(BULTO, String.valueOf(PESO_final), String.valueOf(alto2),
						String.valueOf(largo2), String.valueOf(ancho2));

				StarkenServices.log_totem("BD Peso Sobre", BULTO, data);

				sr = new ServiceResponse(200, "Medidas OK - No consulta CheckMeasure");
			}

		}

		/*
		 * try { Thread.sleep(5000); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		if (sr.getStatus() == 200) {
			return sr;
		} else {

			if (GpioModulo.status_puertaDelantera()) {
				if (GpioModulo.status_puertaTrasera() == false) {
					logger.info("-------Cerrando puerta trasera-------");
					GpioModulo.ONLY_CerrarPuertaTrasera();
					if (GpioModulo.check_puertaTrasera_cerrada()) {
						logger.info("-------Abriendo puerta Delantera-------");
						// GpioModulo.Esperarhasta_puertatrasera_Cerrada();
						GpioModulo.ONLY_AbrirPuertaDelantera();
					}

				} else {
					logger.info("-------Abriendo puerta delantera-------");
					GpioModulo.ONLY_AbrirPuertaDelantera();
				}

			} else {
				logger.info("La puerta delantera esta abierta");
			}

			return sr;

			// logger.info("TerminoConsultaServicio");
			// return new ServiceResponse(500, "Error en el servicio ");
			// return new ServiceResponse(502, "Error en el servicio ");
			// return new ServiceResponse(506, "Error en el servicio ");
			// return new ServiceResponse(509, "Error en el servicio ")
		}

	}

	private ServiceResponse consultarServicioStarkenCheckMeasure(String bulto, String alto, String ancho, String largo,
			String pESO_final, String codUsuario, String ubiCodigo) {
		// aqui puede ser error 502 / El peso y medidas de tu envío no concuerdan con lo
		// ingresado en el totem
		// aqui puede ser error 500

		ServiceResponse sr = new ServiceResponse(500, "DEFAULT - consultarServicioStarkenCheckMeasure");
		logger.info("Consultando por >> " + "  alto: " + alto + "  ancho: " + ancho + "  largo: " + largo + "  PESO: "
				+ pESO_final + " CodUsuario " + codUsuario + " UbiCodigo " + ubiCodigo);

		// validar con llamada a servicio de starken
		sr = StarkenServices.PostValidarBUlto(bulto, pESO_final, alto, largo, ancho,codUsuario,ubiCodigo);
		logger.info("VALOR VARIABLE SR " + sr.getStatus());
		// StarkenServices.setAttribute("pESO_final", pESO_final);

		// System.out.println("ANTES DE CREAR EL ARCVHIVO PESOFINAL");

		logger.info("sr = ->" + sr.getStatus());
		logger.info("sr = ->" + sr.getMessage());

		
		try {
			return sr;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ServiceResponse(500, "error exepcion");
		}

	}

}
