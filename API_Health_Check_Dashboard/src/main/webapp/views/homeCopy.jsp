<% response.addHeader("Refresh","120"); %>
<%@page import="com.wipro.APIs.*"%>
<%@page import="InfluxWrite.*"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Health Checks</title>
   
</head>
<body>
	<h1 style="margin-left:30%">Business and Environment Health Checks</h1>
	<label for="frequency">Frequency of Health Check(Minutes):</label>
	<select name="frequency" id="frequency">
    	<option value="2">2</option>
    	<option value="5">5</option>
    	<option value="10" style="display:block">10</option>
	</select>
	<br>
	<label for="iterations">Iterations in Run:</label>
	<select name="iterations" id="iterations">
    	<option value="5">5</option>
    	<option value="10">10</option>
    	<option value="15">15</option>
	</select>
	<h2>Business Process 1</h2>
	
	<% 
		//this func runs the APIs 5 iterations using RequestChecker class and returns results of all functional 
		//and non-functional validations
		ReqCaller caller=new ReqCaller();
		///ArrayList<Integer> list1=caller.callAPI1();
		ArrayList<double[]> list1=caller.callAPI1();
		double counter1=0,counter2=0,counter3=0;
		double CPU_util;
		double Memory_util;
		int connections;
		int iterations=5;
		String status=null;		
		for(int i=0;i<list1.size();i++)
		{
			counter1=counter1+list1.get(i)[0];
			counter2=counter2+list1.get(i)[1];
			counter3=counter3+list1.get(i)[2];
		}
		//System.out.println(count);
		counter1=counter1/iterations;
		counter2=counter2/iterations;
		counter3=counter3/iterations;
		System.out.println(counter1);
		System.out.println(counter2);	
		System.out.println(counter3);
		
		//getting CPU,memory,connections and writing to Influx
		//Writer w=new Writer();
		//CPU_util=Math.random()*(100-0+1)+0;
		//Memory_util=Math.random()*(100-0+1)+0;
		//connections=(int)(Math.random()*(50-0+1)+0);
		//w.writeNFT_Hosts(\"API1\",CPU_util,Memory_util,connections);
		//if all 3 ok then counter3==1
		//if(CPU_util<=70&&Memory_util<=70&&connections>0)
		{
			//counter3=1;
		}
		//if cpu,memory>90 and connection==0 then counter3==-1 otherwise its default 0 
		//if(CPU_util>90||Memory_util>90)
		{
			//if(connections==0)
				//counter3=-1;
		}
		
		//if in any of the iterations any funcional validation fails or non-functional validations passes in <60% iterations or counter3==-1 
				//we are //setting the background color as red for that API
		if(counter1<100||counter2<60||counter3<60)
		{
			status="red";
		}
		//if all functional passes  
		 //and non func validation passes more than 80% iteration->green 
		 //and non func validation passes 60% iteration->yellow
		if(counter1==100)
		{
			//green doesnt depend on counter3==0 or 1
			if(counter2>80&&counter3>80)	
			{
				status="green";
			}
			//yellow if counter3==0
			if((counter2<=80&&counter2>=60)&&(counter3<=80&&counter3>=60))
			{
				status="yellow";
			}
		}
		//System.out.println(status);
	%>
	<a href="http://localhost:3000/d/Joo3RxJ7k/brand1-api-1-dashboard?orgId=1&refresh=10s" target="_blank" rel="noopener noreferrer"
			style="height: 50px;
    				width:20%;
    				background-color:<%out.println(status);%>;
    				text-align: center;
    				vertical-align: middle;
    				line-height: 50px;
    				display: inline-block;
    				margin-left:2%;
    				margin-right:2%;
    				text-decoration:none;
    				color:black;"
			>API-1</a>
	<!-- API-2 -->
	<% 
		//this func runs the APIs 5 iterations using RequestChecker class and returns results of all functional 
		//and non-functional validations
		
		ArrayList<int[]> list2=caller.callAPI2();
		counter1=0;
		counter2=0;
		counter3=0;		
		for(int i=0;i<list2.size();i++)
		{
			counter1=counter1+list2.get(i)[0];
			counter2=counter2+list2.get(i)[1];
		}
		//System.out.println(count);
		counter1=counter1/iterations;
		counter2=counter2/iterations;
		//System.out.println(counter1);
		//System.out.println(counter2);	
		
		//getting CPU,memory,connections and writing to Influx
		CPU_util=Math.random()*(100-0+1)+0;
		Memory_util=Math.random()*(100-0+1)+0;
		connections=(int)(Math.random()*(50-0+1)+0);
		//w.writeNFT_Hosts("API2",CPU_util,Memory_util,connections);
		//if all 3 ok then counter3==1
		if(CPU_util<=70&&Memory_util<=70&&connections>0)
		{
			counter3=1;
		}
		//if cpu,memory>90 and connection==0 then counter3==-1 otherwise its default 0 
		if(CPU_util>90||Memory_util>90)
		{
			if(connections==0)
				counter3=-1;
		}
		
		//if in any of the iterations any funcional validation fails or non-functional validations passes in <60% iterations or counter3==-1 
				//we are //setting the background color as red for that API
		if(counter1<100||counter2<60||counter3==-1)
		{
			status="red";
		}
		//if all functional passes  
		 //and non func validation passes more than 80% iteration->green 
		 //and non func validation passes 60% iteration->yellow
		if(counter1==100)
		{
			//green doesnt depend on counter3==0 or 1
			if(counter2>80)	
			{
				status="green";
			}
			//yellow if counter3==0
			if((counter2<=80&&counter2>=60)||counter3==0)
			{
				status="yellow";
			}
		}
		//System.out.println(status);
	%>
	<a href="http://localhost:3000/d/xsqjhVxnz/brand1-api-2-dashboard?orgId=1&refresh=10s" target="_blank" rel="noopener noreferrer"
			style="height: 50px;
    				width:20%;
    				background-color:<%out.println(status);%>;
    				text-align: center;
    				vertical-align: middle;
    				line-height: 50px;
    				display: inline-block;
    				margin-left:2%;
    				margin-right:2%;
    				text-decoration:none;
    				color:black;"
			>API-2</a>
	<!-- API-3 -->
	<% 
		//this func runs the APIs 5 iterations using RequestChecker class and returns results of all functional 
		//and non-functional validations
		
		ArrayList<int[]> list3=caller.callAPI3();
		counter1=0;
		counter2=0;
		counter3=0;		
		for(int i=0;i<list3.size();i++)
		{
			counter1=counter1+list3.get(i)[0];
			counter2=counter2+list3.get(i)[1];
		}
		//System.out.println(count);
		counter1=counter1/iterations;
		counter2=counter2/iterations;
		//System.out.println(counter1);
		//System.out.println(counter2);	
		
		//getting CPU,memory,connections and writing to Influx
		//CPU_util=Math.random()*(100-0+1)+0;
		//Memory_util=Math.random()*(100-0+1)+0;
		//connections=(int)(Math.random()*(50-0+1)+0);
		//w.writeNFT_Hosts("API3",CPU_util,Memory_util,connections);
		//if all 3 ok then counter3==1
		if(CPU_util<=70&&Memory_util<=70&&connections>0)
		{
			counter3=1;
		}
		//if cpu,memory>90 and connection==0 then counter3==-1 otherwise its default 0 
		if(CPU_util>90||Memory_util>90)
		{
			if(connections==0)
				counter3=-1;
		}
		
		//if in any of the iterations any funcional validation fails or non-functional validations passes in <60% iterations or counter3==-1 
				//we are //setting the background color as red for that API
		if(counter1<100||counter2<60||counter3==-1)
		{
			status="red";
		}
		//if all functional passes  
		 //and non func validation passes more than 80% iteration->green 
		 //and non func validation passes 60% iteration->yellow
		if(counter1==100)
		{
			//green doesnt depend on counter3==0 or 1
			if(counter2>80)	
			{
				status="green";
			}
			//yellow if counter3==0
			if((counter2<=80&&counter2>=60)||counter3==0)
			{
				status="yellow";
			}
		}
		//System.out.println(status);
	%>
	<a href="http://localhost:3000/d/BFvjr4bnk/brand1-api-3-dashboard?orgId=1&refresh=10s" target="_blank" rel="noopener noreferrer"
			style="height: 50px;
    				width:20%;
    				background-color:<%out.println(status);%>;
    				text-align: center;
    				vertical-align: middle;
    				line-height: 50px;
    				display: inline-block;
    				margin-left:2%;
    				margin-right:2%;
    				text-decoration:none;
    				color:black;"
			>API-3</a>
	<!-- API-4 -->
	<% 
		//this func runs the APIs 5 iterations using RequestChecker class and returns results of all functional 
		//and non-functional validations
		
		ArrayList<int[]> list4=caller.callAPI4();
		counter1=0;
		counter2=0;
		counter3=0;		
		for(int i=0;i<list4.size();i++)
		{
			counter1=counter1+list4.get(i)[0];
			counter2=counter2+list4.get(i)[1];
		}
		//System.out.println(count);
		counter1=counter1/iterations;
		counter2=counter2/iterations;
		//System.out.println(counter1);
		//System.out.println(counter2);	
		
		//getting CPU,memory,connections and writing to Influx
		CPU_util=Math.random()*(100-0+1)+0;
		Memory_util=Math.random()*(100-0+1)+0;
		connections=(int)(Math.random()*(50-0+1)+0);
		//w.writeNFT_Hosts("API4",CPU_util,Memory_util,connections);
		//if all 3 ok then counter3==1
		if(CPU_util<=70&&Memory_util<=70&&connections>0)
		{
			counter3=1;
		}
		//if cpu,memory>90 and connection==0 then counter3==-1 otherwise its default 0 
		if(CPU_util>90||Memory_util>90)
		{
			if(connections==0)
				counter3=-1;
		}
		
		//if in any of the iterations any funcional validation fails or non-functional validations passes in <60% iterations or counter3==-1 
				//we are //setting the background color as red for that API
		if(counter1<100||counter2<60||counter3==-1)
		{
			status="red";
		}
		//if all functional passes  
		 //and non func validation passes more than 80% iteration->green 
		 //and non func validation passes 60% iteration->yellow
		if(counter1==100)
		{
			//green doesnt depend on counter3==0 or 1
			if(counter2>80)	
			{
				status="green";
			}
			//yellow if counter3==0
			if((counter2<=80&&counter2>=60)||counter3==0)
			{
				status="yellow";
			}
		}
		//System.out.println(status);
	%>
	<a href="http://localhost:3000/d/zRAl64bnk/brand1-api-4-dashboard?orgId=1&refresh=10s" target="_blank" rel="noopener noreferrer"
			style="height: 50px;
    				width:20%;
    				background-color:<%out.println(status);%>;
    				text-align: center;
    				vertical-align: middle;
    				line-height: 50px;
    				display: inline-block;
    				margin-left:2%;
    				margin-right:2%;
    				text-decoration:none;
    				color:black;"
			>API-4</a>
	<!-- API-5 -->
	<% 
		//this func runs the APIs 5 iterations using RequestChecker class and returns results of all functional 
		//and non-functional validations
		
		ArrayList<int[]> list5=caller.callAPI5();
		counter1=0;
		counter2=0;
		counter3=0;		
		for(int i=0;i<list5.size();i++)
		{
			counter1=counter1+list5.get(i)[0];
			counter2=counter2+list5.get(i)[1];
		}
		//System.out.println(count);
		counter1=counter1/iterations;
		counter2=counter2/iterations;
		//System.out.println(counter1);
		//System.out.println(counter2);	
		
		//getting CPU,memory,connections and writing to Influx
		CPU_util=Math.random()*(100-0+1)+0;
		Memory_util=Math.random()*(100-0+1)+0;
		connections=(int)(Math.random()*(50-0+1)+0);
		//w.writeNFT_Hosts("API5",CPU_util,Memory_util,connections);
		//if all 3 ok then counter3==1
		if(CPU_util<=70&&Memory_util<=70&&connections>0)
		{
			counter3=1;
		}
		//if cpu,memory>90 and connection==0 then counter3==-1 otherwise its default 0 
		if(CPU_util>90||Memory_util>90)
		{
			if(connections==0)
				counter3=-1;
		}
		
		//if in any of the iterations any funcional validation fails or non-functional validations passes in <60% iterations or counter3==-1 
				//we are //setting the background color as red for that API
		if(counter1<100||counter2<60||counter3==-1)
		{
			status="red";
		}
		//if all functional passes  
		 //and non func validation passes more than 80% iteration->green 
		 //and non func validation passes 60% iteration->yellow
		if(counter1==100)
		{
			//green doesnt depend on counter3==0 or 1
			if(counter2>80)	
			{
				status="green";
			}
			//yellow if counter3==0
			if((counter2<=80&&counter2>=60)||counter3==0)
			{
				status="yellow";
			}
		}
		//System.out.println(status);
	%>
	<a href="http://localhost:3000/d/LnNjWSbnk/brand1-api-5-dashboard?orgId=1&refresh=10s" target="_blank" rel="noopener noreferrer"
			style="height: 50px;
    				width:20%;
    				background-color:<%out.println(status);%>;
    				text-align: center;
    				vertical-align: middle;
    				line-height: 50px;
    				display: inline-block;
    				margin-left:2%;
    				margin-right:2%;
    				margin-top:2%;
    				text-decoration:none;
    				color:black;"
			>API-5</a>				
</body>
</html>