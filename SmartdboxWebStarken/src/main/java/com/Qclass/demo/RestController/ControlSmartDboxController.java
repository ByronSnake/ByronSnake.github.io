package com.Qclass.demo.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Qclass.demo.classes.ServiceResponse;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/API/ControlSmartDbox")
public class ControlSmartDboxController {

final static Logger logger =  LoggerFactory.getLogger(ControlSmartDboxController.class);
	
	@Autowired
    public HttpSession httpSession;
	
	
	@GetMapping("/GetALL")
	ServiceResponse all() {
		
		
		
		
		int number = 20;
		Thread newThread = new Thread(() -> {
		    System.out.println("Factorial of " + number + " is: " + factorial(number));
		});
		newThread.start();
		
		
		/*
		int number = 0;
		
		ExecutorService threadpool = Executors.newCachedThreadPool();
		Future<Long> futureTask = (Future<Long>) threadpool.submit(() -> factorial(number));
		
		//while (!futureTask.isDone()) {
		//    System.out.println("FutureTask is not finished yet..."); 
		//} 
		
		long result = 0;
		
		
		try {
			result = futureTask.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		threadpool.shutdown();
		
		*/
		return new ServiceResponse(number, "asd");
	}
	
	private long factorial(int number) {
		// TODO Auto-generated method stub
		
		
		//httpSession.setAttribute("BULTOS", bultos);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("termino factorial");
		return 1;
	}
	
}
