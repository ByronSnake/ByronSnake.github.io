package com.Qclass.demo.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Qclass.demo.VariablesGlobales.VariablesGlobales;
import com.Qclass.demo.classes.Measurement;
import com.Qclass.demo.classes.ServiceResponse;


import jakarta.servlet.http.HttpSession;

@PreAuthorize("hasAnyRole('ADMIN','FRONT')")
@RestController
@RequestMapping("/API/GuardarMedidasGlobales")
public class GuardarMedidasGlobalesController {

	final static Logger logger =  LoggerFactory.getLogger(GuardarMedidasGlobalesController.class);
	
	@Autowired
    public HttpSession httpSession;
	
	@PostMapping("/Camara")
	ServiceResponse GuardarMedidas(@RequestBody Measurement Medidas) {
	    
		String str = String.format("Medidas alto: %s,  Largo: %s,  Ancho: %s", Medidas.getAlto(), Medidas.getLargo(), Medidas.getAncho());
		logger.info("---> Medidas : "+str);
		logger.info("---> Medidas : "+" Alto:" + Medidas.getAlto());
		logger.info("---> Medidas : "+" Largo:"+ Medidas.getLargo());
		logger.info("---> Medidas : "+" Ancho:"+ Medidas.getAncho());
		
		VariablesGlobales.Alto = 2;
		VariablesGlobales.Ancho = 2;
		VariablesGlobales.Largo = 2;
		
	    return new ServiceResponse(200, Medidas);
	  }
	
	
	@GetMapping("/MedidasCamara/{id}")
	ServiceResponse one(@PathVariable int id) {
		
		String BULTO_EN_Proceso = "";
		try {
			BULTO_EN_Proceso = (String) httpSession.getAttribute("BULTO_EN_Proceso");
			logger.info("---> BULTO_EN_Proceso : "+BULTO_EN_Proceso);
		}catch(Exception e){
			logger.warn("---> Exception : "+e);
		}
		
		if(!BULTO_EN_Proceso.equals("")) {
			if(id >= 0 && id <= 2){
				
				//StarkenServices.encuesta(BULTO_EN_Proceso,id);
				return new ServiceResponse(200, "OK");
			}else {
				return new ServiceResponse(500, "La nota no puede ser distinta de 0, 1 o 2");
			}
		}
		
		
		return new ServiceResponse(200, "OK");
		
	}
}
