package com.Qclass.demo.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Qclass.demo.classes.GpioModulo;
import com.Qclass.demo.classes.MyBean;
import com.Qclass.demo.classes.ServiceResponse;
import com.Qclass.demo.Services.Telegram;
import com.Qclass.demo.classes.AlertasBot;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;

import jakarta.servlet.http.HttpSession;

@PreAuthorize("hasAnyRole('ADMIN','DASHBOARD')")
@RestController
@RequestMapping("/API/TestController")
public class TestController {

	final static Logger logger = LoggerFactory.getLogger(TestController.class);

	@Autowired
    private Telegram alerta;

	
	@Autowired
	public HttpSession httpSession;

	@Autowired
	public MyBean myBean;

	// @PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/TEST")
	ServiceResponse all() {
		logger.info("sessionid " + httpSession.getId());

		// GpioModulo.GetPortNames();

		// HttpHeaders headers = new HttpHeaders();
		// headers.set("Access-Control-Allow-Origin", httpSession.getId());
		// ResponseEntity RE = ResponseEntity.ok().headers(headers).body(new
		// ServiceResponse(200, "BIEN CON HEADER"));
		// ResponseEntity RE = ResponseEntity.ok().body(new ServiceResponse(200, "BIEN
		// CON HEADER"));

		// logger.warn("RE.getHeaders() "+ RE.getHeaders() );
		// headers.set("X-Get-Header", "ExampleHeader");

		return new ServiceResponse(200, "BIEEEEEEEEEEEEEN" + "SerialPortNames:" + "   " + myBean.toString());

		// String ejemplo ="33333333333333111000111333";

		/*
		 * String mensaje = "{\"idInstancia\": 1317," //modificacion el 6 de febrero
		 * 2023 a peticion de QUEOP
		 * 
		 * + "\"IP\": \"10.10.10.11\"," + "\"idEncuesta\": 6252," +
		 * "\"codigoSucursal\": \"SA\"," + "\"rPreguntasEncuesta\": [{" +
		 * "\"respuesta\": \""+"XXX_NOTA-XXX"+"\","// <<<< valor encuesta 0=roja
		 * 1=amarilla 2=verde\r\n" + "\"idPregunta\": 136907" + "}]," +
		 * "\"respuestaNPS\": \"\"," + "\"comentarioNPS\": \"\"," +
		 * "\"respuestaDC\": \"\"," + "\"comentarioDC\": \"\"," +
		 * "\"rPreguntasAbiertas\": [{" + "\"respuesta\": \""+"xxxx-OF-xxxx"+"\",  "//
		 * <<<< OF\r\n" + "\"idPregunta\": 70" + "}]" + "}";
		 * 
		 * 
		 * logger.info("ejemplo >" + mensaje);
		 * 
		 * 
		 * EncuestaClass Encuesta = new EncuestaClass();
		 * 
		 * Encuesta.idInstancia = 1317; Encuesta.IP="10.10.10.11";
		 * Encuesta.idEncuesta=6252; Encuesta.codigoSucursal="SA"; R_Preguntas
		 * rPreguntasEncuesta= new R_Preguntas("XXX_NOTA-XXX", 136907);
		 * Encuesta.agregarRespuestaPreguntasEncuesta(rPreguntasEncuesta); R_Preguntas
		 * rPreguntasAbiertas= new R_Preguntas("xxxx-OF-xxxx", 70);
		 * Encuesta.agregarRespuestaPreguntasAbiertas(rPreguntasAbiertas);
		 * 
		 * 
		 * 
		 * 
		 * 
		 * EncargosClass ECs = new EncargosClass(); ECs.Encargos.add(new
		 * EncargoClass("92003231001735959414446001"));
		 */

		// ServiceResponse sr =
		// StarkenServices.PostCheckearPagoOF("33333333333000959414446000");
		// ServiceResponse sr =
		// StarkenServices.PostCheckearPagoOF("33333333333000959419005000");

		/*
		 * String tipo = "BD TEST"; logger.info("substring(0, 1) = " + tipo.substring(0,
		 * 2));
		 * 
		 * if(!tipo.substring(0, 2).equals("BD")) {
		 * logger.info("logger no tiene BD >> + no se envia al servicio de BD    RETURN"
		 * );
		 * 
		 * }else { logger.info("Logear en BD    RETURN");
		 * StarkenServices.log_totem(tipo,ejemplo,Encuesta); }
		 * 
		 * 
		 */

		/*
		 * String tipo = "BD TEST"; logger.info("substring(0, 1) = " + tipo.substring(0,
		 * 2));
		 * 
		 * if(!tipo.substring(0, 2).equals("BD")) {
		 * logger.info("logger no tiene BD >> + no se envia al servicio de BD    RETURN"
		 * );
		 * 
		 * }else { logger.info("Logear en BD    RETURN");
		 * StarkenServices.log_totem(tipo,ejemplo,Encuesta); }
		 * 
		 * 
		 */

		/*
		 * String ejemplo = "{\r\n" + "    \"Encargos\": [\r\n" + "        {\r\n" +
		 * "            \"ENCACODIGOBARRA\": \"92003231001735959414446001\"\r\n" +
		 * "        },\r\n" + "        {\r\n" +
		 * "            \"ENCACODIGOBARRA\": \"92003231001735959414446002\"\r\n" +
		 * "        },\r\n" + "        {\r\n" +
		 * "            \"ENCACODIGOBARRA\": \"92003231001735959414446003\"\r\n" +
		 * "        },\r\n" + "        {\r\n" +
		 * "            \"ENCACODIGOBARRA\": \"91003231001735959414446004\"\r\n" +
		 * "        }\r\n" + "    ]\r\n" + "}";
		 * 
		 */
		// logger.info("ejemplo >" + ejemplo);

		// return sr;

	}

	@GetMapping("TEST5")
	ServiceResponse all5() {
		
		//AlertasBot.enviarMensajeWhatsApp("+56940079802", " Hola Estimado, Se cayo el qb ¬¬");
		httpSession.invalidate();
		return new ServiceResponse(200, "BIEEEEEEEEEEEEEN ");
	}

	@GetMapping("TEST6")
	ServiceResponse all6() {

		// try {
		// GpioModulo.DebugPinSalidaPuerta();
		// GpioModulo.ONLY_AbrirPuertaTrasera();
		// } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// GpioModulo.pines();
		// GpioModulo.resetarPeso();
		// GpioModulo.resetarPeso2();
		/*
		 * try { GpioModulo.Esperarhasta_puertatrasera_Cerrada();
		 * 
		 * System.out.println("Estado en en test6" +
		 * GpioModulo.Esperarhasta_puertatrasera_Cerrada()); } catch
		 * (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		// GpioModulo.resetearPesa();
		GpioModulo.validarMedidasCamara();
		httpSession.invalidate();

		return new ServiceResponse(200, "BIEEEEEEEEEEEEEN ");
	}

	@GetMapping("/TEST3")
	ServiceResponse all3() {
		logger.warn("sessionid " + httpSession.getId());

		/*
		 * try {
		 * 
		 * // GpioModulo.ONLY_AbrirPuertaDelantera();
		 * GpioModulo.ONLY_AbrirPuertaTrasera(); //
		 * GpioModulo.AccionarCintaTransportadora();
		 * 
		 * } catch (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
//		    GpioModulo.leerDatosBalanza();
//		//System.out.println(GpioModulo.leerDatosBalanza());
//		
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		// GpioModulo.resetearPesa();

		//GpioModulo.leerPuertoSerie3();

		// System.out.println(GpioModulo.leerPuertoSerie3());
		//double temperatura = GpioModulo.temperatura();
		
		GpioModulo.capturarImagenCamara();
		httpSession.invalidate();
		return new ServiceResponse(200, "Temperatura: " + 200 );

	}

	@GetMapping("/TEST2")
	ServiceResponse all2() {
		logger.warn("sessionid " + httpSession.getId());

		// try {

		// GpioModulo.GetPeso(myBean);
		// GpioModulo.leerDatosBalanza();
		// GpioModulo.leerDatosCuteCom();
		// GpioModulo.leerUltimaLineaCutecom();
		// GpioModulo.leerUltimaLineaCutecom();
		// GpioModulo.leerPuertoSerie();
		// GpioModulo.leerPuertoSerie3();
		// double peso = GpioModulo.leerDatosBalanza();
		// GpioModulo.leerDatosBalanza();

		// System.out.println("Peso " + peso);
		System.out.println(GpioModulo.status_puertaTraseraAbierta());
		return new ServiceResponse(200, "mal " +GpioModulo.status_puertaTraseraAbierta());

		// GpioModulo.ONLY_cerrarPuertaDelantera();
		// GpioModulo.ONLY_CerrarPuertaTrasera();
		// GpioModulo.DetenerCintaTransportadora();

		// GpioModulo.GetPortNames();

		// } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	@PostMapping("/CheckPagoBulto")
	String newEmployee(@RequestBody String str) {
		logger.info(" ");
		return str;
	}

	@GetMapping("/Check/{id}")
	ServiceResponse one(@PathVariable String id) {

		logger.info(" ");
		return null;
	}

	@PutMapping("/CheckPagoBulto/{id}")
	String replaceEmployee(@RequestBody String str, @PathVariable Long id) {

		logger.info(" ");
		return str;
	}

	@DeleteMapping("/Delete/{id}")
	void deleteEmployee(@PathVariable Long id) {
		logger.info(" ");
	}

}
