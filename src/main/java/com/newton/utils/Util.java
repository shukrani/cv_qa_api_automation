package com.newton.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Util {
	private static Util instance;
	String jira_attachment_baseURL = "";
	String jira_attachment_authentication = "";

	private Util() {

	}

	public static Util getInstance() {
		if (instance == null) {
			instance = new Util();
		}
		return instance;
	}

	public String takeScreenshot(WebDriver driver, String step) {
		try {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			String filename = "./screenshots/screenshot" + System.currentTimeMillis() + ".png";
			File file = new File(filename);
			FileUtils.copyFile(scrFile, file);
			// Reporter.log("<br> Screenshot for " + step + " captured as <a
			// href='" + file.getAbsolutePath()
			// + "' target='_blank'> screenshot </a> <br>", true);
			return "Screenshot for " + step + " is :" + file.getName() + "\n";
		} catch (Exception e) {
			// Reporter.log("Exception in taking screenshot");
		}
		return null;
	}

	public String takeScreenshot(WebDriver driver) {
		try {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			// Now you can do whatever you need to do with it, for example copy
			// somewhere
			String filename = "./screenshots/screenshot" + System.currentTimeMillis() + ".png";
			File file = new File(filename);
			FileUtils.copyFile(scrFile, file);
			// Reporter.log("Screenshot captured as <a href='" +
			// file.getAbsolutePath()
			// + "' target='_blank'> screenshot </a> <br>");

			return file.getAbsolutePath();
		} catch (Exception e) {
			// Reporter.log("Exception in taking screenshot");
		}
		return "NA";
	}

	public boolean addAttachmentToIssue(String issueKey, String fullfilename) throws IOException {

		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpPost httppost = new HttpPost(jira_attachment_baseURL + "/api/latest/issue/" + issueKey + "/attachments");
		httppost.setHeader("X-Atlassian-Token", "nocheck");
		httppost.setHeader("Authorization", "Basic " + jira_attachment_authentication);

		File fileToUpload = new File(fullfilename);
		FileBody fileBody = new FileBody(fileToUpload);

		HttpEntity entity = MultipartEntityBuilder.create().addPart("file", fileBody).build();

		httppost.setEntity(entity);
		String mess = "executing request " + httppost.getRequestLine();
		// logger.info(mess);

		CloseableHttpResponse response;

		try {
			response = httpclient.execute(httppost);
		} finally {
			httpclient.close();
		}

		if (response.getStatusLine().getStatusCode() == 200)
			return true;
		else
			return false;

	}

	public boolean getAttachmentFromIssue(String contentURI, String fullfilename) throws IOException {

		CloseableHttpClient httpclient = HttpClients.createDefault();

		try {
			HttpGet httpget = new HttpGet(contentURI);
			httpget.setHeader("Authorization", "Basic " + jira_attachment_authentication);

			System.out.println("executing request " + httpget.getURI());

			CloseableHttpResponse response = httpclient.execute(httpget);

			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = response.getEntity();
				if (entity.isStreaming()) {
					byte data[] = EntityUtils.toByteArray(entity);
					FileOutputStream fout = new FileOutputStream(new File(fullfilename));
					fout.write(data);
					fout.close();
				}
			}
		} finally {
			httpclient.close();
		}

		return true;
	}

	// save http request response
	public String saveResponse(String response) throws IOException {
		FileWriter fw = null;
		try {

			String filename = "./responses/response" + System.currentTimeMillis() + ".json";
			File file = new File(filename);

			fw = new FileWriter(file);
			fw.write(response);

			return file.getAbsolutePath();
		} catch (Exception e) {
			// Reporter.log("Exception in taking screenshot");
		} finally {
			fw.close();
		}
		return "NA";
	}

	// readfile as string

	public String readFileAsString(String filePath) {
		String data = "";
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource(filePath).getFile());

			data = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return data;
		}

	}
	
	public void setUrlParams(){
		
	}

	// public void setProxy(String proxyurl) {
	// ClientConfig config = new ClientConfig();
	// config.connectorProvider(new ApacheConnectorProvider());
	// config.property(ClientProperties.PROXY_URI, proxy);
	// config.property(ClientProperties.PROXY_USERNAME, user);
	// config.property(ClientProperties.PROXY_PASSWORD, pass);
	// Client client = JerseyClientBuilder.newClient(config);
	//
	// }

}
