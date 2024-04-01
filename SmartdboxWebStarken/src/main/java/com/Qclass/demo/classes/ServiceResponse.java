package com.Qclass.demo.classes;

import com.google.gson.Gson;

public class ServiceResponse {
	
	int status;
	Object message;
	
	public ServiceResponse(int status, Object message) {
		super();
		this.status = status;
		this.message = message;
	}
	public int getStatus() {
		return status;
	}
	public Object getMessage() {
		return message;
	}
	public void DeserealizarMensaje(String stringOutput) {
		// TODO Auto-generated method stub
		try {
			
			Gson gson = new Gson();
			
			ServiceResponse Resp = gson.fromJson(stringOutput, ServiceResponse.class);
			
			this.message = Resp.message;
			this.status = Resp.status;
			
		}
			catch(Exception e) {
				System.out.println("Error GSON !!!!!!... No se pudo Deserializar");
		}
	}
	
	
}
