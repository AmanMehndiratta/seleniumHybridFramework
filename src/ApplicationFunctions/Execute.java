package ApplicationFunctions;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.aventstack.extentreports.Status;

/*import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;*/

import Constants.Constants;

public class Execute {

	public Execute() throws NoSuchMethodException, SecurityException {
		super();
		// TODO Auto-generated constructor stub
	}

	DriverScript driverScript; 
	
	public void executeKeyword(String data, String object) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		
		DriverScript.currentKeyword = DriverScript.currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.KEYWORD,
				DriverScript.currentTestStepID);
		DriverScript.APP_LOGS.debug(DriverScript.currentKeyword);

		// running loop to check if keyword exists in the keyword
		// class
		for (int i = 0; i < DriverScript.methodKeywords.length; i++) {

			if (DriverScript.methodKeywords[i].getName().equals(DriverScript.currentKeyword)) {
				DriverScript.keyword_execution_result = (String) DriverScript.methodKeywords[i].invoke(DriverScript.keywords, object, data);
				DriverScript.APP_LOGS.debug(DriverScript.keyword_execution_result);
				DriverScript.resultSet.add(DriverScript.keyword_execution_result);

				// capturing screenshot if any test fails
				if (DriverScript.keyword_execution_result.contains(Constants.KEYWORD_FAIL)) {
					DriverScript.capturescreenShot_method.invoke(DriverScript.keywords,
							DriverScript.currentTestModuleID + "_" + driverScript.currentTestSuite + "_" + "_TS"
									+ (DriverScript.currentTestStepID - 1) + "_" + (DriverScript.currentTestDataSetID - 1),
									DriverScript.keyword_execution_result);

					// adding screenshot if test fails
					driverScript.test.log(Status.FAIL,
							DriverScript.keyword_execution_result + driverScript.test.addScreenCaptureFromPath(
									System.getProperty("user.dir") + "//screenshots//" + DriverScript.currentTestModuleID
											+ "_" + driverScript.currentTestSuite + "_" + "_TS" + (DriverScript.currentTestStepID - 1)
											+ "_" + (DriverScript.currentTestDataSetID - 1) + ".jpg"));

				} else {
					driverScript.test.log(Status.PASS, DriverScript.keyword_execution_result);
				}

			}
		
	 else {
		// creating entry in report for skipped test step
		 driverScript.test.log(Status.SKIP, DriverScript.currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, "Test Step Desc",DriverScript.currentTestStepID));
		}	
	}
}}
