package com.newton.tests;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.newton.utils.Config;
import com.newton.utils.ExcelReader;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

public class FlaskAndroidTest extends BaseTest {
	String urlParamsJson;
	String requestJson;

	@Test(dataProvider = "AndroidDataProvider")
	@Parameters("testData")
	public void androidAPITest(Map<String, String> testData)
			throws JsonGenerationException, JsonMappingException, IOException, JSONException {
		Config.vars = testData;

		test = extentReporter.startTest(testData.get("TestName"));
		getSession(testData);
		if (testData.get("LoginRequired").toLowerCase().equals("yes")) {
			getAuthToken(testData);
		}
		requestJson = testData.get("RequestData");
		if (requestJson != null && requestJson.length() > 2) {
			requestJson = util.readFileAsString("requests/" + requestJson + ".json");
		}
		String expectedJson = util.readFileAsString("responses/" + testData.get("ExpectedResponse") + ".json");
		urlParams = testData.get("URLParams");
		if (urlParams != null && urlParams.length() > 2) {
			urlParamsJson = util.readFileAsString("params/" + urlParams + ".json");
		} else {
			urlParamsJson = "{}";
		}
		String actualJson = "";

		WebResource webResource = client.resource(baseURL + testData.get("APIEndPoint"));

		webResource = util.setUrlParams(webResource, urlParamsJson);
		Builder builder = util.setHeaders(webResource, testData);

		switch (testData.get("HTTPMethod").toUpperCase()) {

		case "GET": {
			actualJson = executor.doHttpGet(builder, ClientResponse.class, test);
			break;
		}
		case "POST": {
			if (requestJson != null && requestJson.length() > 2) {
				actualJson = executor.doHttpPost(builder, ClientResponse.class, new JSONObject(requestJson).toString(),
						test);
			} else {
				actualJson = executor.doHttpPost(builder, ClientResponse.class, test);
			}

		}

		}

		executor.verifyJsonEquals(expectedJson, actualJson, "verify search results", false, test);

	}

	public void getSession(Map<String, String> testData)
			throws JsonGenerationException, JsonMappingException, IOException, JSONException {

		// test = extentReporter.startTest(testData.get("TestName"));
		requestJson = "{\"deviceId\": \"" + Config.deviceID + "\"}";

		String actualJson = "";

		WebResource webResource = client.resource(baseURL + "1/public/users/getSession");

		Builder builder = webResource.header("X-VERSION-CODE", Config.versionCode);

		actualJson = executor.doHttpPost(builder, ClientResponse.class, new JSONObject(requestJson).toString(), test);
		JSONObject json = new JSONObject(actualJson);
		Config.sessionToken = json.getJSONObject("d").getString("sessionId");

	}

	public void getAuthToken(Map<String, String> testData)
			throws JsonGenerationException, JsonMappingException, IOException, JSONException {
		requestJson = util.readFileAsString("requests/android/customerLogin.json");
		JSONObject requestJsonObject = new JSONObject(requestJson);
		requestJsonObject.put("emailId", testData.get("Email"));
		requestJsonObject.put("password", util.decryptPassword(testData.get("Password")));
		String actualJson = "";

		WebResource webResource = client.resource(baseURL + "1/public/users/customerLogin");

		Builder builder = webResource.header("X-VERSION-CODE", Config.versionCode);
		builder = builder.header("X-Session", Config.sessionToken);

		actualJson = executor.doHttpPost(builder, ClientResponse.class, requestJsonObject.toString(), test);
		JSONObject json = new JSONObject(actualJson);
		Config.authToken = json.getJSONObject("d").getString("token");

	}

	@DataProvider(name = "AndroidDataProvider"/* , parallel = true */)
	public Object[][] postSearchData() {
		return new ExcelReader().getUserDataFromExcel("testData.xlsx", "android");
	}

}
