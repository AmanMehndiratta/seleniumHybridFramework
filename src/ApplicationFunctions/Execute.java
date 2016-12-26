package ApplicationFunctions;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.oracle.webservices.internal.api.message.PropertySet.Property;

/*import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;*/

import Constants.Constants;
import Utility.ExtentReportConfigrator;
import Utility.Xls_Reader;
import org.apache.log4j.Logger;

public class Execute {

	public static String data;
	public static String object;
	public static String url;
	public static String Username;
	public static String Password;
	public static int currentTestDataID;
	public static Keywords keyword;
	public static String keyword_execution_result;

	
	// public static Method capturescreenShot_method;

	public Execute() throws NoSuchMethodException, SecurityException {
		super();
		// TODO Auto-generated constructor stub
	}

	public void executeKeyword(String currentTestSuite, Xls_Reader currentTestSuiteXLS, int currentTestStepID,
			int currentTestDataSetID, Properties CONFIG, Method methodKeywords[], Method capturescreenShot_method,
			Logger APP_LOGS, ArrayList<String> resultSet, Keywords keywords, String currentKeyword,
			String currentTestModuleID, ExtentReportConfigrator report)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {

		if (currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, Constants.RUNMODE, currentTestStepID)
				.equals(Constants.RUNMODE_YES)) {

			data = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.DATA, currentTestStepID);
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
				data = CONFIG.getProperty(
						currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.DATA, currentTestStepID));

			}

			// getting the key from excel sheet and value against the
			// key will be fetched by the keyword
			object = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.OBJECT, currentTestStepID);

			// running loop to check if keyword exists in the keyword
			// class
			for (int i = 0; i < methodKeywords.length; i++) {

				if (methodKeywords[i].getName().equals(currentKeyword)) {
					keyword_execution_result = (String) methodKeywords[i].invoke(keywords, object, data);
					APP_LOGS.debug(keyword_execution_result);
					resultSet.add(keyword_execution_result);

					// capturing screenshot if any test fails
					if (keyword_execution_result.contains(Constants.KEYWORD_FAIL)) {
						capturescreenShot_method.invoke(
								keywords, currentTestModuleID + "_" + currentTestSuite + "_" + "_TS"
										+ (currentTestStepID - 1) + "_" + (currentTestDataSetID - 1),
								keyword_execution_result);

						report.testFail(keyword_execution_result, currentTestModuleID, currentTestSuite,
								currentTestStepID, currentTestDataSetID);
						break;

					} else {
						report.testPass(keyword_execution_result);
						break;
					}

				}

			}
		} else {

			report.testSkip(currentTestSuiteXLS, currentTestStepID);
	
		}

	}

	public void ExecutePrerequisites(String currentTestSuite, Xls_Reader currentTestSuiteXLS, int currentTestStepID,
			int currentTestDataSetID, Properties CONFIG, Method methodPrerequisites[], Method capturescreenShot_method,
			Logger APP_LOGS, ArrayList<String> resultSet, Prerequisites prerequisites, String currentKeyword,
			String currentTestModuleID, ExtentReportConfigrator report)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {

		if (currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, Constants.RUNMODE, currentTestStepID)
				.equals(Constants.RUNMODE_YES)) {

			data = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.DATA, currentTestStepID);
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
				data = CONFIG.getProperty(
						currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.DATA, currentTestStepID));

			}

			// getting the key from excel sheet and value against the
			// key will be fetched by the keyword
			object = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.OBJECT, currentTestStepID);

			for (currentTestDataID = 2; currentTestDataID <= currentTestSuiteXLS
					.getRowCount(Constants.TEST_DATA_SHEET); currentTestDataID++) {

				if (currentTestSuite.equals(currentTestSuiteXLS.getCellData(Constants.TEST_DATA_SHEET,
						Constants.TEST_DATA_ID, currentTestDataID))) {

					url = currentTestSuiteXLS.getCellData(Constants.TEST_DATA_SHEET, Constants.TEST_URL,
							currentTestDataID);
					Username = currentTestSuiteXLS.getCellData(Constants.TEST_DATA_SHEET, Constants.TEST_USERNAME,
							currentTestDataID);

					Password = currentTestSuiteXLS.getCellData(Constants.TEST_DATA_SHEET, Constants.TEST_PASSWORD,
							currentTestDataID);

					// running loop to check if keyword exists in the keyword
					// class
					for (int i = 0; i < methodPrerequisites.length; i++) {

						if (methodPrerequisites[i].getName().equals(currentKeyword)) {
							keyword_execution_result = (String) methodPrerequisites[i].invoke(prerequisites, object,
									data, url, Username, Password);
							APP_LOGS.debug(keyword_execution_result);
							resultSet.add(keyword_execution_result);

							// capturing screenshot if any test fails
							if (keyword_execution_result.contains(Constants.KEYWORD_FAIL)) {
								capturescreenShot_method.invoke(prerequisites,
										currentTestModuleID + "_" + currentTestSuite + "_" + "_TS"
												+ (currentTestStepID - 1) + "_" + (currentTestDataSetID - 1),
										keyword_execution_result);

								report.testFail(keyword_execution_result, currentTestModuleID, currentTestSuite,
										currentTestStepID, currentTestDataSetID);
								break;
								
							} else {
								report.testPass(keyword_execution_result);
								break;
							}

						}

					}

				}
			}
		} else {
			
			report.testSkip(currentTestSuiteXLS, currentTestStepID);
			
		}
	}

	public void ExecuteApplicationSpecific(String currentTestSuite, Xls_Reader currentTestSuiteXLS) {

	}

}
