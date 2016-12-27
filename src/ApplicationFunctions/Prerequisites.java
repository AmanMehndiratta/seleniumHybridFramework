package ApplicationFunctions;

import static ApplicationFunctions.DriverScript.APP_LOGS;

import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import Constants.ConstantPaths;
import Constants.ConstantVerificationElements;
import Constants.Constants;
import ReusableFunctions.Keywords;

public class Prerequisites extends Keywords{

	
	public static String data;
	public static String object;
	public WebDriver driver;
	//public FirefoxDriver driver;
	public static String url;
	
	
	Keywords keyword = new Keywords();
	
	public String loginIntoEMR(String data, String object, String url,String Username, String Password, ExtentTest parentTest){
		ExtentTest childTest = parentTest.createNode("Login in to EMR", "Logging into emr without logout from frontend");
		
		String nav = keyword.navigate(object, url);
		if(nav.contains("Fail")){
			childTest.log(Status.FAIL, nav);
		}else{
			childTest.log(Status.PASS, nav);
		}
		
		
		String wait = keyword.waitForPageLoad(object, "50000");
		if(wait.contains("Fail")){
			childTest.log(Status.FAIL, wait);
		}else{
			childTest.log(Status.PASS, wait);
		}
		
		
		String enterUsername = keyword.writeInInput("emr_username_login_input", Username);
		if(enterUsername.contains("Fail")){
			childTest.log(Status.FAIL, enterUsername);
		}else{
			childTest.log(Status.PASS, enterUsername);
		}
		
		String enterPassword = keyword.writeInInput("emr_passwd_login_input", "123456");
		if(enterPassword.contains("Fail")){
			childTest.log(Status.FAIL, enterPassword);
		}else{
			childTest.log(Status.PASS, enterPassword);
		}
		
		String loginButton = keyword.clickButton("emr_button_login", data);
		if(loginButton.contains("Fail")){
			childTest.log(Status.FAIL, loginButton);
		}else{
			childTest.log(Status.PASS, loginButton);
		}
		
		return Constants.KEYWORD_PASS + " -- Logged into EMR";
	}
	
	
	public String addAppointment(String data, String object, String url){
		
		//navigating user to doctor DB
		String loadHomePageURL = keyword.navigate(object, url);
		APP_LOGS.debug("Checking if user is logged in" + loadHomePageURL);
		
		String title;
		title = keyword.verifyTitle(ConstantVerificationElements.prm_Login_Screen_Title, data);
		
		if(title.equals(ConstantVerificationElements.prm_Login_Screen_Title)){
			
			//write code for login and then go to add apt.
			
		}
		
		
		//verifying if user is logged in
		if(keyword.getText(ConstantPaths.doctor_DB_TodaysAppointment, data).equals(ConstantVerificationElements.doctor_DB_TodaysAppt_Text)){
			APP_LOGS.debug("User is logged in");
			
			//keyword.clickButton(, data)
		}
		
		
		
		return "static return";
	}
	
}
