package com.wipro.APIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


import org.json.simple.parser.ParseException;

import InfluxWrite.Writer;

public class ReqCaller {
Writer writer=new Writer();
	
	RequestChecker rc=new RequestChecker();
	//this functions are running the API validations for 5 iterations and returning the results of functional and non-functional validations
	//and calling the writer method which writes data to influxdb
	
	int iterations=5;
	public ArrayList<double[]> callAPI1() throws ParseException 
	{
		ArrayList<double[]> list1=new ArrayList<double[]>();
		for(int i=0;i<iterations;i++)
		{
			LinkedHashMap<String,String> validations_API1=(LinkedHashMap<String, String>) rc.GETSingleUser();
			list1.add(writer.write1(validations_API1));
		}
		return list1;
	}
	
	public ArrayList<double[]> callAPI2() throws ParseException
	{
		ArrayList<double[]> list2=new ArrayList<double[]>();
		for(int i=0;i<iterations;i++)
		{
			LinkedHashMap<String,String> validations_API2=(LinkedHashMap<String, String>) rc.GETUsers();
			list2.add(writer.write2(validations_API2));
		}
		return list2;
	}
	
	public ArrayList<double[]> callAPI3() throws ParseException
	{
		ArrayList<double[]> list3=new ArrayList<double[]>();
		for(int i=0;i<iterations;i++)
		{
			LinkedHashMap<String,String> validations_API3=(LinkedHashMap<String, String>) rc.CreateUser();
			list3.add(writer.write3(validations_API3));
		}
		return list3;
	}
	
	public ArrayList<double[]> callAPI4() throws ParseException
	{
		ArrayList<double[]> list4=new ArrayList<double[]>();
		for(int i=0;i<iterations;i++)
		{
			LinkedHashMap<String,String> validations_API4=(LinkedHashMap<String, String>) rc.Login();
			list4.add(writer.write4(validations_API4));
		}
		return list4;
	}
	
	public ArrayList<double[]> callAPI5() throws ParseException
	{
		ArrayList<double[]> list5=new ArrayList<double[]>();
		for(int i=0;i<iterations;i++)
		{
			LinkedHashMap<String,String> validations_API5=(LinkedHashMap<String, String>) rc.SingleUserError();
			list5.add(writer.write5(validations_API5));
		}
		return list5;
	}
	
}
