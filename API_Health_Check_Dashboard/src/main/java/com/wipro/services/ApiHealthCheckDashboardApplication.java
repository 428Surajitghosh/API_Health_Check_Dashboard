package com.wipro.services;


import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import com.wipro.actions.GrafanaLogin;



@SpringBootApplication
public class ApiHealthCheckDashboardApplication {
	
	public static void main(String[] args) throws IOException {
		//For grafana login
		//GrafanaLogin GL=new GrafanaLogin();
		//GL.login();
		
		SpringApplication.run(ApiHealthCheckDashboardApplication.class, args);
		
		//for redirecting to API Dashboard home page
		//GL.Dashboard_Home();
	}

}
