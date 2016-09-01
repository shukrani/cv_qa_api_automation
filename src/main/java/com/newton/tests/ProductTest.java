package com.newton.tests;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.newton.responses.ProductResponse;
import com.newton.utils.ExcelReader;
import com.newton.utils.NoResults;
import com.newton.utils.Search;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ProductTest extends BaseTest {
	@Test(dataProvider = "SearchDataProvider")
	@Parameters("testData")
	public void testGetProductDetails(Map<String, String> testData)
			throws JsonParseException, JsonMappingException, IOException {

		WebResource webResource = client
				.resource(baseURL + "product?productId=" + testData.get("ProductID") + "&with_variant="
						+ testData.get("variantFlag") + "&categoryRequired=" + testData.get("categoryRequired"));

		Map<String, String> response = executor.httpGet(webResource, ClientResponse.class);

		ProductResponse productResponse = jsonParser.getMapper().readValue(response.get("responseData"),
				ProductResponse.class);
		executor.verifyEquals(response.get("status"), "200", "verify status code", false);
		executor.verifyEquals(productResponse.getSuccess(), testData.get("success"), "verify success code", false);
		executor.verifyEquals(productResponse.getMessage(), testData.get("message").trim(), "verify message", false);
		// verify vendor owner
		executor.verifyEquals(productResponse.getData().getProduct().getVendor_owner(),
				testData.get("vendor_owner").trim(), "verify vendor owner", false);

	}

	@DataProvider(name = "SearchDataProvider")
	public Object[][] getSearchData() {
		return new ExcelReader().getUserDataFromExcel("testData.xlsx", "searchData");
	}

}
