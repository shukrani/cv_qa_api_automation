package com.newton.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

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
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.google.gson.JsonObject;
import com.sun.jersey.api.client.WebResource;

public class Util {
	private static Util instance;
	String jira_attachment_baseURL = "";
	String jira_attachment_authentication = "";
	String reportPath;

	private Util() {
		reportPath = getExtentReportPath();
	}

	public static Util getInstance() {
		if (instance == null) {
			instance = new Util();
		}
		return instance;
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

	public WebResource setUrlParams(WebResource webResource, String urlParamsJson) {
		try {
			JSONObject params = new JSONObject(urlParamsJson);
			Iterator<JsonObject> it = params.keys();
			while (it.hasNext()) {
				String key = it.next() + "";
				webResource = webResource.queryParam(key, params.get(key) + "");
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return webResource;
		}

	}

	private String getExtentReportPath() {

		new File("./test-output/extent-reports").mkdirs();
		String reportPath = "./test-output/extent-reports/extent-report" + System.currentTimeMillis() + ".html";
		return reportPath;
	}

	public String getReportPath() {
		return reportPath;
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
