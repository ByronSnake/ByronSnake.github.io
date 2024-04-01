package com.Qclass.demo.classes;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.SerialPortException;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.Qclass.demo.Services.StarkenServices;

public class LectutaSerial {

	public static double leerPuertoSerie3() {
		final double[] peso = { 0 };
		int tiempoMaximoEspera = 10000;

		try {
			Serial serial = SerialFactory.createInstance();

			serial.open("/dev/ttySC0", 9600);
			serial.addListener(new SerialDataEventListener() {
				StringBuilder lineBuilder = new StringBuilder();
				boolean pesoEncontrado = false;
				long tiempoInicio = System.currentTimeMillis();

				@Override
				public void dataReceived(SerialDataEvent event) {
					byte[] buffer = null;
					try {
						buffer = event.getAsciiString().getBytes();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String data = new String(buffer);

					lineBuilder.append(data);

					String line = lineBuilder.toString().trim();
					System.out.println("Datos recibidos: " + line);

					// Utilizar expresión regular para extraer solo los números
					Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
					Matcher matcher = pattern.matcher(line);

					if (matcher.find()) {
						String numeroExtraido = matcher.group();
						String numeroLimpio = numeroExtraido.replaceAll("^0+", ""); // Elimina los ceros iniciales
						System.out.println("Número limpio: " + numeroLimpio);
						peso[0] = Double.parseDouble(numeroLimpio);
						pesoEncontrado = true;
					}

					lineBuilder.setLength(0); // Reinicia el StringBuilder

					if (pesoEncontrado || (System.currentTimeMillis() - tiempoInicio) >= tiempoMaximoEspera) {
						serial.removeListener(this);
						try {
							serial.close();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						;
					}
				}
			});

			// Esperar a que se complete la lectura
			Thread.sleep(tiempoMaximoEspera);

		} catch (IOException | SerialPortException | InterruptedException ex) {
			System.out.println("Error al leer datos del puerto serial: " + ex.getMessage());
		}

		StarkenServices.setAttribute("PESOLimpio", peso[0]);
		return peso[0];
	}

}
