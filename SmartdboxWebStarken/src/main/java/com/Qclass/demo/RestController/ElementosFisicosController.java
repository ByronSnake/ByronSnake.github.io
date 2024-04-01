package com.Qclass.demo.RestController;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Qclass.demo.Services.StarkenServices;
import com.Qclass.demo.classes.EncargoClass;
import com.Qclass.demo.classes.EncargosClass;
import com.Qclass.demo.classes.EncuestaClass;

import com.Qclass.demo.classes.GpioModulo;
import com.Qclass.demo.classes.R_Preguntas;
import com.Qclass.demo.classes.ServiceResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@PreAuthorize("hasAnyRole('ADMIN','DASHBOARD')")
@RestController
@RequestMapping("/API/ElementosFisicos")
public class ElementosFisicosController {

	final static Logger logger = LoggerFactory.getLogger(ElementosFisicosController.class);

	@Autowired
	public HttpSession httpSession;


	@GetMapping("/SistemaOperativo/Reset")
	String SOR(HttpServletRequest request) {
		String ipAccion = request.getLocalAddr();
		logger.info("IP Accion " + ipAccion);
		logger.info("sessionid " + httpSession.getId() + "Comando SistemaOperativo/Reset");

		String operatingSystem = System.getProperty("os.name");

		if (operatingSystem.equals("Linux")) {
			Runtime runtime = Runtime.getRuntime();
			try {
				Process proc = runtime.exec("shutdown -r -t " + 3);

				logger.warn("sessionid " + httpSession.getId() + "Comando Ejecutado");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.exit(0);
		}
		return "Reiniciando Sistema...";
	}

	@GetMapping("/Puerta/Delantera/Abrir")
	String puertaDelanteraabrir(HttpServletRequest request) {
		String ipAccion = request.getLocalAddr();
		logger.info("Ip Accion " + ipAccion);
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
				}

			} else {
				logger.info("-------Abriendo puerta delantera-------");
				GpioModulo.ONLY_AbrirPuertaDelantera();
			}

		} else {
			logger.info("La puerta delantera esta abierta");
		}
		return "Puerta delantera abierta";
	}

	@GetMapping("/Puerta/Trasera/Abrir")
	String puertaTraseraabrir(HttpServletRequest request) {
		String ipAccion = request.getLocalAddr();
		logger.info("Ip Accion " + ipAccion);
		logger.info("Abriendo la puerta trasera...");


			if (GpioModulo.status_puertaTrasera()) {
				// System.out.println("Dentro del if GpioModulo.status_puertaTrasera()== true");
				if (GpioModulo.status_puertaDelantera() == false) {
					logger.info("-------Cerrando puerta delantera-------");
					GpioModulo.ONLY_cerrarPuertaDelantera();

					if (GpioModulo.check_puertaDelantera_cerrada()) {
						logger.info("" + GpioModulo.status_puertaDelantera());
						// System.out.println("IF puertaTaseraAbrir == false");
						// GpioModulo.Esperarhasta_puertaDelantera_Cerrada();
						logger.info("-------Abriendo puerta trasera-------");

						GpioModulo.ONLY_AbrirPuertaTrasera();
					}
				} else {
					logger.info("-------Abriendo puerta Trasera-------");

					GpioModulo.ONLY_AbrirPuertaTrasera();
				}

			} else {
				logger.info("La puerta Trasera esta abierta");
			}

		
		return "Puerta trasera abierta";
	}

	@GetMapping("/Puerta/Delantera/Cerrar")
	String puertaDelanteracerrar(HttpServletRequest request) {
		String ipAccion= request.getLocalAddr();
		logger.info("Ip accion " + ipAccion);

		if (GpioModulo.status_puertaDelantera() == false) {
			logger.info("-------Cerrando puerta delantera-------");
			GpioModulo.ONLY_cerrarPuertaDelantera();
			GpioModulo.apagarLuz();
			// GpioModulo.Esperarhasta_puertaDelantera_Cerrada();
		} else {
			logger.info("Puerta delantera Cerrada");
		}

		// Desarrollar Cerrado puerta delantera

		return "Puerta Delantera cerrada";
	}

	@GetMapping("/Puerta/Trasera/Status")
	String PTS() {

		// GpioModulo.check_puertaDelantera_cerrada();

		if (GpioModulo.status_puertaTrasera()) {
			return "Puerta trasera Cerrada";

		} else {
			return "Puerta trasera Abierta";
		}

	}

	@GetMapping("/Puerta/Delantera/Status")
	// Devuelve el estado de la puerta delantera
	String PDS() {

		if (GpioModulo.status_puertaDelantera()) {
			return "Puerta delantera cerrada";
		} else {
			return "Puerta delantera abierta";
		}

	}

	@GetMapping("/Puerta/Trasera/Cerrar")
	String puertaTraseracerrar(HttpServletRequest request) {

		String ipAccion = request.getLocalAddr();
		logger.info("Ip Accion " + ipAccion);
		if (GpioModulo.status_puertaTrasera() == false) {
			logger.info("-------Cerrando puerta trasera-------");
			GpioModulo.ONLY_CerrarPuertaTrasera();
			// GpioModulo.Esperarhasta_puertatrasera_Cerrada();
		} else {

			logger.info("Puerta trasera cerrada");
		}

		return "Puerta trasera Cerrada";
		// Desarrollar Cerrado puerta Trasera
		// logger.info("sessionid "+ httpSession.getId() + "Comando
		// Clasificador/Accionar");
	}

	@GetMapping("/Cinta/Accionar")
	String accionarCinta(HttpServletRequest request) {
		String ipAccion = request.getLocalAddr();
		logger.info("Ip Accion " + ipAccion);
		
		GpioModulo.AccionarCintaTransportadora();
		
		return "Accionando Cinta";

	}

	@GetMapping("/Cinta/Detener")
	String detenerCinta(HttpServletRequest request) {

		String ipAccion = request.getLocalAddr();
		logger.info("Ip Accion " + ipAccion);
		GpioModulo.DetenerCintaTransportadora();
		
		return "Deteninedo cinta...";

	}

	@GetMapping("/Clasificador/Accionar")
	String AccionarClasificador() {

		try {
			GpioModulo.accionarClasificador();
			Thread.sleep(1000);
			GpioModulo.desactivarClasificador();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("sessionid " + httpSession.getId() + "Comando Clasificador/Accionar");
		return "Clasificador Accionado ";
	}

	@GetMapping("/Pesa/Tarar")
	String TararPesa() {
		// Esto tarea
		GpioModulo.PesoTarea();

		logger.info("sessionid " + httpSession.getId() + "Comando Pesa/Tarar");

		return "msg de OK ";
	}

	@GetMapping("/Pesa/Accionar")
	String AccionarPesa() {

		GpioModulo.PesoPesar();

		logger.info("sessionid " + httpSession.getId() + "Comando Pesa/Tarar");

		return "msg de OK ";
	}
	
	@GetMapping("/Rodillos/Accionar")
	String accionarRodillos(HttpServletRequest request) {
		
		String ipAccion = request.getRemoteAddr();

		GpioModulo.accionarRodillos();
		logger.info("ip Accion " + ipAccion );
		
		return "Accionando rodillos... ";
	}

	@GetMapping("/Rodillos/Detener")
	String detenerRodillos(HttpServletRequest request) {
		
		String ipAccion = request.getRemoteAddr();

		GpioModulo.desactivarRodillos();
		logger.info("ip Accion " + ipAccion );
		
		return "Deteniendo rodillos... ";
	}
	
	@GetMapping("/Puerta/Delantera/Abrir/SinCheck")
	String abrirPuertaDelanteraSinCkeck(HttpServletRequest request) {
		
		String ipAccion = request.getRemoteAddr();
		logger.info("Abriendo puerta delantera sin verificar puerta trasera");
		GpioModulo.ONLY_AbrirPuertaDelantera();
		logger.info("ip Accion " + ipAccion );
		
		return "Abriendo puerta delantera Sin verificar puerta trasera ";
		
	}
}
