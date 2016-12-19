package ApplicationFunctions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.log4j.Logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentXReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

/*import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;*/

import Utility.TestConfig;
import Utility.ZipFile;

public class DriverScript {

	public static Logger APP_LOGS;

	// module.xlsx
	public Xls_Reader automationModuleXLSX;
	public static int testModuleID;
	public static String currentTestModuleExcelKey;
	public static String currentTestModuleID;
	public static String currentTestModuleName;

	// suite.xlsx
	public Xls_Reader suiteXLS;
	public int currentSuiteID;
	public String currentTestSuite;

	// current test suite
	public static Xls_Reader currentTestSuiteXLS;
	public static int currentTestCaseID;
	public static String currentTestCaseName;
	public static int currentTestStepID;
	public static String currentKeyword;
	public static int currentTestDataSetID = 2;
	public static Method method[];
	public static Method capturescreenShot_method;

	public static Keywords keywords;
	public static String keyword_execution_result;
	public static ArrayList<String> resultSet;
	public static String data;
	public static String object;
	public static Properties OR;
	public static Properties CONFIG;
	public static Properties EXCEL_PATH;

	// Extent Report
	ExtentXReporter extentx;
	ExtentTest test;
	

	public DriverScript() throws NoSuchMethodException, SecurityException {
		keywords = new Keywords();
		method = keywords.getClass().getMethods();
		capturescreenShot_method = keywords.getClass().getMethod("captureScreenshot", String.class, String.class);

	}

	public static void main(String[] args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException,
			NoSuchMethodException, SecurityException, AddressException, MessagingException {
		PropertyLoader propertyLoader = new PropertyLoader();
		OR = propertyLoader.getOR();
		CONFIG = propertyLoader.getCONFIG();
		EXCEL_PATH = propertyLoader.getEXCEL_PATH();

		DriverScript test = new DriverScript();
		test.start();

		//ZipFile.zip("E:\\Selenium\\Workspace\\TestProjectHybrid\\test-output");
		// TestConfig.mailSender();
	}

	public void start() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, IOException {
		// initialize the app logs
		APP_LOGS = Logger.getLogger("devpinoyLogger");
		APP_LOGS.debug("Hello");

		APP_LOGS.debug("Loading Module Excel Sheet");
		// creating object for module excel
		automationModuleXLSX = new Xls_Reader(Constants.PRM_Test_Module_Excel);

		// running the loop for module suite sheet
		for (testModuleID = 2; testModuleID <= automationModuleXLSX
				.getRowCount(Constants.TEST_MODULE_SHEET); testModuleID++) {

			APP_LOGS.debug("Checking the Runmode of Module");

			// taking test module and checking runmode
			APP_LOGS.debug(automationModuleXLSX.getCellData(Constants.TEST_MODULE_SHEET, Constants.Test_Module_ID,
					testModuleID) + "--"
					+ automationModuleXLSX.getCellData(Constants.TEST_MODULE_SHEET, Constants.RUNMODE, testModuleID));

			// getting the current test module
			currentTestModuleID = automationModuleXLSX.getCellData(Constants.TEST_MODULE_SHEET,
					Constants.Test_Module_ID, testModuleID);

			currentTestModuleName = automationModuleXLSX.getCellData(Constants.TEST_MODULE_SHEET, "Module Name",
					testModuleID);

			currentTestModuleExcelKey = automationModuleXLSX.getCellData(Constants.TEST_MODULE_SHEET,
					Constants.Test_Module_Excel_Key, testModuleID);

			if (automationModuleXLSX.getCellData(Constants.TEST_MODULE_SHEET, Constants.RUNMODE, testModuleID)
					.equals(Constants.RUNMODE_YES)) {
				
				//creating object here for preventing appending previous of report into new report
				ExtentReports extent = new ExtentReports();
				//initializing report with path and appending info
				ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir")+"\\test-output\\"+currentTestModuleName + ".html");
				extent.attachReporter(htmlReporter);
				htmlReporter.setAppendExisting(false);
				
				//report configuration 
				htmlReporter.config().setDocumentTitle("Clove Dental :)");
				htmlReporter.config().setReportName("PRM Automation");
				htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
				htmlReporter.config().setTheme(Theme.STANDARD);
				
				
				
				//adding info about the report
				extent.setSystemInfo("Host Name",
						automationModuleXLSX.getCellData(Constants.Test_INFO_SHEET, "HostName", 2));
				extent.setSystemInfo("Environment",
						automationModuleXLSX.getCellData(Constants.Test_INFO_SHEET, "Environment", 2));
				extent.setSystemInfo("User Name",
						automationModuleXLSX.getCellData(Constants.Test_INFO_SHEET, "UserName", 2));

				APP_LOGS.debug("Executing Module " + currentTestModuleExcelKey);
				APP_LOGS.debug("Properties loaded. Starting testing");
				APP_LOGS.debug("Intialize Suite xlsx");

				suiteXLS = new Xls_Reader(Constants.defaultPath + EXCEL_PATH.getProperty(currentTestModuleExcelKey));

				// Running the loop for Test Suite sheet
				for (currentSuiteID = 2; currentSuiteID <= suiteXLS
						.getRowCount(Constants.TEST_SUITE_SHEET); currentSuiteID++) {

					// Checking the runmode of each test case and printing in
					// console
					APP_LOGS.debug(suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.Test_Suite_ID,
							currentSuiteID) + " -- "
							+ suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.RUNMODE, currentSuiteID));

					// selecting the current test suite id
					currentTestSuite = suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.Test_Suite_ID,
							currentSuiteID);

					// checking the runmode of the current test suite id in
					if (suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.RUNMODE, currentSuiteID)
							.equals(Constants.RUNMODE_YES)) {

						// starting test for extent report
						test = extent.createTest(
								suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, "Name", currentSuiteID),
								suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, "Desc", currentSuiteID));

						
						// assigning category to the test suite
						test.assignCategory(currentTestModuleName);
						test.assignAuthor(automationModuleXLSX.getCellData(Constants.TEST_MODULE_SHEET, "Author", testModuleID));

						// printing the test suite which is being executing
						APP_LOGS.debug("******Executing the Suite******" + suiteXLS
								.getCellData(Constants.TEST_SUITE_SHEET, Constants.Test_Suite_ID, currentSuiteID));

						// making object of excel sheet
						currentTestSuiteXLS = new Xls_Reader(
								Constants.defaultPath + EXCEL_PATH.getProperty(currentTestModuleExcelKey));

						// printing the current test suite with runmode
						APP_LOGS.debug(currentTestSuite + " -- " + currentTestSuiteXLS
								.getCellData(Constants.TEST_SUITE_SHEET, Constants.RUNMODE, currentSuiteID));

						APP_LOGS.debug("Executing the test step -> " + currentTestSuite);

						// checking if sheet exists
						if (currentTestSuiteXLS.isSheetExist(Constants.TEST_CASES_SHEET)) {

							resultSet = new ArrayList<String>();
							APP_LOGS.debug("Iteration number " + (currentTestDataSetID - 1));

							// running executeKeyword function
							executeKeywords();

							// createXLSReport();

						} else {

							resultSet = new ArrayList<String>();
							executeKeywords();
							// createXLSReport();
						}
					} else {
						
						//creating entry in report for skipped tests 
						test = extent.createTest(
								suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, "Name", currentSuiteID),
								suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, "Desc", currentSuiteID));
						test.assignCategory(currentTestModuleName);
						test.assignAuthor(automationModuleXLSX.getCellData(Constants.TEST_MODULE_SHEET, "Author", testModuleID));
						test.log(Status.SKIP, "Test Suite Skipped -- "
								+ suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, "Name", currentSuiteID));
						
					}
					//extent.flush();
				}
				//pushing result to report
				extent.flush();
				
				//making it null to prevent data of previous modules from appending into new module report
				test=null;
			}
			

		}
	}

	public void executeKeywords() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, IOException {

		// getting the total row count of testCaseSheet
		for (currentTestStepID = 2; currentTestStepID <= currentTestSuiteXLS
				.getRowCount(Constants.TEST_CASES_SHEET); currentTestStepID++) {

			// checking if current test suite is equal to the test case on which
			// index is present
			if (currentTestSuite.equals(
					currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, Constants.TCID, currentTestStepID))) {

				// checking the run mode of the test step
				if (currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, Constants.RUNMODE, currentTestStepID)
						.equals(Constants.RUNMODE_YES)) {

					// getting the data
					data = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.DATA,
							currentTestStepID);
					if (data.startsWith(Constants.DATA_START_COL)) {
						// read actual data value from the corresponding column
						data = currentTestSuiteXLS.getCellData(currentTestSuite, data.split(Constants.DATA_SPLIT)[1],
								currentTestDataSetID);
					} else if (data.startsWith(Constants.CONFIG)) {
						// read actual data value from config.properties
						data = CONFIG.getProperty(data.split(Constants.DATA_SPLIT)[1]);
					} else {

						// getting key from excel sheet and find value against
						// key from config.properties file
						data = CONFIG.getProperty(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET,
								Constants.DATA, currentTestStepID));

					}

					// getting the key from excel sheet and value against the
					// key will be fetched by the keyword
					object = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.OBJECT,
							currentTestStepID);
					currentKeyword = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.KEYWORD,
							currentTestStepID);
					APP_LOGS.debug(currentKeyword);

					// running loop to check if keyword exists in the keyword
					// class
					for (int i = 0; i < method.length; i++) {

						if (method[i].getName().equals(currentKeyword)) {
							keyword_execution_result = (String) method[i].invoke(keywords, object, data);
							APP_LOGS.debug(keyword_execution_result);
							resultSet.add(keyword_execution_result);

							// capturing screenshot if any test fails
							if (keyword_execution_result.contains(Constants.KEYWORD_FAIL)) {
								capturescreenShot_method.invoke(keywords,
										currentTestModuleID + "_" + currentTestSuite + "_" + "_TS"
												+ (currentTestStepID - 1) + "_" + (currentTestDataSetID - 1),
										keyword_execution_result);
								
								//adding screenshot if test fails
								test.log(Status.FAIL,
										keyword_execution_result + test.addScreenCaptureFromPath(
												System.getProperty("user.dir") + "//screenshots//" + currentTestModuleID
														+ "_" + currentTestSuite + "_" + "_TS" + (currentTestStepID - 1)
														+ "_" + (currentTestDataSetID - 1) + ".jpg"));

							} else {
								test.log(Status.PASS, keyword_execution_result);
							}

						}
					}
				} else {
					//creating enty in report for skipped test step
					test.log(Status.SKIP, currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, "Test Step Desc",
							currentTestStepID));
				}
			}
		}

	}

	public void createXLSReport() {

		String colName = Constants.RESULT/* + (currentTestDataSetID - 1) */;
		boolean isColExist = false;

		for (int c = 0; c < currentTestSuiteXLS.getColumnCount(Constants.TEST_STEPS_SHEET); c++) {
			if (currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, c, 1).equals(colName)) {
				isColExist = true;
				break;
			}
		}

		if (!isColExist)
			currentTestSuiteXLS.addColumn(Constants.TEST_STEPS_SHEET, colName);
		int index = 0;
		for (int i = 2; i <= currentTestSuiteXLS.getRowCount(Constants.TEST_STEPS_SHEET); i++) {

			if (currentTestSuite
					.equals(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.TCID, i))) {
				if (currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, Constants.RUNMODE, i)
						.equals(Constants.RUNMODE_YES)) {
					if (resultSet.size() == 0)
						currentTestSuiteXLS.setCellData(Constants.TEST_STEPS_SHEET, colName, i, Constants.KEYWORD_SKIP);
					else
						currentTestSuiteXLS.setCellData(Constants.TEST_STEPS_SHEET, colName, i, resultSet.get(index));
					index++;
				}
			}

		}

		if (resultSet.size() == 0) {
			// skip
			currentTestSuiteXLS.setCellData(currentTestSuite, Constants.RESULT, currentTestDataSetID,
					Constants.KEYWORD_SKIP);
			return;
		} else {
			for (int i = 0; i < resultSet.size(); i++) {
				if (!resultSet.get(i).equals(Constants.KEYWORD_PASS)) {
					currentTestSuiteXLS.setCellData(currentTestSuite, Constants.RESULT, currentTestDataSetID,
							resultSet.get(i));
					return;
				}
			}
		}
		currentTestSuiteXLS.setCellData(currentTestSuite, Constants.RESULT, currentTestDataSetID,
				Constants.KEYWORD_PASS);

	}

}
