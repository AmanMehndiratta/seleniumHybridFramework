package ApplicationFunctions;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

/*import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;*/

import Constants.Constants;
import Utility.Xls_Reader;
public class Execute {

	public static String data;
	public static String object;
	public static String url;
	public static int currentTestDataID;
	
	public Execute() throws NoSuchMethodException, SecurityException {
		super();
		// TODO Auto-generated constructor stub
	}

	public void executeKeyword(ExtentTest test, String currentTestSuite, Xls_Reader currentTestSuiteXLS, int currentTestStepID)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		
		if (currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, Constants.RUNMODE, currentTestStepID)
				.equals(Constants.RUNMODE_YES)) {
		
		data = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.DATA,
				DriverScript.currentTestStepID);
		if (data.startsWith(Constants.DATA_START_COL)) {
			// read actual data value from the corresponding column
			data = currentTestSuiteXLS.getCellData(currentTestSuite, data.split(Constants.DATA_SPLIT)[1],
					DriverScript.currentTestDataSetID);
		} else if (data.startsWith(Constants.CONFIG)) {
			// read actual data value from config.properties
			data = DriverScript.CONFIG.getProperty(data.split(Constants.DATA_SPLIT)[1]);
		} else {

			// getting key from excel sheet and find value against
			// key from config.properties file
			data = DriverScript.CONFIG.getProperty(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET,
					Constants.DATA, DriverScript.currentTestStepID));

		}

		// getting the key from excel sheet and value against the
		// key will be fetched by the keyword
		object = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.OBJECT,
				DriverScript.currentTestStepID);

		

		// running loop to check if keyword exists in the keyword
		// class
		for (int i = 0; i < DriverScript.methodKeywords.length; i++) {

			if (DriverScript.methodKeywords[i].getName().equals(DriverScript.currentKeyword)) {
				DriverScript.keyword_execution_result = (String) DriverScript.methodKeywords[i]
						.invoke(DriverScript.keywords, object, data);
				DriverScript.APP_LOGS.debug(DriverScript.keyword_execution_result);
				DriverScript.resultSet.add(DriverScript.keyword_execution_result);

				// capturing screenshot if any test fails
				if (DriverScript.keyword_execution_result.contains(Constants.KEYWORD_FAIL)) {
					DriverScript.capturescreenShot_method.invoke(DriverScript.keywords,
							DriverScript.currentTestModuleID + "_" + currentTestSuite + "_" + "_TS"
									+ (DriverScript.currentTestStepID - 1) + "_"
									+ (DriverScript.currentTestDataSetID - 1),
							DriverScript.keyword_execution_result);

					// adding screenshot if test fails
					test.log(Status.FAIL, DriverScript.keyword_execution_result + test.addScreenCaptureFromPath(
							System.getProperty("user.dir") + "//screenshots//" + DriverScript.currentTestModuleID + "_"
									+ currentTestSuite + "_" + "_TS" + (DriverScript.currentTestStepID - 1) + "_"
									+ (DriverScript.currentTestDataSetID - 1) + ".jpg"));

				} else {
					test.log(Status.PASS, DriverScript.keyword_execution_result);
				}

			}

			
		}
	}else {
		// creating entry in report for skipped test step
		test.log(Status.SKIP, DriverScript.currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET,
				"Test Step Desc", DriverScript.currentTestStepID));
	}
		
	
	}
	
	
	public void ExecutePrerequisites(ExtentTest test, String currentTestSuite, Xls_Reader currentTestSuiteXLS)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		
		data = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.DATA,
				DriverScript.currentTestStepID);
		if (data.startsWith(Constants.DATA_START_COL)) {
			// read actual data value from the corresponding column
			data = currentTestSuiteXLS.getCellData(currentTestSuite, data.split(Constants.DATA_SPLIT)[1],
					DriverScript.currentTestDataSetID);
		} else if (data.startsWith(Constants.CONFIG)) {
			// read actual data value from config.properties
			data = DriverScript.CONFIG.getProperty(data.split(Constants.DATA_SPLIT)[1]);
		} else {

			// getting key from excel sheet and find value against
			// key from config.properties file
			data = DriverScript.CONFIG.getProperty(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET,
					Constants.DATA, DriverScript.currentTestStepID));

		}

		// getting the key from excel sheet and value against the
		// key will be fetched by the keyword
		object = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.OBJECT,
				DriverScript.currentTestStepID);

		
		for(currentTestDataID = 2; currentTestDataID<=currentTestSuiteXLS.getRowCount(Constants.TEST_DATA_SHEET); currentTestDataID++){
		
		if(currentTestSuite.equals(currentTestSuiteXLS.getCellData(Constants.TEST_DATA_SHEET, Constants.TEST_DATA_ID, currentTestDataID))){
			
			url = currentTestSuiteXLS.getCellData(Constants.TEST_DATA_SHEET, Constants.TEST_URL,
					currentTestDataID);
			
	

		// running loop to check if keyword exists in the keyword
		// class
		for (int i = 0; i < DriverScript.methodPrerequisites.length; i++) {

			if (DriverScript.methodPrerequisites[i].getName().equals(DriverScript.currentKeyword)) {
				DriverScript.keyword_execution_result = (String) DriverScript.methodPrerequisites[i]
						.invoke(DriverScript.prerequisites, object, data, url);
				DriverScript.APP_LOGS.debug(DriverScript.keyword_execution_result);
				DriverScript.resultSet.add(DriverScript.keyword_execution_result);

				// capturing screenshot if any test fails
				if (DriverScript.keyword_execution_result.contains(Constants.KEYWORD_FAIL)) {
					DriverScript.capturescreenShot_method.invoke(DriverScript.prerequisites,
							DriverScript.currentTestModuleID + "_" + currentTestSuite + "_" + "_TS"
									+ (DriverScript.currentTestStepID - 1) + "_"
									+ (DriverScript.currentTestDataSetID - 1),
							DriverScript.keyword_execution_result);

					// adding screenshot if test fails
					test.log(Status.FAIL, DriverScript.keyword_execution_result + test.addScreenCaptureFromPath(
							System.getProperty("user.dir") + "//screenshots//" + DriverScript.currentTestModuleID + "_"
									+ currentTestSuite + "_" + "_TS" + (DriverScript.currentTestStepID - 1) + "_"
									+ (DriverScript.currentTestDataSetID - 1) + ".jpg"));

				} else {
					test.log(Status.PASS, DriverScript.keyword_execution_result);
				}

			}

			else {
				// creating entry in report for skipped test step
				test.log(Status.SKIP, DriverScript.currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET,
						"Test Step Desc", DriverScript.currentTestStepID));
			}
		}
	}
		}
	}
	
	public void ExecuteApplicationSpecific(ExtentTest test, String currentTestSuite, Xls_Reader currentTestSuiteXLS){
		
	}
	
	
}
