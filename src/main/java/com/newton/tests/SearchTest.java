package com.newton.tests;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.newton.utils.ExcelReader;
import com.newton.utils.NoResults;
import com.newton.utils.Search;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class SearchTest extends BaseTest {
	@Test(dataProvider = "PostSearchDataProvider")
	@Parameters("testData")
	public void testSearchProductPost(Map<String, String> testData)
			throws JsonGenerationException, JsonMappingException, IOException, JSONException {

		WebResource webResource = client.resource(baseURL + "v1/getSearch");
		Search search = new Search();
		search.setSearch(testData.get("SearchText"));
		requestData = jsonParser.getJson(search);

		Map<String, String> response = executor.httpPost(webResource, ClientResponse.class, requestData);

		NoResults noResults = jsonParser.getMapper().readValue(response.get("responseData"), NoResults.class);
		executor.verifyEquals(noResults.getMessage(), testData.get("message"), "verify message");

	}

	@DataProvider(name = "PostSearchDataProvider")
	public Object[][] postSearchData() {
		return new ExcelReader().getUserDataFromExcel("testData.xlsx", "postSearchData");
	}

}
