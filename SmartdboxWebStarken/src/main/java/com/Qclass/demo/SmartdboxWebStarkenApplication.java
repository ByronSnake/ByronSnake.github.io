package com.Qclass.demo;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.Qclass.demo.classes.*;

import com.Qclass.demo.classes.LogStarken;

@SpringBootApplication(scanBasePackages = "com.Qclass.demo")
@EnableScheduling
//@EnableConfigurationProperties(LogStarken.class)
public class SmartdboxWebStarkenApplication {
	// para buildear el jar usar este link
	// https://stackoverflow.com/questions/62650732/build-jar-and-run-spring-boot-from-cmd

	//@Autowired
	//private MyBean myBean;
	
	@Autowired
	private DatosResponse datos;

	final static Logger logger = LoggerFactory.getLogger(SmartdboxWebStarkenApplication.class);

	public static void main(String[] args) {
		/*
		 * AnnotationConfigApplicationContext context = new
		 * AnnotationConfigApplicationContext(); context.scan("com.Qclass.demo");
		 * context.refresh(); System.out.println("Refreshing the spring context");
		 * DBConnection dbConnection = context.getBean(DBConnection.class);
		 * 
		 * dbConnection.printDBConfigs(); // close the spring context context.close();
		 * 
		 */

		SpringApplication.run(SmartdboxWebStarkenApplication.class, args);

	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://100.100.183.89:3000", "http://localhost", "http://localhost:3000")
						.allowedMethods("*").allowCredentials(true)
				// .maxAge(3600)
				;
			}
		};
	}

	   @Bean
	    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
	        return (factory) -> {
	            if (datos != null && datos.getPayload() != null && datos.getPayload().length > 0) {
	                // Accede a los datos de payload
	                int puerto = datos.getPayload()[0].getPuerto();
	                factory.setPort(puerto);
	                logger.info("Puerto configurado desde datosQB: " + puerto);
	                
	            } else {
	                factory.setPort(8081); // Valor predeterminado si no hay datos en payload
	                logger.info("Puerto configurado por defecto: 8081");
	            }
	        };
	    }
	   
	   @Bean
	    public ConcurrentMapCacheManager cacheManager() {
	        return new ConcurrentMapCacheManager("micache"); // Nombre de la caché
	    }
}
