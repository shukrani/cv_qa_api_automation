package com.newton.tests;

import java.lang.reflect.Method;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;

import com.newton.executor.Executioner;
import com.newton.reporter.CustomReporter;
import com.newton.utils.Config;
import com.newton.utils.JsonParser;
import com.newton.utils.Util;
import com.sun.jersey.api.client.Client;

/**
 * 
 * @author chhagan
 *
 */
public class BaseTest {
	Client client;
	Executioner executor = new Executioner();
	String baseURL = Config.baseURL;
	CustomReporter reporter;
	JsonParser jsonParser;
	String startReport = "<!DOCTYPE html> <html> <head> <style> table, th, td { border: 1px solid black; border-collapse: collapse; } th, td { padding: 5px; text-align: left; } </style> </head> <body> ";
	String startTable = "<table style='width:100%'> <caption><h3>TESTCASE_NAME</h3></caption> <tr> <th>Start Time</th> <th>Duration</th> <th>Step Description</th> <th>Status</th> <th>Response</th> </tr>";
	String endReport = "</body> </html>";
	String endTable = "</table>";
	String requestData;
	Util util;
	String urlParams;

	@BeforeSuite
	public void beforeSuite() {

		reporter = CustomReporter.getInstance();
		startReport();
		jsonParser = new JsonParser();
		util = Util.getInstance();

	}

	@BeforeMethod
	public void setup(@Optional Method method) {

		client = Client.create();
	}

	@AfterMethod
	public void clean() {
		endTable();

	}

	@AfterSuite
	public void afterSuite() {
		endReport();
	}

	private void endReport() {

		reporter.log(endReport);
		reporter.killReporter();
	}

	private void startReport() {

		reporter.log(startReport);
	}

	protected void startTable(String testName) {

		reporter.log(startTable.replace("TESTCASE_NAME", testName));
	}

	protected void endTable() {
		reporter.log(endTable);
	}

}
