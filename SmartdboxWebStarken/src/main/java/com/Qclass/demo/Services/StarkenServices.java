package com.Qclass.demo.Services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.Qclass.demo.classes.CheckMeasureBulto;
import com.Qclass.demo.classes.Codebeautify;
import com.Qclass.demo.classes.DatosQB;
import com.Qclass.demo.classes.DatosQBService;
import com.Qclass.demo.classes.DatosResponse;
import com.Qclass.demo.classes.EncargoClass;
import com.Qclass.demo.classes.EncargosClass;
import com.Qclass.demo.classes.EncuestaClass;
import com.Qclass.demo.classes.EncuestaResponse;
import com.Qclass.demo.classes.LogStarken;
import com.Qclass.demo.classes.MultipartUtility;
import com.Qclass.demo.classes.MyBean;
//import com.Qclass.demo.classes.MyBean;
import com.Qclass.demo.classes.OF;
import com.Qclass.demo.classes.R_Preguntas;
import com.Qclass.demo.classes.Recepcionar;
import com.Qclass.demo.classes.RespuestaServicio;
import com.Qclass.demo.classes.ServiceResponse;
import com.Qclass.demo.classes.ValidarEncomienda;
import com.google.gson.Gson;

//import DefaultPackageName.StarkenServices;

public class StarkenServices {
	
	private static MyBean myBeanInstance;

	private static final DecimalFormat decimalformat = new DecimalFormat("0.00");

	private static final String RUTAJAR = "/home/ubuntu/Qclass/backEnd";

	// @Autowired
	// public MyBean mybean;
	//@Value("app.env")
	private static String appEnv = "PRD";
	
	//@Autowired
	//private static DatosResponse datos;

	@Autowired
	public static Telegram alertas;
	// public int valor;
	private static final Logger logger2 = LoggerFactory.getLogger(StarkenServices.class);

	static String bearerStarken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjM5LCJuYW1lIjoiZHNhYXNkIHNhZGFzZCIsInJ1biI6IjEyODMxNjA5NCIsIm1hc3Rlcl9pZCI6bnVsbCwiYXBwbGljYXRpb24iOnsiaWQiOjIsIm5hbWUiOiJTdGFya2VuIFBybyIsImNvZGUiOiJQUk8ifSwicm9sZSI6eyJpZCI6MSwiY29kZSI6IlVTRVIiLCJuYW1lIjoiVXN1YXJpbyJ9LCJpYXQiOjE2Mzc2OTI2MzAsImV4cCI6MTYzNzcwMzQzMH0.l1hqEqsPpcckB7WjZ_tb0Dyh049m-HBsp71ffOSuyb8";

	static String Endpoint_var = "https://totemlog.starken.cl/api/loggerqb";
	static String Endpoint_LOG_BD = "https://totemlog.starken.cl/api/loggerBD"; // PRD
//	String NroModulo = String.valueOf(datos.getPayload()[0].getNumero_modulo());
//	String IP = datos.getPayload()[0].getIp();
//	String DeviceName = datos.getPayload()[0].getDevice_name();
//	String sucursal = String.valueOf(datos.getPayload()[0].getAgencodigo());

	public static void setMyBeanInstance(MyBean myBean) {
        myBeanInstance = myBean;
    }
	
	public static void log_totem(String Tipo, String barra, Object T) {
		
		

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		System.out.println(dtf.format(LocalDateTime.now()));

		// String Endpoint = "http://192.168.0.101:5000/api/logger"; //PRD
		// String NroModulo = "1";
		// String IP = "192.168.0.10";
		// String DeviceName = "Socket_Server";
		// String sucursal = "1880";

		//DatosQB[] dtQB = datos.obtenerDatosQuickboxStatic();

		String Endpoint = Endpoint_var; // PRD
		// String Endpoint2 = Endpoint_LOG_BD;
		String NroModulo = myBeanInstance.getNroModulo();
		String IP = myBeanInstance.getIP();
		String DeviceName = myBeanInstance.getDeviceName();
		String sucursal = myBeanInstance.getAgenCodigo();

		String Bearer = " PENDIENTE DE ASIGNAR";

		Thread newThread = new Thread(() -> {

			// si esta comendato es porque no hay que pegarle a PRD
			try {
				URL url = new URL(Endpoint);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(30000);
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");

				String input = new LogStarken(IP, sucursal, NroModulo, DeviceName, Tipo, barra, T).SerializarJson();

				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();

				// -----------
				System.out.println("conn code -> " + conn.getResponseCode());
				System.out.println("conn msg  -> " + conn.getResponseMessage());

				String output;
				String StringOutput = "";

				if (conn.getResponseCode() == 404) {

					BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));

					System.out.println("Output from Server .... \n");
					while ((output = br.readLine()) != null) {
						// System.out.println(output+" \n");
						StringOutput = StringOutput + output;
					}
					System.out.println("Output " + StringOutput);

				}

				if (conn.getResponseCode() == 500) {
					BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
					System.out.println("Output from Server .... \n");
					while ((output = br.readLine()) != null) {
						System.out.println(output + " \n");
						StringOutput = StringOutput + output;
					}
				} else {
					BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
					System.out.println("Output from Server .... \n");
					while ((output = br.readLine()) != null) {
						// System.out.println(output);
						StringOutput = StringOutput + output;
					}
				}

				conn.disconnect();

			} catch (MalformedURLException e) {

				System.out.println("MalformedURLException >>>>>>>>>>>>" + e.getMessage());
				e.printStackTrace();

			} catch (IOException e) {

				System.out.println("IOException >>>>>>>>>>>>" + e.getMessage());
				e.printStackTrace();

			}

			if (!Tipo.substring(0, 2).equals("BD")) {
				// logger2.info("logger no tiene BD >> + no se envia al servicio de BD");
				return;
			}

			try {
				// AHORA SE LOGEA EN SERVICIO LOG BD porque si es funcional
				String Endpoint2 = Endpoint_LOG_BD;

				URL url = new URL(Endpoint2);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(30000);
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");

				String input = new LogStarken(IP, sucursal, NroModulo, DeviceName, Tipo, barra, T).SerializarJson();

				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();

				// -----------
				System.out.println("LOG_BD conn code -> " + conn.getResponseCode());
				System.out.println("LOG_BD conn msg  -> " + conn.getResponseMessage());

				String output;
				String StringOutput = "";

				if (conn.getResponseCode() == 404) {

					BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));

					System.out.println("Output from Server .... \n");
					while ((output = br.readLine()) != null) {
						// System.out.println(output+" \n");
						StringOutput = StringOutput + output;
					}
					System.out.println("Output " + StringOutput);

				}

				if (conn.getResponseCode() == 500) {
					BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
					// System.out.println("Output from Server .... \n");
					while ((output = br.readLine()) != null) {
						// System.out.println(output+" \n");
						StringOutput = StringOutput + output;
					}
				} else {
					BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
					System.out.println("Output from Server .... \n");
					while ((output = br.readLine()) != null) {
						// System.out.println(output);
						StringOutput = StringOutput + output;
					}
				}

				conn.disconnect();

			} catch (MalformedURLException e) {

				System.out.println("MalformedURLException >>>>>>>>>>>>" + e.getMessage());
				e.printStackTrace();

			} catch (IOException e) {

				System.out.println("IOException >>>>>>>>>>>>" + e.getMessage());
				e.printStackTrace();

			} catch (Exception e) {

				System.out.println(e);
				logger2.info("E" + e);
			}

		});
		newThread.start();

	}

	public static ServiceResponse PostCheckearPagoOF(String Bulto) {
		String Endpoint = "";
		
		if (appEnv.equals("QA")) {
			Endpoint = "https://exposeemisionqa.starken.cl/qc/checkPago"; // QA
		}
		else if (appEnv.equals("PRD")){
			Endpoint = "https://exposeemision.starken.cl/qc/checkPago"; // PRD
		}else {
			Endpoint ="";
		}
		// String Endpoint = "https://exposeemisionqa.starken.cl/qc/checkPago"; // QA
		//String Endpoint = "https://exposeemision.starken.cl/qc/checkPago"; // PRD
		
		ServiceResponse respuesta = new ServiceResponse(500, "Error - Default");

		logger2.info("Endpoint " + Endpoint);
		// LogsFront.Loggear("Endpoint "+Endpoint);

		String Bearer = " PENDIENTE DE ASIGNAR";

		String ejemplo = "33333333333333111000111333";
		if (Bulto.length() != ejemplo.length()) {
			return new ServiceResponse(500, "BARCODE no tiene formato Starken");
		}

		String ofBulto = Bulto.substring(14, 23);

		try {
			// URL url = new URL("https://jsonplaceholder.typicode.com/posts");
			URL url = new URL(Endpoint);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			try {
				conn.setRequestProperty("Authorization", "Bearer " + bearerStarken);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger2.error(e.getStackTrace().toString());
				// LogsFront.Loggear(e.getStackTrace().toString());
			}

			OF of = new OF(ofBulto);
			String TO_Json = "";
			try {
				Gson gson = new Gson();
				TO_Json = gson.toJson(of);
			} catch (Exception e) {
				System.out.println("Error GSON !!!!!!");
			}
			logger2.info("TO_Json" + TO_Json);

			logger2.info("input" + TO_Json);
			// LogsFront.Loggear("input" + input);
			StarkenServices.log_totem("BD checkPago Request", Bulto, of);
			OutputStream os = conn.getOutputStream();
			os.write(TO_Json.getBytes());
			os.flush();

			String output;
			String StringOutput = "";

			if (conn.getResponseCode() == 404) {

				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));

				// System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					// System.out.println(output+" \n");
					StringOutput = StringOutput + output;
				}
				logger2.info("Output " + StringOutput);

				ServiceResponse sr = new ServiceResponse(500, "DEFAUTL - no se pudo deserealizar >" + StringOutput);
				try {
					sr.DeserealizarMensaje(StringOutput);
				} catch (Exception e) {

				}
				StarkenServices.log_totem("BD checkPago Request", Bulto, sr);

			} else {

				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				while ((output = br.readLine()) != null) {
					StringOutput = StringOutput + output;
				}
				logger2.info("Output " + StringOutput);
				ServiceResponse sr = new ServiceResponse(500, "DEFAUTL - no se pudo deserealizar >" + StringOutput);
				try {
					sr.DeserealizarMensaje(StringOutput);
				} catch (Exception e) {
				}
				StarkenServices.log_totem("BD checkPago Response", Bulto, sr);
			}
			respuesta.DeserealizarMensaje(StringOutput);
			conn.disconnect();

		} catch (MalformedURLException e) {

			System.out.println("AQUI");
			e.printStackTrace();
			logger2.error(e.getStackTrace().toString());
			// LogsFront.Loggear("error "+e.getMessage());

		} catch (IOException e) {

			System.out.println("AQUI 2");
			e.printStackTrace();
			logger2.error(e.getStackTrace().toString());
			// LogsFront.Loggear("error "+e.getMessage());
		}

		int respuestaFRONT = 500;

		try {
			respuestaFRONT = Integer.valueOf(respuesta.getMessage().toString().substring(0, 3));
			return new ServiceResponse(respuestaFRONT, respuesta.getMessage());
		} catch (Exception e) {

		}

		return respuesta;
	}

	public static ServiceResponse PostValidarBUlto(String Bulto, String peso, String alto, String largo, String ancho, String codUsuario, String ubiCodigo) {

		String Endpoint ="";
		
		CheckMeasureBulto checkMeasureBulto = new CheckMeasureBulto(Bulto, peso, alto, largo, ancho, codUsuario, ubiCodigo);

	//	String Endpoint ="";
		if (appEnv.equals("QA")) {
			Endpoint ="https://exposeemisionqa.starken.cl/qc/checkMeasureBypackge"; // QA
		}else if (appEnv.equals("PRD")) {
			Endpoint = "https://exposeemision.starken.cl/qc/checkMeasureBypackge"; //PRD
		}else {
			Endpoint = "";
		}
		// String Endpoint ="https://exposeemisionqa.starken.cl/qc/checkMeasureBypackge"; // QA
		//String Endpoint = "https://exposeemision.starken.cl/qc/checkMeasureBypackge"; //PRD

		logger2.info("Endpoint " + Endpoint);

		String of = null;
		try {
			of = Bulto.substring(14, 23);

		} catch (Exception ex) {
			of = null;

		}

		// String Endpoint = "";
		String Bearer = " PENDIENTE DE ASIGNAR";

		ServiceResponse respuesta = new ServiceResponse(500, "DEFAULT ");

		try {
			// URL url = new URL("https://jsonplaceholder.typicode.com/posts");
			URL url = new URL(Endpoint);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			// conn.setRequestProperty("Authorization", "Bearer {token}");
			try {
				conn.setRequestProperty("Authorization", "Bearer " + bearerStarken);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// logger.info("Click successfull");
				logger2.error(e.getStackTrace().toString());
				// LogsFront.Loggear("error >>" +e.getStackTrace().toString());
			}
			// e.g. bearer token=
			// eyJhbGciOiXXXzUxMiJ9.eyJzdWIiOiPyc2hhcm1hQHBsdW1zbGljZS5jb206OjE6OjkwIiwiZXhwIjoxNTM3MzQyNTIxLCJpYXQiOjE1MzY3Mzc3MjF9.O33zP2l_0eDNfcqSQz29jUGJC-_THYsXllrmkFnk85dNRbAw66dyEKBP5dVcFUuNTA8zhA83kk3Y41_qZYx43T

			String TO_Json = "";
			try {
				Gson gson = new Gson();
				TO_Json = gson.toJson(checkMeasureBulto);
			} catch (Exception e) {
				System.out.println("Error GSON !!!!!!");
			}
			StarkenServices.log_totem("BD checkMeasureBypackge Request", Bulto, checkMeasureBulto);

			logger2.info("TO_Json" + TO_Json);

			OutputStream os = conn.getOutputStream();
			os.write(TO_Json.getBytes());
			os.flush();

			// -----------
			System.out.println("conn code -> " + conn.getResponseCode());
			System.out.println("conn msg  -> " + conn.getResponseMessage());

			String output;
			String StringOutput = "";

			if (conn.getResponseCode() == 404) {

				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));

				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output + " \n");
					StringOutput = StringOutput + output;
				}

				logger2.info("Output " + StringOutput);

				ServiceResponse sr = new ServiceResponse(500, "DEFAUTL - no se pudo deserealizar >" + StringOutput);
				try {
					sr.DeserealizarMensaje(StringOutput);
				} catch (Exception e) {

				}

				StarkenServices.log_totem("BD checkMeasureBypackge Response", Bulto, sr);

			}

			if (conn.getResponseCode() == 500) {

				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));

				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output + " \n");
					StringOutput = StringOutput + output;
				}

				logger2.info("Output " + StringOutput);

				ServiceResponse sr = new ServiceResponse(500, "DEFAUTL - no se pudo deserealizar >" + StringOutput);
				try {
					sr.DeserealizarMensaje(StringOutput);
				} catch (Exception e) {

				}

				StarkenServices.log_totem("BD checkMeasureBypackge Response", Bulto, sr);

			} else {

				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					// System.out.println(output);
					StringOutput = StringOutput + output;
				}

				logger2.info("Output " + StringOutput);

				ServiceResponse sr = new ServiceResponse(500, "DEFAUTL - no se pudo deserealizar >" + StringOutput);
				try {
					sr.DeserealizarMensaje(StringOutput);
				} catch (Exception e) {

				}
				StarkenServices.log_totem("BD checkMeasureBypackge Response", Bulto, sr);
			}

			respuesta.DeserealizarMensaje(StringOutput);

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

			logger2.error(e.getStackTrace().toString());
			// LogsFront.Loggear("error " + e.getMessage());
		} catch (IOException e) {

			e.printStackTrace();

			logger2.error(e.getStackTrace().toString());
			// LogsFront.Loggear("error " + e.getMessage());
		}
		return respuesta;

	}

	public static ServiceResponse PostRecepcionBulto(String Bulto, String agencia, int codUsuario, int ubicCodigo) {
		String Endpoint = "";
		
		if (appEnv.equals("QA")) {
			Endpoint = "https://exposeemisionqa.starken.cl/qc/recepcion"; // QA
		}else if (appEnv.equals("PRD")) {
			
			Endpoint = "https://exposeemision.starken.cl/qc/recepcion"; // PRD
		}else {
			Endpoint ="";
		}
		
		
		// String Endpoint = "https://exposeemisionqa.starken.cl/qc/recepcion"; // QA
		//String Endpoint = "https://exposeemision.starken.cl/qc/recepcion"; // PRD

		logger2.info("Endpoint " + Endpoint);
		// LogsFront.Loggear("Endpoint "+Endpoint);
		// String Endpoint = "";
		String Bearer = " PENDIENTE DE ASIGNAR";

		// ServiceResponse respuesta = new ServiceResponse(500, "Default");
		ServiceResponse respuesta = new ServiceResponse(500, "Default");
		/*
		 * if (true) { System.out.println("Respondiendo " + Bulto + "Agencia" +
		 * agencia); return respuesta; }
		 */

		try {
			// URL url = new URL("https://jsonplaceholder.typicode.com/posts");
			URL url = new URL(Endpoint);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			try {
				conn.setRequestProperty("Authorization", "Bearer " + bearerStarken);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				// logger2.error(e.getStackTrace().toString());
				// LogsFront.Loggear("error "+e.getMessage());
			}

			Recepcionar recepcionarbulto = new Recepcionar(Bulto, agencia, codUsuario, ubicCodigo);
			String TO_Json = "";
			try {
				Gson gson = new Gson();
				TO_Json = gson.toJson(recepcionarbulto);
				System.out.println("JSOOON " + TO_Json);

			} catch (Exception e) {
				System.out.println("Error GSON !!!!!!");
			}
			logger2.info("TO_Json" + TO_Json);

			logger2.info("input" + TO_Json);
			// LogsFront.Loggear("input" + input);
			StarkenServices.log_totem("BD recepcion Request", Bulto, TO_Json);// ---------------------
			// System.out.println("INPUT.." + input);
			OutputStream os = conn.getOutputStream();
			os.write(TO_Json.getBytes());
			os.flush();

			// -----------
			// System.out.println("conn code -> "+conn.getResponseCode());
			// System.out.println("conn msg -> "+conn.getResponseMessage());

			String output;
			String StringOutput = "";

			if (conn.getResponseCode() == 404) {

				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));

				// System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					// System.out.println(output+" \n");
					StringOutput = StringOutput + output;
				}

				logger2.info("Output " + StringOutput);
				ServiceResponse sr = new ServiceResponse(500, "DEFAUTL - no se pudo deserealizar >" + StringOutput);
				try {
					sr.DeserealizarMensaje(StringOutput);
				} catch (Exception e) {

				}
				// LogsFront.Loggear("Output " +StringOutput);
				StarkenServices.log_totem("BD recepcion Response", Bulto, sr);// ---------------------
			}

			if (conn.getResponseCode() == 500) {

				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));

				// System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					// System.out.println(output+" \n");
					StringOutput = StringOutput + output;
				}

				logger2.info("Output " + StringOutput);
				ServiceResponse sr = new ServiceResponse(500, "DEFAUTL - no se pudo deserealizar >" + StringOutput);
				try {
					sr.DeserealizarMensaje(StringOutput);
				} catch (Exception e) {

				}
				// LogsFront.Loggear("Output " +StringOutput);
				StarkenServices.log_totem("BD recepcion Response", Bulto, sr);// ---------------------

			} else {

				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

				// System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					// System.out.println(output);
					StringOutput = StringOutput + output;
				}

				logger2.info("Output " + StringOutput);
				ServiceResponse sr = new ServiceResponse(500, "DEFAUTL - no se pudo deserealizar >" + StringOutput);
				try {
					sr.DeserealizarMensaje(StringOutput);
				} catch (Exception e) {

				}
				// LogsFront.Loggear("Output " +StringOutput);
				StarkenServices.log_totem("BD recepcion Response", Bulto, sr);// ---------------------
			}
			respuesta.DeserealizarMensaje(StringOutput);

			System.out.println("Respuesta de Recepcion " + respuesta.toString());
			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();
			logger2.error(e.getStackTrace().toString());
			// LogsFront.Loggear("error "+ e.getMessage());

		} catch (IOException e) {

			e.printStackTrace();
			logger2.error(e.getStackTrace().toString());
			// LogsFront.Loggear("error "+ e.getMessage());
		}
		System.out.println("Antes del return repecion " + respuesta);
		return respuesta;
	}

	public static List<String> BultosOF(String Bulto) {
		
		
		List<String> ListaBultos = new ArrayList<>();
		String ejemplo = "33333333333333111000111333";
		System.out.println("Bulto " + Bulto);
		if (Bulto.length() != ejemplo.length()) {

			return null;
		}

		String ofBulto = Bulto.substring(14, 23);

		String POSTS = "";

		String respuestaJson = "";
		
		String strurl = "";
		
		if(appEnv.equals("QA")) {
			strurl = "https://exposeemisionqa.starken.cl/qc/bultosOF/" + ofBulto; // QA
		}else if (appEnv.equals("PRD")) {
			
			strurl = "https://exposeemision.starken.cl/qc/bultosOF/" + ofBulto; //PRD
		}else {
			
			strurl ="";
		}
		// String strurl = "https://exposeemisionqa.starken.cl/qc/bultosOF/" + ofBulto; // QA
		//String strurl = "https://exposeemision.starken.cl/qc/bultosOF/" + ofBulto; //PRD

		logger2.info("Endpoint " + strurl);
		// LogsFront.Loggear("Endpoint "+strurl);
		// System.out.println("URL " + strurl);
		try {

			// https://jsonplaceholder.typicode.com/posts/1
			URL url = new URL(strurl);// your url i.e fetch data from .
			// URL url = new
			// URL("http://localhost:3002/RestWebserviceDemo/rest/json/product/dynamicData?size=5");//your
			// url i.e fetch data from .
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			try {
				conn.setRequestProperty("Authorization", "Bearer " + bearerStarken);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger2.error(e.getStackTrace().toString());
				// LogsFront.Loggear("Error >"+e.getMessage());
			}

			if (conn.getResponseCode() != 200) {

				logger2.info("Respuesta bultoOf " + conn.getResponseCode() + " " + conn.getResponseMessage());
				BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				String errorOutput = "";
				String line;
				while ((line = errorReader.readLine()) != null) {
					errorOutput += line;
				}
				logger2.info("Respuesta Json bultosOF: " + errorOutput);

				StarkenServices.log_totem("BD bultosOF Response", Bulto,
						conn.getResponseCode() + " - " + conn.getResponseMessage());
				
				alertas.enviarAlertaStatic("Bultos OF: " + ofBulto + errorOutput); 
				throw new RuntimeException("Respuesta BultoOf: " + errorOutput);
			}
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);
			String output = "";
			POSTS = output;
			StarkenServices.log_totem("BD bultosOF Request", Bulto, ofBulto);// ---------------------

			// StarkenServices.setAttribute(RUTAJAR+"/"+"Bultos.json", br.toString());

			while ((output = br.readLine()) != null) {
				System.out.println(output);
				POSTS = POSTS + output;
			}

			logger2.info("Output " + POSTS);

			EncargosClass EncargosClass = new EncargosClass();
			try {
				EncargosClass.DeserealizarMensaje(POSTS);
				for (EncargoClass E : EncargosClass.Encargos) {
					ListaBultos.add(E.ENCACODIGOBARRA);
				}

			} catch (Exception e) {
				logger2.info("" + e.getMessage());
				logger2.info("" + e);
			}
			// LogsFront.Loggear("Output " +POSTS);
			StarkenServices.log_totem("BD bultosOF Response", Bulto, EncargosClass);// ---------------------

			conn.disconnect();

			return ListaBultos;

		} catch (Exception e) {
			System.out.println("Exception in NetClientGet:- " + e);
			logger2.info(e.getStackTrace().toString());
			return ListaBultos;
		}
	}

	public static void encuesta(String Barcode, int Notaevaluacion) {

	//	DatosQB[] datosQB = datos.obtenerDatosQuickboxStatic();
		// https://www.codejava.net/java-se/networking/upload-files-by-sending-multipart-request-programmatically
		try {

			if (Notaevaluacion >= 0 && Notaevaluacion <= 2) {
				String nota = String.valueOf(Notaevaluacion);
				ValidarEncomienda validar = new ValidarEncomienda(Barcode, "0", "0", "0", "0");

				String charset = "UTF-8";
				String requestURL = "https://api.queop.com/guarda-respuesta";
				logger2.info("requestURL" + requestURL);
				// LogsFront.Loggear("requestURL " + requestURL);

				EncuestaClass Encuesta = new EncuestaClass();

				Encuesta.idInstancia = 1317;
				Encuesta.IP = myBeanInstance.getIP();
				Encuesta.idEncuesta = 6252;
				Encuesta.codigoSucursal = "SA";
				R_Preguntas rPreguntasEncuesta = new R_Preguntas(nota, 136907);
				Encuesta.agregarRespuestaPreguntasEncuesta(rPreguntasEncuesta);
				R_Preguntas rPreguntasAbiertas = new R_Preguntas(validar.GetOF().trim(), 70);
				Encuesta.agregarRespuestaPreguntasAbiertas(rPreguntasAbiertas);

				String mensaje = "{\"idInstancia\": 1317," // modificacion el 6 de febrero 2023 a peticion de QUEOP

						+ "\"IP\": \"10.10.10.11\"," + "\"idEncuesta\": 6252," + "\"codigoSucursal\": \"SA\","
						+ "\"rPreguntasEncuesta\": [{" + "\"respuesta\": \"" + nota + "\","// <<<< valor encuesta 0=roja
																							// 1=amarilla 2=verde\r\n"
						+ "\"idPregunta\": 136907" + "}]," + "\"respuestaNPS\": \"\"," + "\"comentarioNPS\": \"\","
						+ "\"respuestaDC\": \"\"," + "\"comentarioDC\": \"\"," + "\"rPreguntasAbiertas\": [{"
						+ "\"respuesta\": \"" + validar.GetOF().trim() + "\",  "// <<<< OF\r\n"
						+ "\"idPregunta\": 70" + "}]" + "}";

				String TO_Json = "";
				try {
					Gson gson = new Gson();
					TO_Json = gson.toJson(Encuesta);
				} catch (Exception e) {
					System.out.println("Error GSON !!!!!!");
				}
				logger2.info("input" + TO_Json);

				// StarkenServices.log_totem("BD Queop Eval Request", Barcode, Encuesta);//
				// ---------------------

				try {
					MultipartUtility multipart = new MultipartUtility(requestURL, charset);

					multipart.addFormField("key", "e41e7bde524619bcc33c43fdcf6183bd");
					multipart.addFormField("respuesta", TO_Json);

					multipart.addFormField("keywords", "Java,upload,Spring");

					List<String> response = multipart.finish();

					System.out.println("SERVER REPLIED:");

					String Output = "";
					for (String line : response) {
						System.out.println(line);
						Output = Output + line;
						logger2.info("Ouput" + line);
						// LogsFront.Loggear("Ouput" + line);
					}
					EncuestaResponse ER = new EncuestaResponse();
					try {
						ER.DeserealizarMensaje(Output);
					} catch (Exception e) {

					}
					// StarkenServices.log_totem("BD Queop Eval", Barcode, ER);
					switch (Notaevaluacion) {
					case 0:
						StarkenServices.log_totem("BD Queop Eval", Barcode, "ROJO");
						break; // break es opcional

					case 1:
						StarkenServices.log_totem("BD Queop Eval", Barcode, "AMARILLO");
						break; // break es opcional
					case 2:
						StarkenServices.log_totem("BD Queop Eval", Barcode, "VERDE");
						break; // break es opcional

					}

				} catch (IOException ex) {
					System.out.println(ex);
					logger2.error("IOException" + ex);
					logger2.error("IOException" + ex.getMessage());
					// LogsFront.Loggear("IOException" + ex.getMessage());
					logger2.error("IOException" + ex.getStackTrace());
				}

			} else {
				System.out.println("la nota no puede inferior a 0 o mayor a 2");
				logger2.error("la nota no puede inferior a 0 o mayor a 2");
				// LogsFront.Loggear("la nota no puede inferior a 0 o mayor a 2" );

			}

		} catch (Exception ex2) {
			System.out.println(ex2);
			logger2.error("ENCUESTA exeption" + ex2);
			logger2.error("ENCUESTA exeption" + ex2.getMessage());
			// LogsFront.Loggear("ENCUESTA exeption" + ex2.getMessage());
			logger2.error("ENCUESTA exeption" + ex2.getStackTrace());

		}

	}

	public static void universalServiceCall(String URLService, String Tipo, String Bulto, Object Inputclass,
			String Method) {
		String TO_Json = "";
		String output;
		String StringOutput = "";
		ServiceResponse sr = new ServiceResponse(500, "DEFAUTL - no se pudo deserealizar >" + StringOutput);
		Gson gson = new Gson();

		try {
			// URL url = new URL("https://jsonplaceholder.typicode.com/posts");
			URL url = new URL(URLService);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(Method);
			// conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "Bearer " + bearerStarken);

			// e.g. bearer token=
			// eyJhbGciOiXXXzUxMiJ9.eyJzdWIiOiPyc2hhcm1hQHBsdW1zbGljZS5jb206OjE6OjkwIiwiZXhwIjoxNTM3MzQyNTIxLCJpYXQiOjE1MzY3Mzc3MjF9.O33zP2l_0eDNfcqSQz29jUGJC-_THYsXllrmkFnk85dNRbAw66dyEKBP5dVcFUuNTA8zhA83kk3Y41_qZYx43T

			TO_Json = gson.toJson(Inputclass);
			StarkenServices.log_totem("BD " + Tipo + " Request", Bulto, Inputclass);
			logger2.info("TO_Json" + TO_Json);
			OutputStream os = conn.getOutputStream();
			os.write(TO_Json.getBytes());
			os.flush();
			System.out.println("conn code -> " + conn.getResponseCode());
			System.out.println("conn msg  -> " + conn.getResponseMessage());
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
			while ((output = br.readLine()) != null) {
				StringOutput = StringOutput + output;
			}
			logger2.info("Output " + StringOutput);
			sr.DeserealizarMensaje(StringOutput);
			StarkenServices.log_totem("BD " + Tipo + " Response", Bulto, sr);
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			logger2.error(e.getStackTrace().toString());
		} catch (IOException e) {
			e.printStackTrace();
			logger2.error(e.getStackTrace().toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger2.error(e.getStackTrace().toString());
		}

	}

	public static String getAttribute(String nombre) {

		StringBuilder contenido = new StringBuilder();
		try {
			FileReader file = new FileReader(RUTAJAR + "/" + nombre + ".txt");
			BufferedReader buffer = new BufferedReader(file);
			String linea;

			//System.out.println("Buffer del getAtributte" + buffer.lines());

			while ((linea = buffer.readLine()) != null) {
				// System.out.println("linea" + linea);
				contenido.append(linea);
				//logger2.info("Leyendo " + nombre);
				// contenido.append("\n");
				//logger2.info("Leyendo" + linea);
			}
			buffer.close();

		} catch (IOException e) {
			// TODO: handle exception
			System.out.println("Error al leer el Archivo: " + e.getMessage());
		}

		return contenido.toString();
	}

	public static List<String> getAttributeS(String nombre) {

		List<String> contenido = new ArrayList<>();

		try {
			FileReader file = new FileReader(RUTAJAR + "/" + nombre + ".txt");
			BufferedReader buffer = new BufferedReader(file);
			String linea;

		//	System.out.println("Debug: Leyendo archivo " + nombre);

		//	logger2.info("Debug: leyendo archivo" + nombre);

			//System.out.println("Buffer del getatributte list " + buffer.lines());
			while ((linea = buffer.readLine()) != null) {

			//	System.out.println("Linea leida " + linea);

//				logger2.info("Leyendo " + nombre);

				contenido.add(linea);

	//			logger2.info("Leyendo " + linea);
			}
		//	System.out.println("Debug Archivo leido correctamente ");

			//logger2.info("Debug: Archivo leido correctamente");

			buffer.close();
		} catch (IOException e) {
			System.out.println("Error al leer el Archivo: " + e.getMessage());
			return null;
		}

		return contenido;
	}

	public static void setAttribute(String nombre, String valor) {

		String Contenido = valor;

		try {
			FileWriter Archivo = new FileWriter(RUTAJAR + "/" + nombre + ".txt", false);
			BufferedWriter buffer = new BufferedWriter(Archivo);

			buffer.write(Contenido);

			buffer.close();
			//logger2.info("Escribiendo " + nombre);
			//logger2.info("Escribiendo " + Contenido);
			//System.out.println("Documento Creado");

		} catch (IOException e) {
			// TODO: handle exception

			System.out.println("Error al crear el archivo");
		}
	}

	public static void setAttribute(String nombre, double valor) {

		double Contenido = valor;

		try {
			FileWriter Archivo = new FileWriter(RUTAJAR + "/" + nombre + ".txt", false);
			BufferedWriter buffer = new BufferedWriter(Archivo);

			String pesoString = Double.toString(Contenido);
			buffer.write(pesoString);

			buffer.close();
			//logger2.info("Escribiendo " + nombre);
			//logger2.info("Escribiendo linea" + pesoString);

			//System.out.println("Documento Creado");

		} catch (IOException e) {
			// TODO: handle exception

			System.out.println("Error al crear el archivo");
		}
	}

	public static void setAttribute(String nombre, List<String> valor) {
		// TO FIX MEJORAR LIMPIEZA DE ARCHIVOS.

		List<String> contenido = valor;

		try {
			// archivos para limpiar
			FileWriter Archivo = new FileWriter(RUTAJAR + "/" + nombre + ".txt", false);

			BufferedWriter buffer = new BufferedWriter(Archivo);

			buffer.write("");
			buffer.close();
			Archivo.close();
			// ---------------------------------------------------------------------------------------------

			// archivos nuevos
			FileWriter Archivo2 = new FileWriter(RUTAJAR + "/" + nombre + ".txt", true);
			BufferedWriter buffer2 = new BufferedWriter(Archivo2);

			//logger2.info("Escribiendo " + nombre);

			for (String linea : contenido) {

				buffer2.write(linea);

				buffer2.newLine();

				//logger2.info("Escribiendo linea" + linea);
			}

			buffer2.close();
			Archivo2.close();
			//System.out.println("Documento Creado");

		} catch (IOException e) {
			// TODO: handle exception

			System.out.println("Error al crear el archivo");

		}

	}

}
