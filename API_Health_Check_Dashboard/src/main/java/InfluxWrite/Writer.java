package InfluxWrite;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;


public class Writer {
	String token = "y374nSq_QtSTP1B1a9Q2YIBEIpXZpsnDc9VMAgRQx7JBgKHcJYcfXhREIL21IIesNeCoVhO_ycgrIsiREa8oBA==";
	String bucket1 = "API_Response";

	String org = "Test";

	InfluxDBClient client = InfluxDBClientFactory.create("http://localhost:8086", token.toCharArray());

	double[] per_validations;
	//InfluxDBClient influxDB = InfluxDBFactory.connect("http://localhost:8086", "Surajit_Ghosh","SuId@2021");



	//Getting val LinkedHashMap and writing results to influxdb of total percentage of passedfunctional and non-functional 
	//validations,what are the functional and non-functional validations done and their status


	public double[] write1(LinkedHashMap<String,String> validations_API1)
	{
		WriteApiBlocking writeApi = client.getWriteApiBlocking();

		double total_func_val=0,total_func_val_fail=0;
		double total_non_func_val=0,total_non_func_val_fail=0;
		double total_non_func_host_val=0,total_non_func_host_val_fail=0;
		ArrayList<String> func_val_name=new ArrayList<String>();
		ArrayList<String> non_func_val_name=new ArrayList<String>();
		ArrayList<String> non_func_host_val_name=new ArrayList<String>();
		int j=0;int k=1;
		String errors="NA";
		String anamolies="NA";
		long response_time=-1;
		double CPU_util=-1,Memory_util=-1,DB_Query_times=-1;
		int connections=-1,third_party_calls=-1,downstream_calls=-1,statusCode=-1;
		per_validations=new double[3];
		//System.out.println("func call");
		for(Map.Entry i:validations_API1.entrySet())
		{
			String key=(String) i.getKey();
			String value=(String) i.getValue();
			if(key.startsWith("Functional"))
			{
				total_func_val++;
				if(!value.equals("Passed"))
					total_func_val_fail++;
				func_val_name.add(i.getKey().toString().substring(11));
				//System.out.println(j);
				//System.out.println(""+i.getKey().toString().substring(11));
			}
			if(key.contains("Non_Functional:"))
			{
				//System.out.println("in");
				if(value.equals("Passed")||value.equals("Failed"))
				{
					total_non_func_val++;
					if(!value.equals("Passed"))
						total_non_func_val_fail++;
				}
				non_func_val_name.add(i.getKey().toString().substring(15));
				//System.out.println(k);
				//System.out.println(""+i.getKey().toString().substring(15));

			}
			if(key.contains("Non_Functional_Host:"))
			{
				//System.out.println("in");
				total_non_func_host_val++;
				//System.out.println("total_non_func_host_val:"+total_non_func_host_val);
				if(!value.equals("Passed"))
					total_non_func_host_val_fail++;
				//System.out.println("total_non_func_host_val_fail:"+total_non_func_host_val_fail);
				non_func_host_val_name.add(i.getKey().toString().substring(20));
				//System.out.println("name:"+i.getKey().toString().substring(20));
				//System.out.println(k);
				//System.out.println(""+i.getKey().toString().substring(15));

			}

			if(key.equals("third_level_validations:Status Code"))
			{
				statusCode=Integer.parseInt(value);
				//System.out.println("Status Code:"+statusCode);
			}
			if(key.equals("third_level_validations:Errors"))
			{
				if(!value.equals("NA"))
					errors=value;
				//System.out.println("Errors:"+errors);
			}
			if(key.equals("third_level_validations:Anamolies from logs"))
			{
				if(!value.equals("NA"))
					anamolies=value;
				//System.out.println("Anamolies from logs:"+anamolies);
			}
			if(key.equals("third_level_validations:Response Time"))
			{
				if(!value.equals("NA"))
					response_time=Long.parseLong(value);
				//System.out.println("Res time:"+response_time);
			}
			if(key.equals("third_level_validations:CPU Utilization"))
			{
				CPU_util=Double.parseDouble(value);
				//System.out.println("CPU:"+CPU_util);
			}
			if(key.equals("third_level_validations:Memory Utilization"))
			{
				Memory_util=Double.parseDouble(value);
				//System.out.println("Memory:"+Memory_util);
			}
			if(key.equals("third_level_validations:Connections"))
			{
				connections=Integer.parseInt(value);
				//System.out.println("Connections:"+connections);
			}
			if(key.equals("third_level_validations:DB Query time"))
			{
				DB_Query_times=Double.parseDouble(value);
				//System.out.println("DB Query:"+DB_Query_times);
			}
			if(key.equals("third_level_validations:Third Party Calls"))
			{
				third_party_calls=Integer.parseInt(value);
				//System.out.println("3rd party:"+third_party_calls);
			}
			if(key.equals("third_level_validations:Downstream Calls"))
			{
				downstream_calls=Integer.parseInt(value);
				//System.out.println("Downstream calls:"+downstream_calls);
			}
		}
		
		//System.out.println("Influx Write 3rd Level");
		Point third_level = Point
				.measurement("third_level_Validation")
				.addTag("API", "API1")
				.addField("Status Code",statusCode)
				.addField("Errors",errors)
				.addField("Anamolies from logs",anamolies)
				.addField("Response Time",response_time)
				.addField("CPU Utilization",CPU_util)
				.addField("Memory Utilization",Memory_util)
				.addField("Connections",connections)
				.addField("DB Query time",DB_Query_times)
				.addField("Third Party Calls",third_party_calls)
				.addField("Downstream Calls",downstream_calls)
				.time(Instant.now(), WritePrecision.NS);
		writeApi.writePoint(bucket1, org, third_level);

		
		//percentage of functional validation passed
		double per_func_val_pass=((total_func_val-total_func_val_fail)/total_func_val)*100;
		//percentage of functional validation failed
		double per_func_val_fail=100-per_func_val_pass;
		//System.out.println("per_func_val_pass:"+per_func_val_pass);

		//percentage of non-functional validation passed
		double per_non_func_val_pass=((total_non_func_val-total_non_func_val_fail)/total_non_func_val)*100;
		//percentage of non-functional validation failed
		double per_non_func_val_fail=100-per_non_func_val_pass;
		//System.out.println("per_non_func_val_pass:"+per_non_func_val_pass);

		//int diff=(total_non_func_host_val-total_non_func_host_val_fail);
		//System.out.println("diff:"+diff);
		//double diff2=diff/total_non_func_host_val;
		//System.out.println("diff2:"+diff2);
		//double diff3=diff2=diff2*100;
		//System.out.println("diff3:"+diff3);
		double per_non_func_host_val_pass=((total_non_func_host_val-total_non_func_host_val_fail)/total_non_func_host_val)*100;
		double per_non_func_host_val_fail=100-per_non_func_host_val_pass;
		//System.out.println("per_non_func_host_val_pass:"+per_non_func_host_val_pass);

		per_validations[0]=per_func_val_pass;
		per_validations[1]=per_non_func_val_pass;
		per_validations[2]=per_non_func_host_val_pass;


		//System.out.println(per_func_val_pass);
		//System.out.println(per_non_func_val_pass);
		//System.out.println("per_non_func_host_val_pass:"+per_non_func_host_val_pass);

		//influxdb point to write percentage of functional validations passed
		Point point1 = Point
				.measurement("Percentage_Validation")
				.addTag("API", "API1")
				.addField("Functional_Validation_passed",per_func_val_pass)
				.addField("Functional_Validation_failed",per_func_val_fail)
				.time(Instant.now(), WritePrecision.NS);
		writeApi.writePoint(bucket1, org, point1);
		//
		//func_val_name,status
		Point point2 = Point
				.measurement("Validations")
				.addTag("API", "API1")
				.addField(func_val_name.get(0),validations_API1.get("Functional:"+func_val_name.get(0)))
				.addField(func_val_name.get(1),validations_API1.get("Functional:"+func_val_name.get(1)))
				.addField(func_val_name.get(2),validations_API1.get("Functional:"+func_val_name.get(2)))
				.addField(func_val_name.get(3),validations_API1.get("Functional:"+func_val_name.get(3)))
				.addField(func_val_name.get(4),validations_API1.get("Functional:"+func_val_name.get(4)))
				.time(Instant.now(), WritePrecision.NS);

		//System.out.println(""+val.get("Functional:"+"func_val_name.get(0)"));
		//System.out.println(""+val.get("Functional:Status Code"));
		//System.out.println(""+"Functional:"+func_val_name.get(0));

		writeApi.writePoint(bucket1, org, point2);

		//influxdb point to write data of non-functional validations
		Point point3 = Point
				.measurement("Percentage_Validation")
				.addTag("API", "API1")
				.addField("Non_Functional_Validation_passed",per_non_func_val_pass)
				.addField("Non_Functional_Validation_failed",per_non_func_val_fail)
				.time(Instant.now(), WritePrecision.NS);
		writeApi.writePoint(bucket1, org, point3);
		//non-func_val_name,status
		Point point4 = Point
				.measurement("Validations")
				.addTag("API", "API1")
				.addField(non_func_val_name.get(0),validations_API1.get("Non_Functional:"+non_func_val_name.get(0)))
				.addField(non_func_val_name.get(1),validations_API1.get("Non_Functional:"+non_func_val_name.get(1)))
				.addField(non_func_val_name.get(2),validations_API1.get("Non_Functional:"+non_func_val_name.get(2)))
				.time(Instant.now(), WritePrecision.NS);


		writeApi.writePoint(bucket1, org, point4);

		Point point5= Point
				.measurement("Percentage_Validation")
				.addTag("API", "API1")
				.addField("Non_Functional_Host_Validation_passed",per_non_func_host_val_pass)
				.addField("Non_Functional_Host_Validation_failed",per_non_func_host_val_fail)
				.time(Instant.now(), WritePrecision.NS);

		writeApi.writePoint(bucket1, org, point5);

		Point point6 = Point
				.measurement("Validations")
				.addTag("API", "API1")
				.addField(non_func_host_val_name.get(0),validations_API1.get("Non_Functional_Host:"+non_func_host_val_name.get(0)))
				.addField(non_func_host_val_name.get(1),validations_API1.get("Non_Functional_Host:"+non_func_host_val_name.get(1)))
				.addField(non_func_host_val_name.get(2),validations_API1.get("Non_Functional_Host:"+non_func_host_val_name.get(2)))
				.addField(non_func_host_val_name.get(3),validations_API1.get("Non_Functional_Host:"+non_func_host_val_name.get(3)))
				.addField(non_func_host_val_name.get(4),validations_API1.get("Non_Functional_Host:"+non_func_host_val_name.get(4)))
				.addField(non_func_host_val_name.get(5),validations_API1.get("Non_Functional_Host:"+non_func_host_val_name.get(5)))
				.time(Instant.now(), WritePrecision.NS);

		writeApi.writePoint(bucket1, org, point6);
		//retruning per validations of each iteration	
		return per_validations;	
	}
	
    public double[] write2(LinkedHashMap<String,String> validations_API2)
    {
    	WriteApiBlocking writeApi = client.getWriteApiBlocking();

    	double total_func_val=0,total_func_val_fail=0;
		double total_non_func_val=0,total_non_func_val_fail=0;
		double total_non_func_host_val=0,total_non_func_host_val_fail=0;
		ArrayList<String> func_val_name=new ArrayList<String>();
		ArrayList<String> non_func_val_name=new ArrayList<String>();
		ArrayList<String> non_func_host_val_name=new ArrayList<String>();
		int j=0;int k=1;
		String errors="NA";
		String anamolies="NA";
		long response_time=-1;
		double CPU_util=-1,Memory_util=-1,DB_Query_times=-1;
		int connections=-1,third_party_calls=-1,downstream_calls=-1,statusCode=-1;
		per_validations=new double[3];

    	
		for(Map.Entry i:validations_API2.entrySet())
		{
			String key=(String) i.getKey();
			String value=(String) i.getValue();
			if(key.startsWith("Functional"))
			{
				total_func_val++;
				if(!value.equals("Passed"))
					total_func_val_fail++;
				func_val_name.add(i.getKey().toString().substring(11));
				//System.out.println(j);
				//System.out.println(""+i.getKey().toString().substring(11));
			}
			if(key.contains("Non_Functional:"))
			{
				//System.out.println("in");
				if(value.equals("Passed")||value.equals("Failed"))
				{
					total_non_func_val++;
					if(!value.equals("Passed"))
						total_non_func_val_fail++;
				}
				non_func_val_name.add(i.getKey().toString().substring(15));
				//System.out.println(k);
				//System.out.println(""+i.getKey().toString().substring(15));

			}
			if(key.contains("Non_Functional_Host:"))
			{
				//System.out.println("in");
				total_non_func_host_val++;
				//System.out.println("total_non_func_host_val:"+total_non_func_host_val);
				if(!value.equals("Passed"))
					total_non_func_host_val_fail++;
				//System.out.println("total_non_func_host_val_fail:"+total_non_func_host_val_fail);
				non_func_host_val_name.add(i.getKey().toString().substring(20));
				//System.out.println("name:"+i.getKey().toString().substring(20));
				//System.out.println(k);
				//System.out.println(""+i.getKey().toString().substring(15));
			}

			if(key.equals("third_level_validations:Status Code"))
			{
				statusCode=Integer.parseInt(value);
				//System.out.println("Status Code:"+statusCode);
			}
			if(key.equals("third_level_validations:Errors"))
			{
				if(!value.equals("NA"))
					errors=value;
				//System.out.println("Errors:"+errors);
			}
			if(key.equals("third_level_validations:Anamolies from logs"))
			{
				if(!value.equals("NA"))
					anamolies=value;
				//System.out.println("Anamolies from logs:"+anamolies);
			}
			if(key.equals("third_level_validations:Response Time"))
			{
				if(!value.equals("NA"))
					response_time=Long.parseLong(value);
				//System.out.println("Res time:"+response_time);
			}
			if(key.equals("third_level_validations:CPU Utilization"))
			{
				CPU_util=Double.parseDouble(value);
				//System.out.println("CPU:"+CPU_util);
			}
			if(key.equals("third_level_validations:Memory Utilization"))
			{
				Memory_util=Double.parseDouble(value);
				//System.out.println("Memory:"+Memory_util);
			}
			if(key.equals("third_level_validations:Connections"))
			{
				connections=Integer.parseInt(value);
				//System.out.println("Connections:"+connections);
			}
			if(key.equals("third_level_validations:DB Query time"))
			{
				DB_Query_times=Double.parseDouble(value);
				//System.out.println("DB Query:"+DB_Query_times);
			}
			if(key.equals("third_level_validations:Third Party Calls"))
			{
				third_party_calls=Integer.parseInt(value);
				//System.out.println("3rd party:"+third_party_calls);
			}
			if(key.equals("third_level_validations:Downstream Calls"))
			{
				downstream_calls=Integer.parseInt(value);
				//System.out.println("Downstream calls:"+downstream_calls);
			}
			
		}
		
		Point third_level = Point
				.measurement("third_level_Validation")
				.addTag("API", "API2")
				.addField("Status Code",statusCode)
				.addField("Errors",errors)
				.addField("Anamolies from logs",anamolies)
				.addField("Response Time",response_time)
				.addField("CPU Utilization",CPU_util)
				.addField("Memory Utilization",Memory_util)
				.addField("Connections",connections)
				.addField("DB Query time",DB_Query_times)
				.addField("Third Party Calls",third_party_calls)
				.addField("Downstream Calls",downstream_calls)
				.time(Instant.now(), WritePrecision.NS);
		writeApi.writePoint(bucket1, org, third_level);
		
		//percentage of functional validation passed
		double per_func_val_pass=((total_func_val-total_func_val_fail)/total_func_val)*100;
		//percentage of functional validation failed
		double per_func_val_fail=100-per_func_val_pass;
		//System.out.println("per_func_val_pass:"+per_func_val_pass);

		//percentage of non-functional validation passed
		double per_non_func_val_pass=((total_non_func_val-total_non_func_val_fail)/total_non_func_val)*100;
		//percentage of non-functional validation failed
		double per_non_func_val_fail=100-per_non_func_val_pass;
		//System.out.println("per_non_func_val_pass:"+per_non_func_val_pass);

		//int diff=(total_non_func_host_val-total_non_func_host_val_fail);
		//System.out.println("diff:"+diff);
		//double diff2=diff/total_non_func_host_val;
		//System.out.println("diff2:"+diff2);
		//double diff3=diff2=diff2*100;
		//System.out.println("diff3:"+diff3);
		double per_non_func_host_val_pass=((total_non_func_host_val-total_non_func_host_val_fail)/total_non_func_host_val)*100;
		double per_non_func_host_val_fail=100-per_non_func_host_val_pass;
		//System.out.println("per_non_func_host_val_pass:"+per_non_func_host_val_pass);

		per_validations[0]=per_func_val_pass;
		per_validations[1]=per_non_func_val_pass;
		per_validations[2]=per_non_func_host_val_pass;


		//System.out.println(per_func_val_pass);
		//System.out.println(per_non_func_val_pass);
		//System.out.println("per_non_func_host_val_pass:"+per_non_func_host_val_pass);

		//influxdb point to write percentage of functional validations passed
		Point point1 = Point
				.measurement("Percentage_Validation")
				.addTag("API", "API2")
				.addField("Functional_Validation_passed",per_func_val_pass)
				.addField("Functional_Validation_failed",per_func_val_fail)
				.time(Instant.now(), WritePrecision.NS);
		writeApi.writePoint(bucket1, org, point1);
		//
		//func_val_name,status
		Point point2 = Point
				.measurement("Validations")
				.addTag("API", "API2")
				.addField(func_val_name.get(0),validations_API2.get("Functional:"+func_val_name.get(0)))
				.addField(func_val_name.get(1),validations_API2.get("Functional:"+func_val_name.get(1)))
				.addField(func_val_name.get(2),validations_API2.get("Functional:"+func_val_name.get(2)))
				.addField(func_val_name.get(3),validations_API2.get("Functional:"+func_val_name.get(3)))
				.addField(func_val_name.get(4),validations_API2.get("Functional:"+func_val_name.get(4)))
				.time(Instant.now(), WritePrecision.NS);

		//System.out.println(""+val.get("Functional:"+"func_val_name.get(0)"));
		//System.out.println(""+val.get("Functional:Status Code"));
		//System.out.println(""+"Functional:"+func_val_name.get(0));

		writeApi.writePoint(bucket1, org, point2);

		//influxdb point to write data of non-functional validations
		Point point3 = Point
				.measurement("Percentage_Validation")
				.addTag("API", "API2")
				.addField("Non_Functional_Validation_passed",per_non_func_val_pass)
				.addField("Non_Functional_Validation_failed",per_non_func_val_fail)
				.time(Instant.now(), WritePrecision.NS);
		writeApi.writePoint(bucket1, org, point3);
		//non-func_val_name,status
		Point point4 = Point
				.measurement("Validations")
				.addTag("API", "API2")
				.addField(non_func_val_name.get(0),validations_API2.get("Non_Functional:"+non_func_val_name.get(0)))
				.addField(non_func_val_name.get(1),validations_API2.get("Non_Functional:"+non_func_val_name.get(1)))
				.addField(non_func_val_name.get(2),validations_API2.get("Non_Functional:"+non_func_val_name.get(2)))
				.time(Instant.now(), WritePrecision.NS);


		writeApi.writePoint(bucket1, org, point4);

		Point point5= Point
				.measurement("Percentage_Validation")
				.addTag("API", "API2")
				.addField("Non_Functional_Host_Validation_passed",per_non_func_host_val_pass)
				.addField("Non_Functional_Host_Validation_failed",per_non_func_host_val_fail)
				.time(Instant.now(), WritePrecision.NS);

		writeApi.writePoint(bucket1, org, point5);

		Point point6 = Point
				.measurement("Validations")
				.addTag("API", "API2")
				.addField(non_func_host_val_name.get(0),validations_API2.get("Non_Functional_Host:"+non_func_host_val_name.get(0)))
				.addField(non_func_host_val_name.get(1),validations_API2.get("Non_Functional_Host:"+non_func_host_val_name.get(1)))
				.addField(non_func_host_val_name.get(2),validations_API2.get("Non_Functional_Host:"+non_func_host_val_name.get(2)))
				.addField(non_func_host_val_name.get(3),validations_API2.get("Non_Functional_Host:"+non_func_host_val_name.get(3)))
				.addField(non_func_host_val_name.get(4),validations_API2.get("Non_Functional_Host:"+non_func_host_val_name.get(4)))
				.addField(non_func_host_val_name.get(5),validations_API2.get("Non_Functional_Host:"+non_func_host_val_name.get(5)))
				.time(Instant.now(), WritePrecision.NS);

		writeApi.writePoint(bucket1, org, point6);
		
		return per_validations;
    }
    
    public double[] write3(LinkedHashMap<String,String> validations_API3)
    {
    	WriteApiBlocking writeApi = client.getWriteApiBlocking();

    	double total_func_val=0,total_func_val_fail=0;
		double total_non_func_val=0,total_non_func_val_fail=0;
		double total_non_func_host_val=0,total_non_func_host_val_fail=0;
		ArrayList<String> func_val_name=new ArrayList<String>();
		ArrayList<String> non_func_val_name=new ArrayList<String>();
		ArrayList<String> non_func_host_val_name=new ArrayList<String>();
		int j=0;int k=1;
		String errors="NA";
		String anamolies="NA";
		long response_time=-1;
		double CPU_util=-1,Memory_util=-1,DB_Query_times=-1;
		int connections=-1,third_party_calls=-1,downstream_calls=-1,statusCode=-1;
		per_validations=new double[3];

    	
		for(Map.Entry i:validations_API3.entrySet())
		{
			String key=(String) i.getKey();
			String value=(String) i.getValue();
			if(key.startsWith("Functional"))
			{
				total_func_val++;
				if(!value.equals("Passed"))
					total_func_val_fail++;
				func_val_name.add(i.getKey().toString().substring(11));
				//System.out.println(j);
				//System.out.println(""+i.getKey().toString().substring(11));
			}
			if(key.contains("Non_Functional:"))
			{
				//System.out.println("in");
				if(value.equals("Passed")||value.equals("Failed"))
				{
					total_non_func_val++;
					if(!value.equals("Passed"))
						total_non_func_val_fail++;
				}
				non_func_val_name.add(i.getKey().toString().substring(15));
				//System.out.println(k);
				//System.out.println(""+i.getKey().toString().substring(15));

			}
			if(key.contains("Non_Functional_Host:"))
			{
				//System.out.println("in");
				total_non_func_host_val++;
				//System.out.println("total_non_func_host_val:"+total_non_func_host_val);
				if(!value.equals("Passed"))
					total_non_func_host_val_fail++;
				//System.out.println("total_non_func_host_val_fail:"+total_non_func_host_val_fail);
				non_func_host_val_name.add(i.getKey().toString().substring(20));
				//System.out.println("name:"+i.getKey().toString().substring(20));
				//System.out.println(k);
				//System.out.println(""+i.getKey().toString().substring(15));
			}

			if(key.equals("third_level_validations:Status Code"))
			{
				statusCode=Integer.parseInt(value);
				//System.out.println("Status Code:"+statusCode);
			}
			if(key.equals("third_level_validations:Errors"))
			{
				if(!value.equals("NA"))
					errors=value;
				//System.out.println("Errors:"+errors);
			}
			if(key.equals("third_level_validations:Anamolies from logs"))
			{
				if(!value.equals("NA"))
					anamolies=value;
				//System.out.println("Anamolies from logs:"+anamolies);
			}
			if(key.equals("third_level_validations:Response Time"))
			{
				if(!value.equals("NA"))
					response_time=Long.parseLong(value);
				//System.out.println("Res time:"+response_time);
			}
			if(key.equals("third_level_validations:CPU Utilization"))
			{
				CPU_util=Double.parseDouble(value);
				//System.out.println("CPU:"+CPU_util);
			}
			if(key.equals("third_level_validations:Memory Utilization"))
			{
				Memory_util=Double.parseDouble(value);
				//System.out.println("Memory:"+Memory_util);
			}
			if(key.equals("third_level_validations:Connections"))
			{
				connections=Integer.parseInt(value);
				//System.out.println("Connections:"+connections);
			}
			if(key.equals("third_level_validations:DB Query time"))
			{
				DB_Query_times=Double.parseDouble(value);
				//System.out.println("DB Query:"+DB_Query_times);
			}
			if(key.equals("third_level_validations:Third Party Calls"))
			{
				third_party_calls=Integer.parseInt(value);
				//System.out.println("3rd party:"+third_party_calls);
			}
			if(key.equals("third_level_validations:Downstream Calls"))
			{
				downstream_calls=Integer.parseInt(value);
				//System.out.println("Downstream calls:"+downstream_calls);
			}
			
		}
		
		Point third_level = Point
				.measurement("third_level_Validation")
				.addTag("API", "API3")
				.addField("Status Code",statusCode)
				.addField("Errors",errors)
				.addField("Anamolies from logs",anamolies)
				.addField("Response Time",response_time)
				.addField("CPU Utilization",CPU_util)
				.addField("Memory Utilization",Memory_util)
				.addField("Connections",connections)
				.addField("DB Query time",DB_Query_times)
				.addField("Third Party Calls",third_party_calls)
				.addField("Downstream Calls",downstream_calls)
				.time(Instant.now(), WritePrecision.NS);
		writeApi.writePoint(bucket1, org, third_level);
		
		//percentage of functional validation passed
		double per_func_val_pass=((total_func_val-total_func_val_fail)/total_func_val)*100;
		//percentage of functional validation failed
		double per_func_val_fail=100-per_func_val_pass;
		//System.out.println("per_func_val_pass:"+per_func_val_pass);

		//percentage of non-functional validation passed
		double per_non_func_val_pass=((total_non_func_val-total_non_func_val_fail)/total_non_func_val)*100;
		//percentage of non-functional validation failed
		double per_non_func_val_fail=100-per_non_func_val_pass;
		//System.out.println("per_non_func_val_pass:"+per_non_func_val_pass);

		//int diff=(total_non_func_host_val-total_non_func_host_val_fail);
		//System.out.println("diff:"+diff);
		//double diff2=diff/total_non_func_host_val;
		//System.out.println("diff2:"+diff2);
		//double diff3=diff2=diff2*100;
		//System.out.println("diff3:"+diff3);
		double per_non_func_host_val_pass=((total_non_func_host_val-total_non_func_host_val_fail)/total_non_func_host_val)*100;
		double per_non_func_host_val_fail=100-per_non_func_host_val_pass;
		//System.out.println("per_non_func_host_val_pass:"+per_non_func_host_val_pass);

		per_validations[0]=per_func_val_pass;
		per_validations[1]=per_non_func_val_pass;
		per_validations[2]=per_non_func_host_val_pass;


		//System.out.println(per_func_val_pass);
		//System.out.println(per_non_func_val_pass);
		//System.out.println("per_non_func_host_val_pass:"+per_non_func_host_val_pass);

		//influxdb point to write percentage of functional validations passed
		Point point1 = Point
				.measurement("Percentage_Validation")
				.addTag("API", "API3")
				.addField("Functional_Validation_passed",per_func_val_pass)
				.addField("Functional_Validation_failed",per_func_val_fail)
				.time(Instant.now(), WritePrecision.NS);
		writeApi.writePoint(bucket1, org, point1);
		//
		//func_val_name,status
		Point point2 = Point
				.measurement("Validations")
				.addTag("API", "API3")
				.addField(func_val_name.get(0),validations_API3.get("Functional:"+func_val_name.get(0)))
				.addField(func_val_name.get(1),validations_API3.get("Functional:"+func_val_name.get(1)))
				.addField(func_val_name.get(2),validations_API3.get("Functional:"+func_val_name.get(2)))
				.addField(func_val_name.get(3),validations_API3.get("Functional:"+func_val_name.get(3)))
				.addField(func_val_name.get(4),validations_API3.get("Functional:"+func_val_name.get(4)))
				.time(Instant.now(), WritePrecision.NS);

		//System.out.println(""+val.get("Functional:"+"func_val_name.get(0)"));
		//System.out.println(""+val.get("Functional:Status Code"));
		//System.out.println(""+"Functional:"+func_val_name.get(0));

		writeApi.writePoint(bucket1, org, point2);

		//influxdb point to write data of non-functional validations
		Point point3 = Point
				.measurement("Percentage_Validation")
				.addTag("API", "API3")
				.addField("Non_Functional_Validation_passed",per_non_func_val_pass)
				.addField("Non_Functional_Validation_failed",per_non_func_val_fail)
				.time(Instant.now(), WritePrecision.NS);
		writeApi.writePoint(bucket1, org, point3);
		//non-func_val_name,status
		Point point4 = Point
				.measurement("Validations")
				.addTag("API", "API3")
				.addField(non_func_val_name.get(0),validations_API3.get("Non_Functional:"+non_func_val_name.get(0)))
				.addField(non_func_val_name.get(1),validations_API3.get("Non_Functional:"+non_func_val_name.get(1)))
				.addField(non_func_val_name.get(2),validations_API3.get("Non_Functional:"+non_func_val_name.get(2)))
				.time(Instant.now(), WritePrecision.NS);


		writeApi.writePoint(bucket1, org, point4);

		Point point5= Point
				.measurement("Percentage_Validation")
				.addTag("API", "API3")
				.addField("Non_Functional_Host_Validation_passed",per_non_func_host_val_pass)
				.addField("Non_Functional_Host_Validation_failed",per_non_func_host_val_fail)
				.time(Instant.now(), WritePrecision.NS);

		writeApi.writePoint(bucket1, org, point5);

		Point point6 = Point
				.measurement("Validations")
				.addTag("API", "API3")
				.addField(non_func_host_val_name.get(0),validations_API3.get("Non_Functional_Host:"+non_func_host_val_name.get(0)))
				.addField(non_func_host_val_name.get(1),validations_API3.get("Non_Functional_Host:"+non_func_host_val_name.get(1)))
				.addField(non_func_host_val_name.get(2),validations_API3.get("Non_Functional_Host:"+non_func_host_val_name.get(2)))
				.addField(non_func_host_val_name.get(3),validations_API3.get("Non_Functional_Host:"+non_func_host_val_name.get(3)))
				.addField(non_func_host_val_name.get(4),validations_API3.get("Non_Functional_Host:"+non_func_host_val_name.get(4)))
				.addField(non_func_host_val_name.get(5),validations_API3.get("Non_Functional_Host:"+non_func_host_val_name.get(5)))
				.time(Instant.now(), WritePrecision.NS);

		writeApi.writePoint(bucket1, org, point6);
		
		return per_validations;
    }
    public double[] write4(LinkedHashMap<String,String> validations_API4)
    {
    	WriteApiBlocking writeApi = client.getWriteApiBlocking();

    	double total_func_val=0,total_func_val_fail=0;
		double total_non_func_val=0,total_non_func_val_fail=0;
		double total_non_func_host_val=0,total_non_func_host_val_fail=0;
		ArrayList<String> func_val_name=new ArrayList<String>();
		ArrayList<String> non_func_val_name=new ArrayList<String>();
		ArrayList<String> non_func_host_val_name=new ArrayList<String>();
		int j=0;int k=1;
		String errors="NA";
		String anamolies="NA";
		long response_time=-1;
		double CPU_util=-1,Memory_util=-1,DB_Query_times=-1;
		int connections=-1,third_party_calls=-1,downstream_calls=-1,statusCode=-1;
		per_validations=new double[3];

    	
		for(Map.Entry i:validations_API4.entrySet())
		{
			String key=(String) i.getKey();
			String value=(String) i.getValue();
			if(key.startsWith("Functional"))
			{
				total_func_val++;
				if(!value.equals("Passed"))
					total_func_val_fail++;
				func_val_name.add(i.getKey().toString().substring(11));
				//System.out.println(j);
				//System.out.println(""+i.getKey().toString().substring(11));
			}
			if(key.contains("Non_Functional:"))
			{
				//System.out.println("in");
				if(value.equals("Passed")||value.equals("Failed"))
				{
					total_non_func_val++;
					if(!value.equals("Passed"))
						total_non_func_val_fail++;
				}
				non_func_val_name.add(i.getKey().toString().substring(15));
				//System.out.println(k);
				//System.out.println(""+i.getKey().toString().substring(15));

			}
			if(key.contains("Non_Functional_Host:"))
			{
				//System.out.println("in");
				total_non_func_host_val++;
				//System.out.println("total_non_func_host_val:"+total_non_func_host_val);
				if(!value.equals("Passed"))
					total_non_func_host_val_fail++;
				//System.out.println("total_non_func_host_val_fail:"+total_non_func_host_val_fail);
				non_func_host_val_name.add(i.getKey().toString().substring(20));
				//System.out.println("name:"+i.getKey().toString().substring(20));
				//System.out.println(k);
				//System.out.println(""+i.getKey().toString().substring(15));
			}

			if(key.equals("third_level_validations:Status Code"))
			{
				statusCode=Integer.parseInt(value);
				//System.out.println("Status Code:"+statusCode);
			}
			if(key.equals("third_level_validations:Errors"))
			{
				if(!value.equals("NA"))
					errors=value;
				//System.out.println("Errors:"+errors);
			}
			if(key.equals("third_level_validations:Anamolies from logs"))
			{
				if(!value.equals("NA"))
					anamolies=value;
				//System.out.println("Anamolies from logs:"+anamolies);
			}
			if(key.equals("third_level_validations:Response Time"))
			{
				if(!value.equals("NA"))
					response_time=Long.parseLong(value);
				//System.out.println("Res time:"+response_time);
			}
			if(key.equals("third_level_validations:CPU Utilization"))
			{
				CPU_util=Double.parseDouble(value);
				//System.out.println("CPU:"+CPU_util);
			}
			if(key.equals("third_level_validations:Memory Utilization"))
			{
				Memory_util=Double.parseDouble(value);
				//System.out.println("Memory:"+Memory_util);
			}
			if(key.equals("third_level_validations:Connections"))
			{
				connections=Integer.parseInt(value);
				//System.out.println("Connections:"+connections);
			}
			if(key.equals("third_level_validations:DB Query time"))
			{
				DB_Query_times=Double.parseDouble(value);
				//System.out.println("DB Query:"+DB_Query_times);
			}
			if(key.equals("third_level_validations:Third Party Calls"))
			{
				third_party_calls=Integer.parseInt(value);
				//System.out.println("3rd party:"+third_party_calls);
			}
			if(key.equals("third_level_validations:Downstream Calls"))
			{
				downstream_calls=Integer.parseInt(value);
				//System.out.println("Downstream calls:"+downstream_calls);
			}
			
		}
		
		Point third_level = Point
				.measurement("third_level_Validation")
				.addTag("API", "API4")
				.addField("Status Code",statusCode)
				.addField("Errors",errors)
				.addField("Anamolies from logs",anamolies)
				.addField("Response Time",response_time)
				.addField("CPU Utilization",CPU_util)
				.addField("Memory Utilization",Memory_util)
				.addField("Connections",connections)
				.addField("DB Query time",DB_Query_times)
				.addField("Third Party Calls",third_party_calls)
				.addField("Downstream Calls",downstream_calls)
				.time(Instant.now(), WritePrecision.NS);
		writeApi.writePoint(bucket1, org, third_level);
		
		//percentage of functional validation passed
		double per_func_val_pass=((total_func_val-total_func_val_fail)/total_func_val)*100;
		//percentage of functional validation failed
		double per_func_val_fail=100-per_func_val_pass;
		//System.out.println("per_func_val_pass:"+per_func_val_pass);

		//percentage of non-functional validation passed
		double per_non_func_val_pass=((total_non_func_val-total_non_func_val_fail)/total_non_func_val)*100;
		//percentage of non-functional validation failed
		double per_non_func_val_fail=100-per_non_func_val_pass;
		//System.out.println("per_non_func_val_pass:"+per_non_func_val_pass);

		//int diff=(total_non_func_host_val-total_non_func_host_val_fail);
		//System.out.println("diff:"+diff);
		//double diff2=diff/total_non_func_host_val;
		//System.out.println("diff2:"+diff2);
		//double diff3=diff2=diff2*100;
		//System.out.println("diff3:"+diff3);
		double per_non_func_host_val_pass=((total_non_func_host_val-total_non_func_host_val_fail)/total_non_func_host_val)*100;
		double per_non_func_host_val_fail=100-per_non_func_host_val_pass;
		//System.out.println("per_non_func_host_val_pass:"+per_non_func_host_val_pass);

		per_validations[0]=per_func_val_pass;
		per_validations[1]=per_non_func_val_pass;
		per_validations[2]=per_non_func_host_val_pass;


		//System.out.println(per_func_val_pass);
		//System.out.println(per_non_func_val_pass);
		//System.out.println("per_non_func_host_val_pass:"+per_non_func_host_val_pass);

		//influxdb point to write percentage of functional validations passed
		Point point1 = Point
				.measurement("Percentage_Validation")
				.addTag("API", "API4")
				.addField("Functional_Validation_passed",per_func_val_pass)
				.addField("Functional_Validation_failed",per_func_val_fail)
				.time(Instant.now(), WritePrecision.NS);
		writeApi.writePoint(bucket1, org, point1);
		//
		//func_val_name,status
		Point point2 = Point
				.measurement("Validations")
				.addTag("API", "API4")
				.addField(func_val_name.get(0),validations_API4.get("Functional:"+func_val_name.get(0)))
				.addField(func_val_name.get(1),validations_API4.get("Functional:"+func_val_name.get(1)))
				.addField(func_val_name.get(2),validations_API4.get("Functional:"+func_val_name.get(2)))
				.addField(func_val_name.get(3),validations_API4.get("Functional:"+func_val_name.get(3)))
				.addField(func_val_name.get(4),validations_API4.get("Functional:"+func_val_name.get(4)))
				.time(Instant.now(), WritePrecision.NS);

		//System.out.println(""+val.get("Functional:"+"func_val_name.get(0)"));
		//System.out.println(""+val.get("Functional:Status Code"));
		//System.out.println(""+"Functional:"+func_val_name.get(0));

		writeApi.writePoint(bucket1, org, point2);

		//influxdb point to write data of non-functional validations
		Point point3 = Point
				.measurement("Percentage_Validation")
				.addTag("API", "API4")
				.addField("Non_Functional_Validation_passed",per_non_func_val_pass)
				.addField("Non_Functional_Validation_failed",per_non_func_val_fail)
				.time(Instant.now(), WritePrecision.NS);
		writeApi.writePoint(bucket1, org, point3);
		//non-func_val_name,status
		Point point4 = Point
				.measurement("Validations")
				.addTag("API", "API4")
				.addField(non_func_val_name.get(0),validations_API4.get("Non_Functional:"+non_func_val_name.get(0)))
				.addField(non_func_val_name.get(1),validations_API4.get("Non_Functional:"+non_func_val_name.get(1)))
				.addField(non_func_val_name.get(2),validations_API4.get("Non_Functional:"+non_func_val_name.get(2)))
				.time(Instant.now(), WritePrecision.NS);


		writeApi.writePoint(bucket1, org, point4);

		Point point5= Point
				.measurement("Percentage_Validation")
				.addTag("API", "API4")
				.addField("Non_Functional_Host_Validation_passed",per_non_func_host_val_pass)
				.addField("Non_Functional_Host_Validation_failed",per_non_func_host_val_fail)
				.time(Instant.now(), WritePrecision.NS);

		writeApi.writePoint(bucket1, org, point5);

		Point point6 = Point
				.measurement("Validations")
				.addTag("API", "API4")
				.addField(non_func_host_val_name.get(0),validations_API4.get("Non_Functional_Host:"+non_func_host_val_name.get(0)))
				.addField(non_func_host_val_name.get(1),validations_API4.get("Non_Functional_Host:"+non_func_host_val_name.get(1)))
				.addField(non_func_host_val_name.get(2),validations_API4.get("Non_Functional_Host:"+non_func_host_val_name.get(2)))
				.addField(non_func_host_val_name.get(3),validations_API4.get("Non_Functional_Host:"+non_func_host_val_name.get(3)))
				.addField(non_func_host_val_name.get(4),validations_API4.get("Non_Functional_Host:"+non_func_host_val_name.get(4)))
				.addField(non_func_host_val_name.get(5),validations_API4.get("Non_Functional_Host:"+non_func_host_val_name.get(5)))
				.time(Instant.now(), WritePrecision.NS);

		writeApi.writePoint(bucket1, org, point6);
		
		return per_validations;
    }
    
    public double[] write5(LinkedHashMap<String,String> validations_API5)
    {
    	WriteApiBlocking writeApi = client.getWriteApiBlocking();

    	double total_func_val=0,total_func_val_fail=0;
		double total_non_func_val=0,total_non_func_val_fail=0;
		double total_non_func_host_val=0,total_non_func_host_val_fail=0;
		ArrayList<String> func_val_name=new ArrayList<String>();
		ArrayList<String> non_func_val_name=new ArrayList<String>();
		ArrayList<String> non_func_host_val_name=new ArrayList<String>();
		int j=0;int k=1;
		String errors="NA";
		String anamolies="NA";
		long response_time=-1;
		double CPU_util=-1,Memory_util=-1,DB_Query_times=-1;
		int connections=-1,third_party_calls=-1,downstream_calls=-1,statusCode=-1;
		per_validations=new double[3];

    	
		for(Map.Entry i:validations_API5.entrySet())
		{
			String key=(String) i.getKey();
			String value=(String) i.getValue();
			if(key.startsWith("Functional"))
			{
				total_func_val++;
				if(!value.equals("Passed"))
					total_func_val_fail++;
				func_val_name.add(i.getKey().toString().substring(11));
				//System.out.println(j);
				//System.out.println(""+i.getKey().toString().substring(11));
			}
			if(key.contains("Non_Functional:"))
			{
				//System.out.println("in");
				if(value.equals("Passed")||value.equals("Failed"))
				{
					total_non_func_val++;
					if(!value.equals("Passed"))
						total_non_func_val_fail++;
				}
				non_func_val_name.add(i.getKey().toString().substring(15));
				//System.out.println(k);
				//System.out.println(""+i.getKey().toString().substring(15));

			}
			if(key.contains("Non_Functional_Host:"))
			{
				//System.out.println("in");
				total_non_func_host_val++;
				//System.out.println("total_non_func_host_val:"+total_non_func_host_val);
				if(!value.equals("Passed"))
					total_non_func_host_val_fail++;
				//System.out.println("total_non_func_host_val_fail:"+total_non_func_host_val_fail);
				non_func_host_val_name.add(i.getKey().toString().substring(20));
				//System.out.println("name:"+i.getKey().toString().substring(20));
				//System.out.println(k);
				//System.out.println(""+i.getKey().toString().substring(15));
			}

			if(key.equals("third_level_validations:Status Code"))
			{
				statusCode=Integer.parseInt(value);
				//System.out.println("Status Code:"+statusCode);
			}
			if(key.equals("third_level_validations:Errors"))
			{
				if(!value.equals("NA"))
					errors=value;
				//System.out.println("Errors:"+errors);
			}
			if(key.equals("third_level_validations:Anamolies from logs"))
			{
				if(!value.equals("NA"))
					anamolies=value;
				//System.out.println("Anamolies from logs:"+anamolies);
			}
			if(key.equals("third_level_validations:Response Time"))
			{
				if(!value.equals("NA"))
					response_time=Long.parseLong(value);
				//System.out.println("Res time:"+response_time);
			}
			if(key.equals("third_level_validations:CPU Utilization"))
			{
				CPU_util=Double.parseDouble(value);
				//System.out.println("CPU:"+CPU_util);
			}
			if(key.equals("third_level_validations:Memory Utilization"))
			{
				Memory_util=Double.parseDouble(value);
				//System.out.println("Memory:"+Memory_util);
			}
			if(key.equals("third_level_validations:Connections"))
			{
				connections=Integer.parseInt(value);
				//System.out.println("Connections:"+connections);
			}
			if(key.equals("third_level_validations:DB Query time"))
			{
				DB_Query_times=Double.parseDouble(value);
				//System.out.println("DB Query:"+DB_Query_times);
			}
			if(key.equals("third_level_validations:Third Party Calls"))
			{
				third_party_calls=Integer.parseInt(value);
				//System.out.println("3rd party:"+third_party_calls);
			}
			if(key.equals("third_level_validations:Downstream Calls"))
			{
				downstream_calls=Integer.parseInt(value);
				//System.out.println("Downstream calls:"+downstream_calls);
			}
			
		}
		
		Point third_level = Point
				.measurement("third_level_Validation")
				.addTag("API", "API5")
				.addField("Status Code",statusCode)
				.addField("Errors",errors)
				.addField("Anamolies from logs",anamolies)
				.addField("Response Time",response_time)
				.addField("CPU Utilization",CPU_util)
				.addField("Memory Utilization",Memory_util)
				.addField("Connections",connections)
				.addField("DB Query time",DB_Query_times)
				.addField("Third Party Calls",third_party_calls)
				.addField("Downstream Calls",downstream_calls)
				.time(Instant.now(), WritePrecision.NS);
		writeApi.writePoint(bucket1, org, third_level);
		
		//percentage of functional validation passed
		double per_func_val_pass=((total_func_val-total_func_val_fail)/total_func_val)*100;
		//percentage of functional validation failed
		double per_func_val_fail=100-per_func_val_pass;
		//System.out.println("per_func_val_pass:"+per_func_val_pass);

		//percentage of non-functional validation passed
		double per_non_func_val_pass=((total_non_func_val-total_non_func_val_fail)/total_non_func_val)*100;
		//percentage of non-functional validation failed
		double per_non_func_val_fail=100-per_non_func_val_pass;
		//System.out.println("per_non_func_val_pass:"+per_non_func_val_pass);

		//int diff=(total_non_func_host_val-total_non_func_host_val_fail);
		//System.out.println("diff:"+diff);
		//double diff2=diff/total_non_func_host_val;
		//System.out.println("diff2:"+diff2);
		//double diff3=diff2=diff2*100;
		//System.out.println("diff3:"+diff3);
		double per_non_func_host_val_pass=((total_non_func_host_val-total_non_func_host_val_fail)/total_non_func_host_val)*100;
		double per_non_func_host_val_fail=100-per_non_func_host_val_pass;
		//System.out.println("per_non_func_host_val_pass:"+per_non_func_host_val_pass);

		per_validations[0]=per_func_val_pass;
		per_validations[1]=per_non_func_val_pass;
		per_validations[2]=per_non_func_host_val_pass;


		//System.out.println(per_func_val_pass);
		//System.out.println(per_non_func_val_pass);
		//System.out.println("per_non_func_host_val_pass:"+per_non_func_host_val_pass);

		//influxdb point to write percentage of functional validations passed
		Point point1 = Point
				.measurement("Percentage_Validation")
				.addTag("API", "API5")
				.addField("Functional_Validation_passed",per_func_val_pass)
				.addField("Functional_Validation_failed",per_func_val_fail)
				.time(Instant.now(), WritePrecision.NS);
		writeApi.writePoint(bucket1, org, point1);
		//
		//func_val_name,status
		Point point2 = Point
				.measurement("Validations")
				.addTag("API", "API5")
				.addField(func_val_name.get(0),validations_API5.get("Functional:"+func_val_name.get(0)))
				.addField(func_val_name.get(1),validations_API5.get("Functional:"+func_val_name.get(1)))
				.addField(func_val_name.get(2),validations_API5.get("Functional:"+func_val_name.get(2)))
				.addField(func_val_name.get(3),validations_API5.get("Functional:"+func_val_name.get(3)))
				.addField(func_val_name.get(4),validations_API5.get("Functional:"+func_val_name.get(4)))
				.time(Instant.now(), WritePrecision.NS);

		//System.out.println(""+val.get("Functional:"+"func_val_name.get(0)"));
		//System.out.println(""+val.get("Functional:Status Code"));
		//System.out.println(""+"Functional:"+func_val_name.get(0));

		writeApi.writePoint(bucket1, org, point2);

		//influxdb point to write data of non-functional validations
		Point point3 = Point
				.measurement("Percentage_Validation")
				.addTag("API", "API5")
				.addField("Non_Functional_Validation_passed",per_non_func_val_pass)
				.addField("Non_Functional_Validation_failed",per_non_func_val_fail)
				.time(Instant.now(), WritePrecision.NS);
		writeApi.writePoint(bucket1, org, point3);
		//non-func_val_name,status
		Point point4 = Point
				.measurement("Validations")
				.addTag("API", "API5")
				.addField(non_func_val_name.get(0),validations_API5.get("Non_Functional:"+non_func_val_name.get(0)))
				.addField(non_func_val_name.get(1),validations_API5.get("Non_Functional:"+non_func_val_name.get(1)))
				.addField(non_func_val_name.get(2),validations_API5.get("Non_Functional:"+non_func_val_name.get(2)))
				.time(Instant.now(), WritePrecision.NS);


		writeApi.writePoint(bucket1, org, point4);

		Point point5= Point
				.measurement("Percentage_Validation")
				.addTag("API", "API5")
				.addField("Non_Functional_Host_Validation_passed",per_non_func_host_val_pass)
				.addField("Non_Functional_Host_Validation_failed",per_non_func_host_val_fail)
				.time(Instant.now(), WritePrecision.NS);

		writeApi.writePoint(bucket1, org, point5);

		Point point6 = Point
				.measurement("Validations")
				.addTag("API", "API5")
				.addField(non_func_host_val_name.get(0),validations_API5.get("Non_Functional_Host:"+non_func_host_val_name.get(0)))
				.addField(non_func_host_val_name.get(1),validations_API5.get("Non_Functional_Host:"+non_func_host_val_name.get(1)))
				.addField(non_func_host_val_name.get(2),validations_API5.get("Non_Functional_Host:"+non_func_host_val_name.get(2)))
				.addField(non_func_host_val_name.get(3),validations_API5.get("Non_Functional_Host:"+non_func_host_val_name.get(3)))
				.addField(non_func_host_val_name.get(4),validations_API5.get("Non_Functional_Host:"+non_func_host_val_name.get(4)))
				.addField(non_func_host_val_name.get(5),validations_API5.get("Non_Functional_Host:"+non_func_host_val_name.get(5)))
				.time(Instant.now(), WritePrecision.NS);

		writeApi.writePoint(bucket1, org, point6);
		
		return per_validations;
    }
    
	
}


