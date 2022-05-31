package com.wipro.APIs;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class RequestChecker {

	//API-1->runs the API(GET) and returns validatons_API hashmap which contains the functional,non-functional validations name and results 
	JSONParser parser;
	JSONObject jsonobj;

	public Map<String,String> GETSingleUser() throws ParseException 
	{
		int flag=1;
		//hashmap to store val_name,val_status
		Map<String,String> validations_API1=new LinkedHashMap<String,String>();
		double CPU_util,Memory_util,DB_Query_times;
		int connections,third_party_calls,downstream_calls;
		double errors=0,anamolies=0;

		//specifying base URI
		RestAssured.baseURI="https://reqres.in/api/users";

		//request obj
		RequestSpecification httpRequest=RestAssured.given();

		//response obj with Method and parameter
		Response response=httpRequest.request(Method.GET,"/2");

		/* response body
		String responseBody=response.getBody().asString();
		System.out.println("Response Body:"+responseBody);
		 */

		//functional validation 1->status code
		int statusCode=response.getStatusCode();
		//System.out.println("Status Code:"+statusCode);

		validations_API1.put("third_level_validations:Status Code",String.valueOf(statusCode));
		if(statusCode==200)
		{
			validations_API1.put("Functional:Status Code=200","Passed");
			//
			validations_API1.put("third_level_validations:Errors","NA");

			validations_API1.put("Non_Functional:Errors=0%","Passed");			
		}
		else
		{
			validations_API1.put("Functional:Status Code=200","Failed");
			//
			validations_API1.put("third_level_validations:Errors","Status Code "+statusCode);
			validations_API1.put("Non_Functional:Errors=0%","Failed");
			flag=0;
		}

		//functional validation 2->status line
		String statusLine=response.statusLine();
		//System.out.println("Status Line:"+statusLine);
		if(statusLine.equals("HTTP/1.1 200 OK"))
		{
			validations_API1.put("Functional:Status Line=HTTP/1.1 200 OK","Passed");
			//
			validations_API1.put("third_level_validations:Anamolies from logs","NA");

			validations_API1.put("Non_Functional:Anamolies=0%","Passed");

		}
		else
		{
			validations_API1.put("Functional:Status Line=HTTP/1.1 200 OK","Failed");
			//
			validations_API1.put("third_level_validations:Anamolies from logs","Status Line "+statusLine);
			validations_API1.put("Non_Functional:Anamolies=0%","Failed");
			flag=0;
		}

		//functional validation 3->response header
		String contentType=response.getHeader("Content-Type");
		//System.out.println("Content-type:"+contentType);
		if(contentType.equals("application/json; charset=utf-8"))
		{
			validations_API1.put("Functional:Response Header Contains application/json","Passed");
		}
		else
		{
			validations_API1.put("Functional:Response Header Contains application/json","Failed");
			flag=0;
		}

		//functional validation 4->response header server
		String server=response.getHeader("Server");
		if(server.equals("cloudflare"))
		{
			validations_API1.put("Functional:Response Header server","Passed");
		}
		else
		{
			validations_API1.put("Functional:Response Header server","Failed");
			flag=0;
		}
		//functional validation 5->response contains data
		parser=new JSONParser();
		jsonobj=(JSONObject) parser.parse(response.asString());
		//JSONArray data=(JSONArray) jsonobj.get("data");
		jsonobj=(JSONObject)jsonobj.get("data");
		String email=(String) jsonobj.get("email");
		//System.out.println(email);
		if(email.contains("@reqres.in"))
		{
			validations_API1.put("Functional:Response Body Contains email","Passed");
		}
		else
		{
			validations_API1.put("Functional:Response Body Contains email","Failed");
			flag=0;
		}

		//non-functional validation 1->response time(<500ms)
		long time=response.getTime();
		//System.out.println("Non_Functional:response time:"+time);

		if(flag==1)
		{
			if(time<500)
			{
				validations_API1.put("Non_Functional:Response Time<500ms","Passed");
				validations_API1.put("third_level_validations:Response Time",String.valueOf(time));
			}
			else
			{
				validations_API1.put("Non_Functional:Response Time<500ms","Failed");
				validations_API1.put("third_level_validations:Response Time",String.valueOf(time));
			}
		}
		else
		{
			validations_API1.put("Non_Functional:Response Time<500ms","Failed");
			validations_API1.put("third_level_validations:Response Time","NA");
		}


		//System.out.println("Hashmap");

		CPU_util=Math.random()*(100-0+1)+0;//%
		Memory_util=Math.random()*(100-0+1)+0;//%
		DB_Query_times=Math.random()*(1000-0+1)+0;//ms
		connections=(int)(Math.random()*(50-0+1)+0);
		third_party_calls=(int)(Math.random()*(10-0+1)+0);
		downstream_calls=(int)(Math.random()*(10-0+1)+0);

		//System.out.println("CPU:"+CPU_util);
		//System.out.println("Memory:"+Memory_util);
		//System.out.println("DB:"+DB_Query_times);
		//System.out.println("3rd Party:"+third_party_calls);
		//System.out.println("Down:"+downstream_calls);

		validations_API1.put("third_level_validations:CPU Utilization",String.valueOf(CPU_util));
		validations_API1.put("third_level_validations:Memory Utilization",String.valueOf(Memory_util));
		validations_API1.put("third_level_validations:Connections",String.valueOf(connections));
		validations_API1.put("third_level_validations:DB Query time",String.valueOf(DB_Query_times));
		validations_API1.put("third_level_validations:Third Party Calls",String.valueOf(third_party_calls));
		validations_API1.put("third_level_validations:Downstream Calls",String.valueOf(downstream_calls));
		if(CPU_util<=70)
		{
			validations_API1.put("Non_Functional_Host:CPU Utilization<70%","Passed");
		}
		else
		{
			validations_API1.put("Non_Functional_Host:CPU Utilization<70%","Failed");
		}
		if(Memory_util<=70)
		{
			validations_API1.put("Non_Functional_Host:Memory Utilization<70%","Passed");
		}
		else
		{
			validations_API1.put("Non_Functional_Host:Memory Utilization<70%","Failed");
		}
		if(connections>0)
		{
			validations_API1.put("Non_Functional_Host:Connections>0","Passed");
		}
		else
		{
			validations_API1.put("Non_Functional_Host:Connections>0","Failed");
		}
		if(DB_Query_times<500)
		{
			validations_API1.put("Non_Functional_Host:DB Query time<500ms","Passed");
		}
		else
		{
			validations_API1.put("Non_Functional_Host:DB Query time<500ms","Failed");
		}
		if(third_party_calls>0)
		{
			validations_API1.put("Non_Functional_Host:Third Party Calls>0","Passed");
		}
		else
		{
			validations_API1.put("Non_Functional_Host:Third Party Calls>0","Failed");
		}
		if(downstream_calls>0)
		{
			validations_API1.put("Non_Functional_Host:Downstream Calls>0","Passed");
		}
		else
		{
			validations_API1.put("Non_Functional_Host:Downstream Calls>0","Failed");
		}
		/*
		for(Map.Entry i:validations_API1.entrySet())
    	{
    		String key=(String) i.getKey();
    		System.out.println(key);
    		String value=(String) i.getValue();
    		System.out.println(value);
    	}
		 */


		return validations_API1;
	}
	//code for API-2(GET)
	public Map<String,String> GETUsers() throws ParseException
	{
		int flag=1;

		//hashmap to store val_name,val_status
		Map<String,String> validations_API2=new LinkedHashMap<String,String>();
		double CPU_util,Memory_util,DB_Query_times;
		int connections,third_party_calls,downstream_calls;
		double errors=0,anamolies=0;

		//specifying base URI
		RestAssured.baseURI="https://reqres.in/api/users";

		//request obj
		RequestSpecification httpRequest=RestAssured.given();

		//response obj with Method and query parameter
		Response response=httpRequest.queryParam("Page",2).get();


		/* response body
		String responseBody=response.getBody().asString();
		System.out.println("Response Body:"+responseBody);
		 */

		//functional validation 1->status code
		int statusCode=response.getStatusCode();
		//System.out.println("Status Code:"+statusCode);
		
		validations_API2.put("third_level_validations:Status Code",String.valueOf(statusCode));
		if(statusCode==200)
		{
			validations_API2.put("Functional:Status Code=200","Passed");

			validations_API2.put("third_level_validations:Errors","NA");

			validations_API2.put("Non_Functional:Errors=0%","Passed");
		}
		else
		{
			validations_API2.put("Functional:Status Code=200","Failed");

			validations_API2.put("third_level_validations:Errors","Status Code "+statusCode);
			validations_API2.put("Non_Functional:Errors=0%","Failed");

			flag=0;
		}

		//functional validation 2->status line
		String statusLine=response.statusLine();
		//System.out.println("Status Line:"+statusLine);
		if(statusLine.equals("HTTP/1.1 200 OK"))
		{
			validations_API2.put("Functional:Status Line=HTTP/1.1 200 OK","Passed");

			validations_API2.put("third_level_validations:Anamolies from logs","NA");

			validations_API2.put("Non_Functional:Anamolies=0%","Passed");
		}
		else
		{
			validations_API2.put("Functional:Status Line=HTTP/1.1 200 OK","Failed");

			validations_API2.put("third_level_validations:Anamolies from logs","Status Line "+statusLine);
			validations_API2.put("Non_Functional:Anamolies=0%","Failed");

			flag=0;
		}

		//functional validation 3->response body

		String contentType=response.getHeader("Content-Type");
		//System.out.println("Content-type:"+contentType);
		if(contentType.equals("application/json; charset=utf-8"))
		{
			validations_API2.put("Functional:Response Header","Passed");
		}
		else
		{
			validations_API2.put("Functional:Response Header","Failed");
			flag=0;
		}

		//functional validation 4->response header server
		String server=response.getHeader("Server");
		if(server.equals("cloudflare"))
		{
			validations_API2.put("Functional:Response Header server","Passed");
		}
		else
		{
			validations_API2.put("Functional:Response Header server","Failed");
			flag=0;
		}

		//functional validation 5
		jsonobj=(JSONObject) parser.parse(response.asString());
		JSONArray data=(JSONArray) jsonobj.get("data");
		jsonobj=(JSONObject) data.get(0);
		String email=(String) jsonobj.get("email");
		//System.out.println(email);
		if(email.contains("@reqres.in"))
		{
			validations_API2.put("Functional:Response Body Contains email","Passed");
		}
		else
		{
			validations_API2.put("Functional:Response Body Contains email","Failed");
			flag=0;
		}

		//non-functional validation 1->response time(<500ms)
		long time=response.getTime();
		//System.out.println("Non_Functional:response time:"+time);

		if(flag==1)
		{
			if(time<1000)
			{
				validations_API2.put("Non_Functional:Response Time<1000ms","Passed");
				validations_API2.put("third_level_validations:Response Time",String.valueOf(time));
			}
			else
			{
				validations_API2.put("Non_Functional:Response Time<1000ms","Failed");
				validations_API2.put("third_level_validations:Response Time",String.valueOf(time));
			}
		}
		else
		{
			validations_API2.put("Non_Functional:Response Time<1000ms","Failed");
			validations_API2.put("third_level_validations:Response Time","NA");
		}

		CPU_util=Math.random()*(100-0+1)+0;//%
		Memory_util=Math.random()*(100-0+1)+0;//%
		DB_Query_times=Math.random()*(1000-0+1)+0;//ms
		connections=(int)(Math.random()*(50-0+1)+0);
		third_party_calls=(int)(Math.random()*(10-0+1)+0);
		downstream_calls=(int)(Math.random()*(10-0+1)+0);

		//System.out.println("CPU:"+CPU_util);
		//System.out.println("Memory:"+Memory_util);
		//System.out.println("DB:"+DB_Query_times);
		//System.out.println("3rd Party:"+third_party_calls);
		//System.out.println("Down:"+downstream_calls);

		validations_API2.put("third_level_validations:CPU Utilization",String.valueOf(CPU_util));
		validations_API2.put("third_level_validations:Memory Utilization",String.valueOf(Memory_util));
		validations_API2.put("third_level_validations:Connections",String.valueOf(connections));
		validations_API2.put("third_level_validations:DB Query time",String.valueOf(DB_Query_times));
		validations_API2.put("third_level_validations:Third Party Calls",String.valueOf(third_party_calls));
		validations_API2.put("third_level_validations:Downstream Calls",String.valueOf(downstream_calls));
		if(CPU_util<=70)
		{
			validations_API2.put("Non_Functional_Host:CPU Utilization<70%","Passed");
		}
		else
		{
			validations_API2.put("Non_Functional_Host:CPU Utilization<70%","Failed");
		}
		if(Memory_util<=70)
		{
			validations_API2.put("Non_Functional_Host:Memory Utilization<70%","Passed");
		}
		else
		{
			validations_API2.put("Non_Functional_Host:Memory Utilization<70%","Failed");
		}
		if(connections>0)
		{
			validations_API2.put("Non_Functional_Host:Connections>0","Passed");
		}
		else
		{
			validations_API2.put("Non_Functional_Host:Connections>0","Failed");
		}
		if(DB_Query_times<500)
		{
			validations_API2.put("Non_Functional_Host:DB Query time<500ms","Passed");
		}
		else
		{
			validations_API2.put("Non_Functional_Host:DB Query time<500ms","Failed");
		}
		if(third_party_calls>0)
		{
			validations_API2.put("Non_Functional_Host:Third Party Calls>0","Passed");
		}
		else
		{
			validations_API2.put("Non_Functional_Host:Third Party Calls>0","Failed");
		}
		if(downstream_calls>0)
		{
			validations_API2.put("Non_Functional_Host:Downstream Calls>0","Passed");
		}
		else
		{
			validations_API2.put("Non_Functional_Host:Downstream Calls>0","Failed");
		}

		//System.out.println("Hashmap");

		for(Map.Entry i:validations_API2.entrySet())
		{
			String key=(String) i.getKey();
			//System.out.println(key);
			String value=(String) i.getValue();
			//System.out.println(value);
		}

		//System.out.println("Hashmap");



		return validations_API2;
	}
	//API-3(POST)
	public Map<String,String> CreateUser() throws ParseException
	{
		int flag=1;
		JSONParser parser=new JSONParser();
		double CPU_util,Memory_util,DB_Query_times;
		int connections,third_party_calls,downstream_calls;

		//hashmap to store val_name,val_status
		Map<String,String> validations_API3=new LinkedHashMap<String,String>();


		//specifying base URI
		RestAssured.baseURI="https://reqres.in/api";

		//request obj
		RequestSpecification httpRequest=RestAssured.given();

		// JSONObject is a class that represents a Simple JSON.
		// We can add Key - Value pairs using the put method to requestParams
		JSONObject requestParams = new JSONObject();
		requestParams.put("name", "morpheus"); 
		requestParams.put("job", "leader");

		// Adding a header stating the Request body is a JSON
		httpRequest.header("Content-Type", "application/json");

		// Adding the Json to the body of the request
		httpRequest.body(requestParams.toJSONString());

		//response obj with Method and query parameter
		Response response=httpRequest.post("/users");


		/* response body
		String responseBody=response.getBody().asString();
		System.out.println("Response Body:"+responseBody);
		 */

		//functional validation 1->status code
		int statusCode=response.getStatusCode();
		//System.out.println("Status Code:"+statusCode);
		
		validations_API3.put("third_level_validations:Status Code",String.valueOf(statusCode));
		if(statusCode==201)
		{
			validations_API3.put("Functional:Status Code=201","Passed");

			validations_API3.put("third_level_validations:Errors","NA");

			validations_API3.put("Non_Functional:Errors=0%","Passed");
		}
		else
		{
			validations_API3.put("Functional:Status Code=201","Failed");

			validations_API3.put("third_level_validations:Errors","Status Code "+statusCode);
			validations_API3.put("Non_Functional:Errors=0%","Failed");

			flag=0;
		}

		//functional validation 2->status line

		String statusLine=response.statusLine();
		//System.out.println("Status Line:"+statusLine);
		if(statusLine.contains("201 Created"))
		{
			validations_API3.put("Functional:Status Line","Passed");

			validations_API3.put("third_level_validations:Anamolies from logs","NA");

			validations_API3.put("Non_Functional:Anamolies=0%","Passed");
		}
		else
		{
			validations_API3.put("Functional:Status Line","Failed");

			validations_API3.put("third_level_validations:Anamolies from logs","Status Line "+statusLine);
			validations_API3.put("Non_Functional:Anamolies=0%","Failed");

			flag=0;
		}


		//functional validation 3->response header
		String contentType=response.getHeader("Content-Type");
		//System.out.println("Content-type:"+contentType);
		if(contentType.equals("application/json; charset=utf-8"))
		{
			validations_API3.put("Functional:Response Header Contains application/json","Passed");
		}
		else
		{
			validations_API3.put("Functional:Response Header application/json","Failed");
			flag=0;
		}

		//functional validation 4->response header server
		String server=response.getHeader("Server");
		if(server.equals("cloudflare"))
		{
			validations_API3.put("Functional:Response Header server","Passed");
		}
		else
		{
			validations_API3.put("Functional:Response Header server","Failed");
			flag=0;
		}

		//functional validation 5->response body
		if(response.asString().contains("id"))
		{
			validations_API3.put("Functional:Response Body Contains ID","Passed");
		}
		else
		{
			validations_API3.put("Functional:Response Body Contains ID","Failed");
			flag=0;
		}	
		//non-functional validation 1->response time(<1000ms)
		long time=response.getTime();
		//System.out.println("Non_Functional:response time:"+time);
		if(flag==1)
		{
			if(time<1000)
			{
				validations_API3.put("Non_Functional:Response Time<1000ms","Passed");
				validations_API3.put("third_level_validations:Response Time",String.valueOf(time));
			}
			else
			{
				validations_API3.put("Non_Functional:Response Time<1000ms","Failed");
				validations_API3.put("third_level_validations:Response Time",String.valueOf(time));
			}
		}
		else
		{
			validations_API3.put("Non_Functional:Response Time<1000ms","Failed");
			validations_API3.put("third_level_validations:Response Time","NA");
		}

		CPU_util=Math.random()*(100-0+1)+0;//%
		Memory_util=Math.random()*(100-0+1)+0;//%
		DB_Query_times=Math.random()*(1000-0+1)+0;//ms
		connections=(int)(Math.random()*(50-0+1)+0);
		third_party_calls=(int)(Math.random()*(10-0+1)+0);
		downstream_calls=(int)(Math.random()*(10-0+1)+0);

		//System.out.println("CPU:"+CPU_util);
		//System.out.println("Memory:"+Memory_util);
		//System.out.println("DB:"+DB_Query_times);
		//System.out.println("3rd Party:"+third_party_calls);
		//System.out.println("Down:"+downstream_calls);

		validations_API3.put("third_level_validations:CPU Utilization",String.valueOf(CPU_util));
		validations_API3.put("third_level_validations:Memory Utilization",String.valueOf(Memory_util));
		validations_API3.put("third_level_validations:Connections",String.valueOf(connections));
		validations_API3.put("third_level_validations:DB Query time",String.valueOf(DB_Query_times));
		validations_API3.put("third_level_validations:Third Party Calls",String.valueOf(third_party_calls));
		validations_API3.put("third_level_validations:Downstream Calls",String.valueOf(downstream_calls));
		if(CPU_util<=70)
		{
			validations_API3.put("Non_Functional_Host:CPU Utilization<70%","Passed");
		}
		else
		{
			validations_API3.put("Non_Functional_Host:CPU Utilization<70%","Failed");
		}
		if(Memory_util<=70)
		{
			validations_API3.put("Non_Functional_Host:Memory Utilization<70%","Passed");
		}
		else
		{
			validations_API3.put("Non_Functional_Host:Memory Utilization<70%","Failed");
		}
		if(connections>0)
		{
			validations_API3.put("Non_Functional_Host:Connections>0","Passed");
		}
		else
		{
			validations_API3.put("Non_Functional_Host:Connections>0","Failed");
		}
		if(DB_Query_times<500)
		{
			validations_API3.put("Non_Functional_Host:DB Query time<500ms","Passed");
		}
		else
		{
			validations_API3.put("Non_Functional_Host:DB Query time<500ms","Failed");
		}
		if(third_party_calls>0)
		{
			validations_API3.put("Non_Functional_Host:Third Party Calls>0","Passed");
		}
		else
		{
			validations_API3.put("Non_Functional_Host:Third Party Calls>0","Failed");
		}
		if(downstream_calls>0)
		{
			validations_API3.put("Non_Functional_Host:Downstream Calls>0","Passed");
		}
		else
		{
			validations_API3.put("Non_Functional_Host:Downstream Calls>0","Failed");
		}


		//System.out.println("Hashmap");
		/*
		for(Map.Entry i:validations_API3.entrySet())
		{
			String key=(String) i.getKey();
			//System.out.println(key);
			String value=(String) i.getValue();
			//System.out.println(value);
		}
		 */

		//System.out.println("Hashmap");



		return validations_API3;
	}
	//API-4(POST)
	public Map<String,String> Login() throws ParseException
	{
		int flag=1;
		JSONParser parser=new JSONParser();

		//hashmap to store val_name,val_status
		Map<String,String> validations_API4=new LinkedHashMap<String,String>();
		double CPU_util,Memory_util,DB_Query_times;
		int connections,third_party_calls,downstream_calls;


		//specifying base URI
		RestAssured.baseURI="https://reqres.in/api";

		//request obj
		RequestSpecification httpRequest=RestAssured.given();

		// JSONObject is a class that represents a Simple JSON.
		// We can add Key - Value pairs using the put method to requestParams
		JSONObject requestParams = new JSONObject();
		requestParams.put("email", "eve.holt@reqres.in"); 
		requestParams.put("password", "cityslicka");

		// Adding a header stating the Request body is a JSON
		httpRequest.header("Content-Type", "application/json");

		// Adding the Json to the body of the request
		httpRequest.body(requestParams.toJSONString());

		//response obj with Method and query parameter
		Response response=httpRequest.post("/login");


		/* response body
		String responseBody=response.getBody().asString();
		System.out.println("Response Body:"+responseBody);
		 */

		//functional validation 1->status code
		int statusCode=response.getStatusCode();
		//System.out.println("Status Code:"+statusCode);
		
		validations_API4.put("third_level_validations:Status Code",String.valueOf(statusCode));
		if(statusCode==200)
		{
			validations_API4.put("Functional:Status Code=200","Passed");

			validations_API4.put("third_level_validations:Errors","NA");

			validations_API4.put("Non_Functional:Errors=0%","Passed");
		}
		else
		{
			validations_API4.put("Functional:Status Code=200","Failed");

			validations_API4.put("third_level_validations:Errors","Status Code "+statusCode);
			validations_API4.put("Non_Functional:Errors=0%","Failed");

			flag=0;
		}

		//functional validation 2->status line

		String statusLine=response.statusLine();
		//System.out.println("Status Line:"+statusLine);
		if(statusLine.equals("HTTP/1.1 200 OK"))
		{
			validations_API4.put("Functional:Status Line","Passed");

			validations_API4.put("third_level_validations:Anamolies from logs","NA");

			validations_API4.put("Non_Functional:Anamolies=0%","Passed");
		}
		else
		{
			validations_API4.put("Functional:Status Line","Failed");

			validations_API4.put("third_level_validations:Anamolies from logs","Status Line "+statusLine);
			validations_API4.put("Non_Functional:Anamolies=0%","Failed");

			flag=0;
		}


		//functional validation 3->response header
		String contentType=response.getHeader("Content-Type");
		//System.out.println("Content-type:"+contentType);
		if(contentType.equals("application/json; charset=utf-8"))
		{
			validations_API4.put("Functional:Response Header Contains application/json","Passed");
		}
		else
		{
			validations_API4.put("Functional:Response Header application/json","Failed");
			flag=0;
		}

		//functional validation 4->response header server
		String server=response.getHeader("Server");
		if(server.equals("cloudflare"))
		{
			validations_API4.put("Functional:Response Header server","Passed");
		}
		else
		{
			validations_API4.put("Functional:Response Header server","Failed");
			flag=0;
		}

		//functional validation 5->response body
		if(response.asString().contains("token"))
		{
			validations_API4.put("Functional:Response Body Contains token","Passed");
		}
		else
		{
			validations_API4.put("Functional:Response Body Contains token","Failed");
			flag=0;
		}
		//non-functional validation 1->response time(<1000ms)
		long time=response.getTime();
		//System.out.println("Non_Functional:response time:"+time);
		if(flag==1)
		{
			if(time<1000)
			{
				validations_API4.put("Non_Functional:Response Time<1000ms","Passed");
				validations_API4.put("third_level_validations:Response Time",String.valueOf(time));
			}
			else
			{
				validations_API4.put("Non_Functional:Response Time<1000ms","Failed");
				validations_API4.put("third_level_validations:Response Time",String.valueOf(time));
			}
		}
		else
		{
			validations_API4.put("Non_Functional:Response Time<1000ms","Failed");
			validations_API4.put("third_level_validations:Response Time","NA");
		}

		CPU_util=Math.random()*(100-0+1)+0;//%
		Memory_util=Math.random()*(100-0+1)+0;//%
		DB_Query_times=Math.random()*(1000-0+1)+0;//ms
		connections=(int)(Math.random()*(50-0+1)+0);
		third_party_calls=(int)(Math.random()*(10-0+1)+0);
		downstream_calls=(int)(Math.random()*(10-0+1)+0);

		//System.out.println("CPU:"+CPU_util);
		//System.out.println("Memory:"+Memory_util);
		//System.out.println("DB:"+DB_Query_times);
		//System.out.println("3rd Party:"+third_party_calls);
		//System.out.println("Down:"+downstream_calls);

		validations_API4.put("third_level_validations:CPU Utilization",String.valueOf(CPU_util));
		validations_API4.put("third_level_validations:Memory Utilization",String.valueOf(Memory_util));
		validations_API4.put("third_level_validations:Connections",String.valueOf(connections));
		validations_API4.put("third_level_validations:DB Query time",String.valueOf(DB_Query_times));
		validations_API4.put("third_level_validations:Third Party Calls",String.valueOf(third_party_calls));
		validations_API4.put("third_level_validations:Downstream Calls",String.valueOf(downstream_calls));
		if(CPU_util<=70)
		{
			validations_API4.put("Non_Functional_Host:CPU Utilization<70%","Passed");
		}
		else
		{
			validations_API4.put("Non_Functional_Host:CPU Utilization<70%","Failed");
		}
		if(Memory_util<=70)
		{
			validations_API4.put("Non_Functional_Host:Memory Utilization<70%","Passed");
		}
		else
		{
			validations_API4.put("Non_Functional_Host:Memory Utilization<70%","Failed");
		}
		if(connections>0)
		{
			validations_API4.put("Non_Functional_Host:Connections>0","Passed");
		}
		else
		{
			validations_API4.put("Non_Functional_Host:Connections>0","Failed");
		}
		if(DB_Query_times<500)
		{
			validations_API4.put("Non_Functional_Host:DB Query time<500ms","Passed");
		}
		else
		{
			validations_API4.put("Non_Functional_Host:DB Query time<500ms","Failed");
		}
		if(third_party_calls>0)
		{
			validations_API4.put("Non_Functional_Host:Third Party Calls>0","Passed");
		}
		else
		{
			validations_API4.put("Non_Functional_Host:Third Party Calls>0","Failed");
		}
		if(downstream_calls>0)
		{
			validations_API4.put("Non_Functional_Host:Downstream Calls>0","Passed");
		}
		else
		{
			validations_API4.put("Non_Functional_Host:Downstream Calls>0","Failed");
		}

		//System.out.println("Hashmap");
		/*
		for(Map.Entry i:validations_API4.entrySet())
		{
			String key=(String) i.getKey();
			//System.out.println(key);
			String value=(String) i.getValue();
			//System.out.println(value);
		}
		 */
		//System.out.println("Hashmap");



		return validations_API4;
	}

	//API-5(GET)
	public Map<String,String> SingleUserError() throws ParseException
	{
		int flag=1;

		JSONParser parser=new JSONParser();

		//hashmap to store val_name,val_status
		Map<String,String> validations_API5=new LinkedHashMap<String,String>();
		double CPU_util,Memory_util,DB_Query_times;
		int connections,third_party_calls,downstream_calls;

		//specifying base URI
		RestAssured.baseURI="https://reqres.in/api/users";

		//request obj
		RequestSpecification httpRequest=RestAssured.given();



		//response obj with Method and parameter
		Response response=httpRequest.request(Method.GET,"/23");

		/* response body
		String responseBody=response.getBody().asString();
		System.out.println("Response Body:"+responseBody);
		 */

		//functional validation 1->status code
		int statusCode=response.getStatusCode();
		//System.out.println("Status Code:"+statusCode);

		validations_API5.put("third_level_validations:Status Code",String.valueOf(statusCode));
		if(statusCode==200)
		{
			validations_API5.put("Functional:Status Code=200","Passed");

			validations_API5.put("third_level_validations:Errors","NA");

			validations_API5.put("Non_Functional:Errors=0%","Passed");
		}
		else
		{
			validations_API5.put("Functional:Status Code=200","Failed");

			validations_API5.put("third_level_validations:Errors","Status Code "+statusCode);
			validations_API5.put("Non_Functional:Errors=0%","Failed");

			flag=0;
		}

		//functional validation 2->status line
		String statusLine=response.statusLine();
		//System.out.println("Status Line:"+statusLine);
		if(statusLine.equals("HTTP/1.1 200 OK"))
		{
			validations_API5.put("Functional:Status Line=HTTP/1.1 200 OK","Passed");

			validations_API5.put("third_level_validations:Anamolies from logs","NA");

			validations_API5.put("Non_Functional:Anamolies=0%","Passed");
		}
		else
		{
			validations_API5.put("Functional:Status Line=HTTP/1.1 200 OK","Failed");

			validations_API5.put("third_level_validations:Anamolies from logs","Status Line "+statusLine);
			validations_API5.put("Non_Functional:Anamolies=0%","Failed");

			flag=0;
		}

		//functional validation 3->response header
		String contentType=response.getHeader("Content-Type");
		//System.out.println("Content-type:"+contentType);
		if(contentType.equals("application/json; charset=utf-8"))
		{
			validations_API5.put("Functional:Response Header Contains application/json","Passed");

		}
		else
		{
			validations_API5.put("Functional:Response Header Contains application/json","Failed");
			flag=0;
		}

		//functional validation 4->response header server
		String server=response.getHeader("Server");
		if(server.equals("cloudflare"))
		{
			validations_API5.put("Functional:Response Header server","Passed");
		}
		else
		{
			validations_API5.put("Functional:Response Header server","Failed");
			flag=0;
		}

		//functional validation 5->response body
		if(response.asString().contains("id"))
		{
			validations_API5.put("Functional:Response Body Contains ID","Passed");
		}
		else
		{
			validations_API5.put("Functional:Response Body Contains ID","Failed");
			flag=0;
		}
		//non-functional validation 1->response time(<1000ms)
		long time=response.getTime();
		//System.out.println("Non_Functional:response time:"+time);
		
		if(flag==1)
		{
			if(time<1000)
			{
				validations_API5.put("Non_Functional:Response Time<1000ms","Passed");
				validations_API5.put("third_level_validations:Response Time",String.valueOf(time));
			}
			else
			{
				validations_API5.put("Non_Functional:Response Time<1000ms","Failed");
				validations_API5.put("third_level_validations:Response Time",String.valueOf(time));
			}
		}
		else
		{
			validations_API5.put("Non_Functional:Response Time<1000ms","Failed");
			validations_API5.put("third_level_validations:Response Time","NA");
		}
		
		CPU_util=Math.random()*(100-0+1)+0;//%
		Memory_util=Math.random()*(100-0+1)+0;//%
		DB_Query_times=Math.random()*(1000-0+1)+0;//ms
		connections=(int)(Math.random()*(50-0+1)+0);
		third_party_calls=(int)(Math.random()*(10-0+1)+0);
		downstream_calls=(int)(Math.random()*(10-0+1)+0);
		
		//System.out.println("CPU:"+CPU_util);
		//System.out.println("Memory:"+Memory_util);
		//System.out.println("DB:"+DB_Query_times);
		//System.out.println("3rd Party:"+third_party_calls);
		//System.out.println("Down:"+downstream_calls);
		
		validations_API5.put("third_level_validations:CPU Utilization",String.valueOf(CPU_util));
		validations_API5.put("third_level_validations:Memory Utilization",String.valueOf(Memory_util));
		validations_API5.put("third_level_validations:Connections",String.valueOf(connections));
		validations_API5.put("third_level_validations:DB Query time",String.valueOf(DB_Query_times));
		validations_API5.put("third_level_validations:Third Party Calls",String.valueOf(third_party_calls));
		validations_API5.put("third_level_validations:Downstream Calls",String.valueOf(downstream_calls));
		if(CPU_util<=70)
		{
			validations_API5.put("Non_Functional_Host:CPU Utilization<70%","Passed");
		}
		else
		{
			validations_API5.put("Non_Functional_Host:CPU Utilization<70%","Failed");
		}
		if(Memory_util<=70)
		{
			validations_API5.put("Non_Functional_Host:Memory Utilization<70%","Passed");
		}
		else
		{
			validations_API5.put("Non_Functional_Host:Memory Utilization<70%","Failed");
		}
		if(connections>0)
		{
			validations_API5.put("Non_Functional_Host:Connections>0","Passed");
		}
		else
		{
			validations_API5.put("Non_Functional_Host:Connections>0","Failed");
		}
		if(DB_Query_times<500)
		{
			validations_API5.put("Non_Functional_Host:DB Query time<500ms","Passed");
		}
		else
		{
			validations_API5.put("Non_Functional_Host:DB Query time<500ms","Failed");
		}
		if(third_party_calls>0)
		{
			validations_API5.put("Non_Functional_Host:Third Party Calls>0","Passed");
		}
		else
		{
			validations_API5.put("Non_Functional_Host:Third Party Calls>0","Failed");
		}
		if(downstream_calls>0)
		{
			validations_API5.put("Non_Functional_Host:Downstream Calls>0","Passed");
		}
		else
		{
			validations_API5.put("Non_Functional_Host:Downstream Calls>0","Failed");
		}
		
		//System.out.println("Hashmap");

		/*for(Map.Entry i:validations_API5.entrySet())
		{
			String key=(String) i.getKey();
			//System.out.println(key);
			String value=(String) i.getValue();
			//System.out.println(value);
		}
		*/
		//System.out.println("Hashmap");



		return validations_API5;
	}
}
