package com.Qclass.demo.classes;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CrearExcelAlertas {

	private static final String EXCEL_FILE_PATH = "/home/ubuntu/Qclass/backEnd/";

	@Autowired
	public static DatosResponse datos;
	
	private static MyBean myBeanInstance;
	
	public static void setMyBeanInstance(MyBean myBean) {
        myBeanInstance = myBean;
    }

	public static void agregarAlerta(String chatId, String mensaje) {

		//DatosQB[] dtQB = datos.obtenerDatosQuickboxStatic();
		String sucursal = myBeanInstance.getDeviceName();
		try {
			Workbook workbook;

			// Intenta cargar el libro de trabajo existente
			try {
				workbook = new XSSFWorkbook(new FileInputStream(EXCEL_FILE_PATH + sucursal + "- Alertas.xlsx"));
			} catch (IOException e) {
				// Si el archivo no existe, crea un nuevo libro de trabajo
				workbook = new XSSFWorkbook();
			}

			// Verifica si la hoja "Alertas" ya existe
			Sheet sheet = workbook.getSheet("Alertas");
			if (sheet == null) {
				// Si no existe, crea una nueva hoja
				sheet = workbook.createSheet("Alertas");

				// Crea la primera fila con encabezados
				Row headerRow = sheet.createRow(0);
				headerRow.createCell(0).setCellValue("Sucursal");
				headerRow.createCell(1).setCellValue("Mensaje");
				headerRow.createCell(2).setCellValue("Fecha y Hora");
			}

			// Crea una nueva fila para la alerta
			Row dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
			dataRow.createCell(0).setCellValue(myBeanInstance.getDeviceName());
			dataRow.createCell(1).setCellValue(mensaje);
			dataRow.createCell(2).setCellValue(obtenerFechaYHoraActual());

			// Escribe el libro de trabajo en el archivo Excel
			try (FileOutputStream outputStream = new FileOutputStream(EXCEL_FILE_PATH + sucursal + "- Alertas.xlsx",
					false)) {

				System.out.println("Alerta agregada al archivo Excel correctamente.");
				workbook.write(outputStream);
				workbook.close();
			} catch (Exception e) {
				// Manejo de excepciones
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error al agregar la alerta al archivo Excel: " + e.getMessage());
			for (StackTraceElement element : e.getStackTrace()) {
				System.err.println(element);
			}
		}
	}
	public static void agregarAlertaDatos(String chatId, String mensaje) {

		String ruta = "/home/ubuntu/Qclass/backEnd/Sucursal.txt";
		String sucursal ="";
		
		try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;

            // Leer cada línea del archivo hasta el final
            while ((linea = br.readLine()) != null) {
              //  System.out.println(linea);
                sucursal = linea;
            }
        } catch (IOException e) {
            // Manejar la excepción en caso de error de lectura
            e.printStackTrace();
        }
		
		try {
			Workbook workbook;

			// Intenta cargar el libro de trabajo existente
			try {
				workbook = new XSSFWorkbook(new FileInputStream(EXCEL_FILE_PATH + sucursal + "- Alertas.xlsx"));
			} catch (IOException e) {
				// Si el archivo no existe, crea un nuevo libro de trabajo
				workbook = new XSSFWorkbook();
			}

			// Verifica si la hoja "Alertas" ya existe
			Sheet sheet = workbook.getSheet("Alertas");
			if (sheet == null) {
				// Si no existe, crea una nueva hoja
				sheet = workbook.createSheet("Alertas");

				// Crea la primera fila con encabezados
				Row headerRow = sheet.createRow(0);
				headerRow.createCell(0).setCellValue("Sucursal");
				headerRow.createCell(1).setCellValue("Mensaje");
				headerRow.createCell(2).setCellValue("Fecha y Hora");
			}

			// Crea una nueva fila para la alerta
			Row dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
			dataRow.createCell(0).setCellValue(sucursal);
			dataRow.createCell(1).setCellValue(mensaje);
			dataRow.createCell(2).setCellValue(obtenerFechaYHoraActual());

			// Escribe el libro de trabajo en el archivo Excel
			try (FileOutputStream outputStream = new FileOutputStream(EXCEL_FILE_PATH + sucursal + "- Alertas.xlsx",
					false)) {

				System.out.println("Alerta agregada al archivo Excel correctamente.");
				workbook.write(outputStream);
				workbook.close();
			} catch (Exception e) {
				// Manejo de excepciones
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error al agregar la alerta al archivo Excel: " + e.getMessage());
			for (StackTraceElement element : e.getStackTrace()) {
				System.err.println(element);
			}
		}
	}
	private static String obtenerFechaYHoraActual() {
		LocalDateTime fechaHoraActual = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return fechaHoraActual.format(formatter);
	}
}
