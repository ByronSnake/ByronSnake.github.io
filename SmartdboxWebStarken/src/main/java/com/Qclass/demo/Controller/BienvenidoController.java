package com.Qclass.demo.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Qclass.demo.SmartdboxWebStarkenApplication;

@Controller
public class BienvenidoController {

	
	final static Logger logger =  LoggerFactory.getLogger(BienvenidoController.class);

	
    @GetMapping("/Bienvenido")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		logger.warn("CHECHEANDO LOG >>> WARN");
		return "NewFile";
	}
	
}
