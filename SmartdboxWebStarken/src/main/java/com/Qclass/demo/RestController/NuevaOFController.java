package com.Qclass.demo.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Qclass.demo.classes.ServiceResponse;

import jakarta.servlet.http.HttpSession;

@PreAuthorize("hasAnyRole('ADMIN','FRONT')")
@RestController
@RequestMapping("/API/NuevaOF") //   /API/Evaluar/Encuesta/{id}
public class NuevaOFController {
	
	final static Logger logger =  LoggerFactory.getLogger(NuevaOFController.class);
	
	@Autowired
    public HttpSession httpSession;
	

	@GetMapping("/nuevaOF")
	ServiceResponse all() {
		
		try {
			
			httpSession.invalidate();
			logger.info("Session invalidada por llamado a servicio");
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ServiceResponse(200, " httpSession.invalidate() ");
	}


}
