package com.Qclass.demo.classes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ValidarEncomienda {

	
	String bulto;
	String peso;
	String alto;
	String largo;
	String ancho;
	
	  
	
	public ValidarEncomienda(String bulto, String peso, String alto, String largo, String ancho) {
		super();
		this.bulto = bulto;
		this.peso = peso;
		this.alto = alto;
		this.largo = largo;
		this.ancho = ancho;
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



	public String RetornarJson() {
	    //System.out.println("Public methods must be called by creating objects");
		
		double alto = 0;
		double largo = 0;
		double ancho = 0;
		int INT_alto = 0;
		int INT_largo = 0;
		int INT_ancho = 0;
		
		
		try{
			alto = Double.parseDouble(this.alto)/10;
			alto = Math.round(alto);
			INT_alto = (int)alto ;
			
		}catch (Exception ex){
			System.out.println("alto malo");
		}
		
		try{
			largo = Double.parseDouble(this.largo)/10;
			largo = Math.round(largo);
			INT_largo = (int)largo ;
		}catch (Exception ex){
			//System.out.println("");
		}
		
		try{
			ancho = Double.parseDouble(this.ancho)/10;
			ancho = Math.round(ancho);
			INT_ancho = (int)ancho ;
		}catch (Exception ex){
			//System.out.println("");
		}
		
	    ValidarEncomienda validar = new ValidarEncomienda(this.bulto, this.peso,
	    		String.valueOf(INT_alto), 
	    		String.valueOf(INT_largo), 
	    		String.valueOf(INT_ancho)) ;
		String TO_Json = "";
		
		try {
			Gson gson = new Gson();
			TO_Json =  gson.toJson(validar);
			}
			catch(Exception e) {
				System.out.println("Error GSON !!!!!!");
		}
		
		System.out.println(TO_Json);
		
		this.alto = String.valueOf(INT_alto);
		this.ancho = String.valueOf(INT_ancho);
		this.largo = String.valueOf(INT_largo);
		
		return TO_Json;
		
		
	  }
	  
	
	public String extraerOF() {
		
		String TO_Json = "";
		
		String of = this.bulto.substring(14,23);
		
		try {
			Gson gson = new Gson();
			TO_Json =  gson.toJson(of);
			//System.out.println("TO_Json" + TO_Json);
			}
			catch(Exception e) {
				System.out.println("Error GSON !!!!!!");
		}
		
		
		  String Json_ejemplo = "{\r\n"
		    		+ "    \"of\":\""+ of +"\"\r\n"
		    		+ "}";
		
		  System.out.println("Json_ejemplo" + Json_ejemplo);
		
		return Json_ejemplo;
		
	}
	
	
	
	public String GetOF() { 
		
		  String of = null;
		  try {
			 
			  of = this.bulto.substring(14,23);
		  }catch (Exception ex) {
			  of = null;
			  
		  }
		
		
		
		return of;
		
	}




	public String JsonRecepciondeBulto(String agencia) {
		
		 ValidarEncomienda validar = new ValidarEncomienda(this.bulto, this.peso, this.alto, this.largo, this.ancho) ;
		
		 String JsonRecepcion = "";
		try {
			JsonObject jsObj =  (JsonObject) new Gson().toJsonTree(validar);
			jsObj.remove("peso"); // remove field 'age'
			jsObj.remove("alto"); // remove field 'age'
			jsObj.remove("largo"); // remove field 'age'
			jsObj.remove("ancho"); // remove field 'age'
			jsObj.addProperty("agencia", agencia); // add field 'key'

			System.out.println("jsObj ->"+jsObj);
			
			JsonRecepcion = jsObj.toString();
			}
			catch(Exception e) {
				System.out.println("Error GSON !!!!!!"); 
		}
		
		
		
		return JsonRecepcion;
	} 


}
