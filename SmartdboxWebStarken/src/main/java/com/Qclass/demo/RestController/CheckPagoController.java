package com.Qclass.demo.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Qclass.demo.SmartdboxWebStarkenApplication;

import com.Qclass.demo.Services.StarkenServices;
import com.Qclass.demo.Services.Telegram;
//import com.Qclass.demo.classes.DatosQB;
import com.Qclass.demo.classes.DatosQBService;
import com.Qclass.demo.classes.DatosResponse;
import com.Qclass.demo.classes.GpioModulo;
//import com.Qclass.demo.VariablesGlobales.VariablesGlobales;
import com.Qclass.demo.classes.LogStarken;
import com.Qclass.demo.classes.MyBean;
import com.Qclass.demo.classes.RespuestaServicio;
import com.Qclass.demo.classes.ServiceResponse;
import com.Qclass.demo.classes.respuesta;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

@PreAuthorize("hasAnyRole('ADMIN','FRONT')")
@RestController
@RequestMapping("/API/CheckPagoBulto")
public class CheckPagoController {

	final static Logger logger = LoggerFactory.getLogger(CheckPagoController.class);

	@Autowired
	public HttpSession httpSession;

	@Autowired
	public MyBean myBean;

	@Autowired
	public Telegram alerta;
	// @Autowired
	// private DatosQBService datos;

	// @Value("${LogStarken.IP:novalue}")
	String IP;
	// @Value("${LogStarken.:novalue}")
	String agenCodigo;
	// @Value("${LogStarken.NroModulo:novalue}")
	String NroModulo;
	// @Value("${LogStarken.DeviceName:novalue}")
	String DeviceName;
	boolean MB = false;

	// @Value("${Smartdbox.config.DeviceName:2222222}")
	// private String DeviceName;

	/*
	 * @GetMapping("/GetALL") List<Employee> all() { return repository.findAll(); }
	 * 
	 * 
	 * @PostMapping("/CheckPagoBulto") Employee newEmployee(@RequestBody Employee
	 * newEmployee) { return repository.save(newEmployee); }
	 */
	// Single item

	// @CrossOrigin(origins = "http://190.82.196.160:3000")
	@GetMapping("/Check/{idIN}")
	ServiceResponse one(@PathVariable String idIN) {

		IP = myBean.getIP();

		agenCodigo = myBean.getAgenCodigo();

		NroModulo = myBean.getNroModulo();

		DeviceName = myBean.getDeviceName();

		// System.out.println("ip " + IP + " " +  +" nromodulo " +
		// NroModulo +" devicename " + DeviceName);

		String Bulto = idIN;
		logger.info("Ckeck multi bulto " + idIN.substring(0, 2));

		if (idIN.substring(0, 2).equals("M-")) {

			Bulto = idIN.substring(2, idIN.length());
			logger.info("Bulto " + Bulto);
			MB = true;
		} else {
			Bulto = idIN;
			MB = false;
		}

		logger.warn("********** sessionid = " + httpSession.getId());

		// 200 OK
		// 500 Rechazado por Origen
		// 510 Barcode no corresponde a la of en curso
		// 400 Error No controlado en checkPago
		// 501 "Rechazado por pago
		// int codigo = 200;
		logger.info("Consulta servicio  = " + Bulto + "ID entrada = " + idIN + "Estado MB : " + MB);
		
		// String ejemplo ="00000000000000000000000001";
		// String ejemplo ="11111111111111 111 111 111 000";
		String ejemplo = "33333333333333111000111333";
		if (Bulto.length() != ejemplo.length()) {
			logger.info("Se escaneo un codigo erroneo: " + idIN);
			return new ServiceResponse(500, "BARCODE no tiene formato Starken");
		}

		String of = Bulto.substring(14, 23);

		List<String> bultosguardados = new ArrayList<>();
		List<String> bultos = new ArrayList<>();
		
		

		try {
			bultosguardados = (List<String>) StarkenServices.getAttributeS("BULTOS");

		} catch (Exception e) {
			logger.warn("---> Exception : " + e);
		}

		logger.warn("---> Se consulto BARCODE : " + Bulto);

		try {

			StarkenServices.setAttribute("BULTO_EN_Proceso", Bulto);

			String auxiliar = (String) StarkenServices.getAttribute("BULTO_EN_Proceso");

			System.out.println("Variable auxiliar checkPago " + auxiliar);

			logger.info("---> BULTO_EN_Proceso : " + Bulto);

		} catch (Exception e) {
			logger.warn("---> Exception : " + e);
		}

		ServiceResponse r2 = new ServiceResponse(400, "500 - default");

		try {

			r2 = StarkenServices.PostCheckearPagoOF(Bulto);

			logger.info("---> r2 : " + r2);

			switch (r2.getStatus()) {
			case 200:
				// guardar bulto en proceso
				StarkenServices.setAttribute("BULTO_EN_Proceso", Bulto);

				// pago OK o contado
				if (MB == true) {

					logger.debug("hay bultos guardados");

					if (bultosguardados.size() == 0) {

						logger.info("No hay Bultos Guardados Esto no deberia ocurrir");

						AbrirPuertaDelantera();

						return new ServiceResponse(200, bultosguardados);

					} else {

						if (bultosguardados.get(0).substring(14, 23).equals(of)) {

							logger.info("Session : bultos guardados coinciden con el codigo consultado");

							// Abrir Puerta delantera

							AbrirPuertaDelantera();
							TimerTask alerta = new TimerTask() {
								public void run() {
									// Este código se ejecutará cuando el temporizador expire
									Telegram.enviarAlertaStatic("El cliente no ha dado Confirmar ");
								}
							};

							Timer temporizador = new Timer(true);
							temporizador.schedule(alerta, 50000);
							
							temporizador.cancel();
							return new ServiceResponse(200, bultosguardados);

						} else {

							// StarkenServices.setAttribute("BULTOS", "");

							// eliminar el archivo y dar 200
							logger.warn("Session: Bulto consultado no coincide con los guardados anteriormente");
							logger.info("Session : OLD OF" + bultosguardados.get(0).substring(14, 23));
							logger.info("Session : NEW OF #################" + of);
							return new ServiceResponse(510, bultosguardados);
							// TO_DO CORREGIR MULTI BULTOS

//							AbrirPuertaDelantera();
//
//							return new ServiceResponse(200, bultosguardados);
						}

					}

				} else {

					logger.info("Sesion nueva");

					// no hay bultos, guardar los nuevos

					bultos = StarkenServices.BultosOF(Bulto);

					try {
						if (bultos.size() > 0) {

							StarkenServices.setAttribute("BULTOS", bultos);
							StarkenServices.setAttribute("BULTOS_RECEPCIONADOS", "");
							logger.info("Session : bultos asignados" + bultos.size());

						} else {
							return new ServiceResponse(511, bultos);
						}
					} catch (Exception e) {
						return new ServiceResponse(400, bultos);
					}

					// Abrir Puerta delantera

					AbrirPuertaDelantera();
					TimerTask alerta = new TimerTask() {
						public void run() {
							// Este código se ejecutará cuando el temporizador expire
							Telegram.enviarAlertaStatic("El cliente no ha dado Confirmar ");
						}
					};

					Timer temporizador = new Timer(true);
					temporizador.schedule(alerta, 50000);
					
					temporizador.cancel();

					return new ServiceResponse(200, bultos); // return new
					// ServiceResponse(/*codigo*/200, bultos);
				}

			case 500:
				// System.out.println("Valor r2" + r2); //Rechazado por Origen
				// return new ServiceResponse(500, "Rechazado por Origen ");

				alerta.enviarAlerta( "OF: " + of +" 500 - Rechazado por origen en checkPago ");
				return new ServiceResponse(500, "Rechazado por Origen ");
			// break;
			case 501:
				// Rechazado por pago //return new ServiceResponse(501, "Rechazado por pago");
				alerta.enviarAlerta("OF: " + of +" 501 - Rechazado por pago en checkPago");
				return new ServiceResponse(501, "Rechazado por pago");
			// break;

			default: //
				// Default secuencia de sentencias.
				// return new ServiceResponse(400,"Error No controlado en checkPago");
				alerta.enviarAlerta("OF: " + of +" 400 - Error no controlado en checkPago");
				return new ServiceResponse(r2.getStatus(), "Error No controlado en checkPago");
			}

		} catch (Exception e) {
			logger.warn("---> Exception : " + e);
		}

		logger.warn("---> r2 : " + r2);

		return r2;

	}
	/*
	 * @PutMapping("/CheckPagoBulto/{id}") Employee replaceEmployee(@RequestBody
	 * Employee newEmployee, @PathVariable Long id) {
	 * 
	 * return repository.findById(id) .map(employee -> {
	 * employee.setName(newEmployee.getName());
	 * employee.setRole(newEmployee.getRole()); return repository.save(employee); })
	 * .orElseGet(() -> { newEmployee.setId(id); return
	 * repository.save(newEmployee); }); }
	 * 
	 * 
	 * @DeleteMapping("/Delete/{id}") void deleteEmployee(@PathVariable Long id) {
	 * logger.warn("deleteEmployee "+ id); }
	 */

	private void AbrirPuertaDelantera() {
		// TODO Auto-generated method stub

		double peso = GuardarPeso_TARA(myBean);

		// StarkenServices.setAttribute("PESO_TARA", peso);

		logger.info("Abriendo puerta delantera : ");
		logger.info("Asignando peso tara(DEMO) : 0.01");
		logger.info("PESO_TARA en session: " + httpSession.getId() + " ASIGNADO ->" + peso);

		logger.info("Abriendo la puerta delantera...");
		// Secuencia para abrir puerta delantera

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

	

	}

	/*
	 * Thread newThread = new Thread(() -> { logger.info( "PESO_TARA en session: " +
	 * httpSession.getId() + " ASIGNADO ->" + GuardarPeso_TARA(2500, "COM3")); });
	 * newThread.start();
	 * 
	 * try { GpioModulo.EncenderLamparaLed(); GpioModulo.ONLY_CerrarPuertaTrasera();
	 * // Thread.sleep(1000); GpioModulo.Esperarhasta_puertatrasera_Cerrada();
	 * GpioModulo.ONLY_AbrirPuertaDelantera(); } catch (InterruptedException e) { //
	 * // TODO Auto-generated catch block e.printStackTrace();
	 * logger.error("InterruptedException : " + e);
	 * 
	 * }
	 */

	private double GuardarPeso_TARA(MyBean beam) {
		// TODO Auto-generated method stub

		// GpioModulo.PesoTarea();

		// double peso = GpioModulo.leerDatosBalanza();

		double peso = GpioModulo.leerDatosBalanza();
		// double peso = GpioModulo.GetPeso(TiempoCaptura,driverdevtty_COM); // 2500
		// "COM3"

		// RIO : comebntado double peso = GpioModulo.GetPeso(beam); // 2500 "COM3"

		// RIO : descomentar httpSession.setAttribute("PESO_TARA", peso);

		// System.out.println("ESTOY EN EL GUARDAR PESO TARA DEL CHECK PAGO: " + peso);

		StarkenServices.setAttribute("PESO_TARA", peso);
		// return peso;
		return peso;

	}
}
