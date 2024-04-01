package com.Qclass.demo.classes;

public class R_Preguntas {
	public String 	respuesta;
	public int 		idPregunta;
	
	
	
	public R_Preguntas(String respuesta, int idPregunta) {
		super();
		this.respuesta = respuesta;
		this.idPregunta = idPregunta;
	}
	
	public String getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	public int getIdPregunta() {
		return idPregunta;
	}
	public void setIdPregunta(int idPregunta) {
		this.idPregunta = idPregunta;
	}
	
}
