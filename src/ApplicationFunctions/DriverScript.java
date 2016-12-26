package ApplicationFunctions;

import Utility.Xls_Reader;
import Constants.Constants;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import Utility.PropertyLoader;
import Utility.ExtentReportConfigrator;

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
	public static int currentTestStepID;
	public static String currentKeyword;
	public static int currentTestDataSetID = 2;
	public static Method methodKeywords[];
	public static Method methodPrerequisites[];
	public static Method methodApplicationSpecific[];
	public static Method capturescreenShot_method;

	public static Keywords keywords;
	public static Prerequisites prerequisites;
	public static ApplicationSpecific applicationSpecific;

	public static ArrayList<String> resultSet;

	public static Properties OR;
	public static Properties CONFIG;
	public static Properties EXCEL_PATH;

	Execute execute = new Execute();

	

	
	public DriverScript() throws NoSuchMethodException, SecurityException {
		keywords = new Keywords();
		prerequisites = new Prerequisites();
		applicationSpecific = new ApplicationSpecific();

		methodKeywords = keywords.getClass().getMethods();
		methodPrerequisites = prerequisites.getClass().getMethods();
		methodApplicationSpecific = applicationSpecific.getClass().getMethods();

		capturescreenShot_method = keywords.getClass().getMethod("captureScreenshot", String.class, String.class);
		// capturescreenShot_method =
		// prerequisites.getClass().getMethod("captureScreenshot", String.class,
		// String.class);
		// capturescreenShot_method =
		// methodApplicationSpecific.getClass().getMethod("captureScreenshot",
		// String.class,String.class);
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

		// ZipFile.zip("E:\\Selenium\\Workspace\\TestProjectHybrid\\test-output");
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
				
				
				ExtentReportConfigrator report = new ExtentReportConfigrator();
				report.generateReport(currentTestModuleName);
				
				report.configureReport();
				
				report.reportUserInfo(automationModuleXLSX);

				
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

						report.createTest(currentSuiteID, suiteXLS);
						report.assignTestCategory(currentSuiteID, suiteXLS);
						report.assignTestAuthor(automationModuleXLSX, testModuleID);
						

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
							processKeywords(report);

							// createXLSReport();

						} else {

							resultSet = new ArrayList<String>();
							processKeywords(report);
							// createXLSReport();
						}
					} else {

						report.createTest(currentSuiteID, currentTestSuiteXLS);
						report.assignTestCategory(currentSuiteID, currentTestSuiteXLS);
						report.assignTestAuthor(automationModuleXLSX, testModuleID);
						report.testSuiteSkip(suiteXLS, currentSuiteID);

					}
				}

				report.flush();
				report = null;
				
			}

		}
	}

	public void processKeywords(ExtentReportConfigrator report) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, IOException {

		// getting the total row count of testCaseSheet
		for (currentTestStepID = 2; currentTestStepID <= currentTestSuiteXLS
				.getRowCount(Constants.TEST_CASES_SHEET); currentTestStepID++) {

			// checking if current test suite is equal to the test case on which
			// index is present
			if (currentTestSuite.equals(
					currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, Constants.TCID, currentTestStepID))) {

				// checking the run mode of the test step

				currentKeyword = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.KEYWORD,
						currentTestStepID);
				APP_LOGS.debug(currentKeyword);

				for (int i = 0; i < methodKeywords.length; i++) {

					if (methodKeywords[i].getName().equals(currentKeyword)) {
						execute.executeKeyword(currentTestSuite, currentTestSuiteXLS, currentTestStepID,
								currentTestDataSetID, CONFIG, methodKeywords, capturescreenShot_method, APP_LOGS,
								resultSet, keywords, currentKeyword, currentTestModuleID, report);
						break;
					} else {
						for (int j = 0; j < methodPrerequisites.length; j++) {

							if (methodPrerequisites[i].getName().equals(currentKeyword)) {
								execute.ExecutePrerequisites(currentTestSuite, currentTestSuiteXLS,
										currentTestStepID, currentTestDataSetID, CONFIG, methodPrerequisites,
										capturescreenShot_method, APP_LOGS, resultSet, prerequisites, currentKeyword,
										currentTestModuleID, report);
								break;
							}
						}

					}
				}

				for (int i = 0; i < methodApplicationSpecific.length; i++) {

					if (methodApplicationSpecific[i].getName().equals(currentKeyword)) {
						execute.ExecuteApplicationSpecific(currentTestSuite, currentTestSuiteXLS);
						break;
					}
				}

				

			}
		}
	}
}

