package com.Qclass.demo.classes;

import com.google.gson.Gson;

public class RespuestaServicio {

	
	
	String status;
	
	String message;
	
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	
	public void DeserealizarMensaje(String Json) {
		
		try {
			
			Gson gson = new Gson();
			
			RespuestaServicio Resp = gson.fromJson(Json, RespuestaServicio.class);
			
			this.message = Resp.message;
			this.status = Resp.status;
			
		}
			catch(Exception e) {
				System.out.println("Error GSON !!!!!!... No se pudo Deserializar");
		}
		
		
	}
	
	
	
	
	
	

}
