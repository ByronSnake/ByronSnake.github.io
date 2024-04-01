package com.Qclass.demo.classes;



import com.google.gson.Gson;




public class Measurement {

	
	double peso;
	int alto;
	int largo;
	int ancho;
	
	int volumen;
	int centerX;
	int centerY;
	int paqueteCount;
	
	


	public Measurement(double peso, int alto, int largo, int ancho, int volumen, int centerX, int centerY, int paqueteCount) {
		super();
		this.peso = peso;
		this.alto = alto;
		this.largo = largo;
		this.ancho = ancho;
		this.volumen = volumen;
		this.centerX = centerX;
		this.centerY = centerY;
		this.paqueteCount = paqueteCount;
	}




	public double getPeso() {
		return peso;
	}




	public void setPeso(double peso) {
		this.peso = peso;
	}




	public int getAlto() {
		return alto;
	}




	public void setAlto(int alto) {
		this.alto = alto;
	}




	public int getLargo() {
		return largo;
	}




	public void setLargo(int largo) {
		this.largo = largo;
	}




	public int getAncho() {
		return ancho;
	}




	public void setAncho(int ancho) {
		this.ancho = ancho;
	}




	public double getVolumen() {
		return volumen;
	}




	public void setVolumen(int volumen) {
		this.volumen = volumen;
	}




	public int getCenterX() {
		return centerX;
	}




	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}




	public int getCenterY() {
		return centerY;
	}




	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}




	public int getPaqueteCount() {
		return paqueteCount;
	}




	public void setPaqueteCount(int paqueteCount) {
		this.paqueteCount = paqueteCount;
	}




	public void DeserealizarMensaje(String Json) {
		
		try {
			//System.out.println("try DeserealizarMensaje : "+Json);
			Gson gson = new Gson();
			
			Measurement Medidas = gson.fromJson(Json.trim(), Measurement.class);
			
			this.alto = Medidas.alto;
			this.largo = Medidas.largo;
			this.ancho = Medidas.ancho;
			this.peso = Medidas.peso;
			
		}
			catch(Exception e) {
				System.out.println("Error GSON !!!!!!... No se pudo Deserializar");
				System.out.println("EX" +e.getMessage());
		}
		
		
	}
	
	


}
