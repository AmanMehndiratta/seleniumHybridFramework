package ApplicationFunctions;

import Constants.ConstantPaths;
import Constants.Constants;

import static ApplicationFunctions.DriverScript.APP_LOGS;
import static ApplicationFunctions.DriverScript.CONFIG;
import static ApplicationFunctions.DriverScript.OR;
import static ApplicationFunctions.DriverScript.currentTestCaseName;
import static ApplicationFunctions.DriverScript.currentTestDataSetID;
import static ApplicationFunctions.DriverScript.currentTestSuiteXLS;

import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;

import sun.util.resources.CalendarData;

public class Keywords {

	public static WebDriver driver;
	Prerequisites pre;

	// to open browser
	public String openBrowser(String object, String data) {
		DriverScript.APP_LOGS.debug("Opening browser");
		try {
			if (data.equals("Mozilla"))
				driver = new FirefoxDriver();
			else if (data.equals("IE"))
				driver = new InternetExplorerDriver();
			else if (data.equals("Chrome"))
				driver = new ChromeDriver();

			long implicitWaitTime = Long.parseLong(CONFIG.getProperty("implicitwait"));
			driver.manage().timeouts().implicitlyWait(implicitWaitTime, TimeUnit.SECONDS);
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " -- Unable to open browser" + e.getMessage();
		}
		return Constants.KEYWORD_PASS + " -- Browser Opened";

	}

	// to navigate to link
	public String navigate(String object, String data) {
		APP_LOGS.debug("Navigating to URL");
		try {
			driver.navigate().to(data);
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " -- Not able to navigate" + e.getMessage();
		}
		return Constants.KEYWORD_PASS + " -- Navigate to url";
	}

	// to click on a link
	public String clickLink(String object, String data) {
		APP_LOGS.debug("Clicking on link ");
		try {
			driver.findElement(By.xpath(OR.getProperty(object))).click();
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " -- Not able to click on link" + e.getMessage();
		}

		return Constants.KEYWORD_PASS + " -- Clicked on link";
	}

	// to click on a link by link text
	public String clickLink_linkText(String object, String data) {
		APP_LOGS.debug("Clicking on link ");
		try {
			if (data == null) {
				driver.findElement(By.linkText(OR.getProperty(object))).click();
			} else {
				driver.findElement(By.linkText(CONFIG.getProperty(data))).click();
			}
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "Unable to click link by text" + e.getMessage();
		}
		return Constants.KEYWORD_PASS + " -- Clicked on link";
	}

	// to verify link text
	public String verifyLinkText(String object, String data) {
		APP_LOGS.debug("Verifying link Text");
		try {
			String actual = driver.findElement(By.xpath(OR.getProperty(object))).getText();
			String expected = data;

			if (actual.equals(expected))
				return Constants.KEYWORD_PASS + " -- Link text verified - " + data;
			else
				return Constants.KEYWORD_FAIL + " -- Link text not verified";

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " -- Link text not verified" + e.getMessage();

		}

	}

	// to click a button
	public String clickButton(String object, String data) {
		APP_LOGS.debug("Clicking on Button");
		try {
			driver.findElement(By.xpath(OR.getProperty(object))).click();
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " -- Not able to click on Button" + e.getMessage();
		}

		return Constants.KEYWORD_PASS + " -- Button Clicked";
	}

	// to verify button text
	public String verifyButtonText(String object, String data) {
		APP_LOGS.debug("Verifying the button text");
		try {
			String actual = driver.findElement(By.xpath(OR.getProperty(object))).getText();
			String expected = data;

			if (actual.equals(expected))
				return Constants.KEYWORD_PASS + " -- Button text verified";
			else
				return Constants.KEYWORD_FAIL + " -- Button text not verified " + actual + " -- " + expected;
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Object not found " + e.getMessage();
		}

	}

	// to select list
	public String selectList(String object, String data) {
		APP_LOGS.debug("Selecting from list");
		try {
			if (!data.equals(Constants.RANDOM_VALUE)) {
				driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
			} else {
				// logic to find a random value in list
				WebElement droplist = driver.findElement(By.xpath(OR.getProperty(object)));
				List<WebElement> droplist_cotents = droplist.findElements(By.tagName("option"));
				Random num = new Random();
				int index = num.nextInt(droplist_cotents.size());
				String selectedVal = droplist_cotents.get(index).getText();

				driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(selectedVal);
			}
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " - Could not select from list. " + e.getMessage();

		}

		return Constants.KEYWORD_PASS + " -- List Selected";
	}

	// to verify all list elements
	public String verifyAllListElements(String object, String data) {
		APP_LOGS.debug("Verifying the selection of the list");
		try {
			WebElement droplist = driver.findElement(By.xpath(OR.getProperty(object)));
			List<WebElement> droplist_cotents = droplist.findElements(By.tagName("option"));

			// extract the expected values from OR. properties
			String temp = data;
			String allElements[] = temp.split(",");
			// check if size of array == size if list
			if (allElements.length != droplist_cotents.size())
				return Constants.KEYWORD_FAIL + "- size of lists do not match";

			for (int i = 0; i < droplist_cotents.size(); i++) {
				if (!allElements[i].equals(droplist_cotents.get(i).getText())) {
					return Constants.KEYWORD_FAIL + "- Element not found - " + allElements[i];
				}
			}
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " - Could not select from list. " + e.getMessage();

		}

		return Constants.KEYWORD_PASS + " -- List elements verified";
	}

	// to verify list selection
	public String verifyListSelection(String object, String data) {
		APP_LOGS.debug("Verifying all the list elements");
		try {
			String expectedVal = data;
			// System.out.println(driver.findElement(By.xpath(OR.getProperty(object))).getText());
			WebElement droplist = driver.findElement(By.xpath(OR.getProperty(object)));
			List<WebElement> droplist_cotents = droplist.findElements(By.tagName("option"));
			String actualVal = null;
			for (int i = 0; i < droplist_cotents.size(); i++) {
				String selected_status = droplist_cotents.get(i).getAttribute("selected");
				if (selected_status != null)
					actualVal = droplist_cotents.get(i).getText();
			}

			if (!actualVal.equals(expectedVal))
				return Constants.KEYWORD_FAIL + "Value not in list - " + expectedVal;

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " - Could not find list. " + e.getMessage();

		}
		return Constants.KEYWORD_PASS + " -- List elements verified";

	}

	// to select radio button
	public String selectRadio(String object, String data) {
		APP_LOGS.debug("Selecting a radio button");
		try {
			String temp[] = object.split(Constants.DATA_SPLIT);
			driver.findElement(By.xpath(OR.getProperty(temp[0]) + data + OR.getProperty(temp[1]))).click();
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "- Not able to find radio button" + e.getMessage();

		}

		return Constants.KEYWORD_PASS + " -- Radio button selected";

	}

	/* ...........................test keywords.................... */

	// wait for page load
	public String waitForPageLoad(String object, String data) {
		APP_LOGS.debug("waiting for element to load");
		try {
			// use it from util class

			driver.manage().timeouts().pageLoadTimeout(Long.parseLong(data), TimeUnit.SECONDS);

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "- Not able to find radio button" + e.getMessage();

		}
		return Constants.KEYWORD_PASS + " -- Waited for page load";
	}

	// select dropdown value
	public String selectDropdown(String object, String data) {
		APP_LOGS.debug("Selecting a dropdown element");
		try {
			driver.findElement(By.name(OR.getProperty(object))).click();
			;
			// driver.findElement(By.xpath(OR.getProperty(temp[0])+data+OR.getProperty(temp[1]))).click();
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "- Not able to select dropdown value" + e.getMessage();

		}

		return Constants.KEYWORD_PASS;

	}

	// explicit wait
	public String waitExplicit(String object, String data) throws InterruptedException {
		APP_LOGS.debug("Explicit wait");
		try {
			Thread.sleep(20000);
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "- Not able to select dropdown value" + e.getMessage();

		}

		return Constants.KEYWORD_PASS;

	}

	/* ...........................test keywords end.................... */

	// to verify selected radio button
	public String verifyRadioSelected(String object, String data) {
		APP_LOGS.debug("Verify Radio Selected");
		try {
			String temp[] = object.split(Constants.DATA_SPLIT);
			String checked = driver.findElement(By.xpath(OR.getProperty(temp[0]) + data + OR.getProperty(temp[1])))
					.getAttribute("checked");
			if (checked == null)
				return Constants.KEYWORD_FAIL + "- Radio not selected";

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "- Not able to find radio button" + e.getMessage();

		}

		return Constants.KEYWORD_PASS + " -- Verified Radio Button Selected";

	}

	// to verify if check box is checked
	public String checkCheckBox(String object, String data) {
		APP_LOGS.debug("Checking checkbox");
		try {
			// true or null
			String checked = driver.findElement(By.xpath(OR.getProperty(object))).getAttribute("checked");
			if (checked == null)// checkbox is unchecked
				driver.findElement(By.xpath(OR.getProperty(object))).click();
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " - Could not find checkbo" + e.getMessage();
		}
		return Constants.KEYWORD_PASS + " -- CheckBox selection checked";

	}

	// to verify unchecked box
	public String unCheckCheckBox(String object, String data) {
		APP_LOGS.debug("Unchecking checkBox");
		try {
			String checked = driver.findElement(By.xpath(OR.getProperty(object))).getAttribute("checked");
			if (checked != null)
				driver.findElement(By.xpath(OR.getProperty(object))).click();
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " - Could not find checkbox" + e.getMessage();
		}
		return Constants.KEYWORD_PASS + " -- Checkbox Unchecked";

	}

	// to verify selected check box
	public String verifyCheckBoxSelected(String object, String data) {
		APP_LOGS.debug("Verifying checkbox selected");
		try {
			String checked = driver.findElement(By.xpath(OR.getProperty(object))).getAttribute("checked");
			if (checked != null)
				return Constants.KEYWORD_PASS + " -- Checkbox selection checked";
			else
				return Constants.KEYWORD_FAIL + " - Not selected";

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " - Could not find checkbox" + e.getMessage();

		}

	}

	// to verify check box
	public String verifyText(String object, String data) {
		APP_LOGS.debug("Verifying the text");
		try {
			String actual = driver.findElement(By.xpath(OR.getProperty(object))).getText();
			String expected = data;

			if (actual.equals(expected))
				return Constants.KEYWORD_PASS + " -- Verified Text - " + data;
			else
				return Constants.KEYWORD_FAIL + " -- text not verified " + actual + " -- " + expected;
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Object not found " + e.getMessage();
		}

	}

	// to write into text box
	public String writeInInput(String object, String data) {
		APP_LOGS.debug("Writing in text box");

		try {
			driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Unable to write " + e.getMessage();

		}
		return Constants.KEYWORD_PASS + " -- Wrote in input field";

	}

	public String verifyTextinInput(String object, String data) {
		APP_LOGS.debug("Verifying the text in input box");
		try {
			String actual = driver.findElement(By.xpath(OR.getProperty(object))).getAttribute("value");
			String expected = data;

			if (actual.equals(expected)) {
				return Constants.KEYWORD_PASS + " -- Text in Input Verified";
			} else {
				return Constants.KEYWORD_FAIL + " Not matching ";
			}

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Unable to find input box " + e.getMessage();

		}
	}

	/////////////////////////////////////////
	public String clickImage() {
		APP_LOGS.debug("Clicking the image");

		return Constants.KEYWORD_PASS;
	}

	public String verifyFileName() {
		APP_LOGS.debug("Verifying inage filename");

		return Constants.KEYWORD_PASS;
	}
	//////////////////////////////////////////

	public String verifyTitle(String object, String data) {
		APP_LOGS.debug("Verifying title");
		try {
			String actualTitle = driver.getTitle();
			String expectedTitle = data;
			if (actualTitle.equals(expectedTitle))
				return Constants.KEYWORD_PASS + "Title Verified" + actualTitle;
			else
				return Constants.KEYWORD_FAIL + " -- Title not verified " + expectedTitle + " -- " + actualTitle;
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Error in retrieving title" + e.getMessage();
		}
	}

	public String exist(String object, String data) {
		APP_LOGS.debug("Checking existance of element");
		try {
			driver.findElement(By.xpath(OR.getProperty(object)));
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Element doesn't not exist" + e.getMessage();
		}

		return Constants.KEYWORD_PASS + " -- Element Exists";
	}

	public String click(String object, String data) {
		APP_LOGS.debug("Clicking on any element");
		try {
			driver.findElement(By.xpath(OR.getProperty(object))).click();
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Not able to click" + e.getMessage();
		}
		return Constants.KEYWORD_PASS + " -- Clicked on Element";
	}

	public String synchronize(String object, String data) {
		APP_LOGS.debug("Waiting for page to load");
		((JavascriptExecutor) driver).executeScript("function pageloadingtime()" + "{"
				+ "return 'Page has completely loaded'" + "}" + "return (window.onload=pageloadingtime());");

		return Constants.KEYWORD_PASS;
	}

	public String waitForElementVisibility(String object, String data) {
		APP_LOGS.debug("Waiting for an element to be visible");
		int start = 0;
		int time = (int) Double.parseDouble(data);
		try {
			while (time == start) {
				if (driver.findElements(By.xpath(OR.getProperty(object))).size() == 0) {
					Thread.sleep(1000L);
					start++;
				} else {
					break;
				}
			}
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "Unable to close browser. Check if its open" + e.getMessage();
		}
		return Constants.KEYWORD_PASS + " -- Waited for element visibility";
	}

	public String closeBroswer(String object, String data) {
		APP_LOGS.debug("Closing the browser");
		try {
			driver.quit();
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "Unable to close browser. Check if its open" + e.getMessage();
		}
		return Constants.KEYWORD_PASS + " -- Browser Closed";

	}

	public String pause(String object, String data) throws NumberFormatException, InterruptedException {
		long time = (long) Double.parseDouble(object);
		Thread.sleep(time * 1000L);
		return Constants.KEYWORD_PASS + " -- Static Pause";
	}

	/************************
	 * APPLICATION SPECIFIC KEYWORDS
	 ********************************/

	public String validateLogin(String object, String data) {
		// object of the current test XLS
		// name of my current test case
		System.out.println("xxxxxxxxxxxxxxxxxxxxx");
		String data_flag = currentTestSuiteXLS.getCellData(currentTestCaseName, "Data_correctness",
				currentTestDataSetID);
		while (driver.findElements(By.xpath(OR.getProperty("image_login_process"))).size() != 0) {
			try {
				String visiblity = driver.findElement(By.xpath(OR.getProperty("image_login_process")))
						.getAttribute("style");
				System.out.println("System Processing request - " + visiblity);
				if (visiblity.indexOf("hidden") != -1) {
					// error message on screen
					// YOUR WORK
					String actualErrMsg = driver.findElement(By.xpath(OR.getProperty("error_login"))).getText();
					// String expected=OR;
					if (data_flag.equals(Constants.POSITIVE_DATA))
						return Constants.KEYWORD_FAIL;
					else
						return Constants.KEYWORD_PASS;
				}

			} catch (Exception e) {

			}
		}

		// check for page title
		if (data_flag.equals(Constants.POSITIVE_DATA))
			return Constants.KEYWORD_PASS;
		else
			return Constants.KEYWORD_FAIL;
	}

	public String verifyLaptops(String object, String data) {
		APP_LOGS.debug("Verifying the laptops in app");
		// brand
		String brand = currentTestSuiteXLS.getCellData(currentTestCaseName, "Brand", currentTestDataSetID)
				.toLowerCase();
		for (int i = 1; i <= 4; i++) {
			String text = driver
					.findElement(By.xpath(
							OR.getProperty("laptop_name_link_start") + i + OR.getProperty("laptop_name_link_end")))
					.getText().toLowerCase();
			if (text.indexOf(brand) == -1) {
				return Constants.KEYWORD_FAIL + " Brand not there in - " + text;
			}

		}

		return Constants.KEYWORD_PASS;
	}

	public String verifySearchResults(String object, String data) {
		APP_LOGS.debug("Verifying the Search Results");
		try {
			data = data.toLowerCase();
			for (int i = 3; i <= 5; i++) {
				String text = driver.findElement(By.xpath(OR.getProperty("search_result_heading_start") + i
						+ OR.getProperty("search_result_heading_end"))).getText().toLowerCase();
				if (text.indexOf(data) == -1) {
					return Constants.KEYWORD_FAIL + " Got the text - " + text;
				}
			}

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "Error -->" + e.getMessage();
		}

		return Constants.KEYWORD_PASS;

	}

	// not a keyword
	public void captureScreenshot(String filename, String keyword_execution_result) throws IOException {
		// take screen shots
		if (CONFIG.getProperty("screenshot_everystep").equals("Y")) {
			// capturescreen

			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile,
					new File(System.getProperty("user.dir") + "//screenshots//" + filename + ".jpg"));

		} else if (keyword_execution_result.startsWith(Constants.KEYWORD_FAIL)
				&& CONFIG.getProperty("screenshot_error").equals("Y")) {
			// capture screenshot
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile,
					new File(System.getProperty("user.dir") + "//screenshots//" + filename + ".jpg"));
		}
	}

	/*-------------------------added Later----------------------*/

	public String getText(String object, String data) {
		APP_LOGS.debug("Getting Text from given path");
		String get_text;
		try {
			if ((OR.containsKey(object))) {

				get_text = driver.findElement(By.xpath(OR.getProperty(object))).getText();

			} else {

				get_text = driver.findElement(By.xpath(object)).getText();

			}

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "--Unable to get text-->" + e.getMessage();
		}
		return Constants.KEYWORD_PASS + "-- Got text --->" + get_text;

	}

	/*-------------------------application specific----------------------*/

	public String randomPatientName() {
		StringBuilder pName = new StringBuilder();
		try {
			char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
			Random random = new Random();
			for (int i = 0; i < 10; i++) {
				char c = chars[random.nextInt(chars.length)];
				pName.append(c);
			}
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "- Unable to generate random patient name" + e.getMessage();
		}
		return pName.toString();

	}

	public String randomMobileNumber() {
		StringBuilder pNum = new StringBuilder();
		try{
		for (int i = 0; i < 10; i++) {
			int num = (int) Math.random();
			pNum.append(num);
		}
		}catch(Exception e){
			return Constants.KEYWORD_FAIL + "Unable to generate random patient mob no." + e.getMessage();
		}
		return pNum.toString();
	}

}
