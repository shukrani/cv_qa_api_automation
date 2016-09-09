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

import com.newton.utils.ExcelReader;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class GenericTest extends BaseTest {
	String urlParamsJson;
	String requestJson;

	@Test(dataProvider = "PostSearchDataProvider")
	@Parameters("testData")
	public void restAPITest(Map<String, String> testData)
			throws JsonGenerationException, JsonMappingException, IOException, JSONException {
		requestJson = testData.get("RequestData");

		test = extentReporter.startTest(testData.get("TestName"));

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

		switch (testData.get("HTTPMethod").toUpperCase()) {

		case "GET": {
			actualJson = executor.doHttpGet(webResource, ClientResponse.class, test);
			break;
		}
		case "POST": {
			if (requestJson != null && requestJson.length() > 2) {
				actualJson = executor.doHttpPost(webResource, ClientResponse.class,
						new JSONObject(requestJson).toString(), test);
			} else {
				actualJson = executor.doHttpPost(webResource, ClientResponse.class, test);
			}

		}

		}

		executor.verifyJsonEquals(expectedJson, actualJson, "verify search results", false, test);

	}

	@DataProvider(name = "PostSearchDataProvider"/* , parallel = true */)
	public Object[][] postSearchData() {
		return new ExcelReader().getUserDataFromExcel("testData.xlsx", "summary");
	}

}
