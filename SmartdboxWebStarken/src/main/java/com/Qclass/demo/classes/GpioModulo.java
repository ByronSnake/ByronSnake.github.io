package com.Qclass.demo.classes;




import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;

import com.Qclass.demo.RestController.CheckPagoController;
import com.Qclass.demo.Services.StarkenServices;
import com.Qclass.demo.Services.Telegram;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.exception.UnsupportedPinPullResistanceException;
import com.pi4j.io.serial.Baud;
import com.pi4j.io.serial.DataBits;
import com.pi4j.io.serial.FlowControl;
import com.pi4j.io.serial.Parity;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialConfig;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.StopBits;
import com.pi4j.wiringpi.GpioUtil;

import jakarta.validation.metadata.ReturnValueDescriptor;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

//si hay alguna falla desde hoy 10/01/24 es por checktemperatura 
//comentar @component y compilar 
@Component
public class GpioModulo {
	final static Logger logger = LoggerFactory.getLogger(GpioModulo.class);

	private static final int TIEMPO_MAXIMO_ACTIVACION = 1; // Definir el tiempo máximo en segundos
	private static Timer timer;

	private static boolean comandoEjecutado = false;
	
	private static boolean AlertaEnviada = false;
	
	@Autowired
	private static Telegram alerta;

	private static MyBean myBeanInstance;
	
	@Autowired
	public MyBean myBean;
	@Autowired
	private static DatosResponse datos;
	// private static final String SERVER_HOST;

//	Metodo por si acaso dejase de funcionar el otro metodo 

//	public static double leerPuertoSerie3() {
//		double peso = 0;
//
//		SerialPort serialPort = null;
//
//		try {
//			serialPort = new SerialPort("/dev/ttySC0"); // Reemplaza con el nombre de tu puerto serial
//			serialPort.openPort();
//			serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
//					SerialPort.PARITY_NONE);
//
//			StringBuilder lineBuilder = new StringBuilder();
//			boolean pesoEncontrado = false;
//			int tiempoMaximoEspera = 10000; // Tiempo máximo de espera en milisegundos
//			long tiempoInicio = System.currentTimeMillis();
//
//			while (!pesoEncontrado && (System.currentTimeMillis() - tiempoInicio) < tiempoMaximoEspera) {
//				if (serialPort.getInputBufferBytesCount() > 0) {
//					byte[] buffer = serialPort.readBytes(serialPort.getInputBufferBytesCount());
//					String data = new String(buffer);
//					lineBuilder.append(data);
//
//					String line = lineBuilder.toString().trim();
//					System.out.println("Datos recibidos: " + line);
//
//					// Utilizar expresión regular para extraer solo los números
//					Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
//					Matcher matcher = pattern.matcher(line);
//
//					if (matcher.find()) {
//						String numeroExtraido = matcher.group();
//						String numeroLimpio = numeroExtraido.replaceAll("^0+", ""); // Elimina los ceros iniciales
//						System.out.println("Número limpio: " + numeroLimpio);
//						peso = Double.parseDouble(numeroLimpio);
//						pesoEncontrado = true;
//					}
//
//					lineBuilder.setLength(0); // Reinicia el StringBuilder
//				}
//
//				try {
//					Thread.sleep(100); // Espera un breve período antes de volver a verificar los datos
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//
//		} catch (SerialPortException ex) {
//			System.out.println("Error al leer datos del puerto serial: " + ex.getMessage());
//		} finally {
//			try {
//				if (serialPort != null && serialPort.isOpened()) {
//					serialPort.closePort();
//				}
//			} catch (SerialPortException e) {
//				e.printStackTrace();
//			}
//		}
//
//		StarkenServices.setAttribute("PESOLimpio", peso);
//		return peso;
//	}

	// Metodo funcional de lectura peso Desde el puerto serie
	/*
	 * public static double leerPuertoSerie3() { double peso = 0; boolean pesoValido
	 * = false;
	 * 
	 * SerialPort serialPort = null;
	 * 
	 * try { serialPort = new SerialPort("/dev/ttyUSB0"); // Reemplaza con el nombre
	 * del pueto serial serialPort.openPort();
	 * serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, //
	 * Configuracion puerto serie SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);//
	 * hasta esta linea
	 * 
	 * StringBuilder lineBuilder = new StringBuilder(); int tiempoMaximoEspera =
	 * 5000; // Tiempo máximo de espera en milisegundos long tiempoInicio =
	 * System.currentTimeMillis();
	 * 
	 * serialPort.readBytes(serialPort.getInputBufferBytesCount());
	 * 
	 * // Thread.sleep(100);
	 * 
	 * while (!pesoValido && (System.currentTimeMillis() - tiempoInicio) <
	 * tiempoMaximoEspera) { if (serialPort.getInputBufferBytesCount() > 0) { byte[]
	 * buffer = serialPort.readBytes(serialPort.getInputBufferBytesCount()); String
	 * data = new String(buffer); lineBuilder.append(data);
	 * 
	 * String line = lineBuilder.toString().trim();
	 * System.out.println("Datos recibidos: " + line);
	 * 
	 * // Utilizar expresión regular para extraer solo los números Pattern pattern =
	 * Pattern.compile("\\d+(\\.\\d+)?"); Matcher matcher = pattern.matcher(line);
	 * 
	 * if (matcher.find()) { String numeroExtraido = matcher.group(); String
	 * numeroLimpio = numeroExtraido.replaceAll("^0+", ""); // Elimina los ceros
	 * iniciales System.out.println("Número limpio: " + numeroLimpio); peso =
	 * Double.parseDouble(numeroLimpio); pesoValido = true; break; // Salir del
	 * bucle mientras se ha encontrado un peso válido }
	 * 
	 * lineBuilder.setLength(0); // Reinicia el StringBuilder }
	 * 
	 * try { Thread.sleep(100); // Espera un breve período antes de volver a
	 * verificar los datos } catch (InterruptedException e) { e.printStackTrace(); }
	 * }
	 * 
	 * } catch (SerialPortException ex) {
	 * System.out.println("Error al leer datos del puerto serial: " +
	 * ex.getMessage()); } finally { try { if (serialPort != null &&
	 * serialPort.isOpened()) { serialPort.closePort(); } } catch
	 * (SerialPortException e) { e.printStackTrace(); } }
	 * 
	 * if (!pesoValido) { peso = 320; logger.info("Murio la pesa :("); }
	 * logger.info("PESO LIMPIO " + peso);
	 * StarkenServices.setAttribute("PESOLimpio", peso); return peso; }
	 */

	public static void setMyBeanInstance(MyBean myBean) {
        myBeanInstance = myBean;
    }
	
	private static class TemporizadorAlerta extends TimerTask {

		private String mensajeAlerta;

		public TemporizadorAlerta(String mensajeAlerta) {

			this.mensajeAlerta = mensajeAlerta;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			alerta.enviarAlertaStatic(mensajeAlerta);

		}

	}

// Metodo Optimizado leerPuertoSerie3 Borrar y descomentar si no funciona 
	public static double leerPuertoSerie3() {
		double peso = 0;
		boolean pesoValido = false;

		SerialPort serialPort = null;

		try {
			serialPort = new SerialPort("/dev/ttyUSB0");
			serialPort.openPort();
			serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			StringBuilder lineBuilder = new StringBuilder();
			int tiempoMaximoEspera = 10000; // Tiempo máximo de espera en milisegundos
			long tiempoInicio = System.currentTimeMillis();

			while (!pesoValido && (System.currentTimeMillis() - tiempoInicio) < tiempoMaximoEspera) {
				if (serialPort.getInputBufferBytesCount() > 0) {

					byte[] buffer = serialPort.readBytes(serialPort.getInputBufferBytesCount());

					// Filtra caracteres no imprimibles y no números
					String line = new String(buffer, StandardCharsets.US_ASCII);
					line = line.replaceAll("[^\\d.]", "");

					lineBuilder.append(line);

					if (lineBuilder.length() > 0) {
						System.out.println("Datos recibidos: " + lineBuilder.toString());

						// Utilizar expresión regular para extraer solo los números
						Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
						Matcher matcher = pattern.matcher(lineBuilder);

						if (matcher.find()) {
							String numeroExtraido = matcher.group();
							String numeroLimpio = numeroExtraido.replaceAll("^0+", "");
							// System.out.println("Número limpio: " + numeroLimpio);
							peso = Double.parseDouble(numeroLimpio);
							pesoValido = true;
						}

						lineBuilder.setLength(0); // Reinicia el StringBuilder
					}
				}

				try {
					Thread.sleep(100); // Espera un breve período antes de volver a verificar los datos
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// fin while

		} catch (SerialPortException ex) {
			System.out.println("Error al leer datos del puerto serial: " + ex.getMessage());
		} finally {
			try {
				if (serialPort != null && serialPort.isOpened()) {
					serialPort.closePort();
				}
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}

		if (!pesoValido) {
			peso = 0.32;
			logger.info("Murio la pesa :(");
		}
		// logger.info("PESO LIMPIO " + peso);
		StarkenServices.setAttribute("PESOLimpio", peso);
		return peso;
	}

	public static double leerDatosBalanza() {

		double peso;

		// final GpioController gpio = GpioFactory.getInstance();

		// GpioPinDigitalOutput pinPesaTriger =
		// gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22, "", PinState.LOW);

		// try {
		// Thread.sleep(2000);
		// pinPesaTriger.high();
		// } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// pinRecetearPeso.high();
		// gpio.unprovisionPin(pinRecetearPeso);

		leerPuertoSerie3();
		peso = leerPuertoSerie3();
		logger.info("Peso " + peso);
		// gpio.shutdown();
		// gpio.unprovisionPin(pinPesaTriger);

		// GpioPinDigitalOutput pinRecetearPeso =
		// gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08, "", PinState.LOW);

		// pinRecetearPeso.high();

		// gpio.unprovisionPin(pinRecetearPeso);

		// System.out.println(peso);
		// resetearPesa();
		return peso;

	}

	public static void encenderLuz() {

		final GpioController gpio = GpioFactory.getInstance();

		GpioPinDigitalOutput pinLuz = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07);
		pinLuz.low();
		gpio.unprovisionPin(pinLuz);
		gpio.shutdown();

	}

	public static void apagarLuz() {

		final GpioController gpio = GpioFactory.getInstance();

		GpioPinDigitalOutput pinLuz = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07);

		pinLuz.high();

		gpio.unprovisionPin(pinLuz);
		gpio.shutdown();

	}

	public static Measurement validarMedidasCamara() {

		String ip = myBeanInstance.getIP();
		
		final String SERVER_HOST = ip;
		double peso = 0;
		int alto = 0;
		int largo = 0;
		int ancho = 0;

		int volumen = 0;
		int centerX = 0;
		int centerY = 0;
		int paqueteCount = 0;

		// Server Host
		// final String serverHost = "localhost";
		// final String serverHost = "127.0.0.1";

		Socket socketOfClient = null;
		BufferedWriter os = null;
		BufferedReader is = null;

		String RespuestaCamara = "";

		try {

			// Send a request to connect to the server is listening
			// on machine 'localhost' port 9999.

			socketOfClient = new Socket(SERVER_HOST, 7001);

			socketOfClient.setSoTimeout(2000);
			// Create output stream at the client (to send data to the server)
			os = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));

			// Input stream at Client (Receive data from the server).
			is = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));

		} catch (Exception e) {
			if (!comandoEjecutado) {
				ejecutarComandoUbuntu("sudo systemctl restart quickboxCamera.service");
				
				comandoEjecutado= true;
				
				if (!AlertaEnviada) {
					System.err.println("Error " + e.getMessage());
					alerta.enviarAlertaStatic("Problemas en la camara " + e.getMessage());
					
					AlertaEnviada = true;
				}
				
			}
			
			return validarMedidasCamara();
		}

		try {

			char STX = '\u0002';
			char ETX = '\u0003';

			char[] charArray = { STX, ETX };

			os.write(charArray);

			// End of line
			os.newLine();

			// Flush data.
			os.flush();

			System.out.println("is.read(): " + is.read());

			int etx = 0x03; // Valor decimal del carácter ETX

			StringBuilder responseBuilder = new StringBuilder();
			int charCode;
			while ((charCode = is.read()) != -1) {
				char receivedChar = (char) charCode;

				System.out.println("CHAR:" + receivedChar);

				responseBuilder.append(receivedChar);

				if (charCode == etx) {
					System.out.println("Carácter de control ETX encontrado. Finalizando la lectura.");
					break;
				}
			}

			String response = responseBuilder.toString();
			RespuestaCamara = response;
			System.out.println("Respuesta recibida: " + response);

			os.close();
			is.close();
			socketOfClient.close();
		} catch (Exception e) {
			if (!comandoEjecutado) {
				ejecutarComandoUbuntu("sudo systemctl restart quickboxCamera.service");
				
				comandoEjecutado = true;
				if (!AlertaEnviada) {
					System.err.println("Error " + e.getMessage());
					alerta.enviarAlertaStatic("Problemas en la camara " + e.getMessage());
					
					AlertaEnviada = true;
				}
			}
			
			
			return validarMedidasCamara();
		}

		// RespuestaCamara

		String[] data = RespuestaCamara.split(",");

		peso = GpioModulo.leerDatosBalanza();

		largo = Integer.parseInt(data[0]);
		ancho = Integer.parseInt(data[1]);
		alto = Integer.parseInt(data[2]);
		volumen = 1;

		centerX = Integer.parseInt(data[4]);
		centerY = Integer.parseInt(data[5]);
		paqueteCount = Integer.parseInt(data[6]);

		for (String variable : data) {
			logger.info("DATA: " + variable);
		}

		// transformar de mm a cm
		largo = convertirMmACm(largo);
		ancho = convertirMmACm(ancho);
		alto = convertirMmACm(alto);
		// volumen = convertirMmACm(volumen);

		logMedidas(peso, largo, ancho, alto, volumen, centerX, centerY, paqueteCount);

		return new Measurement(peso, alto, largo, ancho, volumen, centerX, centerY, paqueteCount);

		// return "Holaaaa";
	}
	
	
	public static void capturarImagenCamara() {
		 String cameraIP = "192.168.0.10";  // Reemplaza con la IP de tu cámara
	        int cameraPort = 7001;  // Reemplaza con el puerto correcto

	        try (Socket socket = new Socket(cameraIP, cameraPort);
	             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

	            // Envía un comando a la cámara
	            out.println("GET_DATA");

	            // Lee la respuesta de la cámara
	            String response = in.readLine();
	            System.out.println("Respuesta de la cámara: " + response);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}


	 public static void ejecutarComandoUbuntu(String comando) {
	        try {
	            // Ejecuta el comando en el terminal de Ubuntu
	            Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", comando});
	            System.out.println("Comando ejecutado correctamente.");
	        } catch (IOException e) {
	            System.out.println("Error al ejecutar el comando: " + e.getMessage());
	        }
	    }
//	Metodo para obtener las medidas de la camara

//	public static Measurement validarMedidasCamara() {
//
//		double peso = 0;
//		int alto = 0;
//		int largo = 0;
//		int ancho = 0;
//
//		int volumen = 0;
//		int centerX = 0;
//		int centerY = 0;
//		int paqueteCount = 0;
//
//		String respuestaCamara;
//
//		// final String serverHost = "10.230.183.102";
//		try (Socket socketCamara = conectarCamara()) {
//			if (socketCamara == null)
//				return null;
//
//			respuestaCamara = leerRespuestaCamara(socketCamara);
//
//			if (respuestaCamara == null)
//				return null;
//
//		} catch (Exception e) {
//			// TODO: handle exception
//			System.err.println("Error de comunicación: " + e.getMessage());
//			return null;
//		}
//
//		String[] datosCamara = respuestaCamara.split(",");
//
//		peso = GpioModulo.leerDatosBalanza();
//
//		alto = Integer.parseInt(datosCamara[0]);
//		ancho = Integer.parseInt(datosCamara[1]);
//		largo = Integer.parseInt(datosCamara[2]);
//
//		volumen = Integer.parseInt(datosCamara[3]);
//		centerX = Integer.parseInt(datosCamara[4]);
//		centerY = Integer.parseInt(datosCamara[5]);
//		paqueteCount = Integer.parseInt(datosCamara[6]);
//
//		for (String medidasCamara : datosCamara) {
//			logger.info("datosCamara: " + medidasCamara);
//		}
//
//		largo = convertirMmACm(largo);
//		ancho = convertirMmACm(ancho);
//		alto = convertirMmACm(alto);
//		volumen = convertirMmACm(volumen);
//
//		logMedidas(peso, largo, ancho, alto, volumen, centerX, centerY, paqueteCount);
//
//		return new Measurement(peso, alto, largo, ancho, volumen, centerX, centerY, paqueteCount);
//
//	}

// Metodo para convertir de mm a cm las medidas de la camara 	
	private static int convertirMmACm(int valorEnMm) {
		return Math.round(valorEnMm / 10);
	}

// Metodo para mostrar medidas de la camara
	private static void logMedidas(double peso, int largo, int ancho, int alto, int volumen, int centerX, int centerY,
			int paqueteCount) {
		logger.info("peso: " + peso);
		logger.info("largo: " + largo);
		logger.info("ancho: " + ancho);
		logger.info("alto: " + alto);
		logger.info("volumen: " + volumen);

		logger.info("centerX: " + centerX);
		logger.info("centerY: " + centerY);
		logger.info("paqueteCount: " + paqueteCount);
	}

	public static char hexToControlChar(String hex) {
		int decimalValue = Integer.parseInt(hex, 16);
		return (char) decimalValue;
	}

	public static void AccionarCintaTransportadora() {
		logger.info("---------------Accionando Cinta -------------");
		final GpioController gpio = GpioFactory.getInstance();

		GpioPinDigitalOutput myPinCintaTransportadora = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21);
		myPinCintaTransportadora.low();

		gpio.unprovisionPin(myPinCintaTransportadora);
		gpio.shutdown();

	}

	public static void DetenerCintaTransportadora() {

		logger.info("----------- Deteniendo Cinta --------------");

		final GpioController gpio = GpioFactory.getInstance();

		GpioPinDigitalOutput myPinCintaTransportadora = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21); // PIN NUMBER
		// (AMARILLO)
		// "My LED", // PIN FRIENDLY NAME (optional)
		// PinState.LOW); // PIN STARTUP STATE (optional)

		// myPinCintaTransportadora.toggle();
		myPinCintaTransportadora.high();
		gpio.unprovisionPin(myPinCintaTransportadora);
		gpio.shutdown();

	}

	public static void ONLY_AbrirPuertaTrasera() {

		try {

			final GpioController gpio = GpioFactory.getInstance();

			GpioPinDigitalOutput PinPuertaTrasera = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14, // PIN NUMBER
																										// (AMARILLO)
					"My LED", // PIN FRIENDLY NAME (optional)
					PinState.LOW); // PIN STARTUP STATE (optional)

			// Thread.sleep(5000);

			PinPuertaTrasera.isLow();

			gpio.shutdown();

			gpio.unprovisionPin(PinPuertaTrasera);

		} catch (Exception e) {
			alerta.enviarAlerta("Error al abrir la puerta Trasera");
			System.out.println("Nose puede usar GPIO CONTROL en este equipo");
		}

	}

	public static void resetearPesa() {

		final GpioController gpio = GpioFactory.getInstance();

		GpioPinDigitalOutput PinTriger = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08, // PIN NUMBER
				// (AMARILLO)
				"ResetPesa", // PIN FRIENDLY NAME (optional)
				PinState.LOW);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PinTriger.high();

		GpioPinDigitalOutput pinReset = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "pinTrigger", PinState.LOW);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pinReset.high();

		gpio.shutdown();

		gpio.unprovisionPin(pinReset);

		gpio.unprovisionPin(PinTriger);

		gpio.shutdown();

	}

	public static void PesoTarea() {

		// Este metodo esta dejando la pesa en 0

		final GpioController gpio = GpioFactory.getInstance();

		GpioPinDigitalOutput pinResetPesa = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "pinTrigger",
				PinState.LOW);

		try {
			pinResetPesa.high();
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		gpio.shutdown();

		gpio.unprovisionPin(pinResetPesa);

		gpio.shutdown();

	}

	public static void PesoPesar() {

		final GpioController gpio = GpioFactory.getInstance();

		GpioPinDigitalOutput pinActivacionPesa = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08, // PIN NUMBER
				// (AMARILLO)
				"activarPesa", // PIN FRIENDLY NAME (optional)
				PinState.LOW);

		try {
			pinActivacionPesa.high();
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		gpio.shutdown();

		gpio.unprovisionPin(pinActivacionPesa);

		gpio.shutdown();

	}

	public static void ONLY_CerrarPuertaTrasera() {

		try {

			final GpioController gpio = GpioFactory.getInstance();

			GpioPinDigitalOutput PinPuertaTrasera = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14, // PIN NUMBER
																										// (AMARILLO)
					"My LED", // PIN FRIENDLY NAME (optional)
					PinState.HIGH); // PIN STARTUP STATE (optional)

			// Thread.sleep(1000);
			PinPuertaTrasera.isLow();

			gpio.unprovisionPin(PinPuertaTrasera);
			gpio.shutdown();

		} catch (Exception e) {
			alerta.enviarAlerta("Error al cerrar la puerta Trasera");
			System.out.println("Nose puede usar GPIO CONTROL en este equipo");
		}

	}

	public static void pruebaPuerta() {
		System.out.println("se abrio la puerta");
		timer = new Timer();
		timer.schedule(new TemporizadorAlerta("Mensaje prueba"), TIEMPO_MAXIMO_ACTIVACION * 1000);

	}

	public static void pruebaMetodo(int contador) {
		int cont = 0;
		while (cont != contador) {
			cont++;
			System.out.println("Ejecuntando prueba");

		}

	}

	public static void pruebaCerrarpuerta() {

		System.out.println("Cerrando");
		if (timer != null) {
			timer.cancel();
		}
	}

	public static void ONLY_AbrirPuertaDelantera() {

		try {
			final GpioController gpio = GpioFactory.getInstance();

			GpioPinDigitalOutput PinPuertaDelantera = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, // PIN NUMBER
																										// (AMARILLO)
					"My LED", // PIN FRIENDLY NAME (optional)
					PinState.LOW); // PIN STARTUP STATE (optional)

			// Thread.sleep(1000);
			PinPuertaDelantera.isHigh();

			gpio.unprovisionPin(PinPuertaDelantera);
			gpio.shutdown();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			alerta.enviarAlerta("Error el abrir la puerta Delantera");
			e.printStackTrace();
		}

	}

	public static void ONLY_cerrarPuertaDelantera() {

		try {
			final GpioController gpio = GpioFactory.getInstance();

			GpioPinDigitalOutput PinPuertaDelantera = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, // PIN NUMBER
																										// (AMARILLO)
					"My LED", // PIN FRIENDLY NAME (optional)
					PinState.HIGH); // PIN STARTUP STATE (optional)

			PinPuertaDelantera.isLow();

			gpio.unprovisionPin(PinPuertaDelantera);
			gpio.shutdown();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			alerta.enviarAlerta("Error al cerrar la puerta Delantera");
			e.printStackTrace();
		}

	}

	public static boolean status_puertaDelantera() {

		final GpioController gpio = GpioFactory.getInstance();

		GpioPinDigitalInput pinLOw = gpio.provisionDigitalInputPin(RaspiPin.GPIO_15); // PIN RESISTANCE (optional)

		boolean status = false;

		if (pinLOw.isHigh()) {

			logger.info("Puerta delantera cerrada");
			// System.out.println(PinConfirmacionPuertaDelantera.isLow());
			status = true;

		} else {

			logger.info("Puerta delantera abierta");
			// System.out.println(PinConfirmacionPuertaDelantera.isHigh());
			status = false;
			// break;

		}

		gpio.unprovisionPin(pinLOw);
		gpio.shutdown();

		return status;
	}

	public static boolean status_puertaTrasera() {

		final GpioController gpio = GpioFactory.getInstance();

		GpioPinDigitalInput pinLOw = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29); // PIN RESISTANCE
		// (optional)

		// GpioPinDigitalInput pinHigh =
		// gpio.provisionDigitalInputPin(RaspiPin.GPIO_06);
		boolean status = false;

		// Thread.sleep(1000);
		if (pinLOw.isHigh()) {

			logger.info("Puerta trasera cerrada");

			status = true;

		} else {

			// if (pinHigh.isHigh()) {

			logger.info("Puerta trasera abierta");
			// System.out.println(pinHigh.isHigh());
			status = false;

		}
		gpio.unprovisionPin(pinLOw);
		gpio.shutdown();
		return status;
	}

	public static boolean status_puertaTraseraAbierta() {

		final GpioController gpio = GpioFactory.getInstance();

		GpioPinDigitalInput pinLOw = gpio.provisionDigitalInputPin(RaspiPin.GPIO_28); // PIN RESISTANCE

		boolean status = false;

		if (pinLOw.isHigh()) {

			status = true;

		} else {

			status = false;

		}
		gpio.unprovisionPin(pinLOw);
		gpio.shutdown();
		return status;

	}

	public static void verificarPuertaTraseraAbierta() {
		Timer temporizador = new Timer();
		int maximoSegundos = 50;
		temporizador.schedule(new TemporizadorAlerta("Problemas en el pin superior de la puerta trasera "),
				maximoSegundos * 1000);

		while (!status_puertaTraseraAbierta())
			;
		temporizador.cancel();
		temporizador.purge();
	}

	public static boolean check_puertaDelantera_cerrada() {

		Timer temporizador = new Timer();
		int maximoSegundos = 50;
		final GpioController gpio = GpioFactory.getInstance();

		GpioPinDigitalInput pinLow = gpio.provisionDigitalInputPin(RaspiPin.GPIO_15);

		boolean status = false;

		// logger.info("Antes del while check estado");
		// logger.info("Estado del pin " + pinLow.getState());
		int contador = 0;

		temporizador.schedule(new TemporizadorAlerta("Problemas al cerrar la puerta delantera"), maximoSegundos * 1000);

		while (contador < 2) {
			if (!pinLow.isLow()) {
				contador++;
				if (contador == 2)
					status = true;
			} else {

				contador = 0;

			}

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		temporizador.cancel();
		temporizador.purge();
		gpio.unprovisionPin(pinLow);
		gpio.shutdown();

		return status;
	}

	@Scheduled(fixedRate = 600000) // Ejecuta el método cada 5 segundos
	public void checkTemperatura() {
		try {
			String temperatura = obtenerTemperatura();
		//	temperatura = temperatura.replaceAll("[^\\d.,]", "");
			// temperatura = temperatura.replace(',', '.');
			double temperaturaDouble = Double.parseDouble(temperatura);

			// Establece un umbral de temperatura
			double umbral = 70.0;

			// Compara la temperatura con el umbral
			if (temperaturaDouble > umbral) {
				alerta.enviarAlertaStatic("La Rasperry llego a los: " + temperatura);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static double temperatura() {

		String temperatura;
		double temperaturaDouble = 0;
		try {
			temperatura = obtenerTemperaturaStatic();
			temperaturaDouble = Double.parseDouble(temperatura);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return temperaturaDouble;

	}

	private static String obtenerTemperaturaStatic() throws IOException {
		// Para Linux
		Process process = Runtime.getRuntime().exec("/usr/bin/vcgencmd measure_temp");
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("temp=")) {
					return line.substring(5, line.indexOf("'"));
				}
			}
		}
		return "N/A";
	}



	private String obtenerTemperatura() throws IOException {
		// Para Linux
		Process process = Runtime.getRuntime().exec("/usr/bin/vcgencmd measure_temp");
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("temp=")) {
					return line.substring(5, line.indexOf("'"));
				}
			}
		}
		return "N/A";

		/* Windows */
		/*
		 * try { Process process = Runtime.getRuntime().exec(
		 * "wmic /namespace:\\\\root\\cimv2 path Win32_PerfFormattedData_Counters_ThermalZoneInformation get Temperature"
		 * ); try (InputStream inputStream = process.getInputStream(); BufferedReader
		 * reader = new BufferedReader(new InputStreamReader(inputStream))) { String
		 * line; while ((line = reader.readLine()) != null) {
		 * 
		 * try { int temperatureValue = Integer.parseInt(line.trim());
		 * //System.out.println( + temperatureValue); String celsius =
		 * agregarComa(temperatureValue); return celsius + "°C"; } catch
		 * (NumberFormatException e) { // Ignore lines that are not valid integers } } }
		 * } catch (IOException e) { e.printStackTrace(); // Handle the exception
		 * according to your needs }
		 * 
		 * return "N/A";
		 */

	}

	private static String agregarComa(int temperatura) {

		String temp = Integer.toString(temperatura);

		if (temp.length() > 2) {

			StringBuilder sb = new StringBuilder(temp);

			sb.insert(2, ',');

			return sb.toString();
		}
		return temp;
	}

	public static boolean check_puertaTrasera_cerrada() {

		Timer temporizador = new Timer();
		int maximoSegundos = 30;
		final GpioController gpio = GpioFactory.getInstance();

		GpioPinDigitalInput pinLow = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29);

		boolean status = false;

		int contador = 0;

		temporizador.schedule(new TemporizadorAlerta("Problemas al cerrar la puerta Trasera"), maximoSegundos * 1000);
		while (contador < 2) {
			if (!pinLow.isLow()) {

				contador++;
				if (contador == 2)
					status = true;
			} else {

				contador = 0;

			}

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		temporizador.cancel();
		temporizador.purge();
		gpio.unprovisionPin(pinLow);
		gpio.shutdown();

		return status;

	}

	public static void desactivarClasificador() throws InterruptedException {

		logger.info("------------------- Desactivando Clasificador ------------------");

		final GpioController gpio = GpioFactory.getInstance();

		GpioPinDigitalOutput pinClasificador = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25);

		pinClasificador.high();

		gpio.unprovisionPin(pinClasificador);
		gpio.shutdown();
	}

	public static void accionarClasificador() throws InterruptedException {

		logger.info("--------------------- Accionando Clasificador ------------------");

		final GpioController gpio = GpioFactory.getInstance();

		GpioPinDigitalOutput pinClasificador = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25);

		pinClasificador.low();

		gpio.unprovisionPin(pinClasificador);

		gpio.shutdown();

		System.out.println("Accionando clasificador");

	}

	public static void accionarRodillos() {

		final GpioController gpio = GpioFactory.getInstance();

		GpioPinDigitalOutput pinClasificador = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00);

		logger.info("-------------Activando Sorter--------");
		pinClasificador.low();

		gpio.unprovisionPin(pinClasificador);

		gpio.shutdown();

		System.out.println("Accionando Rodillos");

	}

	public static void desactivarRodillos() {

		final GpioController gpio = GpioFactory.getInstance();

		GpioPinDigitalOutput pinClasificador = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00);
		logger.info("-------------Desactivando Sorter--------");
		pinClasificador.high();

		gpio.unprovisionPin(pinClasificador);
		gpio.shutdown();
	}

}

//
//public static void AccionarCintaTransportadora() throws InterruptedException {
//    final GpioController gpio = GpioFactory.getInstance();
//
//    GpioPinDigitalOutput myPinCintaTransportadora = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27);
//    myPinCintaTransportadora.low();
//
//    gpio.shutdown();
//    gpio.unprovisionPin(myPinCintaTransportadora);
//}
//
//public static double leerPuertoSerie3() {
//    double peso = 0;
//    SerialPort serialPort = null;
//    Lock serialPortLock = new ReentrantLock();
//
//    try {
//        serialPort = new SerialPort("/dev/ttySC0");
//        serialPort.openPort();
//        serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
//                SerialPort.PARITY_NONE);
//
//        StringBuilder lineBuilder = new StringBuilder();
//        boolean pesoEncontrado = false;
//        int tiempoMaximoEspera = 10000; // Tiempo máximo de espera en milisegundos
//        long tiempoInicio = System.currentTimeMillis();
//
//        while (!pesoEncontrado && (System.currentTimeMillis() - tiempoInicio) < tiempoMaximoEspera) {
//            if (serialPort.getInputBufferBytesCount() > 0) {
//                serialPortLock.lock(); // Bloquear acceso al puerto serial
//                try {
//                    byte[] buffer = serialPort.readBytes(serialPort.getInputBufferBytesCount());
//                    String data = new String(buffer);
//                    lineBuilder.append(data);
//
//                    String line = lineBuilder.toString().trim();
//                    System.out.println("Datos recibidos: " + line);
//
//                    // Utilizar expresión regular para extraer solo los números
//                    Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
//                    Matcher matcher = pattern.matcher(line);
//
//                    if (matcher.find()) {
//                        String numeroExtraido = matcher.group();
//                        String numeroLimpio = numeroExtraido.replaceAll("^0+", "");
//                        System.out.println("Número limpio: " + numeroLimpio);
//                        peso = Double.parseDouble(numeroLimpio);
//                        pesoEncontrado = true;
//                    }
//
//                    lineBuilder.setLength(0);
//                } finally {
//                    serialPortLock.unlock(); // Desbloquear acceso al puerto serial
//                }
//            }
//
//            Thread.sleep(100);
//        }
//
//    } catch (SerialPortException | InterruptedException ex) {
//        System.out.println("Error al leer datos del puerto serial: " + ex.getMessage());
//    } finally {
//        try {
//            if (serialPort != null && serialPort.isOpened()) {
//                serialPort.closePort();
//            }
//        } catch (SerialPortException e) {
//            e.printStackTrace();
//        }
//    }
//
//    StarkenServices.setAttribute("PESOLimpio", peso);
//    return peso;
//}
//
//
