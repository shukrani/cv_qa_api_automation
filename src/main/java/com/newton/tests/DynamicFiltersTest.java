package com.newton.tests;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.newton.utils.Config;
import com.newton.utils.ExcelReader;
import com.relevantcodes.extentreports.ExtentTest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class DynamicFiltersTest extends BaseTest {
	String urlParamsJson;
	String requestJson;

	@Test(dataProvider = "PostSearchDataProvider")
	@Parameters("testData")
	public void categoryDynamicFiltersTest(Map<String, String> testData)
			throws JsonGenerationException, JsonMappingException, IOException, JSONException {
		Config.vars = testData;
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
		ExtentTest test = extentReporter.startTest(testData.get("TestName"));

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

		// executor.verifyJsonEquals(expectedJson, actualJson, "verify search
		// results", false, test);

		JSONObject response = new JSONObject(actualJson);
		// verify success message
		String successMessage = response.getString("success");
		executor.verifyEquals("1", successMessage, "verify success message", test);
		// get displayFilters
		JSONArray displayFilters = response.getJSONObject("data").getJSONArray("displayFilters");
		JSONArray filters = new JSONArray();
		Set<String> filterSet = new HashSet<>();
		for (int i = 0; i < displayFilters.length(); i++) {
			response = new JSONObject(actualJson);
			// verify success message
			successMessage = response.getString("success");
			executor.verifyEquals("1", successMessage, "verify success message", test);
			// get displayFilters
			displayFilters = response.getJSONObject("data").getJSONArray("displayFilters");

			JSONObject displayFilter = displayFilters.getJSONObject(i);
			JSONObject filter = new JSONObject();
			String filterName = displayFilter.getString("esFilterName");
			executor.verifyEquals(true, filterSet.add(filterName), "check for duplicate filters" + filterName, test);
			JSONArray filterValue = new JSONArray();
			int expectedCount = 0;
			JSONArray filtersData = displayFilter.getJSONArray("filtersData");
			for (int j = 0; j < filtersData.length(); j++) {
				JSONObject filterData = filtersData.getJSONObject(j);
				String filterName2 = filterData.getString("filterName");
				executor.verifyEquals(true, filterSet.add(filterName2), "check for duplicate filters" + filterName2,
						test);
				filterValue.put(filterName2);
				filter.put(filterName, filterValue);
				filters.put(i, filter);

				// update request json
				JSONObject requestJSon = new JSONObject(requestJson);
				requestJSon.put("filters", filters);
				// get expected count
				int productCount = filterData.getInt("prodCount");
				executor.verifyEquals(true, productCount > 0, "verify product count is greater than zero", test);
				expectedCount += productCount;

				this.test.appendChild(test);
				test = extentReporter.startTest(testData.get("TestName") + i + " " + j);
				// make request with updated filters
				actualJson = executor.doHttpPost(webResource, ClientResponse.class, requestJSon.toString(), test);

				// verify count
				int actualCount = new JSONObject(actualJson).getJSONObject("data").getInt("totalProducts");
				JSONArray products = new JSONObject(actualJson).getJSONObject("data").getJSONArray("data");
				if (filterValue.length() == 1) {
					executor.verifyEquals(expectedCount, actualCount,
							"Verify Total Products Count : " + actualCount + " is equal to :" + expectedCount, test);
				} else {
					executor.verifyEquals(true, (actualCount <= expectedCount), "Verify Total Products Count : "
							+ actualCount + " is less than or equal to  : " + expectedCount, test);
				}
				// verify price range, sorting, discount percentage
				for (int k = 0; k < products.length(); k++) {
					JSONObject product = products.getJSONObject(k);
					int price = (int) Float.parseFloat(product.getString("discounted_price"));
					int discount_percentage = (int) product.getDouble("discount_percentage");
					executor.verifyEquals(true, discount_percentage >= 60, "verify discount percentage >= 60", test);
					executor.verifyEquals(true, (price >= 1000 && price <= 2000),
							"verify price : " + price + " in range 1000-2000", test);
					if (k != 0) {
						int pre_price = (int) Float
								.parseFloat(products.getJSONObject(k - 1).getString("discounted_price"));
						executor.verifyEquals(true, pre_price >= price,
								"verify sorting in desc previous price :" + pre_price + " price :" + price, test);
					}

				}
				// this.test.appendChild(test);

			}

		}

		// get filtersData

	}

	@Test(dataProvider = "feedDataProvider")
	@Parameters("testData")
	public void feedDynamicFiltersTest(Map<String, String> testData)
			throws JsonGenerationException, JsonMappingException, IOException, JSONException {
		Config.vars = testData;
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
		ExtentTest test = extentReporter.startTest(testData.get("TestName"));

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

		// executor.verifyJsonEquals(expectedJson, actualJson, "verify search
		// results", false, test);

		JSONObject response = new JSONObject(actualJson);
		// verify success message
		String successMessage = response.getString("success");
		executor.verifyEquals("1", successMessage, "verify success message", test);
		// get displayFilters
		JSONArray displayFilters = response.getJSONObject("data").getJSONArray("displayFilters");
		JSONArray filters = new JSONArray();
		Set<String> filterSet = new HashSet<>();
		for (int i = 1; i < displayFilters.length(); i++) {
			response = new JSONObject(actualJson);
			// verify success message
			successMessage = response.getString("success");
			executor.verifyEquals("1", successMessage, "verify success message", test);
			// get displayFilters
			displayFilters = response.getJSONObject("data").getJSONArray("displayFilters");

			if (displayFilters.length() <= i)
				continue;
			JSONObject displayFilter = displayFilters.getJSONObject(i);
			JSONObject filter = new JSONObject();
			String filterName = displayFilter.getString("esFilterName");
			executor.verifyEquals(true, filterSet.add(filterName), "check for duplicate filters " + filterName, test);
			JSONArray filterValue = new JSONArray();
			int expectedCount = 0;
			JSONArray filtersData = displayFilter.getJSONArray("filtersData");
			for (int j = 0; j < filtersData.length(); j++) {
				JSONObject filterData = filtersData.getJSONObject(j);
				String filterName2 = filterData.getString("filterName");
				executor.verifyEquals(true, filterSet.add(filterName2), "check for duplicate filters " + filterName2,
						test);
				filterValue.put(filterName2);
				filter.put(filterName, filterValue);
				JSONArray priceRange = new JSONArray();
				JSONObject minMaxPrice = new JSONObject();
				minMaxPrice.put("min", 1000);
				minMaxPrice.put("max", 2000);
				priceRange.put(minMaxPrice);
				filter.put("price", priceRange);

				// discount range
				JSONArray discountRange = new JSONArray();
				JSONObject minMaxDiscount = new JSONObject();

				minMaxDiscount.put("min", 30);
				minMaxDiscount.put("max", 90);
				discountRange.put(minMaxDiscount);
				filter.put("discount_percentage", discountRange);
				filters.put(i - 1, filter);

				// update request json
				JSONObject requestJSon = new JSONObject(requestJson);
				requestJSon.put("filters", filters);
				// get expected count
				int productCount = filterData.getInt("prodCount");
				executor.verifyEquals(true, productCount > 0,
						"verify product count is greater than zero : " + productCount, test);
				expectedCount += productCount;
				this.test.appendChild(test);

				test = extentReporter.startTest(testData.get("TestName") + i + " " + j);

				// make request with updated filters
				actualJson = executor.doHttpPost(webResource, ClientResponse.class, requestJSon.toString(), test);

				// verify count
				int actualCount = new JSONObject(actualJson).getJSONObject("data").getInt("totalProducts");
				JSONArray products = new JSONObject(actualJson).getJSONObject("data").getJSONArray("products");
				if (filterValue.length() == 2) {
					executor.verifyEquals(expectedCount, actualCount,
							"Verify Total Products Count : " + actualCount + " is equal to :" + expectedCount, test);
				} else {
					executor.verifyEquals(true, (actualCount <= expectedCount), "Verify Total Products Count : "
							+ actualCount + " is less than or equal to  : " + expectedCount, test);
				}
				// verify price range, sorting, discount percentage
				for (int k = 0; k < products.length(); k++) {
					JSONObject product = products.getJSONObject(k);
					int price = (int) Float.parseFloat(product.getString("discounted_price"));
					int discount_percentage = (int) product.getDouble("discount_percentage");
					executor.verifyEquals(true, discount_percentage >= 30 && discount_percentage <= 90,
							"verify discount percentage " + discount_percentage + " >= 30 and less than or equal to 90",
							test);
					executor.verifyEquals(true, (price >= 1000 && price <= 2000),
							"verify price : " + price + " in range 1000-2000", test);
					if (k != 0) {
						int pre_price = (int) Float
								.parseFloat(products.getJSONObject(k - 1).getString("discounted_price"));
						executor.verifyEquals(true, pre_price >= price,
								"verify sorting in desc previous price :" + pre_price + " price :" + price, test);
					}

				}

			}

		}

		// get filtersData

	}

	@DataProvider(name = "PostSearchDataProvider"/* , parallel = true */)
	public Object[][] postSearchData() {
		return new ExcelReader().getUserDataFromExcel("testData.xlsx", "dynamic_filters");
	}

	@DataProvider(name = "feedDataProvider"/* , parallel = true */)
	public Object[][] feedDynamicFitlersData() {
		return new ExcelReader().getUserDataFromExcel("testData.xlsx", "feed_dynamic_filters");
	}

}
