package com.Qclass.demo.classes;
///asdsad
public class CheckMeasureBulto {

	String bulto;
	String peso;
	String alto;
	String largo;
	String ancho;
	String codUsuario;
	String ubiCodigo;
	
	public CheckMeasureBulto(String bulto, String peso, String alto, String largo, String ancho, String codUsuario, String ubiCodigo) {
		super();
		this.bulto = bulto;
		this.peso = peso;
		this.alto = alto;
		this.largo = largo;
		this.ancho = ancho;
		this.codUsuario = codUsuario;
		this.ubiCodigo = ubiCodigo;
	}

	
	
	public String getCodUsuario() {
		return codUsuario;
	}



	public void setCodUsuario(String codUsuario) {
		this.codUsuario = codUsuario;
	}



	public String getUbiCodigo() {
		return ubiCodigo;
	}



	public void setUbiCodigo(String ubiCodigo) {
		this.ubiCodigo = ubiCodigo;
	}



	public String getBulto() {
		return bulto;
	}
	public void setBulto(String bulto) {
		this.bulto = bulto;
	}
	public String getPeso() {
		return peso;
	}
	public void setPeso(String peso) {
		this.peso = peso;
	}
	public String getAlto() {
		return alto;
	}
	public void setAlto(String alto) {
		this.alto = alto;
	}
	public String getLargo() {
		return largo;
	}
	public void setLargo(String largo) {
		this.largo = largo;
	}
	public String getAncho() {
		return ancho;
	}
	public void setAncho(String ancho) {
		this.ancho = ancho;
	}
	
}
