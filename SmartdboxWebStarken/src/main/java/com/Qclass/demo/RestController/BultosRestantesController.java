package com.Qclass.demo.RestController;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Qclass.demo.Services.StarkenServices;
import com.Qclass.demo.classes.BultosRestantes;
import com.Qclass.demo.classes.ServiceResponse;

import jakarta.servlet.http.HttpSession;

@PreAuthorize("hasAnyRole('ADMIN','FRONT')")
@RestController
@RequestMapping("/API/BultosRestantes")
public class BultosRestantesController {

	final static Logger logger =  LoggerFactory.getLogger(BultosRestantesController.class);
	
	@Autowired
    public HttpSession httpSession;
	
	//@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "Access-Control-Allow-Origin", exposedHeaders = "X-Get-Header")
	@GetMapping("/GetALL")
	ServiceResponse all() {
		logger.info("Entrando al controlador bultos restantes");
		
		logger.warn("********** sessionid = "+ httpSession.getId());
		BultosRestantes br = new BultosRestantes(0, 0);
		int status = 0;
		List<String> bultosguardados = new ArrayList<>();
		List<String> bultosRecepcionados = new ArrayList<>();
		
		int bultos_QTY 				= 0; 
		int bultosRecepcionados_QTY = 0; 
		
		try {
			bultosguardados =(List<String>) StarkenServices.getAttributeS("BULTOS");
			logger.info("Bultos Guardados " + bultosguardados.toString());
			if(bultosguardados!=null) {
				
				bultos_QTY = bultosguardados.size();
				
			}
			
			
			br.setBultos_QTY(bultos_QTY);
		}catch(Exception e){
			logger.error("---> Exception : "+e);
			status = 500;
			
		}
		try {
			bultosRecepcionados =(List<String>) StarkenServices.getAttributeS("BULTOS_RECEPCIONADOS");
			logger.info("BULTOS_RECEPCIONADOS" + bultosRecepcionados);
			if(bultosRecepcionados!=null) {
				bultosRecepcionados_QTY = bultosRecepcionados.size();
				logger.info("bultosRecepcionados_QTY " + bultosRecepcionados_QTY);
			}
			
			
			br.setBultosRecepcionados_QTY(bultosRecepcionados_QTY);
		}catch(Exception e){
			logger.error("---> Exception : "+e);
			status = 201;
		}
		
		 
		logger.info("Retornando Bultos Recepcionados QTY " + bultosRecepcionados_QTY + "bultos QTY " + bultos_QTY );
		
		return new ServiceResponse(200, br);
	}
	
}
