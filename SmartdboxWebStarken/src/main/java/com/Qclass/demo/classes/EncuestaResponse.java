package com.Qclass.demo.classes;

import com.google.gson.Gson;

public class EncuestaResponse {
	
	int folio;


public void DeserealizarMensaje(String stringOutput) {
	// TODO Auto-generated method stub
	try {
		
		Gson gson = new Gson();
		
		EncuestaResponse ER = gson.fromJson(stringOutput, EncuestaResponse.class);
		
		this.folio = ER.folio;
		
	}
		catch(Exception e) {
			System.out.println("Error GSON !!!!!!... No se pudo Deserializar");
	}
}
}
