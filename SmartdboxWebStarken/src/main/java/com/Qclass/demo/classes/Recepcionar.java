package com.Qclass.demo.classes;

public class Recepcionar {
	
	String bulto;
	
	String agencia;
	int codUsuario;
	int ubicCodigo;

	public Recepcionar(String bulto, String agencia,int codUsuario,int ubicCodigo) {
		super();
		this.bulto = bulto;
		this.agencia = agencia;
		this.codUsuario= codUsuario;
		this.ubicCodigo = ubicCodigo;
	}

	public String getBulto() {
		return bulto;
	}

	public String getAgencia() {
		return agencia;
	}

	public int getCodUsuario() {
		return codUsuario;
	}

	public int getUbicCodigo() {
		return ubicCodigo;
	}
	
	
	
	
}
