package com.newton.executor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.Assert;
import org.testng.Reporter;

import com.newton.reporter.CustomReporter;
import com.newton.utils.Config;
import com.newton.utils.ExcelReader;
import com.newton.utils.Util;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import io.appium.java_client.android.AndroidDriver;

public class Executioner {
	private WebDriver driver;
	private Util util;
	private WebDriverWait wait;
	private long startTime;
	private long duration;
	public StopWatch stopWatch;
	public CustomReporter reporter;

	public Executioner(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
		util = Util.getInstance();
		reporter = CustomReporter.getInstance();
		stopWatch = reporter.getStopWatch();

	}

	public Executioner() {
		util = Util.getInstance();
		reporter = CustomReporter.getInstance();
		stopWatch = reporter.getStopWatch();
	}

	public CustomReporter getReporter() {
		return reporter;
	}

	private void addStep(long start, long duration, String step, String status, String screenShot) {

		if (screenShot.length() > 4) {
			reporter.log("<tr> <td>" + start + "</td> <td>" + duration + "</td> <td>" + step + " </td> <td>" + status
					+ "</td> <td><a href='" + screenShot + "' > view </a></td></tr>");
		} else {
			reporter.log("<tr> <td>" + start + "</td> <td>" + duration + "</td> <td>" + step + " </td> <td>" + status
					+ "</td> <td>" + screenShot + "</td></tr>");
		}
	}

	public WebDriver openBrowser(String host, String port, DesiredCapabilities capabilities) {
		try {

			startTime = stopWatch.getTime();
			WebDriver driver = null;
			if (capabilities.getCapability("deviceName") != null
					&& capabilities.getCapability("deviceName").toString().length() >= 5) {
				driver = new AndroidDriver(new URL("http://" + host + ":4723/wd/hub"), capabilities);

			} else {
				driver = new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), capabilities);
			}
			duration = stopWatch.getTime() - startTime;
			addStep(startTime, duration, "Open browser", "Pass", "NA");

			return driver;
		} catch (MalformedURLException e) {
			addStep(startTime, stopWatch.getTime() - startTime, "Open browser", "Failed", util.takeScreenshot(driver));
			Assert.fail("Unable to start selenium session make sure Grid hub is running on url :" + "http://" + host
					+ ":" + port + "/wd/hub");
			return null;

		}

	}

	public void closeBrowser() {
		try {

			startTime = stopWatch.getTime();
			driver.quit();
			duration = stopWatch.getTime() - startTime;
			addStep(startTime, duration, "Close browser", "Pass", "NA");

		} catch (Exception e) {
			addStep(startTime, stopWatch.getTime() - startTime, "Close browser", "Failed", "NA");
			Assert.fail("Unable to close browser. it may have died");

		}

	}

	public Executioner navigateToURL(String url) {
		startTime = stopWatch.getTime();
		driver.navigate().to(url);
		addStep(startTime, stopWatch.getTime() - startTime, "Open URL " + url, "Pass", util.takeScreenshot(driver));
		driver.manage().window().maximize();
		return this;

	}

	public Executioner click(WebElement e, String elementName, boolean takeScreenshot) {

		try {
			startTime = stopWatch.getTime();
			e.click();
			if (takeScreenshot) {
				addStep(startTime, stopWatch.getTime() - startTime, "Click on " + elementName, "Pass",
						util.takeScreenshot(driver));
			} else {
				addStep(startTime, stopWatch.getTime() - startTime, "Click on " + elementName, "Pass", "NA");
			}
			return this;
		} catch (Exception exception) {
			if (takeScreenshot) {
				addStep(startTime, stopWatch.getTime() - startTime, "Click on " + elementName, "Failed",
						util.takeScreenshot(driver));
			} else {
				addStep(startTime, stopWatch.getTime() - startTime, "Click on " + elementName, "Failed", "NA");
			}
			Assert.fail("unable to click on " + elementName);
			return this;

		}

	}

	public Executioner click(WebElement e, String elementName) {

		try {
			startTime = stopWatch.getTime();
			e.click();
			addStep(startTime, stopWatch.getTime() - startTime, "Click on " + elementName, "Pass",
					util.takeScreenshot(driver));
			return this;
		} catch (Exception exception) {
			addStep(startTime, stopWatch.getTime() - startTime, "Click on " + elementName, "Failed",
					util.takeScreenshot(driver));
			Assert.fail("unable to click on " + elementName + " exception :" + exception.getMessage());
			return this;

		}

	}

	public Executioner sendKeys(WebElement e, String text, String elementName) {

		try {
			startTime = stopWatch.getTime();
			e.sendKeys(text);
			addStep(startTime, stopWatch.getTime() - startTime,
					"enter text : " + text + " in text field : " + elementName, "Pass", util.takeScreenshot(driver));
			return this;
		} catch (Exception exception) {
			addStep(startTime, stopWatch.getTime() - startTime,
					"enter text : " + text + " in text field : " + elementName, "Failed", util.takeScreenshot(driver));
			Assert.fail("unable to enter text : " + text + " in text field : " + elementName + " exception: "
					+ exception.getMessage());
			return this;

		}

	}

	public Executioner sendKeys(WebElement e, Keys text, String elementName) {

		try {
			startTime = stopWatch.getTime();
			e.sendKeys(text);
			addStep(startTime, stopWatch.getTime() - startTime,
					"enter text : " + text + " in text field : " + elementName, "Pass", util.takeScreenshot(driver));
			return this;
		} catch (Exception exception) {
			addStep(startTime, stopWatch.getTime() - startTime,
					"enter text : " + text + " in text field : " + elementName, "Failed", util.takeScreenshot(driver));
			Assert.fail("unable to enter text : " + text + " in text field : " + elementName + " exception: "
					+ exception.getMessage());
			return this;

		}

	}

	public Executioner clear(WebElement e, String elementName) {

		try {
			startTime = stopWatch.getTime();
			e.clear();
			addStep(startTime, stopWatch.getTime() - startTime, "clear text of element :" + elementName, "Pass",
					util.takeScreenshot(driver));
			return this;
		} catch (Exception exception) {
			addStep(startTime, stopWatch.getTime() - startTime, "clear text of element :" + elementName, "Failed",
					util.takeScreenshot(driver));
			Assert.fail("unable to clear text of element : " + elementName + " exception " + exception.getMessage());
			return this;

		}
	}

	public void assertTrue(boolean flag, String step) {

		try {
			startTime = stopWatch.getTime();
			Assert.assertTrue(flag);
			addStep(startTime, stopWatch.getTime() - startTime, step, "Pass", util.takeScreenshot(driver));

		} catch (Exception exception) {
			addStep(startTime, stopWatch.getTime() - startTime, step, "Failed", util.takeScreenshot(driver));
			Assert.fail("expected true found :" + flag);

			// throw exception;

		}
	}

	public void assertEquals(Object actual, Object expected, String message) {

		try {
			startTime = stopWatch.getTime();
			Assert.assertEquals(actual, expected, message);
			addStep(startTime, stopWatch.getTime() - startTime,
					message + " actual : " + actual + " expected :" + expected, "Pass", util.takeScreenshot(driver));
		} catch (Exception exception) {
			addStep(startTime, stopWatch.getTime() - startTime, message, "Failed", util.takeScreenshot(driver));
			Assert.fail("Assertion failed " + actual + " is not equals " + expected);
			// throw exception;

		}
	}

	public Executioner verifyTrue(boolean flag, String step) {

		try {
			startTime = stopWatch.getTime();
			Assert.assertTrue(flag);
			addStep(startTime, stopWatch.getTime() - startTime, step, "Pass", util.takeScreenshot(driver));
			return this;
		} catch (AssertionError exception) {
			addStep(startTime, stopWatch.getTime() - startTime, step, "Failed", util.takeScreenshot(driver));
			// Assert.fail("expected true found :" + flag);
			return this;

		}
	}

	public Executioner verifyEquals(Object actual, Object expected, String message) {

		try {
			startTime = stopWatch.getTime();
			Assert.assertEquals(actual, expected, message);
			addStep(startTime, stopWatch.getTime() - startTime,
					message + " actual : " + actual + " expected : " + expected, "Pass", util.takeScreenshot(driver));
			return this;
		} catch (Exception exception) {
			addStep(startTime, stopWatch.getTime() - startTime, message, "Failed", util.takeScreenshot(driver));
			// Assert.fail("verification failed " + actual + " is not equals " +
			// expected);
			return this;

		}
	}

	public Executioner softWaitForCondition(ExpectedCondition<Boolean> expectedCondition, String step) {
		try {
			startTime = stopWatch.getTime();
			wait.until(expectedCondition);
			addStep(startTime, stopWatch.getTime() - startTime, step, "Pass", util.takeScreenshot(driver));
			return this;
		} catch (Exception e) {
			Reporter.log("wait timeout for condition");
			return this;

		}

	}

	public Executioner softWaitForCondition(ExpectedCondition<Boolean> expectedCondition) {
		try {
			startTime = stopWatch.getTime();
			wait.until(expectedCondition);
			// addStep(startTime, stopWatch.getTime() - startTime, "Wait for
			// condition", "Pass",
			// util.takeScreenshot(driver));
			return this;
		} catch (Exception e) {
			Reporter.log("wait timeout for condition", 0, false);
			return this;

		}

	}

	public Executioner softWaitForWebElement(ExpectedCondition<WebElement> visibilityOf, String step) {
		try {
			startTime = stopWatch.getTime();

			wait.until(visibilityOf);
			addStep(startTime, stopWatch.getTime() - startTime, step, "Pass", util.takeScreenshot(driver));
			return this;
		} catch (Exception e) {
			Reporter.log("wait timeout for web element", 0, false);
			return this;

		}

	}

	public WebElement softWaitForWebElement(ExpectedCondition<WebElement> visibilityOf) {
		try {
			startTime = stopWatch.getTime();

			WebElement e = wait.until(visibilityOf);
			addStep(startTime, stopWatch.getTime() - startTime, "wait for element", "Pass",
					util.takeScreenshot(driver));
			return e;
		} catch (Exception e) {
			Reporter.log("wait timeout for web element", 0, false);
			return null;

		}

	}

	public String getTitle() {

		try {
			startTime = stopWatch.getTime();
			String title = driver.getTitle();
			addStep(startTime, stopWatch.getTime() - startTime, "get title", "Pass", util.takeScreenshot(driver));
			return title;
		} catch (Exception exception) {
			addStep(startTime, stopWatch.getTime() - startTime, "get title", "Failed", util.takeScreenshot(driver));
			Assert.fail("uanble to get page title");
			return null;

		}
	}

	public void softWaitForWebElement(WebElement element) {
		try {
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (Exception e) {
			Reporter.log("wait timeout for web element", 0, false);
			// System.out.println("time out for web element");
		}
	}

	public void softWaitForWebElement(WebElement element, String step) {
		try {
			startTime = stopWatch.getTime();

			wait.until(ExpectedConditions.visibilityOf(element));
			addStep(startTime, stopWatch.getTime() - startTime, step, "Pass", util.takeScreenshot(driver));

		} catch (Exception e) {

			addStep(startTime, stopWatch.getTime() - startTime, step, "Fail", util.takeScreenshot(driver));
		}
	}

	public boolean isElementExist(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public WebElement getElement(By by) {
		try {
			return driver.findElement(by);
		} catch (Exception e) {
			return null;
		}
	}

	public void mouseClick(WebElement e) {
		new Actions(driver).moveToElement(e).click(e).build().perform();
	}

	public boolean isElementExist(WebElement e) {
		try {

			return e.getTagName() != null;
		} catch (Exception exception) {
			return false;
		}

	}

	public void switchToFrame(WebElement frame) {

		try {

			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frame));

			addStep(startTime, stopWatch.getTime() - startTime, "switch to frame ", "Pass",
					util.takeScreenshot(driver));

		} catch (Exception e) {
			addStep(startTime, stopWatch.getTime() - startTime, "switch to frame ", "Failed",
					util.takeScreenshot(driver));
			Assert.fail("wait time out for frame");

		}

	}

	public void switchToDefaultContent() {

		try {

			driver.switchTo().defaultContent();

			addStep(startTime, stopWatch.getTime() - startTime, "switch to frame ", "Pass",
					util.takeScreenshot(driver));

		} catch (Exception e) {
			addStep(startTime, stopWatch.getTime() - startTime, "switch to frame ", "Failed",
					util.takeScreenshot(driver));
			Assert.fail("wait time out for frame");

		}

	}

	public void refresh() {
		try {
			startTime = stopWatch.getTime();
			driver.navigate().refresh();
			addStep(startTime, stopWatch.getTime() - startTime, "refresh page", "Pass", util.takeScreenshot(driver));

		} catch (Exception e) {
			Reporter.log("wait timeout for web element", 0, false);
		}

	}

	public void selectFromDropDown(WebElement element, String typeOfSelection, String valueToBeSelected) {
		try {
			startTime = stopWatch.getTime();
			if (typeOfSelection.equalsIgnoreCase("value")) {
				new Select(element).selectByValue(valueToBeSelected);
			} else {
				new Select(element).selectByVisibleText(valueToBeSelected);
			}

			addStep(startTime, stopWatch.getTime() - startTime, "Select " + valueToBeSelected + " from drop down",
					"Pass", util.takeScreenshot(driver));

		} catch (Exception e) {
			Reporter.log("wait timeout for web element", 0, false);
		}

	}

	public Executioner verifyEquals(Object actual, Object expected, String message, boolean flag) {
		String screenshot = "NA";
		try {

			startTime = stopWatch.getTime();
			Assert.assertEquals(actual, expected, message);
			if (flag) {
				screenshot = util.takeScreenshot(driver);
			}
			addStep(startTime, stopWatch.getTime() - startTime,
					message + " actual : " + actual + " expected : " + expected, "Pass", screenshot);
			Reporter.log("Staus: PASS, Expected: " + expected + ", Actual : " + actual, true);
			return this;
		} catch (AssertionError exception) {
			addStep(startTime, stopWatch.getTime() - startTime,
					message + " actual : " + actual + " expected : " + expected, "Failed", screenshot);
			// Assert.fail("verification failed " + actual + " is not equals " +
			// expected);
			Reporter.log("Staus: FAIL, Expected: " + expected + ", Actual : " + actual, true);
			return this;

		}

	}

	public Executioner assertEquals(Object actual, Object expected, String message, boolean flag) {
		String screenshot = "NA";
		try {

			startTime = stopWatch.getTime();
			Assert.assertEquals(actual, expected, message);
			if (flag) {
				screenshot = util.takeScreenshot(driver);
			}
			addStep(startTime, stopWatch.getTime() - startTime,
					message + " actual : " + actual + " expected : " + expected, "Pass", screenshot);
			Reporter.log("Staus: PASS, Expected: " + expected + ", Actual : " + actual, true);
			return this;
		} catch (AssertionError exception) {
			addStep(startTime, stopWatch.getTime() - startTime,
					message + " actual : " + actual + " expected : " + expected, "Failed", screenshot);
			Assert.fail("assertion failed " + actual + " is not equals " + expected);
			Reporter.log("Staus: FAIL, Expected: " + expected + ", Actual : " + actual, true);
			return this;

		}

	}

	public Map<String, String> httpGet(WebResource webResource, Class<ClientResponse> c) {
		String screenshot = "NA";
		Map<String, String> response = new HashMap<String, String>();
		ClientResponse clientResponse = null;
		try {

			startTime = stopWatch.getTime();
			clientResponse = webResource.accept("application/json").get(c);
			String output = clientResponse.getEntity(String.class);
			response.put("responseData", output);
			response.put("status", clientResponse.getStatus() + "");

			// System.out.println(output);

			screenshot = util.saveResponse(output);

			addStep(startTime, stopWatch.getTime() - startTime, "Method : GET <br> URL: " + webResource.getURI()
					+ " <br> <br> Response Code:" + clientResponse.getStatus(), "Pass", screenshot);

			return response;
		} catch (AssertionError exception) {
			addStep(startTime, stopWatch.getTime() - startTime,
					"HTTP GET \n URL: " + webResource.getURI() + " \n Response Code:" + clientResponse.getStatus(),
					"Failed", screenshot);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return response;
		}
		return response;

	}

	public String doHttpGet(WebResource webResource, Class<ClientResponse> c) {
		String screenshot = "NA";
		Map<String, String> response = new HashMap<String, String>();
		ClientResponse clientResponse = null;
		String output = "";
		try {

			startTime = stopWatch.getTime();
			clientResponse = webResource.accept("application/json").get(c);
			output = clientResponse.getEntity(String.class);

			screenshot = util.saveResponse(output);

			addStep(startTime, stopWatch.getTime() - startTime, "Method : GET <br> URL: " + webResource.getURI()
					+ " <br> <br> Response Code:" + clientResponse.getStatus(), "Pass", screenshot);

			return output;
		} catch (AssertionError exception) {
			addStep(startTime, stopWatch.getTime() - startTime,
					"HTTP GET \n URL: " + webResource.getURI() + " \n Response Code:" + clientResponse.getStatus(),
					"Failed", screenshot);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return output;
		}
		return output;

	}

	public Map<String, String> httpPost(WebResource webResource, Class<ClientResponse> c, String requestData) {
		String screenshot = "NA";
		Map<String, String> response = new HashMap<String, String>();
		ClientResponse clientResponse = null;
		try {

			startTime = stopWatch.getTime();
			clientResponse = webResource.accept("application/json").type(MediaType.APPLICATION_JSON).post(c,
					requestData);
			String output = clientResponse.getEntity(String.class);
			response.put("responseData", output);
			response.put("status", clientResponse.getStatus() + "");

			// System.out.println(output);

			screenshot = util.saveResponse(output);

			addStep(startTime, stopWatch.getTime() - startTime, "Method : POST <br> URL: " + webResource.getURI()
					+ " <br> <br> Request Data : " + requestData + " <br>Response Code:" + clientResponse.getStatus(),
					"Pass", screenshot);

			return response;
		} catch (AssertionError exception) {
			addStep(startTime, stopWatch.getTime() - startTime,
					"HTTP GET \n URL: " + webResource.getURI() + " \n Response Code:" + clientResponse.getStatus(),
					"Failed", screenshot);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return response;
		}
		return response;

	}

	public String doHttpPost(WebResource webResource, Class<ClientResponse> c, String requestData) {
		String screenshot = "NA";
		Map<String, String> response = new HashMap<String, String>();
		ClientResponse clientResponse = null;
		String output = "";
		try {

			startTime = stopWatch.getTime();
			clientResponse = webResource.type("application/json").post(c, requestData);
			output = clientResponse.getEntity(String.class);

			// System.out.println(output);

			screenshot = util.saveResponse(output);

			addStep(startTime, stopWatch.getTime() - startTime, "Method : POST <br> URL: " + webResource.getURI()
					+ " <br> <br> Request Data : " + requestData + " <br>Response Code:" + clientResponse.getStatus(),
					"Pass", screenshot);

			return output;
		} catch (AssertionError exception) {
			addStep(startTime, stopWatch.getTime() - startTime,
					"HTTP GET \n URL: " + webResource.getURI() + " \n Response Code:" + clientResponse.getStatus(),
					"Failed", screenshot);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();//

		} finally {
			return output;
		}

	}

	public Executioner verifyJsonEquals(String expected, String actual, String message, boolean flag) {

		String screenshot = "NA";
		try {

			startTime = stopWatch.getTime();
			JSONAssert.assertEquals(expected, actual, flag);
			if (flag) {
				screenshot = util.takeScreenshot(driver);
			}
			addStep(startTime, stopWatch.getTime() - startTime,
					message + "<br> actual : " + actual + "<br><br> expected : " + expected, "Pass", screenshot);
			Reporter.log("Staus: PASS, Expected: " + expected + ", Actual : " + actual, true);
			return this;
		} catch (AssertionError exception) {
			addStep(startTime, stopWatch.getTime() - startTime, message + " <br> " + exception.getMessage(), "Failed",
					screenshot);
			// Assert.fail("verification failed " + actual + " is not equals " +
			// expected);
			exception.getMessage();
			Reporter.log("Staus: FAIL, Expected: " + expected + ", Actual : " + actual, true);
			return this;

		}

	}

	public static void main(String[] args) {
		System.out.println(" in executioner");

		Util util = Util.getInstance();
		Client client = Client.create();
		ExcelReader reader = new ExcelReader();
		reader.getUserDataFromExcel("testData.xlsx", "summary");
		WebResource webResource = client.resource(Config.baseURL + "v1/getSearch");

		String requestJson = util.readFileAsString("requests/search.json");
		String expectedJson = util.readFileAsString("responses/expectedResults.json");

		JSONAssert.assertEquals("{'data':true}", "{'data':false}", true);

	}

}
