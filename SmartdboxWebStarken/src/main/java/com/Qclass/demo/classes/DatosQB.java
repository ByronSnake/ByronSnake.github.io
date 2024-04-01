package com.Qclass.demo.classes;

import com.google.gson.annotations.SerializedName;

public class DatosQB {
	
		   
    @SerializedName("id")
    private int idQB;
	@SerializedName("ip")
	private String ipQB;
	@SerializedName("numero_modulo")
	private int nroModulo;
	@SerializedName("device_name")
	private String deviceName;
	@SerializedName("agencodigo")
	private int agenCodigo;
	@SerializedName("codusuario")
	private int codUsuario;
	@SerializedName("ubiccodigo")
	private int ubiCodigo;
	@SerializedName("puerto")
	private int puertoQB;
	@SerializedName("peso_limite")
	private int pesoLimite;
	@SerializedName("peso_tolerancia_maxima")
	private int pesoToleranciaMaxima;
	@SerializedName("peso_accionar_patin")
	private int pesoAccionarPatin;
	@SerializedName("medidas_limite")
	private String medidasLimite;
	@SerializedName("medidas_tolerancia_maxima")
	private int medidasToleranciaMaxima;
	
	
	public int getId() {
		return idQB;
	}
	public void setId(int id) {
		this.idQB = id;
	}
	public String getIp() {
		return ipQB;
	}
	public void setIp(String ip) {
		this.ipQB = ip;
	}
	public int getNumero_modulo() {
		return nroModulo;
	}
	public void setNumero_modulo(int numero_modulo) {
		this.nroModulo = numero_modulo;
	}
	public String getDevice_name() {
		return deviceName;
	}
	public void setDevice_name(String device_name) {
		this.deviceName = device_name;
	}
	public int getAgencodigo() {
		return agenCodigo;
	}
	public void setAgencodigo(int agencodigo) {
		this.agenCodigo = agencodigo;
	}
	public int getCodusuario() {
		return codUsuario;
	}
	public void setCodusuario(int codusuario) {
		this.codUsuario = codusuario;
	}
	public int getUbiccodigo() {
		return ubiCodigo;
	}
	public void setUbiccodigo(int ubiccodigo) {
		this.ubiCodigo = ubiccodigo;
	}
	public int getPuerto() {
		return puertoQB;
	}
	public void setPuerto(int puerto) {
		this.puertoQB = puerto;
	}
	public int getPeso_limite() {
		return pesoLimite;
	}
	public void setPeso_limite(int peso_limite) {
		this.pesoLimite = peso_limite;
	}
	public int getPeso_tolerancia_maxima() {
		return pesoToleranciaMaxima;
	}
	public void setPeso_tolerancia_maxima(int peso_tolerancia_maxima) {
		this.pesoToleranciaMaxima = peso_tolerancia_maxima;
	}
	public int getPeso_accionar_patin() {
		return pesoAccionarPatin;
	}
	public void setPeso_accionar_patin(int peso_accionar_patin) {
		this.pesoAccionarPatin = peso_accionar_patin;
	}
	public String getMedidas_limite() {
		return medidasLimite;
	}
	public void setMedidas_limite(String medidas_limite) {
		this.medidasLimite = medidas_limite;
	}
	public int getMedidas_tolerancia_maxima() {
		return medidasToleranciaMaxima;
	}
	public void setMedidas_tolerancia_maxima(int medidas_tolerancia_maxima) {
		this.medidasToleranciaMaxima = medidas_tolerancia_maxima;
	}
	@Override
	public String toString() {
		return "DatosQB [idQB=" + idQB + ", ipQB=" + ipQB + ", nroModulo=" + nroModulo + ", deviceName=" + deviceName
				+ ", agenCodigo=" + agenCodigo + ", codUsuario=" + codUsuario + ", ubiCodigo=" + ubiCodigo
				+ ", puertoQB=" + puertoQB + ", pesoLimite=" + pesoLimite + ", pesoToleranciaMaxima="
				+ pesoToleranciaMaxima + ", pesoAccionarPatin=" + pesoAccionarPatin + ", medidasLimite=" + medidasLimite
				+ ", medidasToleranciaMaxima=" + medidasToleranciaMaxima + "]";
	}
	
	
	
	

}
