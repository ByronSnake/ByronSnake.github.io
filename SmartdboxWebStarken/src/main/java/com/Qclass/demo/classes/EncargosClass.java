package com.Qclass.demo.classes;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class EncargosClass {
	
	 public ArrayList < EncargoClass > Encargos = new ArrayList < EncargoClass > ();
	 
	 
	 
	 
	 public void setEncargos(ArrayList<EncargoClass> encargos) {
		Encargos = encargos;
	}


	public void DeserealizarMensaje(String Json) {
			try {
				Gson gson = new Gson();
				EncargosClass E = gson.fromJson(Json, EncargosClass.class);
				
				this.Encargos = E.Encargos;
				System.out.println("EEncargosClass serializado"  );
				
			}
				catch(Exception e) {
					System.out.println("Error GSON !!!!!!... No se pudo Deserializar");
			}
		}
	 
	 
	 
	 
	 
	
	
}
