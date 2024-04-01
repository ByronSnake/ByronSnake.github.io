package com.Qclass.demo.VariablesGlobales;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VariablesGlobales_BORRAR {

	private static final Logger logger = LoggerFactory.getLogger(VariablesGlobales_BORRAR.class);
	//private static final String configFileURL = "/home/pi/configuracion.properties";
	//private static final String configFileURL = "\\home\\pi\\configuraciones.properties";
	//private static final String configFileURL = "C:\\temp\\configuraciones.properties";
	
	 public static String height = null; //dejar en null en PRD
	 public static String width = null;
	 public static String length = null;
	 public static double Initialweight = 0;
	 public static double weight = 0;
	 //public static double Doubleweight = 0;
	 
	
	 
	// public static String height = "120"; //dejar en null en PRD
	// public static String width = "120";
	// public static String length = "120";
	// public static String weight = "1.20";
	 public static boolean ENABLEWEIGHT = true;
	 public static int BucleReinicioListenerBalanza = 10000; //tiempo en milisegundos
	 
	 public static boolean BlockPuertaDelantera = true;
	 public static String baererServicioStarken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjM5LCJuYW1lIjoiZHNhYXNkIHNhZGFzZCIsInJ1biI6IjEyODMxNjA5NCIsIm1hc3Rlcl9pZCI6bnVsbCwiYXBwbGljYXRpb24iOnsiaWQiOjIsIm5hbWUiOiJTdGFya2VuIFBybyIsImNvZGUiOiJQUk8ifSwicm9sZSI6eyJpZCI6MSwiY29kZSI6IlVTRVIiLCJuYW1lIjoiVXN1YXJpbyJ9LCJpYXQiOjE2Mzc2OTI2MzAsImV4cCI6MTYzNzcwMzQzMH0.l1hqEqsPpcckB7WjZ_tb0Dyh049m-HBsp71ffOSuyb8";
	 static boolean DemoMode = false;
	 static int SocketPORT = 2221;
	 static int SocketPORTCamara = 2222;
	 static int MAXWEIGHT = 25;
	 static boolean ENABLEBUTTONS = true;
	 
	 static int TimerCinta = 1750; //1750 milisegundos
	 
	 public static String SUCURSAL = "DEFINIR_VG";
	 public static String IP = "DEFINIR_VG";
	 public static String DEVICENAME = "DEFINIR_VG";
	 public static String ENDPOINTLOGTF = "DEFINIR_VG";
	 public static String NROMODULO = "DEFINIR_VG";
	 
	 
	 public static String IMG_PathFolder = "DEFINIR_VG";
	 
	 
	 
	 
	 
	// static boolean DemoMode = true;
	// static int SocketPORT = 100;
	 
//	 alto 		ancho    largo 	 peso
//   height 	width	 length	 weight
	 static String verVariables() {
		 
		 return  
				 " L:"+ length+
				 " W:"+ width+
				 " H:"+ height+
				 "   weight= "+ weight;
		 
	 }
	 
	 
	 
	 static void printALLVariables() {
		 System.out.println("----------------");
		 System.out.println("printALLVariables");
		 System.out.println("----------------");
		 System.out.println("height="+height);
		 System.out.println("width="+width);
		 System.out.println("length="+length);
		 System.out.println("Initialweight="+Initialweight);
		 System.out.println("weight="+weight);
		 System.out.println("ENABLEWEIGHT="+ENABLEWEIGHT);
		 System.out.println("BucleReinicioListenerBalanza="+BucleReinicioListenerBalanza);
		 System.out.println("BlockPuertaDelantera="+BlockPuertaDelantera);
		 System.out.println("baererServicioStarken="+baererServicioStarken);
		 System.out.println("DemoMode="+DemoMode);
		 System.out.println("SocketPORT="+SocketPORT);
		 System.out.println("SocketPORTCamara="+SocketPORTCamara);
		 System.out.println("MAXWEIGHT="+MAXWEIGHT);
		 System.out.println("ENABLEBUTTONS="+ENABLEBUTTONS);
		 System.out.println("TimerCinta="+TimerCinta);
		 System.out.println("SUCURSAL="+SUCURSAL);
		 System.out.println("IP="+IP);
		 System.out.println("DEVICENAME="+DEVICENAME);
		 System.out.println("ENDPOINTLOGTF="+ENDPOINTLOGTF);
		 System.out.println("NROMODULO="+NROMODULO);
		 System.out.println("----------------");
	 }
	 
	 public static void iniciarProperties() throws FileNotFoundException,IOException{
		 
		
		 
		 Properties prop = new Properties();
		// prop.load(new FileReader(dir2));
		 //application.properties
		 //File F = new File("configuraciones.properties");//Original
		 File F = new File("\\src\\main\\resources\\application.properties");//Springboot config file
		 System.out.println( "F.path "+F.getAbsolutePath());
		 FileInputStream FIS = new FileInputStream(F);
		 prop.load(FIS);
		 prop.entrySet()
		 	.forEach(System.out::println);
		 
		
		 System.out.println("valor de la propiedad"+ prop.getProperty("language","MODIFICAR.PROPERTIES"));
		 
		 try {
			 SUCURSAL 		= prop.getProperty("SUCURSAL","MODIFICAR.PROPERTIES");
			 IP 			= prop.getProperty("IP","MODIFICAR.PROPERTIES");
			DEVICENAME 		= prop.getProperty("DEVICENAME","MODIFICAR.PROPERTIES");
			ENDPOINTLOGTF 	= prop.getProperty("ENDPOINTLOGTF","MODIFICAR.PROPERTIES");
			NROMODULO 		= prop.getProperty("NROMODULO","MODIFICAR.PROPERTIES");
			
			IMG_PathFolder 	= prop.getProperty("IMG_PathFolder","MODIFICAR.PROPERTIES");
			
			 
		 }catch(Exception e) {
			 
			 System.out.println("exeption _>>>"+e.getMessage());
			 logger.error("LOGS. PROPERTIES ERROR DE VARIABLES");
		 }
		
		 
		 
		 try {
			 String valor = prop.getProperty("XXXX","MODIFICAR.PROPERTIES");
				 if(valor.toUpperCase().equals("TRUE")) {
					 ENABLEWEIGHT = true;
					 
				 }else {
					 ENABLEWEIGHT = false;
				 }
			 }catch(Exception e) {	logger.error("Enableweight.PROPERTIES ERROR DE VARIABLE");System.out.println("exeption _>>>"+e.getMessage());}
		 
		 try {
			 String valor = prop.getProperty("ENABLEBUTTONS","MODIFICAR.PROPERTIES");
				 if(valor.toUpperCase().equals("TRUE")) {
					 ENABLEBUTTONS = true;
					 
				 }else {
					 ENABLEBUTTONS = false;
				 }
			 }catch(Exception e) {	logger.error("ENABLEBUTTONS.PROPERTIES ERROR DE VARIABLE");System.out.println("exeption _>>>"+e.getMessage());}
		 
		 
		 
		 try {baererServicioStarken	 		= prop.getProperty("BAERERSERCVICIOSTARKEN","MODIFICAR.PROPERTIES"); 
		 }catch(Exception e) {	logger.error("BAERERSERCVICIOSTARKEN.PROPERTIES ERROR DE VARIABLE");System.out.println("exeption _>>>"+e.getMessage());}
		 
		 
		 try {
			 SocketPORT	 					= Integer.parseInt(prop.getProperty("SOCKETPORT","MODIFICAR.PROPERTIES"));
		 }catch(Exception e) {	logger.error("SOCKETPORT.PROPERTIES ERROR DE VARIABLE");System.out.println("exeption _>>>"+e.getMessage());}
		 
		 
		 try {
			 TimerCinta	 					= Integer.parseInt(prop.getProperty("TIMERCINTA","MODIFICAR.PROPERTIES"));
		 }catch(Exception e) {	logger.error("TIMERCINTA.PROPERTIES ERROR DE VARIABLE");System.out.println("exeption _>>>"+e.getMessage());}
		 
		 
		 try {
			 BucleReinicioListenerBalanza	= Integer.parseInt(prop.getProperty("BUCLEREINICIOLISTENERBALANZA","MODIFICAR.PROPERTIES"));
		 }catch(Exception e) {	logger.error("BUCLEREINICIOLISTENERBALANZA.PROPERTIES ERROR DE VARIABLE");System.out.println("exeption _>>>"+e.getMessage());}
		 
		 try {
			 MAXWEIGHT						= Integer.parseInt(prop.getProperty("MAXWEIGHT","MODIFICAR.PROPERTIES"));
		 }catch(Exception e) {	logger.error("MAXWEIGHT.PROPERTIES ERROR DE VARIABLE");System.out.println("exeption _>>>"+e.getMessage());}
		 
		
		 logger.info("Archivo .properties cargado");
		 
	 }
	 
	 
	 
	 

}
