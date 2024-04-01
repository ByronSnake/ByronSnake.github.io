package com.Qclass.demo.classes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Barcode {
	public String codigobarra;
	public String sucursal;
	
	
	
	public Barcode(String codigobarra, String sucursal) {
		//super();
		String limpiar = "";
		try{
			limpiar = codigobarra.replace("\n", "").replace("\r", "");
			this.codigobarra = limpiar;
			System.out.println("codigobarra Limpio sin \n");
		}catch(Exception ex) {
			this.codigobarra = codigobarra;
		}
		
		
		this.sucursal = sucursal;
	}
	
	public String getCodigobarra() {
		return codigobarra;
	}
	public void setCodigobarra(String codigobarra) {
		this.codigobarra = codigobarra;
	}
	public String getSucursal() {
		return sucursal;
	}
	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}
	
	public void DeserealizarMensaje(String Json) {
		
		try {
			System.out.println("try DeserealizarMensaje : "+Json.replace("\n", ""));
			Gson gson = new Gson();
			
			Barcode bc = gson.fromJson(Json.trim(), Barcode.class);
			
			this.codigobarra = bc.codigobarra.substring(0, bc.codigobarra.length()-1);
			this.sucursal = bc.sucursal;
			
		}
			catch(Exception e) {
				System.out.println("Error GSON !!!!!!... No se pudo Deserializar a Barcode");
				System.out.println("EX" +e.getMessage());
		}
		
		
	}
	
	
	
	public String SerializarJson() {
		Barcode barcode = new Barcode(this.codigobarra, this.sucursal);
		String json = "";
		     try {
		     GsonBuilder builder = new GsonBuilder();
		     Gson gson = builder.create();
		     json = gson.toJson(barcode);
		    // System.out.println(json);
		     }
		     catch (Exception e){

		     System.out.println("ERROR, No fue posible serializar el JSON !");
		     }
		     
		     return json;

		}
	
	
	
}
