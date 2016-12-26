package Utility;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentXReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import Constants.Constants;

public class ExtentReportConfigrator {

	ExtentXReporter extentx;
	ExtentTest test;
	ExtentReports extent = new ExtentReports();
	ExtentHtmlReporter htmlReporter;

	
	//creating report 
	public void generateReport(String currentTestModuleName) {
		
		htmlReporter = new ExtentHtmlReporter(
				System.getProperty("user.dir") + "\\test-output\\" + currentTestModuleName + ".html");

		extent.attachReporter(htmlReporter);

		htmlReporter.setAppendExisting(false);
	}
	
	
	public void configureReport() {

		List<Status> statusHierarchy = Arrays.asList(Status.FATAL, Status.FAIL, Status.ERROR, Status.WARNING,
				Status.SKIP, Status.PASS, Status.INFO);

		// report configuration
		htmlReporter.config().setDocumentTitle("Clove Dental :)");
		htmlReporter.config().setReportName("PRM Automation");
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setReportName("Today's Report");
		extent.config().statusConfigurator().setStatusHierarchy(statusHierarchy);
	}

	public void reportUserInfo(Xls_Reader automationModuleXLSX) throws UnknownHostException {
		
		InetAddress addr;
	    addr = InetAddress.getLocalHost();
	    String hostname = addr.getHostName();
	    String ipAddress = addr.getHostAddress();
	    
		
		// adding info about the report
		extent.setSystemInfo("Host Name", hostname);
		extent.setSystemInfo("IP Address", ipAddress);
		extent.setSystemInfo("OS Info", System.getProperty("os.name"));
		extent.setSystemInfo("Environment",
				automationModuleXLSX.getCellData(Constants.Test_INFO_SHEET, "Environment", 2));
		extent.setSystemInfo("User Name", automationModuleXLSX.getCellData(Constants.Test_INFO_SHEET, "UserName", 2));
	}

	public ExtentTest createTest(int currentSuiteID, Xls_Reader suiteXLS) {
		// starting test for extent report
		test = extent.createTest(suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, "Name", currentSuiteID),
				suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, "Desc", currentSuiteID));
		return test;
	}

	public void assignTestCategory(int currentSuiteID, Xls_Reader suiteXLS) {
		
		
		test.assignCategory(suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, "Desc", currentSuiteID));
	}

	public void assignTestAuthor(Xls_Reader automationModuleXLSX, int testModuleID) {
		test.assignAuthor(automationModuleXLSX.getCellData(Constants.TEST_MODULE_SHEET, "Author", testModuleID));
	}

	public void testSuiteSkip(Xls_Reader suiteXLS, int currentSuiteID) {
		test.log(Status.SKIP,
				"Test Suite Skipped -- " + suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, "Name", currentSuiteID));
	}

	public void testPass(String keyword_execution_result) {
		test.log(Status.PASS, keyword_execution_result);
	}

	public void testSkip(Xls_Reader currentTestSuiteXLS, int currentTestStepID) {
		test.log(Status.SKIP,
				currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, "Test Step Desc", currentTestStepID));
	}

	public void testFail(String keyword_execution_result, String currentTestModuleID, String currentTestSuite,
			int currentTestStepID, int currentTestDataSetID) throws IOException {
		test.log(Status.FAIL,
				keyword_execution_result + test.addScreenCaptureFromPath(System.getProperty("user.dir")
						+ "//screenshots//" + currentTestModuleID + "_" + currentTestSuite + "_" + "_TS"
						+ (currentTestStepID - 1) + "_" + (currentTestDataSetID - 1) + ".jpg"));
	}

	public void flush() {
		extent.flush();
		test = null;
	}

}
