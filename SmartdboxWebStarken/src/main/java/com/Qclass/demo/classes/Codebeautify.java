package com.Qclass.demo.classes;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class Codebeautify {

	 public ArrayList < Encargo > Encargos = new ArrayList < Encargo > ();

	public ArrayList<Encargo> getEncargos() {
		return Encargos;
	}

	public void setEncargos(ArrayList<Encargo> encargos) {
		Encargos = encargos;
	}
	
	public List<String> Listencargos(){
		
		List<String> EL = new ArrayList<String>();
		
		
		
		try {
			
			for (int i=0; i<Encargos.size(); i++) 
    		{ 
    			
				String barcode= Encargos.get(i).getENCACODIGOBARRA();
				
    			EL.add(barcode);
    			
    		}
			
		}catch(Exception ex) {
			
			
		}
		
		return EL;
	}


	public void inicializar(String jsonTEXT) {
		
		 Codebeautify rt = new Codebeautify();

 		try {
 			
 			Gson gson = new Gson();
 			
 			rt = gson.fromJson(jsonTEXT, Codebeautify.class);
 		}
 			catch(Exception e) {
 				System.out.println("Error GSON !!!!!!... No se pudo Deserializar a ROOT");
 				System.out.println("EX" +e.getMessage());
 		}
 		
 		this.Encargos = rt.Encargos;
	}
	 // Getter Methods 



	 // Setter Methods 


	

}
