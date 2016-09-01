package com.newton.reporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.Reporter;
import org.testng.xml.XmlSuite;

public class CustomReporter implements IReporter {

	private PrintWriter mOut;
	private static CustomReporter instance;
	private StopWatch stopWatch;

	public CustomReporter() {
		new File("./test-output/custom-reports").mkdirs();
		String reportPath = "./test-output/custom-reports/custom-report" + System.currentTimeMillis() + ".html";
		stopWatch = new StopWatch();
		stopWatch.start();
		try {
			File reportFile = new File(reportPath);
			mOut = new PrintWriter(new BufferedWriter(new FileWriter(reportFile)), true);
			Reporter.log("<h1><a href='" + reportFile.getAbsolutePath() + "'>Custom Report</a></h1>");
			instance = this;

		} catch (IOException e) {
			System.out.println("Error in creating writer: " + e);
		}

	}

	public static CustomReporter getInstance() {
		if (instance == null) {
			instance = new CustomReporter();
		}
		return instance;
	}

	public StopWatch getStopWatch() {
		return stopWatch;
	}

	public void log(String s) {
		mOut.print(s);
	}

	// private void print(String text) {
	// System.out.println(text);
	// mOut.println(text + "");
	// }

	public void killReporter() {

		mOut.flush();
		mOut.close();
	}

	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		// TODO Auto-generated method stub

	}

}