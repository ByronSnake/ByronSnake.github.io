package com.Qclass.demo.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.Qclass.demo.Services.StarkenServices;
import com.Qclass.demo.classes.DatosQB;
import com.Qclass.demo.classes.DatosResponse;
import com.Qclass.demo.classes.GpioModulo;
import com.Qclass.demo.classes.Measurement;
import com.Qclass.demo.classes.MyBean;
import com.Qclass.demo.classes.ServiceResponse;
import jakarta.servlet.http.HttpSession;

@PreAuthorize("hasAnyRole('ADMIN','FRONT')")
@RestController
@RequestMapping("/API/Recepcion")
public class RecepcionController {

	final static Logger logger = LoggerFactory.getLogger(RecepcionController.class);

	@Autowired
	public HttpSession httpSession;

	@Autowired
	private MyBean myBean;
	
	@Autowired
	private DatosResponse datosQB;
	//@Value("${LogStarken.sucursal:novalue}")
	String sucursal;
	//@Value("${LogStarken.codUsuario:novalue}")
	int codUsuario;
	//@Value("${LogStarken.ubicCodigo:novalue}")
	int ubicCodigo;
	
	

	@GetMapping("/RecepcionarBultoSesion")
	ServiceResponse all() {
		logger.info("ConsultaServicio RecepcionarBultoSesion");
		logger.warn("********** sessionid = " + httpSession.getId());
		String BULTO = "";
		List<String> bultosRecepcionados = new ArrayList<>();

		
		
		sucursal =myBean.getAgenCodigo();
		codUsuario = Integer.parseInt(myBean.getCodUsuario());
		ubicCodigo = Integer.parseInt(myBean.getUbiCodigo());
		double pesoClasificador = myBean.getPesoAccionarPatin();

		try {
			GpioModulo.ONLY_AbrirPuertaTrasera();
			GpioModulo.verificarPuertaTraseraAbierta();
			GpioModulo.AccionarCintaTransportadora();
			GpioModulo.accionarRodillos();
			Thread.sleep(3000);
			
			GpioModulo.ONLY_CerrarPuertaTrasera();
			if (Double.parseDouble(StarkenServices.getAttribute("pESO_final")) > pesoClasificador) {

				GpioModulo.accionarClasificador();
				Thread.sleep(100);
				GpioModulo.desactivarClasificador();

			}
			if (GpioModulo.check_puertaTrasera_cerrada()) {
				GpioModulo.DetenerCintaTransportadora();
				GpioModulo.apagarLuz();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		try {

			// String contenidoArchivo =
			// (List<String>)StarkenServices.getAttributeS("BULTOS_RECEPCIONADOS");

			BULTO = (String) StarkenServices.getAttribute("BULTO_EN_Proceso");

			logger.info("Bulto en proceso " + BULTO);

			bultosRecepcionados = (List<String>) StarkenServices.getAttributeS("BULTOS_RECEPCIONADOS");

			logger.info("Bultos recepcionados " + bultosRecepcionados.toString());

		} catch (Exception e) {
			logger.warn("---> Exception : " + e);
		}

		logger.info("voy a entrar al if (bultosRecepcionados != null " + BULTO + "Bultos recepcionados "
				+ bultosRecepcionados);

		if (bultosRecepcionados != null) {
			if (bultosRecepcionados.size() > 0) {

				// logger.info("---> bultosRecepcionados != null ");
				// si hay bultos recepcionados
				String ordenFleteAux = bultosRecepcionados.get(0).substring(0, 23);

				logger.info("Orden flete aux " + ordenFleteAux);

				if (ordenFleteAux.equals(BULTO.substring(0, 23))) {

					logger.info("Dentro del if orden flete aux " + ordenFleteAux);
					StarkenServices.setAttribute("BULTOS_RECEPCIONADOS", bultosRecepcionados);

				} else {

					bultosRecepcionados.clear();

					StarkenServices.setAttribute("BULTOS_RECEPCIONADOS", bultosRecepcionados);
				}

			} else {

				logger.info("LOGGER DEL ELSE BULTOS RECEPCIONADOS = NULL");
			}

		} else {
			// logger.info("---> bultosRecepcionados == null ");

			// NO hay bultos recepcionados porque el primero de la sesion da un null en el
			// rescate de la variable de sesion
			// inicializarlo denuevo y guardarlo
			bultosRecepcionados = new ArrayList<>();

			StarkenServices.setAttribute("BULTOS_RECEPCIONADOS", bultosRecepcionados);

		}

		// logger.info("---> BULTO : "+BULTO);
		// logger.info("---> sucursal : "+sucursal);
		// logger.warn("bultosRecepcionados "+ bultosRecepcionados);

		/*
		 * try { Thread.sleep(5000); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		// ServiceResponse sr = new ServiceResponse(400, "DEFAULT");
		// ServiceResponse sr = new ServiceResponse(200, "DEFAULT");
		// sr = StarkenServices.PostRecepcionBulto(BULTO,sucursal); // servicio oficial
		// para recepcionar
		
		
		
		ServiceResponse sr = new ServiceResponse(400, "DEFAULT");

		sr = StarkenServices.PostRecepcionBulto(BULTO, sucursal, codUsuario, ubicCodigo);

		if (sr.getStatus() == 200) {
			System.out.println("******************Pase por aca 2---------------");
			bultosRecepcionados.add(BULTO);

			StarkenServices.setAttribute("BULTOS_RECEPCIONADOS", bultosRecepcionados);

			try {
				
				GpioModulo.desactivarRodillos();
				return new ServiceResponse(200, " - Bulto recepcionado");

			} catch (Exception e) {
				// TODO: handle exception
				
				e.getMessage();
				logger.info("Error recepcion");
			}

		} else {

			GpioModulo.desactivarRodillos();
			
			return new ServiceResponse(400, "Error  - Bulto no se pudo recepcionar");

		}

		logger.info("bultosRecepcionados " + bultosRecepcionados);

		// System.out.println("sr estatus" + bultosRecepcionados.toString()); //
		StarkenServices.setAttribute("BULTOS_RECEPCIONADOS", bultosRecepcionados);

		// ServiceResponse sr = new ServiceResponse(200, "DEFAULT");
		return sr;
		// return new ServiceResponse(400, "Error - Bulto no se pudo recepcionar");
		// return new ServiceResponse(200, "Recepcionado - Bulto esta listo");
	}

}
