package com.newton.tests;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.newton.utils.ExcelReader;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class GenericTest extends BaseTest {
	@Test(dataProvider = "PostSearchDataProvider")
	@Parameters("testData")
	public void testSearchProductPost(Map<String, String> testData)
			throws JsonGenerationException, JsonMappingException, IOException, JSONException {

		String requestJson = util.readFileAsString("requests/" + testData.get("RequestData") + ".json");
		String expectedJson = util.readFileAsString("responses/" + testData.get("ExpectedResponse") + ".json");
		String urlParamsJson = util.readFileAsString("params/" + testData.get("URLParams") + ".json");
		String actualJson = "";

		WebResource webResource = client.resource(baseURL + testData.get("APIEndPoint"));

		switch (testData.get("HTTPMethod").toUpperCase()) {

		case "GET": {
			actualJson = executor.doHttpGet(webResource, ClientResponse.class);
			break;
		}
		case "POST":
			actualJson = executor.doHttpPost(webResource, ClientResponse.class, new JSONObject(requestJson).toString());

		}

		// JSONAssert.assertEquals(expectedJson, actualJson, false);
		executor.verifyJsonEquals(expectedJson, actualJson, "verify search results", false);

	}

	@DataProvider(name = "PostSearchDataProvider")
	public Object[][] postSearchData() {
		return new ExcelReader().getUserDataFromExcel("testData.xlsx", "summary");
	}

}
