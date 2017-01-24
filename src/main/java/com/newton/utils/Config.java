package com.newton.utils;

import java.util.HashMap;
import java.util.Map;

public class Config {
	public static String baseURL = "http://172.30.17.108:9000/v1/";
	// public static String baseURL =
	// "http://ec2-52-221-239-89.ap-southeast-1.compute.amazonaws.com:5000/apidocs/index.html/";
	public static int versionCode = 23;
	public static String authToken = "";
	public static String sessionToken = "";
	public static Map<String,String> vars=new HashMap<String,String>();

}
