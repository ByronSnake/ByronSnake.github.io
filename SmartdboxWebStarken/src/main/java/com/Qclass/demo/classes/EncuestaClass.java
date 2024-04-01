package com.Qclass.demo.classes;

import java.util.ArrayList;

public class EncuestaClass {
	
	public int 		idInstancia;
	public String 	IP;
	public int 		idEncuesta;
	public String 	codigoSucursal;
	
	public ArrayList < Object > rPreguntasEncuesta = new ArrayList < Object > ();
	
	public String 	respuestaNPS= "";
	public String 	comentarioNPS= "";
	public String 	respuestaDC = "";
	
	public ArrayList < Object > rPreguntasAbiertas = new ArrayList < Object > ();
	
	
	public int getIdInstancia() {
		return idInstancia;
	}

	public void setIdInstancia(int idInstancia) {
		this.idInstancia = idInstancia;
	}

	

	public int getIdEncuesta() {
		return idEncuesta;
	}

	public void setIdEncuesta(int idEncuesta) {
		this.idEncuesta = idEncuesta;
	}

	public String getCodigoSucursal() {
		return codigoSucursal;
	}

	public void setCodigoSucursal(String codigoSucursal) {
		this.codigoSucursal = codigoSucursal;
	}

	public ArrayList<Object> getrPreguntasEncuesta() {
		return rPreguntasEncuesta;
	}

	public void setrPreguntasEncuesta(ArrayList<Object> rPreguntasEncuesta) {
		this.rPreguntasEncuesta = rPreguntasEncuesta;
	}

	public String getRespuestaNPS() {
		return respuestaNPS;
	}

	public void setRespuestaNPS(String respuestaNPS) {
		this.respuestaNPS = respuestaNPS;
	}

	public String getComentarioNPS() {
		return comentarioNPS;
	}

	public void setComentarioNPS(String comentarioNPS) {
		this.comentarioNPS = comentarioNPS;
	}

	public String getRespuestaDC() {
		return respuestaDC;
	}

	public void setRespuestaDC(String respuestaDC) {
		this.respuestaDC = respuestaDC;
	}

	public ArrayList<Object> getrPreguntasAbiertas() {
		return rPreguntasAbiertas;
	}

	public void setrPreguntasAbiertas(ArrayList<Object> rPreguntasAbiertas) {
		this.rPreguntasAbiertas = rPreguntasAbiertas;
	}
	
	public void agregarRespuestaPreguntasAbiertas(Object obj) {
		this.rPreguntasAbiertas.add(obj);
	}

	
	public void agregarRespuestaPreguntasEncuesta(Object obj) {
		this.rPreguntasEncuesta.add(obj);
	}
	
	
}


