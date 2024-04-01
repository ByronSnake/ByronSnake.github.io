package com.Qclass.demo.Services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramBot;


import com.Qclass.demo.classes.AlertasBot;
import com.Qclass.demo.classes.CrearExcelAlertas;
import com.Qclass.demo.classes.DatosQB;
import com.Qclass.demo.classes.DatosResponse;
import com.Qclass.demo.classes.MyBean;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;


@Component
public class Telegram {

	private final AlertasBot alertas;

	private final static AlertasBot alertasStatic = new AlertasBot();

	final static Logger logger = LoggerFactory.getLogger(Telegram.class);

	//@Autowired
	//public static DatosResponse datos;

	private static MyBean myBeanInstance;
	
	@Autowired
	public MyBean mybean;

	
	
	
	@Autowired
	public Telegram(AlertasBot alertas) {

		this.alertas = alertas;

	}
	public static void setMyBeanInstance(MyBean myBean) {
        myBeanInstance = myBean;
    }
	
	public static void enviarAlertaDatosCaidos(String mensaje) {

		String ruta = "/home/ubuntu/Qclass/backEnd/Sucursal.txt";
		String sucursal = "";

		try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
			String linea;

			// Leer cada línea del archivo hasta el final
			while ((linea = br.readLine()) != null) {
				// System.out.println(linea);
				sucursal = linea;
			}
		} catch (IOException e) {
			// Manejar la excepción en caso de error de lectura
			e.printStackTrace();
		}
		

		String destinatario = "-4042502376";
		enviarNotificacionStatic(destinatario, "¡Alerta!\n" + "Sucursal: " + sucursal + " " + mensaje);
		CrearExcelAlertas.agregarAlertaDatos(destinatario, mensaje);
		
		//JsonObject jsonAlerta = new JsonObject();
		//jsonAlerta.addProperty("sucursal", sucursal);
		//jsonAlerta.addProperty("mensaje", mensaje);

		// Convertir el objeto JSON a una cadena JSON
		//String jsonString = new Gson().toJson(jsonAlerta);
		// Enviar el JSON a la otra aplicación
		//enviarNotificacionPorJson(jsonAlerta);
	}
	private static void enviarNotificacionPorJson(JsonObject jsonAlerta) {
        try {
            String url = "http://localhost:8082/API/Prueba/alerta";  // Reemplaza con la URL correcta
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);

            // Configurar el encabezado para indicar que el contenido es JSON
            httpPost.setHeader("Content-Type", "application/json");

            // Crear el objeto JSON con la información de la alerta
            StringEntity jsonEntity = new StringEntity(jsonAlerta.toString());
            httpPost.setEntity(jsonEntity);

            // Realizar la solicitud
            HttpResponse response = httpClient.execute(httpPost);

            
            int statusCode = response.getStatusLine().getStatusCode();
            logger.info("Respuesta del servidor: " + statusCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public void enviarAlerta(String mensaje) {
		// DatosQB[] dtQB = datos.obtenerDatosQuickboxStatic();
		String sucursal = mybean.getDeviceName();
		
		// Reemplaza "destinatario" con el ID del chat o el nombre de usuario de la
		// persona a la que deseas enviar la alerta
		String destinatario = "-4042502376";
		enviarNotificacion(destinatario, "¡ALERTA!\n" + " Sucursal: " + sucursal + " " + mensaje);

		CrearExcelAlertas.agregarAlerta(destinatario, mensaje);
		
		//JsonObject jsonAlerta = new JsonObject();
		//jsonAlerta.addProperty("sucursal", sucursal);
		//jsonAlerta.addProperty("mensaje", mensaje);

		// Convertir el objeto JSON a una cadena JSON
		//String jsonString = new Gson().toJson(jsonAlerta);
		// Enviar el JSON a la otra aplicación
		//enviarNotificacionPorJson(jsonAlerta);
	}

	private void enviarNotificacion(String chatId, String mensaje) {
		SendMessage sendMessage = new SendMessage(chatId, mensaje);
		try {
			alertas.execute(sendMessage);
		} catch (TelegramApiException e) {
			e.printStackTrace(); // Manejar la excepción según sea necesario
			logger.error("Error al enviar mensaje a Telegram: " + e.getMessage());

		}
	}

	public static void enviarAlertaStatic(String mensaje) {
		//DatosQB[] dtQB = datos.obtenerDatosQuickboxStatic();
		String sucursal = myBeanInstance.getDeviceName();
		String destinatario = "-4042502376";
		System.out.println("Sucursal: " + sucursal);
		enviarNotificacionStatic(destinatario, "¡ALERTA!\n" + " Sucursal: " + sucursal + " " + mensaje);
		CrearExcelAlertas.agregarAlerta(destinatario, mensaje);
		
		//JsonObject jsonAlerta = new JsonObject();
		//jsonAlerta.addProperty("sucursal", sucursal);
		//jsonAlerta.addProperty("mensaje", mensaje);

		// Convertir el objeto JSON a una cadena JSON
		//String jsonString = new Gson().toJson(jsonAlerta);
		// Enviar el JSON a la otra aplicación
		//enviarNotificacionPorJson(jsonAlerta);
	}

	private static void enviarNotificacionStatic(String chatId, String mensaje) {
		SendMessage sendMessage = new SendMessage(chatId, mensaje);

		try {
			alertasStatic.execute(sendMessage);
		} catch (TelegramApiException e) {
			e.printStackTrace(); // Manejar la excepción según sea necesario
			logger.error("Error al enviar mensaje a Telegram: " + e.getMessage());

		}
	}
}
