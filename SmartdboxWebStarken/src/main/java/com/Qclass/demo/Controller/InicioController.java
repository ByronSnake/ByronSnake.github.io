package com.Qclass.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Qclass.demo.classes.respuesta;

@Controller
public class InicioController {

	@GetMapping("/Inicio")
	public String Inicio(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		
		//respuesta r = new respuesta("123",200,"aqui hay informacion muy poco util");
		
		
		return "NewFile";
	}
}



