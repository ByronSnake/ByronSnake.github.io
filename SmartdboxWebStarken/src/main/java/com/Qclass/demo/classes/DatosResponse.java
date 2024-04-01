package com.Qclass.demo.classes;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.Qclass.demo.Services.Telegram;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import jakarta.annotation.PostConstruct;

@Component
public class DatosResponse {

	@Autowired
	private DatosQBService datosQBService;
	@SerializedName("statusCode")
	private int statusCode;
	@SerializedName("message")
	private String[] message;
	@SerializedName("payload")
	private DatosQB[] payload;

	// @Autowired
	// public Telegram alerta;

	@Autowired
	public static Telegram alertaStatic;
	// @Value("app.env")
	private static String appEnv = "PRD";

	@PostConstruct
	public void init() {
		// Llama a obtenerDatosQuickbox al iniciar la aplicación
		obtenerDatosQuickbox();
		// System.out.println("Datos endpoint " + obtenerDatosQuickbox());
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String[] getMessage() {
		return message;
	}

	public void setMessage(String[] message) {
		this.message = message;
	}

	public DatosQB[] getPayload() {
		return payload;
	}

	public void setPayload(DatosQB[] payload) {
		this.payload = payload;
	}

	private DatosResponse() {
	}

	@Override
	public String toString() {
		return "DatosResponse [statusCode=" + statusCode + ", message=" + Arrays.toString(message) + ", payload="
				+ Arrays.toString(payload) + "]";
	}

	
	
	public void obtenerDatosQuickbox() {

		String ambiente = "PRD";
		String ipAddress = obtenerDireccionIPEth0();

		//String ipAddress = "10.233.10.100";
		// System.out.println("IP de la máquina local: " + ipAddress);

		if (ipAddress == null) {
			// Si la IP es nula, no hagas nada y muestra una advertencia
			System.err.println("La IP es nula, no se pueden obtener datos.");
			return;
		}

		String url = "";

		if (ambiente.equals("QA")) {
			url = "https://apidashboardqa.starken.cl/quickbox/datosQuickbox/" + ipAddress;
		} else if (ambiente.equals("PRD")) {

			url = "https://apidashboard.starken.cl/quickbox/datosQuickbox/" + ipAddress;
		} else {
			url = "";
		}

		DatosResponse datosResponse = null;

		try {
			// Crea una conexión HTTP a la URL
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");

			// Lee la respuesta JSON
			Scanner scanner = new Scanner(connection.getInputStream());

			StringBuilder jsonResponse = new StringBuilder();

			while (scanner.hasNextLine()) {
				jsonResponse.append(scanner.nextLine());
			}

			scanner.close();
			// System.out.println("Respuesta json " + jsonResponse.toString());

			// Convierte la respuesta JSON en un objeto Java utilizando Gson
			Gson gson = new Gson();
			datosResponse = gson.fromJson(jsonResponse.toString(), DatosResponse.class);
			if (datosResponse != null && datosResponse.getPayload() != null) {
				// Configura los datos en la instancia actual
				this.statusCode = datosResponse.getStatusCode();
				this.message = datosResponse.getMessage();
				this.payload = datosResponse.getPayload();

				if (payload.length > 0) {

					guardarPayloadEnArchivo(datosResponse.getPayload(),
							"/home/ubuntu/Qclass/backEnd/datosQB.json");

					datosQBService.guardarDatosQB(payload[0]);
				} else {
					alertaStatic.enviarAlertaDatosCaidos("Problemas al obtener los datos de la Parametrizacion");
				}

			} else {
				alertaStatic.enviarAlertaDatosCaidos("El servicio esta caido");
			}
		} catch (IOException e) {
			e.printStackTrace();
			alertaStatic.enviarAlertaDatosCaidos("Error al obtener datos desde el servicio.");

		}

	}

	public String obtenerDireccionIPEth0() {
		try {
			// Ejecuta el comando "ip a" para obtener información de las interfaces de red
			Process process = Runtime.getRuntime().exec("ip a");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			boolean eth0Found = false;

			while ((line = reader.readLine()) != null) {
				// Busca la línea que contiene "eth0" y una dirección IP
				if (line.contains("eth0") && line.contains("inet")) {
					int startIndex = line.indexOf("inet") + 5;
					int endIndex = line.indexOf("/", startIndex);
					if (endIndex > startIndex) {
						String ipAddress = line.substring(startIndex, endIndex);
						return ipAddress;
					}
				}
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static DatosQB[] obtenerDatosQuickboxStatic() {
		String ambiente = "PRD";

		String ipAddress = obtenerDireccionIPEth0Static();
		//String ipAddress = "10.233.10.100";
		// System.out.println("IP de la máquina local: " + ipAddress);

		if (ipAddress == null) {
			// Si la IP es nula, no hagas nada y muestra una advertencia
			System.err.println("La IP es nula, no se pueden obtener datos.");
			return null;
		}

		String url = "";

		if (ambiente.equals("QA")) {
			url = "https://apidashboardqa.starken.cl/quickbox/datosQuickbox/" + ipAddress;
		} else if (ambiente.equals("PRD")) {

			url = "https://apidashboard.starken.cl/quickbox/datosQuickbox/" + ipAddress;
		} else {
			url = "";
		}
		DatosResponse datosResponse = null;

		try {
			// Crea una conexión HTTP a la URL
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");

			// Lee la respuesta JSON
			Scanner scanner = new Scanner(connection.getInputStream());

			StringBuilder jsonResponse = new StringBuilder();

			while (scanner.hasNextLine()) {
				jsonResponse.append(scanner.nextLine());
			}

			scanner.close();
			// System.out.println("Respuesta json " + jsonResponse.toString());

			// Convierte la respuesta JSON en un objeto Java utilizando Gson
			Gson gson = new Gson();
			datosResponse = gson.fromJson(jsonResponse.toString(), DatosResponse.class);
			if (datosResponse != null && datosResponse.getPayload() != null) {
				// Configura los datos en la instancia actual
				// Debes manejar la lógica de guardar datos en datosQBService
				int statusCode = datosResponse.getStatusCode();
				String[] message = datosResponse.getMessage();
				DatosQB[] payload = datosResponse.getPayload();
				// datosQBService.guardarDatosQB(payload[0]);

				if (payload.length > 0) {
					guardarPayloadEnArchivo(datosResponse.getPayload(),
							"/home/ubuntu/Qclass/backEnd/datosQB.json");

					return payload;
				} else {
					alertaStatic.enviarAlertaDatosCaidos("Problemas al obtener los datos de la parametrizacion");
				}
			} else {
				alertaStatic.enviarAlertaDatosCaidos(" El servicio DatosQB esta caido");
			}
		} catch (IOException e) {
			e.printStackTrace();
			alertaStatic.enviarAlertaDatosCaidos("Error al obtener datos desde el servicio DatosQB.");
		}
		return null;

	}

	public static String obtenerDireccionIPEth0Static() {
		try {
			// Ejecuta el comando "ip a" para obtener información de las interfaces de red
			Process process = Runtime.getRuntime().exec("ip a");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			boolean eth0Found = false;

			while ((line = reader.readLine()) != null) {
				// Busca la línea que contiene "eth0" y una dirección IP
				if (line.contains("eth0") && line.contains("inet")) {
					int startIndex = line.indexOf("inet") + 5;
					int endIndex = line.indexOf("/", startIndex);
					if (endIndex > startIndex) {
						String ipAddress = line.substring(startIndex, endIndex);
						return ipAddress;
					}
				}
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private boolean alertaEnviada = false; // Estado de la alerta
	private static long inicioAlertaTiempo = 0; // Momento en que se inició la alerta

	//@Scheduled(fixedRate = 5000) // Ejecutar cada 5 segundos
	public void verificarDatosQuickbox() {
		DatosResponse datosResponse = null;
		String ambiente = "PRD";
		String ipAddress = obtenerDireccionIPEth0Static();

		String url = "";

		if (ambiente.equals("QA")) {
			url = "https://apidashboardqa.starken.cl/quickbox/datosQuickbox/" + ipAddress;
		} else if (ambiente.equals("PRD")) {
			url = "https://apidashboard.starken.cl/quickbox/datosQuickbox/" + ipAddress;
		} else {
			url = "";
		}

		try {
			HttpURLConnection conexion = (HttpURLConnection) new URL(url).openConnection();
			conexion.setRequestMethod("GET");
			int responseCode = conexion.getResponseCode();

			Scanner scanner = new Scanner(conexion.getInputStream());
			StringBuilder jsonResponse = new StringBuilder();

			while (scanner.hasNextLine()) {
				jsonResponse.append(scanner.nextLine());
			}

			scanner.close();

			Gson gson = new Gson();
			datosResponse = gson.fromJson(jsonResponse.toString(), DatosResponse.class);

			if (datosResponse != null && datosResponse.getPayload() != null) {
				DatosQB[] payload = datosResponse.getPayload();
				verificarPayload(payload);
			} else {
				manejarAlerta("El servicio DatosQB está caído. Código de estado: " + datosResponse.statusCode);
			}

		} catch (IOException e) {
			e.getMessage();
			if (alertaEnviada == false) {
				verificarPayload(payload);
				manejarAlerta("Error al conectar con el servicio. Mensaje de error: " + e.getMessage());

			}
		}
	}

	private static void guardarPayloadEnArchivo(DatosQB[] payload, String ruta) {
	    if (payload == null || payload.length == 0) {
	        System.err.println("El payload es nulo o vacío. No se puede guardar en el archivo.");
	        return;
	    }

	    try (FileWriter fileWriter = new FileWriter(ruta)) {
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        String jsonPayload = gson.toJson(payload);
	        fileWriter.write(jsonPayload);
	        System.out.println("DatosQB guardado en el archivo de forma ordenada: " + ruta);
	    } catch (IOException e) {
	        e.printStackTrace();
	        System.err.println("Error al guardar los datos en el archivo.");
	    }
	}

	private void verificarPayload(DatosQB[] datosQB) {
		// System.out.println("Datosqb[] " + datosQB);
		if (datosQB != null && datosQB.length > 0) {

			// System.out.println("AlertaEnviada " + alertaEnviada);

			if (alertaEnviada) {
				// System.out.println("if (alertaEnviada) " + alertaEnviada);
				long segundos = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - inicioAlertaTiempo);
				System.out.println("Segundos " + segundos);
				String formatoTiempo = formatearTiempo(segundos);
				manejarAlerta("Se han vuelto a obtener datos después de " + formatoTiempo + ". Tiempo transcurrido: "
						+ formatoTiempo);
				alertaEnviada = false;
			}
		} else {
			// System.out.println("else " + alertaEnviada);
			if (!alertaEnviada) {
				// System.out.println(" if (!alertaEnviada) " + alertaEnviada);
				inicioAlertaTiempo = System.currentTimeMillis();
				// System.out.println("Inicio Tiempo " + inicioAlertaTiempo);

				manejarAlerta("Problemas al obtener los datos de la parametrización. Iniciando alerta.");
			}
		}
	}

	private void manejarAlerta(String mensaje) {
		alertaStatic.enviarAlertaDatosCaidos(mensaje);
		alertaEnviada = true;
	}

	private String formatearTiempo(long segundos) {
		long horas = segundos / 3600;
		long minutos = (segundos % 3600) / 60;
		long segundosRestantes = segundos % 60;

		if (horas > 0) {
			return String.format("%d horas %d minutos %d segundos", horas, minutos, segundosRestantes);
		} else if (minutos > 0) {
			return String.format("%d minutos %d segundos", minutos, segundosRestantes);
		} else {
			return String.format("%d segundos", segundosRestantes);
		}
	}

}
