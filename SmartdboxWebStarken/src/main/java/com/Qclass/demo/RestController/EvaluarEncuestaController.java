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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Qclass.demo.Services.StarkenServices;
import com.Qclass.demo.classes.Codebeautify;
import com.Qclass.demo.classes.Evaluacion;
import com.Qclass.demo.classes.ServiceResponse;
import com.google.gson.Gson;

import jakarta.servlet.http.HttpSession;

@PreAuthorize("hasAnyRole('ADMIN','FRONT')")
@RestController
@RequestMapping("/API/Evaluar") //   /API/Evaluar/Encuesta/{id}
public class EvaluarEncuestaController {

	
final static Logger logger =  LoggerFactory.getLogger(EvaluarEncuestaController.class);
	
	@Autowired
    public HttpSession httpSession;
	

	@GetMapping("/Nota/{id}")
	ServiceResponse one(@PathVariable int id) {

		logger.info("ConsultaServicio Evaluar/Nota/" + id);
		logger.warn("********** sessionid = "+ httpSession.getId());
		httpSession.invalidate();
		logger.info("httpSession.invalidate()");
		

		String BULTO_EN_Proceso = "";
		try {
			BULTO_EN_Proceso = (String) StarkenServices.getAttribute("BULTO_EN_Proceso");
			logger.info("---> BULTO_EN_Proceso : "+BULTO_EN_Proceso);
		}catch(Exception e){
			logger.warn("---> Exception : "+e);
		}
		
		if(BULTO_EN_Proceso != null) {
			if(!BULTO_EN_Proceso.equals("")) {
				if(id >= 0 && id <= 2){
					
					StarkenServices.encuesta(BULTO_EN_Proceso,id);
					
					return new ServiceResponse(200, "OK");
				}else {
					return new ServiceResponse(500, "La nota no puede ser distinta de 0, 1 o 2");
				}
			}
		}
		
		
		
		return new ServiceResponse(200, "OK");		
		
	}

	
	
}
