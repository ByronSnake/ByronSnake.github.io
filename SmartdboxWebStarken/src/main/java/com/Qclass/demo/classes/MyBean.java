package com.Qclass.demo.classes;

import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.Qclass.demo.Services.StarkenServices;
import com.Qclass.demo.Services.Telegram;
import com.google.gson.Gson;

import jakarta.annotation.PostConstruct;


@Component
public class MyBean {
	// https://stackoverflow.com/questions/25764459/spring-boot-application-properties-value-not-populating

	final static Logger logger = LoggerFactory.getLogger(MyBean.class);

	@Autowired 
	public static Telegram alertas;
	//@Autowired
	//private DatosResponse datos;
	//@Autowired
//	private final DatosQBService datosQB;

	private DatosQB datosQB;
	private int id;
	private String prop;
	private String IP;
	private String agenCodigo;
	private String NroModulo;
	private String DeviceName;
	private String usbDriver;

	private int TiempoCapturaPeso; // milisegundos
	private double limitePeso; // limite en kilos
	private int limiteMedidaAlto; // limite en centimetros
	private int limiteMedidaLargo;
	private int limiteMedidaAncho;
	private double pesoToleranciaMaxima; // en kilogramos
	private int medidasToleranciaMaxima;
	
	private String codUsuario;
	private String ubiCodigo;
	private double pesoAccionarPatin;
	
	public int getToleranciaMedidasLimite() {
		return medidasToleranciaMaxima;
	}

	public void setToleranciaMedidasLimite(int toleranciaMedidasLimite) {
		this.medidasToleranciaMaxima = toleranciaMedidasLimite;
	}
	@Value("${app.env}")
	private String appEnv; // ( PRD - QA )

	public int getId() {
		return id;
	}

	
	public double getPesoAccionarPatin() {
		return pesoAccionarPatin;
	}

	public void setPesoAccionarPatin(double pesoAccionarPatin) {
		this.pesoAccionarPatin = pesoAccionarPatin;
	}

	public double getPesoToleranciaMaxima() {
		return pesoToleranciaMaxima;
	}

	public void setPesoToleranciaMaxima(double pesoToleranciaMaxima) {
		this.pesoToleranciaMaxima = pesoToleranciaMaxima;
	}

	public int getMedidasToleranciaMaxima() {
		return medidasToleranciaMaxima;
	}

	public void setMedidasToleranciaMaxima(int medidasToleranciaMaxima) {
		this.medidasToleranciaMaxima = medidasToleranciaMaxima;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public void setAgenCodigo(String agenCodigo) {
		this.agenCodigo = agenCodigo;
	}

	public void setNroModulo(String nroModulo) {
		NroModulo = nroModulo;
	}

	public void setDeviceName(String deviceName) {
		DeviceName = deviceName;
	}

	public void setLimitePeso(double limitePeso) {
		this.limitePeso = limitePeso;
	}

	public void setLimiteMedidaAlto(int limiteMedidaAlto) {
		this.limiteMedidaAlto = limiteMedidaAlto;
	}

	public void setLimiteMedidaLargo(int limiteMedidaLargo) {
		this.limiteMedidaLargo = limiteMedidaLargo;
	}

	public void setLimiteMedidaAncho(int limiteMedidaAncho) {
		this.limiteMedidaAncho = limiteMedidaAncho;
	}

	public String getCodUsuario() {
		return codUsuario;
	}

	public void setCodUsuario(String codUsuario) {
		this.codUsuario = codUsuario;
	}

	public String getUbiCodigo() {
		return ubiCodigo;
	}

	public void setUbiCodigo(String ubiCodigo) {
		this.ubiCodigo = ubiCodigo;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static Logger getLogger() {
		return logger;
	}

	public String getProp() {
		return prop;
	}

	public String getIP() {
		return IP;
	}

	public String getAgenCodigo() {
		return agenCodigo;
	}

	public String getNroModulo() {
		return NroModulo;
	}

	public String getDeviceName() {
		return DeviceName;
	}

	public String getUsbDriver() {
		return usbDriver;
	}

	public int getTiempoCapturaPeso() {
		return TiempoCapturaPeso;
	}

	public double getLimitePeso() {
		return limitePeso;
	}

	public int getLimiteMedidaAlto() {
		return limiteMedidaAlto;
	}

	public int getLimiteMedidaLargo() {
		return limiteMedidaLargo;
	}

	public int getLimiteMedidaAncho() {
		return limiteMedidaAncho;
	}

	public double getToleranciaPesoSobre() {
		return pesoToleranciaMaxima;
	}

	public String getAppEnv() {
		return appEnv;
	}



	
	@PostConstruct
    public void initialize() {
        String rutaArchivoJson = "/home/ubuntu/Qclass/backEnd/datosQB.json";
        try (FileReader fileReader = new FileReader(rutaArchivoJson)) {
            Gson gson = new Gson();
            DatosQB[] datosQB = gson.fromJson(fileReader, DatosQB[].class);

            if (datosQB != null && datosQB.length>0) {
                cargarDatosDesdeJson(datosQB);
                this.datosQB = datosQB[0];
                logger.info("Datos cargados correctamente \n" + this.toString());

                // Establece la instancia de MyBean en StarkenServices
                StarkenServices.setMyBeanInstance(this);
                Telegram.setMyBeanInstance(this);
                CrearExcelAlertas.setMyBeanInstance(this);
                GpioModulo.setMyBeanInstance(this);
                
            } else {
                cargarDesdeConstructor();
                logger.info(this.toString());
            }
        } catch (IOException e) {
            logger.error("Error al leer el archivo JSON.", e);
            cargarDesdeConstructor();
            logger.info(this.toString());
        }
    }



	private void cargarDatosDesdeJson(DatosQB[] datosQB) {
	    this.IP = datosQB[0].getIp();
	    this.id = datosQB[0].getId();
	    this.agenCodigo = String.valueOf(datosQB[0].getAgencodigo());
	    this.NroModulo = String.valueOf(datosQB[0].getNumero_modulo());
	    this.DeviceName = datosQB[0].getDevice_name();
	    this.limitePeso = datosQB[0].getPeso_limite();
	    String[] medidasLimite = datosQB[0].getMedidas_limite().split("x");
	    this.limiteMedidaAlto = Integer.parseInt(medidasLimite[0]);
	    this.limiteMedidaLargo = Integer.parseInt(medidasLimite[1]);
	    this.limiteMedidaAncho = Integer.parseInt(medidasLimite[2]);
	    this.pesoToleranciaMaxima = datosQB[0].getPeso_tolerancia_maxima();
	    this.medidasToleranciaMaxima = datosQB[0].getMedidas_tolerancia_maxima();
	    this.codUsuario = String.valueOf(datosQB[0].getCodusuario());
	    this.ubiCodigo = String.valueOf(datosQB[0].getUbiccodigo());
	    this.pesoAccionarPatin = datosQB[0].getPeso_accionar_patin();
	}


	private void cargarDesdeConstructor() {
		// TODO Auto-generated method stub
		this.IP = "1.1.1.1";
		this.prop = "0";
		this.agenCodigo = "0";
		NroModulo = "0";
		DeviceName = "Error";
		this.usbDriver = "0";
		TiempoCapturaPeso = 0;
		this.limitePeso = 0;
		this.limiteMedidaAlto = 0;
		this.limiteMedidaLargo = 0;
		this.limiteMedidaAncho = 0;
		pesoToleranciaMaxima = 0;
		this.codUsuario = "null";
	    this.ubiCodigo = "null";
	    this.pesoAccionarPatin =0;
		//this.appEnv = "PRD";
		alertas.enviarAlertaDatosCaidos("Problemas al cargar los datos de la parametrizacion, se necesita reinicio del qb");
	
		
	}
	

	@Override
	public String toString() {
		return "MyBean [ prop=" + prop + ", IP=" + IP + ", agenCodigo=" + agenCodigo + ", NroModulo=" + NroModulo
				+ ", DeviceName=" + DeviceName + ", usbDriver=" + usbDriver + ", TiempoCapturaPeso=" + TiempoCapturaPeso
				+ ", limitePeso=" + limitePeso + ", limiteMedidaAlto=" + limiteMedidaAlto + ", limiteMedidaLargo="
				+ limiteMedidaLargo + ", limiteMedidaAncho=" + limiteMedidaAncho + ", ToleranciaPesoSobre="
				+ pesoToleranciaMaxima +"ToleranciaMedidasLimite "+ medidasToleranciaMaxima + ", appEnv=" + appEnv + " , ID " + id + " ]";
	}

	/*
	 * @Autowired public MyBean(@Value("${some.prop}") String prop
	 * ,@Value("${LogStarken.IP}") String IP ,@Value("${LogStarken.sucursal}")
	 * String sucursal ,@Value("${LogStarken.NroModulo}") String NroModulo
	 * ,@Value("${LogStarken.DeviceName}") String DeviceName
	 * ,@Value("${hardware.usbDriver}") String usbDriver
	 * 
	 * 
	 * 
	 * ,@Value("${hardware.usbDriver}") String TiempoCapturaPeso
	 * ,@Value("${hardware.usbDriver}") String limitePeso
	 * ,@Value("${hardware.usbDriver}") String limiteMedidaAlto
	 * ,@Value("${hardware.usbDriver}") String limiteMedidaLargo
	 * ,@Value("${hardware.usbDriver}") String limiteMedidaAncho
	 * ,@Value("${hardware.usbDriver}") String ToleranciaPesoSobre
	 * ,@Value("${hardware.usbDriver}") String appEnv ) { this.prop = prop; this.IP
	 * = IP; this.sucursal = sucursal; this.NroModulo = NroModulo; this.DeviceName =
	 * DeviceName; this.usbDriver = usbDriver; String v= "MyBean [prop=" + prop +
	 * ", IP=" + IP + ", sucursal=" + sucursal + ", NroModulo=" + NroModulo +
	 * ", DeviceName=" + DeviceName + ", usbDriver=" + usbDriver +"]";
	 * logger.info(v);
	 * 
	 * }
	 */

}
